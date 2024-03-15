package org.mithras.machinelearning.neuralnetwork.layers.corePkg;

import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;

public class Dropout extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.FULLY_CONNECTED;
    public float rate;
    public int[] noise_shape;
    public int seed;

    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }

    @Override
    public String getLayerName()
    {
        return "Dropout";
    }
}
