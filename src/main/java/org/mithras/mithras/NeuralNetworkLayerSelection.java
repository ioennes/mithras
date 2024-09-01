package org.mithras.mithras;

import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.mithras.machinelearning.neuralnetwork.layers.LayerFactory;
import org.mithras.structures.State;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

import static org.mithras.mithras.Translator.translate;
import static org.reflections.scanners.Scanners.SubTypes;

public class NeuralNetworkLayerSelection
{
    String modelName;

    public void initializeScene(Stage stage, String modelName)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("NeuralNetworkLayerSelection.fxml")));
            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(StyleUtil.getCss());

            this.modelName = modelName;
            State.setModelName(modelName);

            ScrollPane scrollPane = (ScrollPane) scene.lookup("#nnscrollpane");
            GridPane gridPane = new GridPane();
            gridPane.setPadding(new Insets(20));
            gridPane.setHgap(20);
            gridPane.setVgap(5);

            gridPane.minWidthProperty().bind(scrollPane.widthProperty());
            gridPane.minHeightProperty().bind(scrollPane.heightProperty());
            gridPane.setMaxWidth(Double.MAX_VALUE);
            gridPane.setMaxHeight(Double.MAX_VALUE);

            createLayerButtons(gridPane);

            scrollPane.setContent(gridPane);
            stage.setScene(scene);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void createLayerButtons(GridPane gridPane)
    {
        String layerDirLocation = "org.mithras.machinelearning.neuralnetwork.layers.";
        String[] packages = new String[]{"corePkg", "convolutionPkg", "poolPkg"};

        for (int i = 0; i < packages.length; i++)
        {
            String pkg = packages[i];
            Reflections reflect = new Reflections(new ConfigurationBuilder()
                    .setUrls(ClasspathHelper.forPackage(layerDirLocation + pkg))
                    .setScanners(new SubTypesScanner(false)));

            Set<Class<?>> classes = reflect.get(SubTypes.of(Object.class).asClass());
            ArrayList<String> orderedLayers = new ArrayList<>();

            Text pkgLabel = new Text(translate(pkg));
            gridPane.add(pkgLabel, i, 0);

            GridPane.setHalignment(pkgLabel, HPos.CENTER);

            VBox vbox = new VBox();
            vbox.setMaxHeight(Double.MAX_VALUE);
            gridPane.add(vbox, i, 1);

            for (Class<?> cls : classes)
            {
                if (cls.getPackage().getName().equals(layerDirLocation + pkg))
                {
                    orderedLayers.add(cls.getSimpleName());
                }
            }
            orderedLayers.sort(String::compareToIgnoreCase);

            for (String layer : orderedLayers)
            {
                Button layerBtn = new Button(layer);
                layerBtn.setStyle("-fx-min-width: 250px");
                layerBtn.setOnAction(e ->
                        new ClassInputDialog().start(LayerFactory.createLayer(layer), modelName, -1));

                vbox.getChildren().add(layerBtn);
            }
        }
    }

    public void switchToLayerView() throws IOException
    {
        SceneManager.switchToDNNCodeView(State.getModelName());
    }

    public void switchToMain() throws IOException
    {
        SceneManager.switchToMain();
        State.setModelName(null);
    }
}