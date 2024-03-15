package org.mithras.machinelearning.neuralnetwork.layers;

import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.mithras.machinelearning.neuralnetwork.layers.activationPkg.relu;
import org.mithras.machinelearning.neuralnetwork.layers.activationPkg.sigmoid;

public abstract class BaseConvolutional extends BaseLayer implements Activatable
{
    private final ConnectionType connectionType = ConnectionType.FULLY_CONNECTED;
    public int filters = 32;
    public int[] kernel_size = new int[]{1};
    public int[] strides;
    public String padding;
    public int[] dilation_rate;
    public int groups;
    public String activation;
    public boolean use_bias = true;
    public String kernel_initializer;
    public String bias_initializer;
    public String kernel_regularizer;
    public String bias_regularizer;
    public String activity_regularizer;
    public String kernel_constraint;
    public String bias_constraint;
    String layerName = "BaseConvolutional";

    public String getLayerName()
    {
        return layerName;
    }

    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }

    @Override
    public WritableImage neuralFunction(WritableImage inputInstance)
    {
        int width = (int) inputInstance.getWidth();
        int height = (int) inputInstance.getHeight();
        PixelReader pixelReader = inputInstance.getPixelReader();
        WritableImage edgeImage = new WritableImage(width, height);
        PixelWriter pixelWriter = edgeImage.getPixelWriter();

        // Sobel Filter to approximate convolutions
        int[][] sobelX = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] sobelY = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        for (int y = 1; y < height - 1; y++)
        {
            for (int x = 1; x < width - 1; x++)
            {
                pixelWriter.setColor(x, y, convolve(pixelReader, sobelX, sobelY, x, y));
            }
        }

        switch (activation)
        {
            case "relu" -> relu.relu(edgeImage);
            case "sigmoid" -> sigmoid.sigmoid(edgeImage);
            default ->
            {
                return edgeImage;
            }
        }

        return edgeImage;
    }

    private Color convolve(PixelReader pixelReader, int[][] sobelX, int[][] sobelY, int x, int y)
    {
        int gradX = 0, gradY = 0;

        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                Color color = pixelReader.getColor(x + i, y + j);
                int gray = (int) (255 * color.getRed());

                gradX += sobelX[i + 1][j + 1] * gray;
                gradY += sobelY[i + 1][j + 1] * gray;
            }
        }

        int magnitude = (int) Math.sqrt(gradX * gradX + gradY * gradY);
        magnitude = Math.min(255, magnitude);
        return Color.grayRgb(magnitude);
    }

    @Override
    public String getActivation()
    {
        return activation;
    }
}
