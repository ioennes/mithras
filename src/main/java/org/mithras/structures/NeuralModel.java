package org.mithras.structures;

import org.mithras.machinelearning.neuralnetwork.compilation.CompileData;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;

import java.util.ArrayList;

public class NeuralModel extends Model
{
    private final ArrayList<BaseLayer> layers = new ArrayList<>();
    private final CompileData compilationData = new CompileData();

    public NeuralModel(String name)
    {
        super(name);
    }

    public ArrayList<BaseLayer> getLayers()
    {
        return layers;
    }

    public void addLayer(BaseLayer layer)
    {
        layers.add(layer);
    }

    public void removeLayer(BaseLayer layer)
    {
        layers.remove(layer);
    }

    public CompileData getCompilationData()
    {
        return compilationData;
    }

    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(getName()).append(" = Sequential([\n\t");
        for (BaseLayer layer : layers)
        {
            s.append(layer.toString()).append(",\n\t");
        }
        s.replace(s.length() - 3, s.length(), "");
        s.append("\n])\n\n");

        getCompilationData().getCompileData(s, getName());
        s.append("\n");

        return s.toString();
    }
}
