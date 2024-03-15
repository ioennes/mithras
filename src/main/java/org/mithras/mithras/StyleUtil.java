package org.mithras.mithras;

import java.util.Objects;

public class StyleUtil
{
    private static String css;

    public static String getCss()
    {
        if (css == null)
        {
            css = Objects.requireNonNull(
                    StyleUtil.class.getResource("stylesheet.css")
            ).toExternalForm();
        }
        return css;
    }
}
