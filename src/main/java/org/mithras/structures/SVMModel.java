package org.mithras.structures;

import org.mithras.machinelearning.ConfigData;
import org.mithras.machinelearning.svm.SVM;

import static org.mithras.structures.StringComposer.compose;

public class SVMModel extends Model
{
    private final SVM svm;
    private final ConfigData configData = new ConfigData();

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
        s.append("\n\n");
        s.append("xtr, xts, ytr, yts = train_test_split(X, y, test_size=0.2, stratify=y, shuffle=True, random_state=43)\n");
        s.append(getName()).append(".fit(xtr, ytr)\n");
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
