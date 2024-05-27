package org.mithras.mithras;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;
import org.mithras.structures.NeuralModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NeuralNetworkView
{
    private final int unitThreshold = 8;
    private final int whThreshold = 80;
    private final int vgap = 40;
    private final int hgap = 100;
    ArrayList<WritableImage> oldImages = new ArrayList<>();
    ArrayList<WritableImage> newImages = new ArrayList<>();
    private String modelName;
    private Stage stage;
    private Pane pane;
    private WritableImage image;
    private final LinkedHashMap<Integer, Group> layerPositions = new LinkedHashMap<>();

    public void initializeScene(Stage stage, String modelName)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DNNView.fxml")));

            this.modelName = modelName;
            this.stage = stage;
            pane = new Pane();

            assignImage();

            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(pane);
            scrollPane.setFitToHeight(true);
            scrollPane.setFitToWidth(true);

            pane.getChildren().add(root);

            Button backButton = (Button) root.lookup("#backbtn");
            pane.getChildren().remove(backButton);

            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(backButton);
            StackPane.setAlignment(backButton, Pos.BOTTOM_CENTER);
            StackPane.setMargin(backButton, new Insets(0, 0, 25, 0));

            stackPane.prefWidthProperty().bind(pane.widthProperty());
            stackPane.prefHeightProperty().bind(pane.heightProperty());

            pane.getChildren().add(stackPane);

            drawLayers((int) stage.getHeight());
            connectLayers((int) stage.getHeight());

            scrollPane.setStyle("-fx-background-color: black");
            pane.setStyle("-fx-background-color: black");
            Scene scene = new Scene(scrollPane, 1200, 1000);
            scene.getStylesheets().add(StyleUtil.getCss());
            stage.setScene(scene);

            stage.heightProperty().addListener((observable, oldValue, newValue) -> {
                pane.getChildren().clear();
                pane.getChildren().add(root);
                try
                {
                    drawLayers(newValue.intValue());
                } catch (ExecutionException | InterruptedException e)
                {
                    throw new RuntimeException(e);
                }
                connectLayers(newValue.intValue());
            });
        } catch (IOException | InterruptedException | ExecutionException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void assignImage()
    {
//        if (DatasetHandler.trainPath != null)
//        {
//            BufferedImage tmp;
//            if (image == null)
//            {
//                tmp = DatasetHandler.getRandomImage();
//                image = SwingFXUtils.toFXImage(tmp, null);
//            }
//            try
//            {
//                oldImages.add(image);
//            } catch (Exception e)
//            {
//                throw new RuntimeException(e);
//            }
//        }
    }

    private void drawLayers(int stageHeight) throws ExecutionException, InterruptedException
    {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int startX = 15;
        for (BaseLayer layer : ((NeuralModel) ModelManager.models.get(modelName)).getLayers())
        {
            BaseLayer.updateShapeParameters(((NeuralModel) ModelManager.models.get(modelName)).getLayers());

            int[] dimensions = layer.getDimensions();
            int units = Math.min(unitThreshold, dimensions[dimensions.length - 1]);
            int width = Math.min(whThreshold, dimensions[0]);
            int height = 1;

            Group group = new Group();

            if (dimensions.length > 2)
            {
                height = Math.min(whThreshold, dimensions[1]);
            }

            int startY = (stageHeight / 2) - ((height * units + (units - 1) * vgap) / 2);

            for (int i = 0; i < units; i++)
            {
                Rectangle node = new Rectangle(width, height);
                node.setArcHeight(10);
                node.setArcWidth(15);
                node.setStroke(Color.WHITE);
                node.setStrokeWidth(2);
                node.setFill(Color.TRANSPARENT);

                node.setX(startX);
                node.setY(startY);

                group.getChildren().add(node);

                // Add the image, if it exists
                for (int cnt = 0; cnt < units; cnt++)
                {
                    if (!oldImages.isEmpty())
                        getImage(oldImages, units, i);
                    if (image != null)
                    {
                        Future<WritableImage> future = executorService.submit(() -> layer.neuralFunction(image));
                        WritableImage nftmp = future.get();
                        ImageView imageView = new ImageView(nftmp);

                        imageView.setFitHeight(height);
                        imageView.setFitWidth(width);

                        imageView.setX(startX);
                        imageView.setY(startY);

                        imageView.setOnMouseClicked(event -> {
                            Stage popup = new Stage();
                            ImageView popupImageView = new ImageView(nftmp);
                            popupImageView.setFitHeight(500);
                            popupImageView.setFitWidth(500);
                            popup.setScene(new Scene(new Group(popupImageView)));
                            popup.show();
                        });

                        pane.getChildren().add(imageView);

                        newImages.add(nftmp);
                    }
                }
                startY += height + vgap;
            }

            oldImages.clear();
            oldImages.addAll(newImages);
            newImages.clear();

            layerPositions.put(startX, group);
            pane.getChildren().add(group);

            startX += width + hgap;
        }
        pane.setStyle("-fx-background-color: black");
    }

    private WritableImage getImage(ArrayList<WritableImage> images, int d, int i)
    {
        if (images.isEmpty()) return null;
        if (images.size() == 1) return images.get(0);
        if (images.size() != d) return images.get(new Random().nextInt(images.size()));
        return images.get(i);
    }

    private void connectLayers(int stageHeight)
    {
        ArrayList<BaseLayer> layers = ((NeuralModel) ModelManager.models.get(modelName)).getLayers();
        for (int i = 0; i < layers.size() - 1; i++)
        {
            int[] dim_from = layers.get(i).getDimensions();
            int width_from = Math.min(whThreshold, dim_from[0]);

            int[] dim_to = layers.get(i + 1).getDimensions();
            int width_to = Math.min(whThreshold, dim_to[0]);

            int[] dim1 = ((NeuralModel) ModelManager.models.get(modelName)).getLayers().get(i).getDimensions();
            int[] dim2 = ((NeuralModel) ModelManager.models.get(modelName)).getLayers().get(i + 1).getDimensions();
            int fromX = 15 + (width_from + hgap) * i;
            int toX = 15 + (width_to + hgap) * (i + 1);

            int fromY = (stageHeight / 2) - ((Math.min(unitThreshold, dim1[dim1.length - 1]) *
                    (Math.min(whThreshold, dim1[0]) + vgap)) / 2);
            int toY = (stageHeight / 2) - ((Math.min(unitThreshold, dim2[dim2.length - 1]) *
                    (Math.min(whThreshold, dim2[0]) + vgap)) / 2);


            if (layers.get(i + 1).getConnectionType() == BaseLayer.ConnectionType.FULLY_CONNECTED)
            {
                fullyConnectLayers(dim1, dim2, fromX, toX, fromY, toY);
            } else if (layers.get(i + 1).getConnectionType() == BaseLayer.ConnectionType.ONE_TO_ONE)
            {
                OTOConnectLayers(dim1, dim2, fromX, toX, fromY, toY);
            } else
            {
                throw new RuntimeException("Unknown connection type");
            }
        }
    }

    private void fullyConnectLayers(int[] dim1, int[] dim2, int fromX, int toX, int fromY, int toY)
    {
        int units1 = Math.min(unitThreshold, dim1[dim1.length - 1]);
        int units2 = Math.min(unitThreshold, dim2[dim2.length - 1]);
        int height1 = 1;
        int height2 = 1;
        int width1 = Math.min(whThreshold, dim1[0]);
        int width2 = Math.min(whThreshold, dim2[0]);

        if (dim1.length > 2)
        {
            height1 = Math.min(whThreshold, dim1[1]);
        }
        if (dim2.length > 2)
        {
            height2 = Math.min(whThreshold, dim2[1]);
        }

        int startY1 = (int) ((stage.getHeight() / 2) - ((height1 * units1 + (units1 - 1) * vgap) / 2));
        int startY2Initial = (int) ((stage.getHeight() / 2) - ((height2 * units2 + (units2 - 1) * vgap) / 2));

        for (int i = 0; i < units1; i++)
        {
            int startY2 = startY2Initial;
            for (int j = 0; j < units2; j++)
            {
                int x1 = fromX + width1;
                int y1 = startY1 + (height1 / 2);
                int x2 = toX;
                int y2 = startY2 + (height2 / 2);

                Line line = new Line(x1, y1, x2, y2);
                line.setStroke(Color.WHITE);
                line.setStrokeWidth(1);
                pane.getChildren().add(line);

                startY2 += height2 + vgap;
            }
            startY1 += height1 + vgap;
        }
    }

    /**
     * Connects the layers of the DNN. Each node is connected to its adjacent node, respecting any changes in
     * size of the next node.
     */
    private void OTOConnectLayers(int[] dim1, int[] dim2, int fromX, int toX, int fromY, int toY)
    {
        int units1 = Math.min(unitThreshold, dim1[dim1.length - 1]);
        int units2 = Math.min(unitThreshold, dim2[dim2.length - 1]);
        int height1 = 1;
        int height2 = 1;
        int width1 = Math.min(whThreshold, dim1[0]);
        int width2 = Math.min(whThreshold, dim2[0]);

        if (dim1.length > 2)
        {
            height1 = Math.min(whThreshold, dim1[1]);
        }
        if (dim2.length > 2)
        {
            height2 = Math.min(whThreshold, dim2[1]);
        }

        int startY1 = (int) ((stage.getHeight() / 2) - ((height1 * units1 + (units1 - 1) * vgap) / 2));
        int startY2 = (int) ((stage.getHeight() / 2) - ((height2 * units2 + (units2 - 1) * vgap) / 2));

        int units = Math.min(units1, units2);

        for (int i = 0; i < units; i++)
        {
            int x1 = fromX + width1;
            int y1 = startY1 + (height1 / 2);
            int x2 = toX;
            int y2 = startY2 + (height2 / 2);

            Line line = new Line(x1, y1, x2, y2);
            line.setStroke(Color.WHITE);
            line.setStrokeWidth(1);
            pane.getChildren().add(line);

            startY1 += height1 + vgap;
            startY2 += height2 + vgap;
        }
    }
}