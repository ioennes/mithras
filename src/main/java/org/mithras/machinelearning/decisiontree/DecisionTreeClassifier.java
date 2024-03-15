package org.mithras.machinelearning.decisiontree;

public class DecisionTreeClassifier extends Tree
{
    public String criterion = "gini";
    public String splitter = "best";
    public int max_depth;
    public float min_samples_split = 2;
    public float min_samples_leaf = 1;
    public float min_weight_fraction_leaf = 0.0f;
    public int max_features;
    public int random_state;
    public int max_leaf_nodes;
    public float min_impurity_decrease = 0.0f;
    // class weight
    public float ccp_alpha = 0.0f;
    public int[] monotonic_cst;
}
