package org.mithras.machinelearning.neuralnetwork.modelParams.initializers;

import org.mithras.machinelearning.neuralnetwork.modelParams.BaseInitializer;

public class VarianceScaling extends BaseInitializer
{
    public float scale;
    public String mode;
    public String distribution;
    public int seed;
}
