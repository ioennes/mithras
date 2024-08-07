package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class mish extends BaseActivation
{
    String layerName = "mish";



    private double mish(double x)
    {
        return x * Math.tanh(Math.log(1 + Math.exp(x)));
    }
}
