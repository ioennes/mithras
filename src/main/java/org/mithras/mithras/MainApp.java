package org.mithras.mithras;

import javafx.application.Application;
import javafx.stage.Stage;


public class MainApp extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        SceneManager.setPrimaryStage(primaryStage);
        SceneManager.switchToMain();
        primaryStage.show();
    }
}