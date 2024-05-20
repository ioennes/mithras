package org.mithras.structures;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class DatasetHandler
{
    public static Path datasetPath;
    public static Path trainPath;
    public static Path testPath;
    public static Path validPath;
    static LabelRepresentation labelRepresentation = LabelRepresentation.IMAGE_FOLDER;

    public static boolean setDirectory(final Path selectedDatasetPath) throws IOException
    {
        if (!Files.isDirectory(selectedDatasetPath))
        {
            throw new IOException("The selected path is not a directory.");
        }

        datasetPath = selectedDatasetPath;
        trainPath = null;
        testPath = null;
        validPath = null;

        // Check for train, test, and valid directories in the selected path
        if (Files.isDirectory(datasetPath.resolve("train")))
        {
            trainPath = datasetPath.resolve("train");
        }
        if (Files.isDirectory(datasetPath.resolve("test")))
        {
            testPath = datasetPath.resolve("test");
        }
        if (Files.isDirectory(datasetPath.resolve("valid")))
        {
            validPath = datasetPath.resolve("valid");
        }

        if (trainPath == null)
        {
            throw new IOException("No train path specified, make sure it is named train.");
        }

        System.out.println("Train Path: " + trainPath);
        return true;
    }

    public static BufferedImage getRandomImage()
    {
        if (trainPath == null || !Files.exists(trainPath.resolve("images")))
        {
            System.out.println("Training path is not set or does not exist.");
            return null;
        }

        Path imagesPath = trainPath.resolve("images");
        try (Stream<Path> files = Files.walk(imagesPath))
        {
            List<Path> imageFiles = files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".jpg") || path.toString().endsWith(".png"))
                    .toList();

            if (imageFiles.isEmpty())
            {
                System.out.println("No images found in the directory.");
                return null;
            }

            Random random = new Random();
            Path randomImagePath = imageFiles.get(random.nextInt(imageFiles.size()));

            System.out.println("Random Image: " + randomImagePath);

            return ImageIO.read(new File(randomImagePath.toString()));
        } catch (IOException e)
        {
            System.out.println("Error accessing training images directory.");
        }
        return null;
    }

    public static String preprocessImageFolder(int x, int y)
    {
        StringBuilder sb = new StringBuilder();

        sb.append("train = tf.keras.preprocessing.image_dataset_from_directory(\n");
        sb.append("    \"" + (trainPath == null ? "ENTER_PATH" : trainPath.toString()) + "\",\n");
        sb.append("    image_size=(" + x + ", " + y + "))\n");
        sb.append("train = train.prefetch(tf.data.AUTOTUNE)\n");

        if (testPath == null)
        {
            return sb.toString();
        }
        sb.append("test = tf.keras.preprocessing.image_dataset_from_directory(\n");
        sb.append("    \"" + testPath.toString() + "\",\n");
        sb.append("    image_size=(" + x + ", " + y + "))\n");
        sb.append("test = test.prefetch(tf.data.AUTOTUNE)\n");

        if (validPath == null)
        {
            return sb.toString();
        }
        sb.append("valid = tf.keras.preprocessing.image_dataset_from_directory(\n");
        sb.append("    \"" + validPath.toString() + "\",\n");
        sb.append("    image_size=(" + x + ", " + y + "))\n");
        sb.append("valid = valid.prefetch(tf.data.AUTOTUNE)\n\n");

        return sb.toString();
    }

    private static void preprocessImageCSV()
    {
        // TODO
    }

    enum LabelRepresentation
    {
        IMAGE_FOLDER,
        IMAGE_CSV,
    }
}
