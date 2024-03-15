package org.mithras.structures;

import org.mithras.machinelearning.ConfigData;
import org.mithras.machinelearning.decisiontree.Tree;

import static org.mithras.structures.StringComposer.compose;

public class TreeModel extends Model
{
    private final Tree tree;
    private ConfigData configData = new ConfigData();

    public TreeModel(String name, Tree tree)
    {
        super(name);
        this.tree = tree;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(getName()).append(" = ").append(tree.getClass().getSimpleName()).append("(");
        compose(s, getTree());
        s.append("\n");
        return s.toString();
    }

    public ConfigData getConfigData()
    {
        return configData;
    }

    public Tree getTree()
    {
        return tree;
    }
}
