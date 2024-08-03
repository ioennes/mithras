package org.mithras.structures;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;
import org.mithras.machinelearning.neuralnetwork.layers.convolutionPkg.Conv1D;
import org.mithras.machinelearning.neuralnetwork.layers.convolutionPkg.Conv2D;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Dense;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Dropout;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Flatten;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Input;
import org.mithras.machinelearning.neuralnetwork.layers.poolPkg.*;
import org.mithras.mithras.Card;
import org.mithras.mithras.ModelManager;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Input.class, name = "Input"),
        @JsonSubTypes.Type(value = Dense.class, name = "Dense"),
        @JsonSubTypes.Type(value = Flatten.class, name = "Flatten"),
        @JsonSubTypes.Type(value = Dropout.class, name = "Dropout"),
        @JsonSubTypes.Type(value = Flatten.class, name = "Flatten"),
        @JsonSubTypes.Type(value = MaxPool1D.class, name = "MaxPool1D"),
        @JsonSubTypes.Type(value = MaxPool2D.class, name = "MaxPool2D"),
        @JsonSubTypes.Type(value = AveragePool1D.class, name = "AveragePool1D"),
        @JsonSubTypes.Type(value = AveragePool2D.class, name = "AveragePool2D"),
        @JsonSubTypes.Type(value = GlobalMaxPool1D.class, name = "MaxPool1D"),
        @JsonSubTypes.Type(value = GlobalMaxPool2D.class, name = "MaxPool2D"),
        @JsonSubTypes.Type(value = GlobalAveragePool1D.class, name = "AveragePool1D"),
        @JsonSubTypes.Type(value = GlobalAveragePool2D.class, name = "AveragePool2D"),
        @JsonSubTypes.Type(value = Conv1D.class, name = "Conv1D"),
        @JsonSubTypes.Type(value = Conv2D.class, name = "Conv2D"),
})

public class DNNDeserializer
{
    public static void deserializeDNN(String json)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            JsonNode node = mapper.readTree(json);
            String modelName = node.get("model_name").asText();
            NeuralModel model = new NeuralModel(modelName);
            JsonNode layers = node.get("layers");
            for (JsonNode layer : layers)
            {
                String type = layer.fieldNames().next();
                JsonNode layerData = layer.get(type);
                BaseLayer l = switch (type)
                {
                    case "Input" -> mapper.treeToValue(layerData, Input.class);
                    case "Dense" -> mapper.treeToValue(layerData, Dense.class);
                    case "Flatten" -> mapper.treeToValue(layerData, Flatten.class);
                    case "Dropout" -> mapper.treeToValue(layerData, Dropout.class);
                    case "MaxPooling1D" -> mapper.treeToValue(layerData, MaxPool1D.class);
                    case "MaxPooling2D" -> mapper.treeToValue(layerData, MaxPool2D.class);
                    case "AveragePooling1D" -> mapper.treeToValue(layerData, AveragePool1D.class);
                    case "AveragePooling2D" -> mapper.treeToValue(layerData, AveragePool2D.class);
                    case "GlobalMaxPooling1D" -> mapper.treeToValue(layerData, GlobalMaxPool1D.class);
                    case "GlobalMaxPooling2D" -> mapper.treeToValue(layerData, GlobalMaxPool2D.class);
                    case "GlobalAveragePooling1D" -> mapper.treeToValue(layerData, GlobalAveragePool1D.class);
                    case "GlobalAveragePooling2D" -> mapper.treeToValue(layerData, GlobalAveragePool2D.class);
                    case "Conv1D" -> mapper.treeToValue(layerData, Conv1D.class);
                    case "Conv2D" -> mapper.treeToValue(layerData, Conv2D.class);
                    default -> null;
                };
                model.getLayers().add(l);
                System.out.println(l.toString());
            }
            ModelManager.models.put(modelName, model);
            ModelManager.cards.add(new Card(modelName, "Neural", Card.CardType.NeuralNetwork));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}