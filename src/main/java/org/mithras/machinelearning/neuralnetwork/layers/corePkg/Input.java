package org.mithras.machinelearning.neuralnetwork.layers.corePkg;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;

public class Input extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.FULLY_CONNECTED;
    public int[] shape;
    @JsonProperty("sparse")
    public boolean sparse;
    String layerName = "Input";
    @JsonProperty("batch_size")
    private int batch_size;
    @JsonProperty("name")
    private String name;
    @JsonProperty("dtype")
    private String dtype;
    @JsonProperty("tensor")
    private String tensor;
    @JsonProperty("ragged")
    private String ragged;

    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }


}