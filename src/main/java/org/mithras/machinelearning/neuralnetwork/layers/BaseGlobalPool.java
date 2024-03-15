package org.mithras.machinelearning.neuralnetwork.layers;

public class BaseGlobalPool extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.ONE_TO_ONE;
    public boolean keepdims;

    @Override
    public String getLayerName()
    {
        return null;
    }

    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }
}
