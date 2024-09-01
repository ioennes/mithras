package org.mithras.mithras;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mithras.structures.NeuralModel;

import java.io.IOException;

public class CreateDNN
{
    private Scene scene;

    public void initializeScene(Stage stage)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateDNN.fxml"));
            loader.setControllerFactory(param -> this);
            Parent root = loader.load();
            scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(StyleUtil.getCss());

            ComboBox cb = (ComboBox) scene.lookup("#dnntype");
            initializeTypesDNN(cb);

            stage.setScene(scene);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveDNN(ActionEvent e) throws IOException
    {
        String modelName = ((TextField) scene.lookup("#nnname")).getText();
        String modelType = ((ComboBox) scene.lookup("#dnntype")).getValue().toString();
        if (!ModelManager.models.containsKey(modelName))
        {
            ModelManager.cards.add(new Card(modelName, modelType, Card.CardType.NeuralNetwork));
            ModelManager.models.put(modelName, new NeuralModel(modelName, modelType));
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
                "Classification",
                "Regression"
        ));
    }
}
