package org.mithras.mithras;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.mithras.machinelearning.svm.*;
import org.mithras.structures.NeuralModel;
import org.mithras.structures.SVMModel;

import java.io.IOException;

public class CreateSVM
{
    private Scene scene;

    public void initializeScene(Stage stage)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateSVM.fxml"));
            loader.setControllerFactory(param -> this);
            Parent root = loader.load();
            scene = new Scene(root);
            scene.getStylesheets().add(StyleUtil.getCss());

            ComboBox cb = (ComboBox) scene.lookup("#svmtype");
            initializeTypesDNN(cb);

            stage.setScene(scene);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveSVM(ActionEvent e) throws IOException
    {
        String modelName = ((TextField) scene.lookup("#svmname")).getText();
        String modelType = ((ComboBox<?>) scene.lookup("#svmtype")).getValue().toString();

        SVM svm;
        switch (modelType)
        {
            case "Linear SVC" -> svm = new LinearSVC();
            case "Linear SVR" -> svm = new LinearSVR();
            case "NuSVC" -> svm = new NuSVC();
            case "SVC" -> svm = new SVC();
            default -> svm = new SVM();
        }

        if (ModelManager.models.containsKey(modelName) || modelName.matches("^[0-9].*"))
        {
            Platform.runLater(() ->
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Error");
                alert.setHeaderText("Model name error");
                alert.setContentText("Model name must not start with a digit or already exist.");
                alert.showAndWait();
            });
        }
        else
        {
            ModelManager.cards.add(new Card(modelName, modelType, Card.CardType.SVM));
            ModelManager.models.put(modelName, new SVMModel(modelName, svm));
            SceneManager.switchToMain();
        }
    }

    public void back(ActionEvent e) throws IOException
    {
        SceneManager.switchToModelSelection();
    }

    private void initializeTypesDNN(ComboBox<String> cb)
    {
        cb.setItems(FXCollections.observableArrayList(
                "SVC",
                "Linear SVC",
                "NuSVC",
                "Linear SVR",
                "NuSVR",
                "SVR",
                "OneClassSVM"
        ));
    }
}
