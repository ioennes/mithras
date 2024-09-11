package org.mithras.machinelearning.svm;

public class LinearSVC extends SVM
{
    public String penalty = "l2";
    public String loss = "squared_hinge";
    public boolean dual = true;
    public float tol = 1e-4f;
    public float C = 1.0f;
    public String multi_class = "ovr";
    public boolean fit_intercept = true;
    public float intercept_scaling = 1.0f;
    // class weight : UNDERSTAND LATER
    public int verbose = 0;
    public int max_iter = 1000;
}
