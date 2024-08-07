package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class linear extends BaseActivation
{
    String layerName = "linear";

    public static float linear(float x)
    {
        return x;
    }


}
