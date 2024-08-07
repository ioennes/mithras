package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class hard_sigmoid extends BaseActivation
{
    String layerName = "hard_sigmoid";

    public static float hard_sigmoid(float x)
    {
        return (float) Math.max(0, Math.min(1, (0.2 * x) + 0.5));
    }


}
