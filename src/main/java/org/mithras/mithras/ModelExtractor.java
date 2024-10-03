package org.mithras.mithras;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mithras.structures.DNNDeserializer;
import org.mithras.structures.SVMDeserializer;
import org.mithras.structures.TreeDeserializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

public class ModelExtractor
{
    private static Path path;
    private static String mithrasCode = "";

    public static void extractModels(Path filepath) throws IOException, InterruptedException
    {
        path = filepath;
        prepareFile();
        extractMithrasCode();
        injectCode();
        ProcessBuilder processBuilder = new ProcessBuilder("python", "./pyrun.py");
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        Process p = processBuilder.start();
        p.waitFor();
        parse();
        SceneManager.switchToMain();
        Files.deleteIfExists(Path.of("./jsonmodel.json"));
        Files.deleteIfExists(Path.of("./pyrun.py"));
    }

    private static void prepareFile() throws IOException, InterruptedException
    {
        if (!path.toString().endsWith(".py"))
        {
            throw new IOException("File is not a Python file.");
        }

        Files.createFile(Path.of("./pyrun.py"));
    }

    private static void extractMithrasCode() throws IOException, InterruptedException
    {
        StringBuilder code = new StringBuilder();
        boolean isMithrasSection = false;
        boolean mithrasFound = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString())))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                if (line.contains("# Mithras"))
                {
                    isMithrasSection = !isMithrasSection;
                    mithrasFound = true;
                    continue;
                }
                if (isMithrasSection)
                {
                    code.append(line).append(System.lineSeparator());
                }
            }
        }

        if (!mithrasFound)
        {
            // If no Mithras section is found, assume the whole file
            try (BufferedReader reader = new BufferedReader(new FileReader(path.toString())))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    code.append(line).append(System.lineSeparator());
                }
            }
        }
        else
        {
            mithrasCode = code.toString();
        }
    }

    private static void injectCode() throws IOException
    {
        String code =
                """
                        \nfrom tensorflow.keras.layers import deserialize as layer_deserialize
                        
                        json_string = ""
                        
                        
                        def replace_format(d):
                            if isinstance(d, dict):
                                if "class_name" in d and "config" in d:
                                    config_str = ', '.join(f'{k}={v}' for k, v in d["config"].items())
                                    return f"{d['class_name']}({config_str})"
                                else:
                                    return {k: replace_format(v) for k, v in d.items()}
                            elif isinstance(d, list):
                                return [replace_format(v) for v in d]
                            else:
                                return d
                        
                        
                        count = 0
                        
                        def model_summary_to_json(model):
                            global count, json_string
                            model_config = {"model_name": f"dnn{count}", "layers": []}
                            count += 1
                        
                            # Add the input layer to the JSON
                            input_layer_config = {"Input": {"shape": [dim for dim in model.input_shape if dim is not None],
                                                            #"dtype": 'float32',
                                                            "sparse": False,
                                                            "name": 'input'}}
                            model_config["layers"].append(input_layer_config)
                        
                            for layer in model.layers:
                                layer_config = layer.get_config()
                                explicit_config = {}
                                try:
                                    default_layer = layer_deserialize({'class_name': layer.__class__.__name__, 'config': {}})
                                    default_config = default_layer.get_config()
                                    for key, value in layer_config.items():
                                        if key not in default_config or value != default_config[key]:
                                            explicit_config[key] = value
                                except TypeError:
                                    explicit_config = layer_config
                                explicit_config.pop("name", None)
                                model_config["layers"].append({layer.__class__.__name__: explicit_config})
                            json_string += f"{json.dumps(replace_format(json.loads(json.dumps(model_config, indent=4))), indent=4)}\\n\\n"
                        
                        
                        def svmdt_summary_to_json(model):
                            global count, json_string
                            model_config = {"model_name": f"model{count}", "model_type": type(model).__name__, "parameters": {}}
                            count += 1
                            model_config["parameters"] = model.get_params()
                            if ("class_weight" in model_config["parameters"]):
                                    del model_config["parameters"]["class_weight"]
                            json_string += f"{json.dumps(replace_format(json.loads(json.dumps(model_config, indent=4))), indent=4)}\\n\\n"
                        
                        global_vars = dict(globals())
                        for (name, obj) in global_vars.items():
                            if (isinstance(obj, Sequential)):
                                model_summary_to_json(obj)
                            elif (isinstance(obj, (DecisionTreeClassifier, DecisionTreeRegressor))):
                                svmdt_summary_to_json(obj)
                            elif (isinstance(obj, (SVC, NuSVC, LinearSVC, LinearSVR))):
                                svmdt_summary_to_json(obj)
                        
                        with open("jsonmodel.json", "w") as file:
                            file.write(json_string)
                        """;

        StringBuilder imports = new StringBuilder("\n");
        PyTranscriber.writeImports(imports);

        System.out.println(mithrasCode);

        Files.writeString(Path.of("./pyrun.py"), imports, StandardOpenOption.APPEND);
        Files.writeString(Path.of("./pyrun.py"), mithrasCode, StandardOpenOption.APPEND);
        Files.writeString(Path.of("./pyrun.py"), code, StandardOpenOption.APPEND);
    }

    private static void parse() throws IOException
    {
        BufferedReader br = new BufferedReader(new FileReader("jsonmodel.json"));
        String everything;
        try
        {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null)
            {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            everything = sb.toString();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        } finally
        {
            br.close();
        }

        String[] jsons = everything.split("\\n\\n");
        for (String json : jsons)
        {
            JsonNode node = new ObjectMapper().readTree(json);
            String modelType = node.has("model_type") ? node.get("model_type").asText() : "DNN";
            if (modelType.contains("SV"))
            {
                SVMDeserializer.deserializeSVM(json);
            }
            else if (modelType.contains("Tree"))
            {
                TreeDeserializer.deserializeTree(json);
            }
            else
            {
                DNNDeserializer.deserializeDNN(json);
            }
        }
    }
}