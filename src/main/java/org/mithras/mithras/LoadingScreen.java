package org.mithras.mithras;

import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadingScreen
{
    private final Stage stage;

    public LoadingScreen()
    {
        stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        StackPane root = new StackPane();
        ProgressIndicator progressIndicator = new ProgressIndicator();
        root.getChildren().add(progressIndicator);
        Scene scene = new Scene(root, 100, 100);
        stage.setScene(scene);
    }

    public void show()
    {
        stage.show();
    }

    public void hide()
    {
        stage.hide();
    }
}
