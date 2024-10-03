package org.mithras.mithras;

import java.util.Arrays;
import java.util.HashSet;

public class ExpertFields
{
    private static boolean isExpert = false;
    private static final HashSet<String> expertFields = new HashSet<>(
            Arrays.asList(
                    "min_weight_fraction_leaf",
                    "ccp_alpha",
                    "monotonic_cst",
                    "gamma",
                    "gammaText",
                    "coef0",
                    "class_weight",
                    "decision_function_shape",
                    "filters",
                    "dilation_rate",
                    "kernel_initializer",
                    "bias_initializer",
                    "kernel_regularizer",
                    "bias_regularizer",
                    "activity_regularizer",
                    "kernel_constraint",
                    "bias_constraint",
                    "jitCompile",
                    "pssShards",
                    "min_impurity_split",
                    "validationSplit",
                    "gain",
                    "minval",
                    "maxval"
            )
    );

    public static boolean isExpertField(String field)
    {
        return expertFields.contains(field);
    }

    public static boolean isExpert()
    {
        return isExpert;
    }
    public static void setExpert(boolean expert)
    {
        isExpert = expert;
    }
}
