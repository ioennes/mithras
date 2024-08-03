package org.mithras.machinelearning.neuralnetwork.modelParams;

import java.util.HashMap;

public class BaseInitializer extends BaseModelParam
{
    private static final HashMap<String, BaseInitializer> initializers = new HashMap<>();
    //                    Code  , Class

    public static void addInitializer(String code, BaseInitializer initializer)
    {
        initializers.put(code, initializer);
    }

    public static HashMap<String, BaseInitializer> getInitializers()
    {
        return initializers;
    }
}