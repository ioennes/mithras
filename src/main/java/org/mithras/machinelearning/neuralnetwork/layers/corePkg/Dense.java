package org.mithras.machinelearning.neuralnetwork.layers.corePkg;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;

public class Dense extends BaseLayer
{
    private final ConnectionType connectionType = ConnectionType.FULLY_CONNECTED;
    @JsonProperty("units")
    public int units;
    @JsonProperty("trainable")
    public boolean trainable = true;
    @JsonProperty("activation")
    public String activation = null;
    @JsonProperty("use_bias")
    public boolean use_bias = true;
    @JsonProperty("kernel_constraint")
    public String kernel_constraint = null;
    @JsonProperty("bias_constraint")
    public String bias_constraint = null;
    @JsonProperty("kernel_initializer")
    private String kernel_initializer = null;
    @JsonProperty("bias_initializer")
    private String bias_initializer = null;
    @JsonProperty("kernel_regularizer")
    private String kernel_regularizer = null;
    @JsonProperty("bias_regularizer")
    private String bias_regularizer = null;
    @JsonProperty("activity_regularizer")
    private String activity_regularizer = null;
    @JsonProperty("dtype")
    private String dtype = "float32";

    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }
}