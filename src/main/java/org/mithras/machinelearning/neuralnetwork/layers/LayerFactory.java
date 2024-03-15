package org.mithras.machinelearning.neuralnetwork.layers;

import org.mithras.machinelearning.neuralnetwork.layers.activationPkg.*;
import org.mithras.machinelearning.neuralnetwork.layers.convolutionPkg.Conv1D;
import org.mithras.machinelearning.neuralnetwork.layers.convolutionPkg.Conv2D;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Dense;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Dropout;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Flatten;
import org.mithras.machinelearning.neuralnetwork.layers.corePkg.Input;
import org.mithras.machinelearning.neuralnetwork.layers.poolPkg.*;

public class LayerFactory
{
    public static BaseLayer createLayer(String layer)
    {
        switch (layer)
        {
            /* CORE */
            case "Input" ->
            {
                return new Input();
            }
            case "Dense" ->
            {
                return new Dense();
            }
            case "Flatten" ->
            {
                return new Flatten();
            }
            case "Dropout" ->
            {
                return new Dropout();
            }

            /* CONVOLUTIONAL */
            case "Conv1D" ->
            {
                return new Conv1D();
            }
            case "Conv2D" ->
            {
                return new Conv2D();
            }

            /* ACTIVATION */
            case "elu" ->
            {
                return new elu();
            }
            case "selu" ->
            {
                return new selu();
            }
            case "softmax" ->
            {
                return new softmax();
            }
            case "softplus" ->
            {
                return new softplus();
            }
            case "softsign" ->
            {
                return new softsign();
            }
            case "relu" ->
            {
                return new relu();
            }
            case "tanh" ->
            {
                return new tanh();
            }
            case "sigmoid" ->
            {
                return new sigmoid();
            }
            case "hard_sigmoid" ->
            {
                return new hard_sigmoid();
            }
            case "exponential" ->
            {
                return new exponential();
            }
            case "linear" ->
            {
                return new linear();
            }
            case "swish" ->
            {
                return new swish();
            }
            case "gelu" ->
            {
                return new gelu();
            }
            case "mish" ->
            {
                return new mish();
            }
            case "leakyrelu" ->
            {
                return new leakyrelu();
            }

            // Pooling
            case "MaxPool1D" ->
            {
                return new MaxPool1D();
            }
            case "MaxPool2D" ->
            {
                return new MaxPool2D();
            }
            case "AveragePooling1D" ->
            {
                return new AveragePool1D();
            }
            case "AveragePooling2D" ->
            {
                return new AveragePool2D();
            }
            case "GlobalMaxPooling1D" ->
            {
                return new GlobalMaxPool1D();
            }
            case "GlobalMaxPooling2D" ->
            {
                return new GlobalMaxPool2D();
            }
            case "GlobalAveragePooling1D" ->
            {
                return new GlobalAveragePool1D();
            }
            case "GlobalAveragePooling2D" ->
            {
                return new GlobalAveragePool2D();
            }

            default -> throw new IllegalArgumentException("Unknown layer: " + layer);
        }
    }
}
