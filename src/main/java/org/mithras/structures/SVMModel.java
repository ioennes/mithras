package org.mithras.structures;

import org.mithras.machinelearning.ConfigData;
import org.mithras.machinelearning.svm.SVM;

import static org.mithras.structures.StringComposer.compose;

public class SVMModel extends Model
{
    private final SVM svm;
    private ConfigData configData = new ConfigData();

    public SVMModel(String name, SVM svm)
    {
        super(name);
        this.svm = svm;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(getName()).append(" = ").append(svm.getClass().getSimpleName()).append("(");
        compose(s, getSvm());
        s.append("\n");
        return s.toString();
    }

    public ConfigData getConfigData()
    {
        return configData;
    }

    public SVM getSvm()
    {
        return svm;
    }
}
