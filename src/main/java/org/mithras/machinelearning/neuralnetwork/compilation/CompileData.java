package org.mithras.machinelearning.neuralnetwork.compilation;

import org.mithras.structures.StringComposer;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompileData
{
    public String optimizer;
    public String loss;
    public String metrics;
    public String[] lossWeights;
    public String weightedMetrics;
    public boolean runEagerly;
    public int stepsPerExecution;
    public boolean jitCompile;
    public int pssShards;
    public int epochs;
    public float validationSplit;
    public int batchSize;
    public int kfold = 1;

    public void getCompileData(StringBuilder sb, String modelName)
    {
        sb.append(modelName).append(".compile(");
        if (optimizer != null && !optimizer.isEmpty())
            sb.append("optimizer=\"").append(optimizer).append("\",\n");
        if (loss != null && !loss.isEmpty())
            sb.append("\tloss=\"").append(loss).append("\",\n");
        if (metrics != null && !metrics.isEmpty())
            sb.append("\tmetrics=[").append(metrics).append("],\n");
        if (lossWeights != null && lossWeights.length != 0)
            sb.append("\tloss_weights=").append(Arrays.toString(lossWeights)).append(",\n");
        if (weightedMetrics != null && !weightedMetrics.isEmpty())
            sb.append("\tweighted_metrics=").append(weightedMetrics).append(",\n");
        if (runEagerly || stepsPerExecution != 0 || jitCompile || pssShards != 0)
        {
            sb.append("\trun_eagerly=").append(runEagerly).append(",\n");
            sb.append("\tsteps_per_execution=").append(stepsPerExecution).append(",\n");
            sb.append("\tjit_compile=").append(jitCompile).append(",\n");
            sb.append("\texperimental_pss_shards=").append(pssShards).append(",");
        }

        if (sb.lastIndexOf("\n") == sb.length() - 1)
        {
            sb.deleteCharAt(sb.length() - 1);
        }
        if (sb.lastIndexOf(",") == sb.length() - 1)
        {
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append(")\n");

        String regex = "\n.*\\.compile\\(\\)";

        String str = sb.toString();

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        if (matcher.find()) {
            String replaced = matcher.replaceAll("");

            sb.setLength(0);;
            sb.append(replaced);
        }

        sb.append(modelName).append(".fit(").append("x=train, epochs=").append(epochs)
                .append(", batch_size=").append(batchSize).append(", validation_split=")
                .append(validationSplit).append(")");
    }
}
