package org.mithras.machinelearning.neuralnetwork.layers.corePkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;

public class Flatten extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.ONE_TO_ONE;
    String layerName = "Flatten";

    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }

    public String getLayerName()
    {
        return layerName;
    }
}
