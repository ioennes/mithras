package org.mithras.structures;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Model
{
    private String name;
    private JSONObject metrics;
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

    public JSONObject getMetrics()
    {
        return metrics;
    }

    public void setMetrics(Path metrics_path) throws IOException
    {
        this.metrics = new JSONObject(new String(Files.readAllBytes(metrics_path)));
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