package org.mithras.mithras;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mithras.machinelearning.decisiontree.DecisionTreeClassifier;
import org.mithras.machinelearning.decisiontree.Tree;
import org.mithras.structures.TreeModel;

import java.io.IOException;

public class CreateTree
{
    private Scene scene;

    public void initializeScene(Stage stage)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateTree.fxml"));
            loader.setControllerFactory(param -> this);
            Parent root = loader.load();
            scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(StyleUtil.getCss());

            ComboBox cb = (ComboBox) scene.lookup("#treetype");
            initializeTypesTree(cb);

            stage.setScene(scene);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void saveTree(ActionEvent e) throws IOException
    {
        String modelName = ((TextField) scene.lookup("#treename")).getText();
        String modelType = ((ComboBox<?>) scene.lookup("#treetype")).getValue().toString();

        Tree tree;
        switch (modelType)
        {
            case "Decision Tree Classifier" -> tree = new DecisionTreeClassifier();
            default -> tree = new Tree();
        }

        if (!ModelManager.models.containsKey(modelName))
        {
            ModelManager.cards.add(new Card(modelName, modelType, Card.CardType.DecisionTreeClassifier));
            ModelManager.models.put(modelName, new TreeModel(modelName, tree));
            SceneManager.switchToMain();
        }
    }

    public void back(ActionEvent e) throws IOException
    {
        SceneManager.switchToModelSelection();
    }

    private void initializeTypesTree(ComboBox<String> cb)
    {
        cb.setItems(FXCollections.observableArrayList(
                "Decision Tree Classifier"
        ));
    }
}
