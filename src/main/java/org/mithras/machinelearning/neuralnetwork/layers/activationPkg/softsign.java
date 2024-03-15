package org.mithras.machinelearning.neuralnetwork.layers.activationPkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseActivation;

public class softsign extends BaseActivation
{
    String layerName = "softsign";

    @Override
    public String getLayerName()
    {
        return layerName;
    }
}
