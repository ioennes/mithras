package org.mithras.mithras;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainApp extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you an expert?", ButtonType.YES, ButtonType.NO);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Expert Confirmation");
        alert.setHeaderText(null);
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                ExpertFields.setExpert(true);
            }
        });

        SceneManager.setPrimaryStage(primaryStage);
        SceneManager.switchToMain();

        primaryStage.show();

        primaryStage.show();
    }
}