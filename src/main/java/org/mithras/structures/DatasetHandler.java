package org.mithras.structures;

import java.io.File;

public class DatasetHandler
{
    private static File dataset;

    public static File getDataset()
    {
        return dataset;
    }

    public static void setDataset(File dataset)
    {
        DatasetHandler.dataset = dataset;
    }

    public static String preprocessFile()
    {
        String sb = "filepath = \"" + dataset.getAbsolutePath() + "\"\n" +
                "df = pd.read_csv(filepath)\n" +
                "X = df.iloc[:, :-1]\n" +
                "y = df.iloc[:, -1]\n";
        return sb;
    }
}