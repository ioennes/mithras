package org.mithras.machinelearning.svm;

public class LinearSVR extends SVM
{
    public float epsilon = 0.0f;
    public float tol = 1e-4f;
    public float C = 1.0f;
    public String loss = "epsilon_insensitive";
    public boolean fit_intercept = true;
    public float intercept_scaling = 1.0f;
    public boolean dual = true;
    public int verbose = 0;
    public int max_iter = 1000;
}
