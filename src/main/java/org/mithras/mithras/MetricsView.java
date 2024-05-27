package org.mithras.mithras;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mithras.structures.State;

import java.io.IOException;
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
            Scene scene = new Scene(root, 1200, 1000);
            scene.getStylesheets().add(StyleUtil.getCss());

            // Set the model name and initialize charts
            this.modelName = modelName;
            State.setModelName(modelName);
            setupCharts(scene);
            stage.setScene(scene);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void setupCharts(Scene scene)
    {
        // Get the JSON object containing metrics
        JSONObject jsonObject = ModelManager.models.get(modelName).getMetrics();
        System.out.println(jsonObject.toString());

        // Add series to the charts
        Pane trainChartContainer = (Pane) scene.lookup("#trainChartContainer");
        Pane validChartContainer = (Pane) scene.lookup("#validChartContainer");

        boolean trainBar = isSingleValueMetrics(jsonObject, "", "loss", "accuracy", "f1_score",
                "recall", "precision");
        boolean validBar = isSingleValueMetrics(jsonObject, "val_", "loss", "accuracy", "f1_score",
                "recall", "precision");

        if (trainBar || validBar)
        {
            // Create and configure the bar charts
            BarChart<Number, String> trainBarChart = createBarChart("Training Metrics");
            BarChart<Number, String> validBarChart = createBarChart("Validation Metrics");

            // Add new bar charts to the containers
            trainChartContainer.getChildren().clear();
            trainChartContainer.getChildren().add(trainBarChart);
            validChartContainer.getChildren().clear();
            validChartContainer.getChildren().add(validBarChart);

            // Add data to the bar charts
            addSeriesToBarChart(jsonObject, trainBarChart, "", "loss", "accuracy", "f1_score", "recall", "precision");
            addSeriesToBarChart(jsonObject, validBarChart, "val_", "loss", "accuracy", "f1_score", "recall", "precision");
        } else
        {
            // Create and configure the line charts
            LineChart<Number, Number> trainLineChart = createLineChart("Training Metrics");
            LineChart<Number, Number> validLineChart = createLineChart("Validation Metrics");

            // Add new line charts to the containers
            trainChartContainer.getChildren().clear();
            trainChartContainer.getChildren().add(trainLineChart);
            validChartContainer.getChildren().clear();
            validChartContainer.getChildren().add(validLineChart);

            // Add data to the line charts
            addSeriesToLineChart(jsonObject, trainLineChart, "", "loss", "accuracy", "f1_score", "recall", "precision");
            addSeriesToLineChart(jsonObject, validLineChart, "val_", "loss", "accuracy", "f1_score", "recall", "precision");
        }
    }

    private boolean isSingleValueMetrics(JSONObject jsonObject, String prefix, String... metricNames)
    {
        System.out.println(jsonObject.toString());
        for (String metric : metricNames)
        {
            System.out.println(prefix + metric);
            if (jsonObject.has(prefix + metric))
            {
                JSONArray array = jsonObject.getJSONArray(prefix + metric);
                System.out.println(array.length());
                if (array.length() == 1)
                {
                    return true;
                }
            }
        }
        return false;
    }

    private BarChart<Number, String> createBarChart(String title)
    {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Metric");
        yAxis.setLabel("Value");
        BarChart<Number, String> barChart = new BarChart<>(yAxis, xAxis);
        barChart.setTitle(title);
        return barChart;
    }

    private LineChart<Number, Number> createLineChart(String title)
    {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Epoch");
        yAxis.setLabel("Value");
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        return lineChart;
    }

    private void addSeriesToBarChart(JSONObject jsonObject, BarChart<Number, String> barChart, String prefix, String... metricNames)
    {
        for (String metric : metricNames)
        {
            if (jsonObject.has(prefix + metric))
            {
                XYChart.Series<Number, String> series = new XYChart.Series<>();
                series.setName(prefix.isEmpty() ? metric : metric + " (Validation)");

                JSONArray metricArray = jsonObject.getJSONArray(prefix + metric);
                for (int i = 0; i < metricArray.length(); i++)
                {
                    series.getData().add(new XYChart.Data<>(metricArray.getDouble(i), metric));
                }

                barChart.getData().add(series);
            }
        }
    }

    private void addSeriesToLineChart(JSONObject jsonObject, LineChart<Number, Number> lineChart, String prefix, String... metricNames)
    {
        for (String metric : metricNames)
        {
            if (jsonObject.has(prefix + metric))
            {
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                series.setName(prefix.isEmpty() ? metric : metric + " (Validation)");

                JSONArray metricArray = jsonObject.getJSONArray(prefix + metric);
                for (int i = 0; i < metricArray.length(); i++)
                {
                    series.getData().add(new XYChart.Data<>(i + 1, metricArray.getDouble(i)));
                }

                lineChart.getData().add(series);
            }
        }
    }
}
