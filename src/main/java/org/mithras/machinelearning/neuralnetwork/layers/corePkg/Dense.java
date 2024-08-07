package org.mithras.machinelearning.neuralnetwork.layers.corePkg;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.mithras.machinelearning.neuralnetwork.layers.Activatable;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;

public class Dense extends BaseLayer implements Activatable
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
    @JsonProperty("kernel_initializer")
    public String kernel_initializer = null;
    @JsonProperty("bias_initializer")
    public String bias_initializer = null;
    @JsonProperty("kernel_regularizer")
    public String kernel_regularizer = null;
    @JsonProperty("bias_regularizer")
    public String bias_regularizer = null;
    @JsonProperty("activity_regularizer")
    public String activity_regularizer = null;
    @JsonProperty("kernel_constraint")
    public String kernel_constraint = null;
    @JsonProperty("bias_constraint")
    public String bias_constraint = null;
    @JsonProperty("dtype")
    public String dtype = "float32";
    String layerName = "Dense";



    @Override
    public ConnectionType getConnectionType()
    {
        return connectionType;
    }

    @Override
    public String getActivation()
    {
        return activation;
    }
}