package org.mithras.mithras;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;
import org.mithras.structures.NeuralModel;
import org.mithras.structures.State;

import java.io.IOException;
import java.util.Objects;

public class DNNCodeView
{

    NeuralModel model;

    public void initializeScene(Stage stage, NeuralModel model)
    {
        try
        {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("DNNCodeView.fxml")));
            Scene scene = new Scene(root, 1200, 800);

            this.model = model;

            ListView<BaseLayer> listView = (ListView<BaseLayer>) scene.lookup("#code");

            listView.setCellFactory(new Callback<ListView<BaseLayer>, ListCell<BaseLayer>>()
            {
                @Override
                public ListCell<BaseLayer> call(ListView<BaseLayer> listView)
                {
                    ListCell<BaseLayer> cell = new ListCell<BaseLayer>()
                    {
                        @Override
                        protected void updateItem(BaseLayer item, boolean empty)
                        {
                            super.updateItem(item, empty);
                            if (empty || item == null)
                            {
                                setText(null);
                                setGraphic(null);
                            }
                            else
                            {
                                setText(item.toString());
                                setOnMouseClicked(event ->
                                {
                                    if (event.getButton() == MouseButton.SECONDARY)
                                    {
                                        new ClassInputDialog<BaseLayer>().start(item, model.getName(), getIndex());
                                        fillList(listView.getItems());
                                    }
                                    if (event.getButton() == MouseButton.MIDDLE)
                                    {
                                        model.removeLayer(item);
                                        fillList(listView.getItems());
                                    }
                                });
                            }
                        }
                    };

                    // Enable dragging
                    cell.setOnDragDetected(event ->
                    {
                        if (!cell.isEmpty())
                        {
                            Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
                            ClipboardContent content = new ClipboardContent();
                            content.putString(Integer.toString(cell.getIndex()));
                            db.setContent(content);
                            event.consume();
                        }
                    });

                    cell.setOnDragOver(event ->
                    {
                        if (event.getGestureSource() != cell &&
                                event.getDragboard().hasString())
                        {
                            event.acceptTransferModes(TransferMode.MOVE);
                        }
                        event.consume();
                    });

                    cell.setOnDragDropped(event ->
                    {
                        Dragboard db = event.getDragboard();
                        boolean success = false;
                        if (db.hasString())
                        {
                            int draggedIndex = Integer.parseInt(db.getString());
                            int thisIndex = cell.getIndex();

                            if (draggedIndex != thisIndex)
                            {
                                BaseLayer itemToMove = listView.getItems().remove(draggedIndex);
                                listView.getItems().add(thisIndex, itemToMove);
                                model.getLayers().remove(draggedIndex);
                                model.getLayers().add(thisIndex, itemToMove);
                                BaseLayer.updateShapeParameters(model.getLayers());
                                success = true;
                            }
                        }
                        event.setDropCompleted(success);
                        event.consume();
                    });

                    cell.setOnDragDone(DragEvent::consume);

                    return cell;
                }
            });

            ObservableList<BaseLayer> items = listView.getItems();
            fillList(items);

            scene.getStylesheets().add(StyleUtil.getCss());

            stage.setScene(scene);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void fillList(ObservableList<BaseLayer> items)
    {
        items.clear();
        items.addAll(model.getLayers());
    }

    public void switchToMain() throws IOException
    {
        SceneManager.switchToMain();
        State.setModelName(null);
    }

    public void switchToNNLS() throws IOException
    {
        SceneManager.switchToNNLS(State.getModelName());
    }
}