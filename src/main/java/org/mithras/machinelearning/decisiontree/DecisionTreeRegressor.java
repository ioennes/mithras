package org.mithras.machinelearning.decisiontree;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DecisionTreeRegressor extends Tree
{
    @JsonProperty("criterion")
    public String criterion = "squared_error";
    @JsonProperty("splitter")
    public String splitter = "best";
    @JsonProperty("max_depth")
    public int max_depth;
    @JsonProperty("min_samples_split")
    public int min_samples_split = 2;
    @JsonProperty("min_samples_leaf")
    public int min_samples_leaf = 1;
    @JsonProperty("min_weight_fraction_leaf")
    public float min_weight_fraction_leaf = 0.0f;
    @JsonProperty("max_features")
    public int max_features;
    @JsonProperty("max_leaf_nodes")
    public int max_leaf_nodes;
    @JsonProperty("min_impurity_decrease")
    public float min_impurity_decrease = 0.0f;
    // class weight
    @JsonProperty("ccp_alpha")
    public float ccp_alpha = 0.0f;
    @JsonProperty("monotonic_cst")
    public int[] monotonic_cst;
}
