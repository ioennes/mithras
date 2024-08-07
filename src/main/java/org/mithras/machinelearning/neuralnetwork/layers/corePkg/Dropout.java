package org.mithras.machinelearning.neuralnetwork.layers.corePkg;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;

public class Dropout extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.FULLY_CONNECTED;

    @JsonProperty("rate")
    public float rate;
    @JsonProperty("noise_shape")
    public int[] noise_shape;
    @JsonProperty("seed")
    public int seed;

    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }
}
