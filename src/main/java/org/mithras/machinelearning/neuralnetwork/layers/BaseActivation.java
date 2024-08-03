package org.mithras.machinelearning.neuralnetwork.layers;

public abstract class BaseActivation extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.ONE_TO_ONE;
    String layerName = "BaseActivation";
    private int x;
    private boolean isImplicit = false;

    public String getLayerName()
    {
        return layerName;
    }

    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }

    public boolean isImplicit()
    {
        return isImplicit;
    }

    public void makeImplicit()
    {
        isImplicit = true;
    }
}
