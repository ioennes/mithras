package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class exponential extends BaseActivation
{
    public static float exponential(float x)
    {
        return (float) Math.exp(x);
    }
}
