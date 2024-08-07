package org.mithras.machinelearning.neuralnetwork.layers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BasePool extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.ONE_TO_ONE;
    String layerName = "BasePool";
    @JsonProperty("trainable")
    private boolean trainable = true;
    @JsonProperty("dtype")
    private String dtype;
    @JsonProperty("data_format")
    private String data_format;
    @JsonProperty("pool_size")
    public int[] pool_size;
    @JsonProperty("strides")
    public int[] strides;
    @JsonProperty("padding")
    public String padding;




    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }
}
