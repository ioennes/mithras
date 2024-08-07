package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class leakyrelu extends BaseActivation
{
    public float alpha = 0.3f;
    String layerName = "leakyrelu";



    public WritableImage neuralFunction(WritableImage inputInstance)
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
                double alpha = color.getOpacity();
                double red = Math.max(color.getRed() * alpha, color.getRed());
                double green = Math.max(color.getGreen() * alpha, color.getGreen());
                double blue = Math.max(color.getBlue() * alpha, color.getBlue());

                pixelWriter.setColor(x, y, new Color(red, green, blue, alpha));
            }
        }
        return outputInstance;
    }
}
