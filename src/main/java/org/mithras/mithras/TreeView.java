package org.mithras.mithras;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;

public class TreeView
{
    private String modelName;

    public void initializeScene(Stage stage, String modelName) throws IOException
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DNNView.fxml")));

            this.modelName = modelName;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void setupFile()
    {
        StringBuilder sb = new StringBuilder();
        PyTranscriber.writeImports(sb);

        sb.append("\n");
        sb.append(ModelManager.models.get(modelName).toString());
        sb.append("plt.figure(figsize=(20, 20))\n");
        sb.append("plot_tree(").append(modelName).append(", filled=True)\n");
        sb.append("plt.savefig('output.png')\n");

        try (PrintWriter out = new PrintWriter("dtplt.py"))
        {
            out.println(sb);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void getImage()
    {
        BufferedImage image = null;
        try
        {
            Process p = new ProcessBuilder("python", "dtplt.py").start();
            p.waitFor();
            image = ImageIO.read(new File("output.png"));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
