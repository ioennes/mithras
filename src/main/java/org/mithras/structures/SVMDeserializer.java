package org.mithras.structures;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;
import org.mithras.machinelearning.svm.*;
import org.mithras.mithras.Card;
import org.mithras.mithras.ModelManager;

import java.util.logging.Logger;

public class SVMDeserializer
{
    private static final Logger logger = Logger.getLogger(SVMDeserializer.class.getName());

    public static void deserializeSVM(String json)
    {
        ObjectMapper mapper = new ObjectMapper();
        try
        {
            JsonNode node = mapper.readTree(json);
            String modelName = node.get("model_name").asText();
            String modelType = node.get("model_type").asText();
            JsonNode parameters = node.get("parameters");

            SVM svm = switch (modelType)
            {
                case "SVC" -> mapper.treeToValue(parameters, SVC.class);
                case "NuSVC" -> mapper.treeToValue(parameters, NuSVC.class);
                case "LinearSVC" -> mapper.treeToValue(parameters, LinearSVC.class);
                case "LinearSVR" -> mapper.treeToValue(parameters, LinearSVR.class);
                default -> throw new IllegalStateException("Unexpected value: " + modelType);
            };

            SVMModel model = new SVMModel(modelName, svm);
            ModelManager.models.put(modelName, model);

            modelType = switch (modelType)
            {
                case "LinearSVC" -> "Linear SVC";
                case "LinearSVR" -> "Linear SVR";
                default -> modelType;
            };

            ModelManager.cards.add(new Card(modelName, modelType, Card.CardType.SVM));
        } catch (Exception e)
        {
            logger.severe("Error deserializing the model: " + e.getMessage());
            logger.severe("JSON content: " + json);
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