package org.mithras.machinelearning.neuralnetwork.modelParams;

import java.util.HashMap;

public class BaseRegularizer extends BaseModelParam
{
    private static final HashMap<String, BaseRegularizer> regularizers = new HashMap<>();
    //                    Code  , Class

    public static void addRegularizer(String code, BaseRegularizer regularizer)
    {
        regularizers.put(code, regularizer);
    }

    public static HashMap<String, BaseRegularizer> getRegularizers()
    {
        return regularizers;
    }
}
