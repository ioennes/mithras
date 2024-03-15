package org.mithras.structures;

public class Model
{
    private String name;

    public Model(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName()
    {
        this.name = name;
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}