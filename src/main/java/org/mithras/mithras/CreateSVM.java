package org.mithras.mithras;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mithras.machinelearning.svm.LinearSVC;
import org.mithras.machinelearning.svm.LinearSVR;
import org.mithras.machinelearning.svm.SVM;
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
            scene = new Scene(root, 1200, 1000);
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
            default -> svm = new SVM();
        }

        if (!ModelManager.models.containsKey(modelName))
        {
            ModelManager.cards.add(new Card(modelName, modelType, Card.CardType.SVM));
            ModelManager.models.put(modelName, new SVMModel(modelName, svm));
            SceneManager.switchToMain();
        }
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
