package org.mithras.machinelearning.neuralnetwork.layers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BaseGlobalPool extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.ONE_TO_ONE;
    @JsonProperty("keepdims")
    public boolean keepdims;
    @JsonProperty("trainable")
    private boolean trainable = true;
    @JsonProperty("dtype")
    private String dtype;
    @JsonProperty("data_format")
    private String data_format;

    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }
}
