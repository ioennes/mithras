package org.mithras.mithras;

public class Translator
{
    public static String translate(String text)
    {
        return switch (text)
        {
            // SVM Translation
            case "C" -> "Strictness (C)";
            case "kernel" -> "Kernel";
            case "degree" -> "Degree";
            case "gamma", "gammaText" -> "Gamma";
            case "coef0" -> "Bias (coef0)";
            case "shrinking" -> "Shrinking";
            case "probability" -> "Probability";
            case "tol" -> "Tolerance";
            case "cache_size" -> "Cache Size";
            case "class_weight" -> "Class Weight";
            case "verbose" -> "Verbose";
            case "max_iter" -> "Max Iterations";
            case "decision_function_shape" -> "Decision Function Shape";
            case "break_ties" -> "Break Ties";
            case "random_state" -> "Random State";

            // Neural Network Translation
            case "filters" -> "Filters";
            case "kernel_size" -> "Kernel Size";
            case "strides" -> "Strides";
            case "padding" -> "Padding";
            case "dilation_rate" -> "Dilation Rate";
            case "groups" -> "Groups";
            case "activation" -> "Activation";
            case "use_bias" -> "Use Bias";
            case "kernel_initializer" -> "Kernel Initializer";
            case "bias_initializer" -> "Bias Initializer";
            case "kernel_regularizer" -> "Kernel Regularizer";
            case "bias_regularizer" -> "Bias Regularizer";
            case "activity_regularizer" -> "Activity Regularizer";
            case "kernel_constraint" -> "Kernel Constraint";
            case "bias_constraint" -> "Bias Constraint";

            case "units" -> "Units";
            case "rate" -> "Rate";
            case "noise_shape" -> "Noise Shape";
            case "seed" -> "Seed";
            case "shape" -> "Shape";
            case "dtype" -> "Data Type";
            case "batch_size" -> "Batch Size";
            case "name" -> "Name";
            case "sparse" -> "Sparse";
            case "tensor" -> "Tensor";
            case "ragged" -> "Ragged";
            case "type_spec" -> "Metadata (type_spec)";

            case "pool_size" -> "Pool Size";
            case "gain" -> "Gain";
            case "minval" -> "Minimum Value";
            case "maxval" -> "Maximum Value";

            // Datatype Translation
            case "class [I" -> "Integer Tuple";
            case "class java.lang.String" -> "String";
            case "class [Ljava.lang.String;" -> "String Array";
            case "int" -> "Integer";
            case "boolean" -> "Boolean";
            case "float" -> "Float";

            // Packages
            case "convolutionPkg" -> "Convolutional Layers";
            case "activationPkg" -> "Activation Functions";
            case "corePkg" -> "Core Layers";
            case "poolPkg" -> "Pooling Layers";

            // Compilation
            case "CompileData" -> "Compilation Data";
            case "loss" -> "Loss";
            case "optimizer" -> "Optimizer";
            case "metrics" -> "Metrics";
            case "lossWeights" -> "Loss Weights";
            case "weightedMetrics" -> "Weighted Metrics";
            case "runEagerly" -> "Run Eagerly";
            case "stepsPerExecution" -> "Steps Per Execution";
            case "jitCompile" -> "JIT Compile";
            case "pssShards" -> "PSS Shards";
            case "epochs" -> "Epochs";
            case "batchSize" -> "Batch Size";
            case "validationSplit" -> "Validation Split";

            default -> text;
        };
    }
}
