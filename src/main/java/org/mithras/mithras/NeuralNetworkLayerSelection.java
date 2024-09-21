package org.mithras.mithras;

import javafx.application.Platform;
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
import org.jfree.chart.ui.Layer;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;
import org.mithras.machinelearning.neuralnetwork.layers.LayerFactory;
import org.mithras.structures.State;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import io.github.classgraph.ClassInfo;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.mithras.mithras.Translator.translate;
import static org.reflections.scanners.Scanners.SubTypes;

public class NeuralNetworkLayerSelection
{
    String modelName;
    private static final Map<String, List<String>> classCache = new ConcurrentHashMap<>();

    public void initializeScene(Stage stage, String modelName)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("NeuralNetworkLayerSelection.fxml")));
            Scene scene = new Scene(root);
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

    private void createLayerButtons(GridPane gridPane) {
        String layerDirLocation = "org.mithras.machinelearning.neuralnetwork.layers.";
        String[] packages = new String[]{"corePkg", "convolutionPkg", "poolPkg"};

        if (classCache.isEmpty()) {
            ExecutorService executorService = Executors.newFixedThreadPool(packages.length);

            List<Future<?>> futures = new ArrayList<>();

            for (String pkg : packages) {
                Future<?> future = executorService.submit(() -> {
                    try (ScanResult scanResult = new ClassGraph()
                            .enableClassInfo()
                            .acceptPackages(layerDirLocation + pkg)
                            .scan()) {
                        List<String> classNames = scanResult.getSubclasses(BaseLayer.class.getName())
                                .filter(classInfo -> classInfo.getPackageName().equals(layerDirLocation + pkg))
                                .stream() // Convert ClassInfoList to Stream<ClassInfo>
                                .map(ClassInfo::getSimpleName)
                                .sorted(String::compareToIgnoreCase)
                                .collect(Collectors.toList());
                        classCache.put(pkg, classNames);
                    }
                });
                futures.add(future);
            }

            // Wait for all futures to complete
            for (Future<?> future : futures) {
                try {
                    future.get();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            executorService.shutdown();
        }

        Platform.runLater(() -> {
            for (int i = 0; i < packages.length; i++) {
                String pkg = packages[i];
                List<String> orderedLayers = classCache.get(pkg);
                if (orderedLayers == null) continue;

                Text pkgLabel = new Text(translate(pkg));
                gridPane.add(pkgLabel, i, 0);
                GridPane.setHalignment(pkgLabel, HPos.CENTER);

                VBox vbox = new VBox();
                vbox.setMaxHeight(Double.MAX_VALUE);
                gridPane.add(vbox, i, 1);

                for (String layer : orderedLayers) {
                    Button layerBtn = new Button(layer);
                    layerBtn.setStyle("-fx-min-width: 250px");
                    layerBtn.setOnAction(e -> new ClassInputDialog().start(LayerFactory.createLayer(layer), modelName, -1));
                    vbox.getChildren().add(layerBtn);
                }
            }
        });
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