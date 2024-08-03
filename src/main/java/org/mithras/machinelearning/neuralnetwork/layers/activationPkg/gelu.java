package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class gelu extends BaseActivation
{
    public boolean approximate = false;
    String layerName = "gelu";

    public String getLayerName()
    {
        return layerName;
    }
}
