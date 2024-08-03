package org.mithras.machinelearning.neuralnetwork.modelParams;

import org.mithras.machinelearning.neuralnetwork.modelParams.initializers.*;
import org.mithras.machinelearning.neuralnetwork.modelParams.regularizers.L1;
import org.mithras.machinelearning.neuralnetwork.modelParams.regularizers.L1L2;
import org.mithras.machinelearning.neuralnetwork.modelParams.regularizers.L2;
import org.mithras.machinelearning.neuralnetwork.modelParams.regularizers.OrthogonalRegularizer;

public class ModelParamFactory
{
    public static BaseModelParam createModelParam(String name)
    {
        return switch (name)
        {
            // Initializers
            case "Constant" -> new Constant();
            case "GlorotNormal" -> new GlorotNormal();
            case "GlorotUniform" -> new GlorotUniform();
            case "HeNormal" -> new HeNormal();
            case "HeUniform" -> new HeUniform();
            case "LecunNormal" -> new LecunNormal();
            case "LecunUniform" -> new LecunUniform();
            case "Identity" -> new Identity();
            case "Orthogonal" -> new Orthogonal();
            case "RandomNormal" -> new RandomNormal();
            case "RandomUniform" -> new RandomUniform();
            case "TruncatedNormal" -> new TruncatedNormal();
            case "VarianceScaling" -> new VarianceScaling();
            case "Ones" -> new Ones();
            case "Zeros" -> new Zeros();

            // Regularizers
            case "L1" -> new L1();
            case "L2" -> new L2();
            case "L1L2" -> new L1L2();
            case "OrthogonalRegularizer" -> new OrthogonalRegularizer();

            default -> null;
        };
    }
}
