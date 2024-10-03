package org.mithras.structures;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import org.mithras.machinelearning.decisiontree.DecisionTreeClassifier;
import org.mithras.machinelearning.decisiontree.DecisionTreeRegressor;
import org.mithras.machinelearning.decisiontree.Tree;
import org.mithras.mithras.Card;
import org.mithras.mithras.ModelManager;

public class TreeDeserializer
{
    public static void deserializeTree(String json)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            JsonNode node = mapper.readTree(json);
            String modelName = node.get("model_name").asText();
            String modelType = node.get("model_type").asText();
            JsonNode parameters = node.get("parameters");

            Tree tree = switch (modelType)
            {
                case "DecisionTreeClassifier" -> mapper.treeToValue(parameters, DecisionTreeClassifier.class);
                case "DecisionTreeRegressor" -> mapper.treeToValue(parameters, DecisionTreeRegressor.class);
                default -> throw new IllegalStateException("Unexpected value: " + modelType);
            };


            TreeModel model = new TreeModel(modelName, tree);

            System.out.println(model.toString());
            System.out.println(model.getName());
            ModelManager.models.put(modelName, model);
            ModelManager.cards.add(new Card(modelName,
                    modelType.equals("DecisionTreeClassifier") ? "Decision Tree Classifier" : "Decision Tree Regressor",
                    modelType.equals("DecisionTreeClassifier") ? Card.CardType.DecisionTreeClassifier : Card.CardType.DecisionTreeRegressor));
        } catch (Exception e)
        {
            Platform.runLater(() ->
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Error");
                alert.setHeaderText("Error deserializing the model");
                alert.showAndWait();
            });
        }
    }
}