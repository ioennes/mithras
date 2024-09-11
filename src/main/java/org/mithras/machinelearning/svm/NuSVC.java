package org.mithras.machinelearning.svm;

public class NuSVC extends SVM
{
    public float nu = 0.5f;
    public String kernel = "rbf";
    public int degree = 3;
    public String gamma = "scale";
    public float coef0 = 0.0f;
    public boolean shrinking = true;
    public boolean probability = true;
    public float tol = 1e-3f;
    public float cache_size = 200f;
    public boolean verbose = false;
    public int max_iter = -1;
    public String decision_function_shape = "ovr";
    public boolean break_ties = false;
}
