package org.mithras.mithras;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.mithras.structures.NeuralModel;
import org.mithras.structures.State;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class MetricsView
{

    private String modelName;

    public void initializeScene(Stage stage, String modelName)
    {
        try
        {
            // Load the FXML file
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("MetricsView.fxml")));
            // Set up the scene with a size and stylesheet
            Scene scene = new Scene(root);
            scene.getStylesheets().add(StyleUtil.getCss());

            // Set the model name and initialize charts
            this.modelName = modelName;
            State.setModelName(modelName);
            setup(scene);
            Button backButton = (Button) scene.lookup("#backbtn");
            if (backButton != null)
            {
                backButton.toFront();
            }
            stage.setScene(scene);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setup(Scene scene)
    {
        ModelStatistics statistics = ModelManager.models.get(modelName).getStatistics();
        if (ModelManager.models.get(modelName) instanceof NeuralModel)
        {
            setupLineCharts(scene, statistics);
        }
        else
        {
            setupCenteredVBox(scene, statistics);
        }
    }

    private void setupLineCharts(Scene scene, ModelStatistics statistics)
    {
        HBox hbox = new HBox();
        hbox.setSpacing(20);
        hbox.setPadding(new Insets(20));
        hbox.setPrefWidth(scene.getWidth());
        hbox.setPrefHeight(scene.getHeight());

        LineChart<Number, Number> trainChart = createLineChart("Training", statistics);
        LineChart<Number, Number> validChart = createLineChart("Validation", statistics);

        trainChart.prefWidthProperty().bind(hbox.widthProperty().multiply(0.5));
        validChart.prefWidthProperty().bind(hbox.widthProperty().multiply(0.5));

        hbox.getChildren().addAll(trainChart, validChart);

        ((Pane) scene.getRoot()).getChildren().add(hbox);
    }

    private LineChart<Number, Number> createLineChart(String title, ModelStatistics statistics)
    {
        int idx = title.equals("Training") ? 0 : 1;

        NumberAxis xAxis = new NumberAxis(1, statistics.avg_accuracy.get(idx).size(), 1);
        xAxis.setLabel("Epoch");
        xAxis.setTickUnit(1);
        xAxis.setMinorTickVisible(false);

        NumberAxis yAxis = new NumberAxis(0, 100, 10);
        yAxis.setLabel("Value (%)");

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);

        XYChart.Series<Number, Number> acc_series = new XYChart.Series<>();
        acc_series.setName("Accuracy");
        addData(acc_series, statistics.avg_accuracy.get(idx));

        XYChart.Series<Number, Number> recall_series = new XYChart.Series<>();
        recall_series.setName("Recall");
        addData(recall_series, statistics.avg_recall.get(idx));

        XYChart.Series<Number, Number> pres_series = new XYChart.Series<>();
        pres_series.setName("Precision");
        addData(pres_series, statistics.avg_precision.get(idx));

        lineChart.getData().addAll(acc_series, recall_series, pres_series);

        return lineChart;
    }

    private void addData(XYChart.Series<Number, Number> series, ArrayList<Double> avg)
    {
        for (int i = 0; i < avg.size(); i++)
        {
            series.getData().add(new XYChart.Data<>(i + 1, avg.get(i) * 100));
        }
    }

    private void setupCenteredVBox(Scene scene, ModelStatistics statistics)
    {
        Label accuracyLabel = new Label("Accuracy");
        accuracyLabel.setStyle("-fx-font-size: 18px;");

        Label acc = new Label(String.format("%.2f%% ±%.2f%%", statistics.avg_accuracy.get(1).get(0) * 100,
                statistics.std_accuracy.get(1).get(0) * 100));
        acc.setStyle("-fx-font-size: 24px;");

        Label recallLabel = new Label("Recall");
        recallLabel.setStyle("-fx-font-size: 18px;");

        Label re = new Label(String.format("%.2f%% ±%.2f%%", statistics.avg_recall.get(1).get(0) * 100,
                statistics.std_recall.get(1).get(0) * 100));
        re.setStyle("-fx-font-size: 24px;");

        Label precisionLabel = new Label("Precision");
        precisionLabel.setStyle("-fx-font-size: 18px;");

        Label pr = new Label(String.format("%.2f%% ±%.2f%%", statistics.avg_precision.get(1).get(0) * 100,
                statistics.std_precision.get(1).get(0) * 100));
        pr.setStyle("-fx-font-size: 24px;");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(20);
        vbox.setPadding(new Insets(20));

        vbox.getChildren().addAll(accuracyLabel, acc, recallLabel, re, precisionLabel, pr);

        ((Pane) scene.getRoot()).getChildren().add(vbox);
    }
}
