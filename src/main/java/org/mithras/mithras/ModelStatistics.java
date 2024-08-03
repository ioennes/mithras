package org.mithras.mithras;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ModelStatistics
{
    public int num_iterations = 1;

    ArrayList<ArrayList<Double>> avg_loss = new ArrayList<>();
    ArrayList<ArrayList<Double>> avg_accuracy = new ArrayList<>();
    ArrayList<ArrayList<Double>> avg_f1 = new ArrayList<>();
    ArrayList<ArrayList<Double>> avg_recall = new ArrayList<>();
    ArrayList<ArrayList<Double>> avg_precision = new ArrayList<>();
    ArrayList<ArrayList<Double>> std_loss = new ArrayList<>();
    ArrayList<ArrayList<Double>> std_accuracy = new ArrayList<>();
    ArrayList<ArrayList<Double>> std_f1 = new ArrayList<>();
    ArrayList<ArrayList<Double>> std_recall = new ArrayList<>();
    ArrayList<ArrayList<Double>> std_precision = new ArrayList<>();

    private static ArrayList<Double> calculateAverage(ArrayList<ArrayList<Double>> raw_values)
    {
        ArrayList<Double> average_values = new ArrayList<>();
        for (int i = 0; i < raw_values.get(0).size(); i++)
        {
            double sum = 0;

            for (ArrayList<Double> rawValue : raw_values)
            {
                sum += rawValue.get(i);
            }

            average_values.add(sum / raw_values.size());
        }
        return average_values;
    }

    private static ArrayList<Double> calculateStandardDeviation(ArrayList<ArrayList<Double>> raw_values, ArrayList<Double> means)
    {
        ArrayList<Double> stddev_values = new ArrayList<>();
        for (int i = 0; i < raw_values.get(0).size(); i++)
        {
            double sum = 0;

            for (ArrayList<Double> rawValue : raw_values)
            {
                sum += Math.pow(rawValue.get(i) - means.get(i), 2);
            }

            stddev_values.add(Math.sqrt(sum / raw_values.size()));
        }

        return stddev_values;
    }

    public void run(String modelName) throws IOException, InterruptedException
    {
        ArrayList<ArrayList<ArrayList<Double>>> loss = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Double>>> accuracy = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Double>>> f1 = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Double>>> recall = new ArrayList<>();
        ArrayList<ArrayList<ArrayList<Double>>> precision = new ArrayList<>();

        loss.add(new ArrayList<>());
        loss.add(new ArrayList<>());
        accuracy.add(new ArrayList<>());
        accuracy.add(new ArrayList<>());
        f1.add(new ArrayList<>());
        f1.add(new ArrayList<>());
        recall.add(new ArrayList<>());
        recall.add(new ArrayList<>());
        precision.add(new ArrayList<>());
        precision.add(new ArrayList<>());

        avg_loss.add(new ArrayList<>());
        avg_loss.add(new ArrayList<>());
        std_loss.add(new ArrayList<>());
        std_loss.add(new ArrayList<>());
        avg_accuracy.add(new ArrayList<>());
        avg_accuracy.add(new ArrayList<>());
        std_accuracy.add(new ArrayList<>());
        std_accuracy.add(new ArrayList<>());
        avg_f1.add(new ArrayList<>());
        avg_f1.add(new ArrayList<>());
        std_f1.add(new ArrayList<>());
        std_f1.add(new ArrayList<>());
        avg_recall.add(new ArrayList<>());
        avg_recall.add(new ArrayList<>());
        std_recall.add(new ArrayList<>());
        std_recall.add(new ArrayList<>());
        avg_precision.add(new ArrayList<>());
        avg_precision.add(new ArrayList<>());
        std_precision.add(new ArrayList<>());
        std_precision.add(new ArrayList<>());

        for (int i = 0; i < num_iterations; i++)
        {
            JSONObject metrics = PyTranscriber.run(modelName);
            for (String key : metrics.keySet())
            {
                JSONArray metric = metrics.getJSONArray(key);
                ArrayList<Double> metricAL = new ArrayList<>();

                for (int j = 0; j < metric.length(); j++)
                {
                    metricAL.add(metric.getDouble(j));
                }

                switch (key)
                {
                    case "loss":
                        loss.get(0).add(metricAL.isEmpty() ? new ArrayList<>() : metricAL);
                        break;
                    case "accuracy":
                        accuracy.get(0).add(metricAL.isEmpty() ? new ArrayList<>() : metricAL);
                        break;
                    case "f1_score":
                        f1.get(0).add(metricAL.isEmpty() ? new ArrayList<>() : metricAL);
                        break;
                    case "recall":
                        recall.get(0).add(metricAL.isEmpty() ? new ArrayList<>() : metricAL);
                        break;
                    case "precision":
                        precision.get(0).add(metricAL.isEmpty() ? new ArrayList<>() : metricAL);
                        break;
                    case "val_loss":
                        loss.get(1).add(metricAL.isEmpty() ? new ArrayList<>() : metricAL);
                        break;
                    case "val_accuracy":
                        accuracy.get(1).add(metricAL.isEmpty() ? new ArrayList<>() : metricAL);
                        break;
                    case "val_f1_score":
                        f1.get(1).add(metricAL.isEmpty() ? new ArrayList<>() : metricAL);
                        break;
                    case "val_recall":
                        recall.get(1).add(metricAL.isEmpty() ? new ArrayList<>() : metricAL);
                        break;
                    case "val_precision":
                        precision.get(1).add(metricAL.isEmpty() ? new ArrayList<>() : metricAL);
                        break;
                }
            }
        }

        for (int i = 0; i < 2; i++)
        {
            if (!loss.get(i).isEmpty())
            {
                avg_loss.set(i, calculateAverage(loss.get(i)));
                std_loss.set(i, calculateStandardDeviation(loss.get(i), avg_loss.get(i)));
            }
            if (!accuracy.get(i).isEmpty())
            {
                avg_accuracy.set(i, calculateAverage(accuracy.get(i)));
                std_accuracy.set(i, calculateStandardDeviation(accuracy.get(i), avg_accuracy.get(i)));
            }
            if (!f1.get(i).isEmpty())
            {
                avg_f1.set(i, calculateAverage(f1.get(i)));
                std_f1.set(i, calculateStandardDeviation(f1.get(i), avg_f1.get(i)));
            }
            if (!recall.get(i).isEmpty())
            {
                avg_recall.set(i, calculateAverage(recall.get(i)));
                std_recall.set(i, calculateStandardDeviation(recall.get(i), avg_recall.get(i)));
            }
            if (!precision.get(i).isEmpty())
            {
                avg_precision.set(i, calculateAverage(precision.get(i)));
                std_precision.set(i, calculateStandardDeviation(precision.get(i), avg_precision.get(i)));
            }
        }
        System.out.println(avg_loss.toString());
        System.out.println(avg_accuracy.toString());
    }
}
