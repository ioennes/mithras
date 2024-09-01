package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class selu extends BaseActivation
{
    String layerName = "selu";

    public static float selu(float x)
    {
        return (float) (1.0507009873554804934193349852946 * (x > 0 ? x : 1.6732632423543772848170429916717 * (Math.exp(x) - 1)));
    }
}
