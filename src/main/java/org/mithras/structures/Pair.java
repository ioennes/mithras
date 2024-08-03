package org.mithras.structures;

import java.util.Objects;

public class Pair<K, V>
{
    private K first;
    private V second;

    public Pair(K first, V second)
    {
        this.first = first;
        this.second = second;
    }

    public K getFirst()
    {
        return first;
    }

    public void setFirst(K first)
    {
        this.first = first;
    }

    public V getSecond()
    {
        return second;
    }

    public void setSecond(V second)
    {
        this.second = second;
    }

    @Override
    public String toString()
    {
        return "Pair{" + "first=" + first + ", second=" + second + '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (!Objects.equals(first, pair.first)) return false;
        return Objects.equals(second, pair.second);
    }

    @Override
    public int hashCode()
    {
        int result = first != null ? first.hashCode() : 0;
        result = 31 * result + (second != null ? second.hashCode() : 0);
        return result;
    }
}