package org.mithras.machinelearning.neuralnetwork.modelParams.initializers;

import org.mithras.machinelearning.neuralnetwork.modelParams.BaseInitializer;

public class TruncatedNormal extends BaseInitializer
{
    public float mean;
    public float stddev;
    public int seed;
}
