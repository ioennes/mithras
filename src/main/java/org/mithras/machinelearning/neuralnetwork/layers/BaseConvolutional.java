package org.mithras.machinelearning.neuralnetwork.layers;

import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import org.mithras.machinelearning.neuralnetwork.layers.activationPkg.relu;
import org.mithras.machinelearning.neuralnetwork.layers.activationPkg.sigmoid;

public abstract class BaseConvolutional extends BaseLayer implements Activatable
{
    private final ConnectionType connectionType = ConnectionType.FULLY_CONNECTED;
    @JsonProperty("filters")
    public int filters = 32;
    @JsonProperty("kernel_size")
    public int[] kernel_size = new int[]{1};
    @JsonProperty("strides")
    public int[] strides;
    @JsonProperty("padding")
    public String padding;
    @JsonProperty("dilation_rate")
    public int[] dilation_rate;
    @JsonProperty("groups")
    public int groups;
    @JsonProperty("activation")
    public String activation;
    @JsonProperty("use_bias")
    public boolean use_bias = true;
    @JsonProperty("kernel_constraint")
    public String kernel_constraint;
    @JsonProperty("bias_constraint")
    public String bias_constraint;
    String layerName = "BaseConvolutional";
    @JsonProperty("kernel_initializer")
    private String kernel_initializer;
    @JsonProperty("bias_initializer")
    private String bias_initializer;
    @JsonProperty("kernel_regularizer")
    private String kernel_regularizer;
    @JsonProperty("bias_regularizer")
    private String bias_regularizer;
    @JsonProperty("activity_regularizer")
    private String activity_regularizer;
    @JsonProperty("trainable")
    private boolean trainable = true;
    //@JsonProperty("dtype")
    //private String dtype;
    @JsonProperty("data_format")
    private String data_format;

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
