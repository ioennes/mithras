package org.mithras.structures;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

import static org.mithras.machinelearning.neuralnetwork.layers.BaseLayer.isDefaultValue;

public class StringComposer
{
    public static boolean isStringField(Class<?> fieldType)
    {
        return fieldType.equals(String.class);
    }

    public static boolean isArrayField(Class<?> fieldType, Class<?> componentType)
    {
        return fieldType.isArray() && fieldType.getComponentType().equals(componentType);
    }

    public static void appendStringValue(StringBuilder output, String fieldName, Object value)
    {
        if (fieldName.contains("regularizer") || fieldName.contains("initializer"))
        {
            output.append(fieldName).append("=").append(value).append(", ");
        }
        else
        {
            output.append(fieldName).append("=\"").append(value).append("\", ");
        }
    }

    public static void appendArrayValue(StringBuilder output, String fieldName, int[] array)
    {
        String arrayString;
        if (array.length == 1)
        {
            arrayString = array[0] + ",";
        }
        else
        {
            arrayString = Arrays.toString(array).replaceAll("[\\[\\]]", "");
        }
        output.append(fieldName).append("=(").append(arrayString).append("), ");
    }

    public static void appendOtherValue(StringBuilder output, String fieldName, Object value)
    {
        if (value instanceof Boolean)
        {
            value = ((Boolean) value) ? "True" : "False";
        }
        if (!isDefaultValue(value))
        {
            output.append(fieldName).append("=").append(value).append(", ");
        }
    }

    public static void trimTrailingComma(StringBuilder output)
    {
        int lastCommaIndex = output.lastIndexOf(", ");
        if (lastCommaIndex != -1)
        {
            output.delete(lastCommaIndex, output.length());
        }
    }

    public static void compose(StringBuilder output, Object object)
    {
        for (Field field : object.getClass().getFields())
        {
            if (field.getName().equals("kfold") || field.getName().equals("dtype"))
            {
                continue;
            }
            try
            {
                if (Modifier.isPrivate(field.getModifiers()) || field.get(object) == null)
                {
                    continue;
                }
            } catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }

            Object value = null;
            try
            {
                value = field.get(object);
            } catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
            String fieldName = field.getName();
            Class<?> fieldType = field.getType();

            if (isStringField(fieldType) && !isDefaultValue(value))
            {
                appendStringValue(output, fieldName, value);
            }
            else if (isArrayField(fieldType, int.class))
            {
                appendArrayValue(output, fieldName, (int[]) value);
            }
            else
            {
                appendOtherValue(output, fieldName, value);
            }
        }

        trimTrailingComma(output);
        output.append(")");
    }
}
