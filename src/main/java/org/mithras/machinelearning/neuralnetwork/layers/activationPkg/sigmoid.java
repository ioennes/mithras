package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class sigmoid extends BaseActivation
{
    String layerName = "sigmoid";

    public static float sigmoid(float x)
    {
        return (float) (1 / (1 + Math.exp(-x)));
    }

    public static WritableImage sigmoid(WritableImage inputInstance)
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
                gray = sigmoid((float) gray);
                pixelWriter.setColor(x, y, new Color(gray, gray, gray, color.getOpacity()));
            }
        }
        return outputInstance;
    }


}
