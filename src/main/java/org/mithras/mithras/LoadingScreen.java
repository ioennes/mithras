package org.mithras.mithras;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadingScreen extends Application
{
    private static Stage loadingStage;

    public static void show()
    {
        Platform.runLater(() ->
        {
            System.out.println("LoadingScreen.show() called");
            if (loadingStage == null)
            {
                new LoadingScreen().start(new Stage());
            }
            else
            {
                loadingStage.show();
            }
        });
    }

    public static void hide()
    {
        Platform.runLater(() ->
        {
            System.out.println("LoadingScreen.hide() called");
            if (loadingStage != null)
            {
                loadingStage.close();
            }
        });
    }

    @Override
    public void start(Stage primaryStage)
    {
        loadingStage = primaryStage;
        primaryStage.initStyle(StageStyle.UTILITY);
        Label loadingLabel = new Label("Please wait...");
        loadingLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        StackPane root = new StackPane(loadingLabel);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Loading...");
        primaryStage.show();
    }
}