package org.mithras.machinelearning.neuralnetwork.layers;

import javafx.scene.image.WritableImage;
import org.mithras.machinelearning.neuralnetwork.layers.convolutionPkg.Conv1D;
import org.mithras.machinelearning.neuralnetwork.layers.convolutionPkg.Conv2D;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Dense;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Dropout;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Flatten;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Input;

import java.util.ArrayList;

import static org.mithras.structures.StringComposer.compose;

public abstract class BaseLayer
{
    protected int[] input_shape;
    protected int[] output_shape;
    boolean trainable = true;
    String name = null;
    String dtype = null;
    boolean dynamic = false;
    String layerName = null;

    public static void updateShapeParameters(ArrayList<BaseLayer> layers)
    {
        if (layers.isEmpty())
            return;

        if (layers.get(0) instanceof Input)
            layers.get(0).output_shape = ((Input) layers.get(0)).shape;

        for (int i = 1; i < layers.size(); i++)
        {
            layers.get(i).input_shape = layers.get(i - 1).output_shape.clone();
            layers.get(i).output_shape = layers.get(i).input_shape.clone();

            if (layers.get(i) instanceof BaseConvolutional)
            {
                updateConvolutionalShape(layers, i, (BaseConvolutional) layers.get(i));
            } else if (layers.get(i) instanceof BasePool)
            {
                updatePoolShape(layers, i, (BasePool) layers.get(i));
            } else if (layers.get(i) instanceof BaseGlobalPool)
            {
                updateGlobalPoolShape(layers, i, (BaseGlobalPool) layers.get(i));
            } else if (layers.get(i) instanceof Flatten)
            {
                updateFlattenShape(layers, i, (Flatten) layers.get(i));
            } else if (layers.get(i) instanceof Dense)
            {
                updateDenseShape(layers, i, (Dense) layers.get(i));
            } else if (layers.get(i) instanceof Dropout)
            {
                updateDropoutShape(layers, i, (Dropout) layers.get(i));
            }
        }
    }

    private static void updateConvolutionalShape(ArrayList<BaseLayer> layers, int index, BaseConvolutional c)
    {
        int p = 0;

        if (c.padding != null && c.padding.equals("same"))
            p = 1;
        if (c instanceof Conv2D)
        {
            if (c.kernel_size == null)
                c.kernel_size = new int[]{1, 1};
            else if (c.kernel_size.length == 1)
                c.kernel_size = new int[]{c.kernel_size[0], c.kernel_size[0]};

            if (c.strides == null)
                c.strides = new int[]{1, 1};
            else if (c.strides.length == 1) c.strides = new int[]{c.strides[0], c.strides[0]};
        } else if (c instanceof Conv1D)
        {
            if (c.kernel_size == null)
                c.kernel_size = new int[]{1};
            if (c.strides == null)
                c.strides = new int[]{1};
        }

        for (int j = 0; j < c.output_shape.length - 1; j++)
            c.output_shape[j] = ((c.output_shape[j] + 2 * p - c.kernel_size[j]) / c.strides[j]) + 1;

        c.output_shape[c.output_shape.length - 1] = c.filters;

        layers.get(index).output_shape = c.output_shape.clone();
    }

    private static void updatePoolShape(ArrayList<BaseLayer> layers, int index, BasePool p)
    {
        int p2 = 0;
        if (p.padding != null && p.padding.equals("same"))
            p2 = 1;
        if (p.pool_size == null)
            p.pool_size = new int[]{1, 1};
        if (p.strides == null)
            p.strides = new int[]{1, 1};

        for (int j = 0; j < p.output_shape.length - 1; j++)
            p.output_shape[j] = ((p.output_shape[j] + 2 * p2 - p.strides[j]) / p.pool_size[j]) + 1;

        layers.get(index).output_shape = p.output_shape.clone();
    }

    private static void updateGlobalPoolShape(ArrayList<BaseLayer> layers, int index, BaseGlobalPool gp)
    {
        gp.output_shape = new int[]{1, 1, gp.output_shape[gp.output_shape.length - 1]};
        layers.get(index).output_shape = gp.output_shape.clone();
    }

    private static void updateFlattenShape(ArrayList<BaseLayer> layers, int index, Flatten f)
    {
        int size = 1;
        for (int j = 0; j < f.input_shape.length; j++)
            size *= f.input_shape[j];
        f.output_shape = new int[]{size, 1, f.input_shape[f.input_shape.length - 1]};
        layers.get(index).output_shape = f.output_shape.clone();
    }

    private static void updateDenseShape(ArrayList<BaseLayer> layers, int index, Dense d)
    {
        d.output_shape[d.output_shape.length - 1] = d.units;
    }

    private static void updateDropoutShape(ArrayList<BaseLayer> layers, int index, Dropout d)
    {
        d.output_shape[d.output_shape.length - 1] =
                (int) (d.output_shape[d.output_shape.length - 1] * (1 - d.rate));
    }

    /**
     * Method to check if an object is holding its default value
     *
     * @param object
     * @return true if default
     */
    public static boolean isDefaultValue(Object object)
    {
        if (object instanceof Integer && (int) object == 0) return true;
        if (object instanceof Float && (float) object == 0.0f) return true;
        if (object instanceof String && ((String) object).isEmpty()) return true;
        else return object == null;
    }

    abstract public String getLayerName();

    abstract public ConnectionType getConnectionType();

    public int[] getDimensions()
    {
        return output_shape;
    }

    public WritableImage neuralFunction(WritableImage inputInstance)
    {
        return inputInstance;
    }

    @Override
    public String toString()
    {
        StringBuilder output = new StringBuilder("layers." + this.getClass().getSimpleName() + "(");
        compose(output, this);
        return output.toString();
    }


    public enum ConnectionType
    {
        FULLY_CONNECTED, CONVOLUTIONAL, RECURRENT, ONE_TO_ONE, RANDOM, LATERAL
    }
}
