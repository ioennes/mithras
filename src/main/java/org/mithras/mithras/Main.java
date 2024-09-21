package org.mithras.mithras;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main
{
    public void initializeScene(Stage stage) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Main.fxml")));
            Scene scene = new Scene(root);

            ScrollPane scrollPane = (ScrollPane) scene.lookup("#cardscrollpane");
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(6.0);
            gridPane.setVgap(6.0);
            gridPane.setPrefHeight(800.0);
            gridPane.setPrefWidth(1200.0);
            gridPane.setPadding(new Insets(20));

            ColumnConstraints col1 = new ColumnConstraints();
            ColumnConstraints col2 = new ColumnConstraints();
            gridPane.getColumnConstraints().addAll(col1, col2);

            RowConstraints row1 = new RowConstraints();
            RowConstraints row2 = new RowConstraints();
            gridPane.getRowConstraints().addAll(row1, row2);

            scrollPane.setContent(gridPane);

            updateCards(gridPane);

            scene.getStylesheets().add(StyleUtil.getCss());

            stage.setScene(scene);
        } catch (IOException e) {
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