package org.mithras.mithras;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.mithras.structures.DatasetHandler;
import org.mithras.structures.NeuralModel;
import org.mithras.structures.State;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class SceneManager
{
    private static Stage primaryStage;

    public static void setPrimaryStage(Stage ps)
    {
        primaryStage = ps;
    }

    public static void switchToMain() throws IOException
    {
        Main main = new Main();
        main.initializeScene(primaryStage);
        State.setModelName(null);
    }

    public static void switchToModelSelection() throws IOException
    {
        ModelSelection ms = new ModelSelection();
        ms.initializeScene(primaryStage);
    }

    public static void switchToNNLS(String modelName) throws IOException
    {
        NeuralNetworkLayerSelection nnls = new NeuralNetworkLayerSelection();
        nnls.initializeScene(primaryStage, modelName);
    }

    public static void switchToCreateSVM() throws IOException
    {
        CreateSVM svm = new CreateSVM();
        svm.initializeScene(primaryStage);
    }

    public static void switchToCreateDNN() throws IOException
    {
        CreateDNN dnn = new CreateDNN();
        dnn.initializeScene(primaryStage);
    }

    public static void switchToDNNView(String modelName) throws IOException
    {
        NeuralNetworkView dnnv = new NeuralNetworkView();
        dnnv.initializeScene(primaryStage, modelName);
    }

    public static void switchToCreateTree() throws IOException
    {
        CreateTree tree = new CreateTree();
        tree.initializeScene(primaryStage);
    }

    public static void switchToDNNCodeView(String modelName) throws IOException
    {
        DNNCodeView dnncv = new DNNCodeView();
        dnncv.initializeScene(primaryStage, (NeuralModel) ModelManager.models.get(modelName));
    }

    public static void switchToMetrics(String modelName) throws IOException
    {
        MetricsView metricsView = new MetricsView();
        metricsView.initializeScene(primaryStage, modelName);
    }

    public static void openCSVBrowser() throws IOException
    {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setContentText("Is the dataset numeric or image-based?");

        ButtonType numericButton = new ButtonType("Numeric");
        ButtonType imageButton = new ButtonType("Image");

        alert.getButtonTypes().setAll(numericButton, imageButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent())
        {
            if (result.get() == numericButton)
            {
                DatasetHandler.setNumeric(true);

                alert.setTitle("Dataset Task");
                alert.setContentText("Is the dataset regression or classification?");

                ButtonType classification = new ButtonType("Classification");
                ButtonType regression = new ButtonType("Regression");

                alert.getButtonTypes().setAll(classification, regression);

                result = alert.showAndWait();

                if (result.get() == classification)
                {
                    DatasetHandler.setRegression(false);
                }
                else if (result.get() == regression)
                {
                    DatasetHandler.setRegression(true);
                }
            }
            else if (result.get() == imageButton)
            {
                DatasetHandler.setNumeric(false);

                alert.setTitle("Image Type");
                alert.setContentText("Is the image RGB or Grayscale?");

                ButtonType rgbButton = new ButtonType("RGB");
                ButtonType grayscaleButton = new ButtonType("Grayscale");

                alert.getButtonTypes().setAll(rgbButton, grayscaleButton);

                result = alert.showAndWait();

                if (result.isPresent())
                {
                    if (result.get() == rgbButton)
                    {
                        DatasetHandler.setGrayscale(false);
                    }
                    else if (result.get() == grayscaleButton)
                    {
                        DatasetHandler.setGrayscale(true);
                    }
                }
            }

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select CSV File");
            File file = fileChooser.showOpenDialog(primaryStage);

            if (file != null)
            {
                DatasetHandler.setDataset(file);
            }
        }
        alert = new Alert(AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Import Successful");
        alert.setHeaderText("The dataset has been imported successfully.");
        alert.showAndWait();
    }

    public static void openTranscriber() throws IOException
    {
        new ClassInputDialog<>().start(new PyTranscriber(), null, -1);
        PyTranscriber.transcribe();
    }

    public static void openImporter() throws IOException, InterruptedException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Models Directory");

        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null)
        {
            ModelExtractor.extractModels(file.toPath());
        }
    }
}
