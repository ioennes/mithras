package org.mithras.machinelearning.neuralnetwork.layers.corePkg;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;

public class Input extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.FULLY_CONNECTED;
    public int[] shape;
    @JsonProperty("batch_size")
    public int batch_size;
    @JsonProperty("name")
    public String name;
    @JsonProperty("dtype")
    public String dtype;
    @JsonProperty("sparse")
    public boolean sparse;
    @JsonProperty("tensor")
    public String tensor;
    @JsonProperty("ragged")
    public String ragged;
    String layerName = "Input";

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