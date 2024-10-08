package org.mithras.mithras;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.StageStyle;
import org.json.JSONObject;
import org.mithras.structures.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class PyTranscriber
{
    public static String filename;
    public static Path environment = Paths.get("python");

    public static JSONObject run(String model) throws IOException, InterruptedException
    {
        StringBuilder sb = new StringBuilder();

        writeImports(sb);
        writeDataset(sb);
        writeModel(sb, model);

        try (PrintWriter out = new PrintWriter(filename + ".py"))
        {
            out.println(sb);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        ProcessBuilder processBuilder = new ProcessBuilder(environment.toString(), filename + ".py");
        processBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
        processBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
        Process p = processBuilder.start();

        StringBuilder output = new StringBuilder();
        StringBuilder errorOutput = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
             BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream())))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                output.append(line).append("\n");
            }
            while ((line = errorReader.readLine()) != null)
            {
                errorOutput.append(line).append("\n");
            }
        }

        p.waitFor();

        Path metrics_path = Path.of("metrics.json");
        if (Files.notExists(metrics_path))
        {
            Platform.runLater(() ->
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initStyle(StageStyle.UTILITY);
                alert.setTitle("Error");
                alert.setHeaderText("Error, will keep previous run, if any.");
                TextArea textArea = new TextArea(output + "\n" + errorOutput);
                textArea.setEditable(false);
                textArea.setWrapText(true);
                alert.getDialogPane().setContent(textArea);
                alert.showAndWait();
            });
            return null;
        }

        if (!(ModelManager.models.get(model) instanceof NeuralModel))
        {
            ModelManager.models.get(model).setPlot(Path.of("plot.png"));
        }

        JSONObject metrics = new JSONObject(new String(Files.readAllBytes(metrics_path)));
        Files.deleteIfExists(metrics_path);
        Files.deleteIfExists(Path.of("plot.png"));
        return metrics;
    }

    public static void transcribe()
    {
        StringBuilder sb = new StringBuilder();

        writeImports(sb);
        writeDataset(sb);
        writeModels(sb);

        try (PrintWriter out = new PrintWriter(filename + ".py"))
        {
            out.println(sb);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void writeImports(StringBuilder sb)
    {
        sb.append("import os").append("\n");
        sb.append("os.environ['TF_CPP_MIN_LOG_LEVEL'] = '2'").append("\n");
        sb.append("import tensorflow as tf").append("\n");
        sb.append("import matplotlib.pyplot as plt").append("\n");
        sb.append("from matplotlib.pyplot import subplots").append("\n");
        sb.append("import pandas as pd").append("\n");
        sb.append("import numpy as np").append("\n");
        sb.append("import json").append("\n");
        sb.append("import cv2").append("\n");
        sb.append("from tensorflow.keras import models, layers").append("\n");
        sb.append("from tensorflow.keras.models import Sequential").append("\n");
        sb.append("from sklearn.tree import *").append("\n");
        sb.append("from sklearn.svm import *").append("\n");
        sb.append("from sklearn.model_selection import train_test_split").append("\n");
        sb.append("from sklearn.decomposition import PCA").append("\n");
        sb.append("from sklearn.metrics import *").append("\n");
        sb.append("from tensorflow.keras.initializers import *").append("\n");
        sb.append("from tensorflow.keras.regularizers import *").append("\n\n");
    }

    public static void writeDataset(StringBuilder sb)
    {
        sb.append(DatasetHandler.preprocessFile());
    }

    private static void writeModels(StringBuilder sb)
    {
        Set<String> keys = ModelManager.models.keySet();


        for (String key : keys)
        {
            Model model = ModelManager.models.get(key);
            sb.append(model.toString()).append("\n");
        }
    }

    private static void writeModel(StringBuilder sb, String model)
    {
        if (ModelManager.models.get(model) instanceof NeuralModel)
        {
            sb.append(ModelManager.models.get(model).toString()).append("\n\n");
            sb.append("with open('metrics.json', 'w') as f:\n");
            sb.append("\tjson.dump(history.history, f)\n");
            return;
        }

        sb.append(ModelManager.models.get(model).toString()).append("\n\n");
        sb.append("y_pred = ")
                .append(ModelManager.models.get(model).getName())
                .append(".predict(xts)").append("\n\n");
        sb.append("""
                metrics = {
                    'val_accuracy': [accuracy_score(yts, y_pred)],
                    'val_precision': [precision_score(yts, y_pred)],
                    'val_recall': [recall_score(yts, y_pred)],
                }
                
                metrics_json = json.dumps(metrics, indent=4)
                
                with open('./metrics.json', 'w') as f:
                    f.write(metrics_json)
                """);

        if (ModelManager.models.get(model) instanceof SVMModel)
        {
            sb.append("""
                    def plot_svm_dataset(svm, X, y, num_instances=50):
                        # Ensure we have more than one class in the subset
                        indices = np.random.choice(np.arange(X.shape[0]), min(len(X), num_instances), replace=False)
                        X_subset = X.iloc[indices]
                        y_subset = np.array(y)[indices]
                    
                        # Reduce to two dimensions using PCA
                        pca = PCA(n_components=2)
                        X_pca = pca.fit_transform(X_subset)
                    
                        # Fit the SVM to the reduced data
                        svm.fit(X_pca, y_subset)
                    
                        # Create a mesh grid for plotting the decision boundary
                        h = 0.02  # Step size in the mesh
                        x_min, x_max = X_pca[:, 0].min() - 1, X_pca[:, 0].max() + 1
                        y_min, y_max = X_pca[:, 1].min() - 1, X_pca[:, 1].max() + 1
                        xx, yy = np.meshgrid(np.arange(x_min, x_max, h),
                                             np.arange(y_min, y_max, h))
                    
                        Z = svm.predict(np.c_[xx.ravel(), yy.ravel()])
                        Z = Z.reshape(xx.shape)
                    
                        plt.figure(figsize=(10, 6))
                        plt.contourf(xx, yy, Z, alpha=0.8, cmap=plt.cm.coolwarm)
                        plt.scatter(X_pca[:, 0], X_pca[:, 1], c=y_subset, edgecolors='k', marker='o', cmap=plt.cm.coolwarm)
                    
                        # Plot the decision boundary (hyperplane)
                        ax = plt.gca()
                        xlim = ax.get_xlim()
                        ylim = ax.get_ylim()
                    
                        # Create grid to evaluate model
                        xx = np.linspace(xlim[0], xlim[1], 30)
                        yy = np.linspace(ylim[0], ylim[1], 30)
                        YY, XX = np.meshgrid(yy, xx)
                        xy = np.vstack([XX.ravel(), YY.ravel()]).T
                        Z = svm.decision_function(xy).reshape(XX.shape)
                    
                        # Plot decision boundary and margins
                        ax.contour(XX, YY, Z, colors='k', levels=[-1, 0, 1], alpha=0.5,
                                   linestyles=['--', '-', '--'])
                    
                        plt.title('SVM Decision Boundary and Dataset')
                        plt.xlabel('Principal Component 1')
                        plt.ylabel('Principal Component 2')
                        plt.savefig('plot.png', dpi=500)
                    """);
            sb.append("plot_svm_dataset(").append(ModelManager.models.get(model).getName())
                    .append(", xts, yts)");
        }
        else if (ModelManager.models.get(model) instanceof TreeModel)
        {
            sb.append("fig, ax = subplots(figsize=(12,12))\n");
            sb.append("plot_tree(").append(ModelManager.models.get(model).getName()).append(", feature_names=X.columns," +
                    " ax=ax, filled=True, rounded=True, proportion=False, precision=2, node_ids=False, impurity=False)\n");
            sb.append("plt.savefig('plot.png', dpi=500)");
        }
    }
}
