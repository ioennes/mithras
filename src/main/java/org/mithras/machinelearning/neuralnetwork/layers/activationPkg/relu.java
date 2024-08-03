package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class relu extends BaseActivation
{
    public float max_value;
    public float negative_slope;
    public float threshold;
    String layerName = "relu";

    public static float relu(float x)
    {
        return Math.max(0, x);
    }

    public static WritableImage relu(WritableImage inputInstance)
    {
        int width = (int) inputInstance.getWidth();
        int height = (int) inputInstance.getHeight();
        PixelReader pixelReader = inputInstance.getPixelReader();
        WritableImage outputInstance = new WritableImage(width, height);
        PixelWriter pixelWriter = outputInstance.getPixelWriter();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                Color color = pixelReader.getColor(x, y);
                double gray = color.getRed();
                gray = relu((float) gray);
                pixelWriter.setColor(x, y, new Color(gray, gray, gray, color.getOpacity()));
            }
        }
        return outputInstance;
    }

    public String getLayerName()
    {
        return layerName;
    }
}
