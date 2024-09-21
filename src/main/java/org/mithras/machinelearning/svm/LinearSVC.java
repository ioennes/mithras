package org.mithras.machinelearning.svm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinearSVC extends SVM
{
    @JsonProperty("penalty")
    public String penalty = "l2";
    @JsonProperty("loss")
    public String loss = "squared_hinge";
    @JsonProperty("dual")
    public boolean dual = true;
    @JsonProperty("tol")
    public float tol = 1e-4f;
    @JsonProperty("C")
    public float C = 1.0f;
    @JsonProperty("multi_class")
    public String multi_class = "ovr";
    @JsonProperty("fit_intercept")
    public boolean fit_intercept = true;
    @JsonProperty("intercept_scaling")
    public float intercept_scaling = 1.0f;
    @JsonProperty("verbose")
    public int verbose = 0;
    @JsonProperty("max_iter")
    public int max_iter = 1000;
    @JsonProperty("class_weight")
    private String class_weight = null;
}
