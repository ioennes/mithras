package org.mithras.mithras;

import org.mithras.structures.*;

import java.io.PrintWriter;
import java.util.Set;

public class PyTranscriber
{
    public static String filename;
    public static int x_dim;
    public static int y_dim;
    private static int kfcount = 0;

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

    private static void writeImports(StringBuilder sb)
    {
        sb.append("import tensorflow as tf").append("\n");
        sb.append("from tensorflow.keras import models, layers").append("\n");
        sb.append("from sklearn.tree import *").append("\n");
        sb.append("from sklearn.svm import *").append("\n");
        sb.append("from sklearn.model_selection import train_test_split").append("\n");
        sb.append("from sklearn.metrics import *").append("\n");
        sb.append("from sklearn.model_selection import KFold").append("\n\n");
    }

    private static void writeDataset(StringBuilder sb)
    {
        DatasetHandler.preprocessImageFolder(x_dim, y_dim);
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
}