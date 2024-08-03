package org.mithras.machinelearning.neuralnetwork.layers;

import javafx.scene.image.WritableImage;

public interface Activatable
{
    String getActivation();

    WritableImage neuralFunction(WritableImage inputInstance);
}
