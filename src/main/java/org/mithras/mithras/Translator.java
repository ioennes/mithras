package org.mithras.mithras;

public class Translator
{
    public static String translate(String text)
    {
        return switch (text)
        {
            // Tree Translation
            case "criterion" -> "Criterion";
            case "splitter" -> "Splitter";
            case "max_depth" -> "Max Depth";
            case "min_samples_split" -> "Min Samples Split";
            case "min_samples_leaf" -> "Min Samples Leaf";
            case "min_weight_fraction_leaf" -> "Min Weight Fraction Leaf";
            case "max_features" -> "Max Features";
            case "max_leaf_nodes" -> "Max Leaf Nodes";
            case "min_impurity_decrease" -> "Min Impurity Decrease";
            case "min_impurity_split" -> "Min Impurity Split";
            case "ccp_alpha" -> "CCP Alpha";
            case "monotonic_cst" -> "Monotonic Constraints";
            case "presort" -> "Presort";

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
            case "decision_function_shape" -> "Decision Shape";
            case "break_ties" -> "Break Ties";
            case "random_state" -> "Random State";
            case "dual" -> "Dual";
            case "multi_class" -> "Multi-Class";
            case "fit_intercept" -> "Fit Intercept";
            case "penalty" -> "Penalty";

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
            case "interface java.nio.file.Path" -> "Path";

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
            case "validationSplit" -> "Validation Split (%)";

            // Others
            case "environment" -> "Python Binary Path";
            case "filename" -> "File Name";
            case "num_iterations" -> "Iterations";

            default -> text;
        };
    }

    public static String annotate(String text)
    {
        return switch (text)
        {
            // Tree Annotation
            case "Criterion" -> "Function to measure split quality (gini, entropy, log_loss)";
            case "Splitter" -> "Strategy to choose split at each node (best, random)";
            case "Max Depth" -> "Maximum depth of the tree";
            case "Min Samples Split" -> "Minimum number of samples required to split an internal node";
            case "Min Samples Leaf" -> "Minimum number of samples required to be at a leaf node";
            case "Min Weight Fraction Leaf" ->
                    "Minimum weighted fraction of the sum total of weights (of all the input samples) required to be at a leaf node";
            case "Max Features" -> "Number of features to consider when looking for the best split";
            case "Max Leaf Nodes" -> "Limits tree's leaf nodes, prioritizing impurity reduction";
            case "Min Impurity Decrease" ->
                    "A node will be split if this split induces a decrease of the impurity greater than or equal to this value";
            case "Min Impurity Split" -> "Threshold for early stopping in tree growth";
            case "CCP Alpha" -> "Complexity parameter used for Minimal Cost-Complexity Pruning";
            case "Monotonic Constraints" -> "Monotonic constraints to be enforced in the decision tree";
            case "Presort" -> "Whether to presort the data to speed up the finding of best splits in fitting";

            // SVM Annotation
            case "Strictness (C)" -> "Penalty parameter of the error term";
            case "Kernel" ->
                    "Specifies the kernel type to be used in the algorithm (linear, poly, rbf, sigmoid, precomputed)";
            case "Degree" -> "Degree of the polynomial kernel function";
            case "Gamma" -> "Kernel coefficient for rbf, poly, and sigmoid";
            case "Bias (coef0)" -> "Independent term in kernel function";
            case "Shrinking" -> "Whether to use the shrinking heuristic";
            case "Probability" -> "Whether to enable probability estimates";
            case "Tolerance" -> "Tolerance for stopping criterion";
            case "Cache Size" -> "Size of the kernel cache";
            case "Class Weight" -> "Weights associated with classes in the form {class_label: weight}";
            case "Verbose" -> "Enable verbose output";
            case "Max Iterations" -> "Hard limit on iterations within solver";
            case "Decision Shape" -> "Whether to return one-vs-rest (ovr) or one-vs-one (ovo)";
            case "Break Ties" -> "Whether to break ties in decision function";
            case "Random State" -> "Seed for random number generator";
            case "Dual" -> "Select the algorithm to either solve the dual or primal optimization problem";
            case "Multi-Class" -> "Strategy to use for multiclass classification";
            case "Fit Intercept" -> "Whether to calculate the intercept for this model";
            case "Penalty" -> "Regularization term";

            // Neural Network Annotation
            case "Filters" -> "Number of output filters in the convolution";
            case "Kernel Size" -> "Size of the operation window";
            case "Strides" -> "Strides of the operation along the height and width";
            case "Padding" -> "Padding mode (valid, same)";
            case "Dilation Rate" -> "Dilation rate to use for dilated convolution";
            case "Groups" -> "Number of blocked connections from input channels to output channels";
            case "Activation" -> "Activation function to use";
            case "Use Bias" -> "Whether to use a bias vector";
            case "Kernel Initializer" -> "Initializer for the kernel weights matrix";
            case "Bias Initializer" -> "Initializer for the bias vector";
            case "Kernel Regularizer" -> "Regularizer function applied to the kernel weights matrix";
            case "Bias Regularizer" -> "Regularizer function applied to the bias vector";
            case "Activity Regularizer" -> "Regularizer function applied to the output of the layer";
            case "Kernel Constraint" -> "Constraint function applied to the kernel matrix";
            case "Bias Constraint" -> "Constraint function applied to the bias vector";

            case "Units" -> "Dimensionality of the output space";
            case "Rate" -> "Dropout rate";
            case "Noise Shape" -> "Shape of the binary dropout mask";
            case "Seed" -> "Random seed";
            case "Shape" -> "Target shape";
            case "Data Type" -> "Data type of the layer's weights (useful for quantization)";
            case "Batch Size" -> "Number of samples per gradient update";
            case "Name" -> "Name of the layer";
            case "Sparse" -> "Whether the placeholder created is sparse";
            case "Tensor" -> "Tensor to be used as input for the layer";
            case "Ragged" -> "Whether the placeholder created is ragged";
            case "Metadata (type_spec)" -> "Metadata for the layer's type specification";

            case "Pool Size" -> "Size of the pooling window";
            case "Gain" -> "Gain to be applied to the activation function";
            case "Minimum Value" -> "Lower bound of the range of random values to generate";
            case "Maximum Value" -> "Upper bound of the range of random values to generate";

            case "Optimizer" -> "Optimization Algorithm to Use";
            case "Loss" -> "Loss Function to Minimize";
            case "Loss Weights" -> "Weights to apply to the loss";
            case "Metrics" -> "Metrics to Monitor";
            case "Weighted Metrics" -> "List of metrics to be evaluated and weighted by sample_weight or class_weight";
            case "Run Eagerly" -> "Whether to run the model eagerly";
            case "Steps Per Execution" -> "Number of batches to run during each tf.function call";
            case "JIT Compile" -> "Whether to compile the model using XLA";
            case "PSS Shards" -> "Number of shards to split the model's variables into";
            case "Epochs" -> "Number of epochs to train the model";
            case "Validation Split (%)" -> "Fraction of the training data to be used as validation data";

            default -> text;
        };
    }
}
