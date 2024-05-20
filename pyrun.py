from tensorflow.keras.models import Sequential
from tensorflow.keras.layers import Dense, Input

dnn = Sequential(
    [
        Input(shape=(32, 32, 2)),
        Dense(units=32, activation="relu"),
        Dense(32, activation="relu"),
        Dense(10, activation="softmax")
    ]
)

dnn2 = Sequential(
    [
        Input(shape=(32, 64, 2)),
        Dense(units=32, activation="relu"),
        Dense(32, activation="relu"),
        Dense(10, activation="softmax")
    ]
)
from tensorflow.keras.layers import deserialize as layer_deserialize

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
                                    "dtype": 'float32',
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
    json_string += f"{json.dumps(replace_format(json.loads(json.dumps(model_config, indent=4))), indent=4)}\n\n"


def svmdt_summary_to_json(model):
    global count, json_string
    model_config = {"model_name": f"model{count}", "parameters": {}}
    count += 1
    model_config["parameters"] = model.get_params()
    json_string += f"{json.dumps(replace_format(json.loads(json.dumps(model_config, indent=4))), indent=4)}\n\n"

global_vars = dict(globals())
for name, obj in global_vars.items():
    if isinstance(obj, Sequential):
        model_summary_to_json(obj)
    elif isinstance(obj, (DecisionTreeClassifier, DecisionTreeRegressor)):
        svmdt_summary_to_json(obj)
    elif isinstance(obj, (SVC, NuSVC, LinearSVC)):
        svmdt_summary_to_json(obj)

with open("jsonmodel.json", "w") as file:
    file.write(json_string)
