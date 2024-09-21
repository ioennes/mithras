package org.mithras.machinelearning.svm;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LinearSVR extends SVM
{
    @JsonProperty("epsilon")
    public float epsilon = 0.0f;
    @JsonProperty("tol")
    public float tol = 1e-4f;
    @JsonProperty("C")
    public float C = 1.0f;
    @JsonProperty("loss")
    public String loss = "epsilon_insensitive";
    @JsonProperty("fit_intercept")
    public boolean fit_intercept = true;
    @JsonProperty("intercept_scaling")
    public float intercept_scaling = 1.0f;
    @JsonProperty("dual")
    public boolean dual = true;
    @JsonProperty("verbose")
    public int verbose = 0;
    @JsonProperty("max_iter")
    public int max_iter = 1000;
}
