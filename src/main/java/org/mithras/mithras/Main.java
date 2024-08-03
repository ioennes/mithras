package org.mithras.mithras;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main
{
    private static Main instance;

    public void initializeScene(Stage stage)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
            Scene scene = new Scene(root, 1200, 800);

            GridPane gridPane = (GridPane) scene.lookup("#cardgrid");

            updateCards(gridPane);

            scene.getStylesheets().add(StyleUtil.getCss());

            stage.setScene(scene);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void updateCards(GridPane gp)
    {
        gp.getChildren().clear();

        for (int i = 0; i < ModelManager.cards.size(); i++)
        {
            Card card = ModelManager.cards.get(i);
            gp.add(card, 0, i);
        }
    }
}
