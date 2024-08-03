package org.mithras.structures;

import org.mithras.mithras.ModelStatistics;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

public class Model
{
    private String name;
    private ModelStatistics metrics;
    private BufferedImage plot;

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

    public ModelStatistics getStatistics()
    {
        return metrics;
    }

    public void setStatistics(ModelStatistics statistics)
    {
        this.metrics = statistics;
    }

    public BufferedImage getPlot()
    {
        return plot;
    }

    public void setPlot(Path plot_path) throws IOException
    {
        this.plot = ImageIO.read(plot_path.toFile());
    }

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }
}