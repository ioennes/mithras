package org.mithras.structures;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class DatasetHandler
{
    private static File dataset;
    private static boolean isNumeric = true;
    private static boolean isRegression;
    private static boolean isGrayscale = true;

    public static File getDataset()
    {
        return DatasetHandler.dataset;
    }

    public static void setDataset(File dataset)
    {
        DatasetHandler.dataset = dataset;
    }

    public static boolean isNumeric()
    {
        return DatasetHandler.isNumeric;
    }

    public static void setNumeric(boolean isNumeric)
    {
        DatasetHandler.isNumeric = isNumeric;
    }

    public static boolean isRegression()
    {
        return DatasetHandler.isRegression;
    }

    public static void setRegression(boolean isRegression)
    {
        DatasetHandler.isRegression = isRegression;
    }

    public static boolean isGrayscale()
    {
        return DatasetHandler.isGrayscale;
    }

    public static void setGrayscale(boolean isGrayscale)
    {
        DatasetHandler.isGrayscale = isGrayscale;
    }

    public static String getDatasetName()
    {
        return dataset.getName();
    }

    public static String preprocessFile()
    {
        String sb = "filepath = \"" + dataset.getAbsolutePath() + "\"\n";

        if (DatasetHandler.isNumeric())
        {
            sb += "df = pd.read_csv(filepath)\n" +
                    "X = df.iloc[:, :-1]\n" +
                    "y = df.iloc[:, -1]\n";
        }
        else
        {
            sb += "df = pd.read_csv(filepath)\n" +
                    "images = []\n" +
                    "labels = []\n" +
                    "for index, row in df.iterrows():\n" +
                    "    img_path = os.path.join('" + dataset.getParent() + "', row[0])\n" +
                    "    image = cv2.imread(img_path, cv2.IMREAD_" + (DatasetHandler.isGrayscale() ? "GRAYSCALE" : "COLOR") + ")\n" +
                    "    images.append(image)\n" +
                    "    labels.append(row[1])\n" +
                    "X = np.array(images)\n" +
                    "y = pd.get_dummies(labels).values\n";
        }

        return sb;
    }

    public static BufferedImage getImage()
    {
        if (DatasetHandler.isNumeric())
        {
            return null;
        }

        String csvFile = DatasetHandler.dataset.getPath();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile)))
        {
            String line;
            ArrayList<String> firstColumnData = new ArrayList<>();

            while ((line = br.readLine()) != null)
            {
                String[] columns = line.split(",");

                if (columns.length > 0)
                {
                    firstColumnData.add(columns[0].trim());
                }
            }

            Random random = new Random();
            int randomIndex = random.nextInt(firstColumnData.size());
            String imagePath = firstColumnData.get(randomIndex);

            File imgFile = new File(dataset.getParent() + "/" + imagePath);
            if (imgFile.exists())
            {
                return ImageIO.read(imgFile);
            }
            else
            {
                System.out.println("Image file not found: " + imagePath);
                return null;
            }

        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
