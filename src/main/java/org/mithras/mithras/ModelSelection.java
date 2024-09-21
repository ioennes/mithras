package org.mithras.mithras;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ModelSelection
{
    public void initializeScene(Stage stage)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ModelSelection.fxml")));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(StyleUtil.getCss());

            stage.setScene(scene);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
