package org.mithras.machinelearning.svm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SVC extends SVM
{
    @JsonProperty("C")
    public float C = 1.0f;
    @JsonProperty("kernel")
    public String kernel = "rbf";
    @JsonProperty("degree")
    public int degree = 3;
    @JsonProperty("gamma")
    public String gamma = "scale";
    @JsonProperty("coef0")
    public float coef0 = 0.0f;
    @JsonProperty("shrinking")
    public boolean shrinking = true;
    @JsonProperty("probability")
    public boolean probability = false;
    @JsonProperty("tol")
    public float tol = 1e-3f;
    @JsonProperty("cache_size")
    public float cache_size = 200f;
    @JsonProperty("verbose")
    public boolean verbose = false;
    @JsonProperty("max_iter")
    public int max_iter = -1;
    @JsonProperty("decision_function_shape")
    public String decision_function_shape = "ovr";
    @JsonProperty("break_ties")
    public boolean break_ties = false;
}
