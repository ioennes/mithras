package org.mithras.machinelearning.neuralnetwork.layers;

public class BasePool extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.ONE_TO_ONE;
    public int[] pool_size;
    public int[] strides;
    public String padding;
    String layerName = "BasePool";

    public String getLayerName()
    {
        return layerName;
    }

    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }
}
