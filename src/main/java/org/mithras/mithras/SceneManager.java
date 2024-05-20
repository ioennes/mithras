package org.mithras.mithras;

import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.mithras.structures.DatasetHandler;
import org.mithras.structures.NeuralModel;
import org.mithras.structures.State;

import java.io.File;
import java.io.IOException;

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

    public static void openFolderBrowser() throws IOException
    {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Dataset Main Directory");

        File directory = directoryChooser.showDialog(primaryStage);
        if (directory != null)
        {
            DatasetHandler.setDirectory(directory.toPath());
        }
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
            String filePath = file.getAbsolutePath();
            ModelExtractor.extractModels(file.toPath());
        }
    }
}