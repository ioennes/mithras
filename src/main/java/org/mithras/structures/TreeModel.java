package org.mithras.structures;

import org.mithras.machinelearning.decisiontree.Tree;

import static org.mithras.structures.StringComposer.compose;

public class TreeModel extends Model
{
    private final Tree tree;

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
        s.append("\n\n");
        s.append("xtr, xts, ytr, yts = train_test_split(X, y, test_size=0.2, stratify=y, shuffle=True, random_state=43)\n");
        s.append(getName()).append(".fit(xtr, ytr)\n");
        return s.toString();
    }

    public Tree getTree()
    {
        return tree;
    }
}
