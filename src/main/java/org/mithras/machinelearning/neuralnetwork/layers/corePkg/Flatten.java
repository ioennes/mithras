package org.mithras.machinelearning.neuralnetwork.layers.corePkg;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;

public class Flatten extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.ONE_TO_ONE;
    String layerName = "Flatten";

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
