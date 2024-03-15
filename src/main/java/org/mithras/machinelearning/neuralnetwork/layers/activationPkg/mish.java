package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class mish extends BaseActivation
{
    String layerName = "mish";

    public String getLayerName()
    {
        return layerName;
    }

    private double mish(double x)
    {
        return x * Math.tanh(Math.log(1 + Math.exp(x)));
    }
}
