module org.mithras.mithras
{
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    requires org.reflections;
    requires py4j;
    requires com.fasterxml.jackson.databind;
    requires spring.web;
    requires java.net.http;
    requires jdk.httpserver;
    requires javafx.swing;
    requires org.json;
    requires javafx.graphics;
    requires io.github.classgraph;
    requires org.jfree.jfreechart;
    requires java.logging;

    opens org.mithras.machinelearning.neuralnetwork.layers.corePkg to com.fasterxml.jackson.databind;
    opens org.mithras.machinelearning.neuralnetwork.layers.poolPkg to com.fasterxml.jackson.databind;
    opens org.mithras.machinelearning.neuralnetwork.layers.convolutionPkg to com.fasterxml.jackson.databind;
    opens org.mithras.machinelearning.neuralnetwork.layers to com.fasterxml.jackson.databind;
    opens org.mithras.machinelearning.svm to com.fasterxml.jackson.databind;
    exports org.mithras.machinelearning.svm to com.fasterxml.jackson.databind;
    exports org.mithras.machinelearning.decisiontree to com.fasterxml.jackson.databind;

    opens org.mithras.mithras to javafx.fxml;
    exports org.mithras.structures;
    exports org.mithras.mithras;
}