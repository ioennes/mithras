package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

import java.awt.image.BufferedImage;

public class tanh extends BaseActivation
{
    String layerName = "tanh";

    public String getLayerName()
    {
        return layerName;
    }

    public BufferedImage neuralFunction(BufferedImage inputInstance)
    {
        for (int y = 0; y < inputInstance.getHeight(); y++)
        {
            for (int x = 0; x < inputInstance.getWidth(); x++)
            {
                int pixel = inputInstance.getRGB(x, y);

                int alpha = (pixel >> 24) & 0xff;
                int red = applyTanhToColorComponent((pixel >> 16) & 0xff);
                int green = applyTanhToColorComponent((pixel >> 8) & 0xff);
                int blue = applyTanhToColorComponent(pixel & 0xff);

                pixel = (alpha << 24) | (red << 16) | (green << 8) | blue;
                inputInstance.setRGB(x, y, pixel);
            }
        }
        return inputInstance;
    }

    private int applyTanhToColorComponent(int colorComponent)
    {
        // Normalize the color component to the range [-1, 1]
        double normalized = (colorComponent / 255.0) * 2 - 1;
        // Apply tanh and rescale back to [0, 255]
        double tanhValue = Math.tanh(normalized);
        return (int) ((tanhValue + 1) / 2 * 255);
    }
}
