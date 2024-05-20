package org.mithras.mithras;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;
import org.mithras.structures.Model;
import org.mithras.structures.NeuralModel;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.mithras.machinelearning.neuralnetwork.layers.BaseLayer.isDefaultValue;
import static org.mithras.mithras.Translator.translate;

/**
 * ClassInputDialog is a class that provides a dialog for inputting class parameters.
 * It uses JavaFX for the GUI and reflection to dynamically create input fields based on the class fields.
 */
public class ClassInputDialog<T>
{
    // A map to store the fields of the class and their corresponding input nodes
    LinkedHashMap<Field, Node> fieldInputMap = new LinkedHashMap<>();
    // The stage for the dialog
    Stage dialogStage = new Stage();
    // The object whose parameters are being inputted
    private Model model;
    private T object;
    private int index;

    /**
     * Starts the input dialog for a given object.
     *
     * @param object The object whose parameters are to be inputted
     */
    public void start(T object, String objName, int index)
    {
        try
        {
            this.object = object;
            this.model = ModelManager.models.get(objName);
            this.index = index;

            initialize(object);
        } catch (IOException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Initializes the input dialog.
     *
     * @param object The object whose parameters are to be inputted
     * @throws IOException            If there is an error loading the FXML
     * @throws IllegalAccessException If there is an error accessing a field
     */
    public void initialize(T object) throws IOException, IllegalAccessException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassInputDialog.fxml"));
        loader.setController(this);
        Parent root = loader.load();
        Scene scene = new Scene(root, 600, -1);

        GridPane gridPane = (GridPane) scene.lookup("#inputgrid");
        gridPane.setPadding(new Insets(20));

        dialogStage.setTitle(object.getClass().getSimpleName() + " Input");
        dialogStage.setScene(scene);

        setRowConstraints(gridPane, 40, object.getClass().getFields().length + 2);

        boolean hasFields = displayFieldsAndInput(object, gridPane);

        scene.getStylesheets().add(StyleUtil.getCss());

        Platform.runLater(() -> dialogStage.sizeToScene());
        dialogStage.showAndWait();

        if (!hasFields)
        {
            ok();
            dialogStage.close();
        }
    }

    /**
     * Sets the row constraints for a given GridPane.
     *
     * @param gridPane The GridPane to set the constraints for
     * @param height   The height for the rows
     * @param rowCount The number of rows
     */
    private void setRowConstraints(GridPane gridPane, int height, int rowCount)
    {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(height);
        rowConstraints.setPrefHeight(height);
        rowConstraints.setMaxHeight(height);

        for (int i = 0; i < rowCount; i++)
        {
            gridPane.getRowConstraints().add(rowConstraints);
        }

        gridPane.layout();
    }

    /**
     * Displays the fields of a class and their corresponding input nodes.
     *
     * @param object The object whose fields are to be displayed
     * @param gp     The GridPane to add the fields to
     * @return true if successful, false if fails or no fields.
     * @throws IllegalAccessException If there is an error accessing a field
     */
    private boolean displayFieldsAndInput(T object, GridPane gp) throws IllegalAccessException
    {
        Field[] fields = object.getClass().getFields();
        if (fields.length == 0) return false;

        addHeadersToGridPane(gp, object.getClass().getSimpleName());

        for (int i = 0; i < fields.length; i++)
        {
            addFieldToGridPane(gp, fields[i], object, i + 4);
        }
        return true;
    }

    /**
     * Adds headers to the GridPane.
     *
     * @param gp         The GridPane to add the headers to
     * @param objectName The name of the object
     */
    private void addHeadersToGridPane(GridPane gp, String objectName)
    {
        String descriptorStyle = "-fx-font-weight: bold; -fx-font-size: 15";

        gp.add(createStyledText(objectName, "-fx-font-weight: bold; -fx-font-size: 24"), 0, 0);
        gp.add(createStyledText("Parameter", descriptorStyle), 0, 2);
        gp.add(createStyledText("Datatype", descriptorStyle), 1, 2);
        gp.add(createStyledText("Value", descriptorStyle), 2, 2);
    }

    /**
     * Creates a Text node with a given style.
     *
     * @param content The content of the Text node
     * @param style   The style of the Text node
     * @return The created Text node
     */
    private Text createStyledText(String content, String style)
    {
        Text text = new Text(content);
        text.setStyle(style);
        return text;
    }

    /**
     * Adds a field and its corresponding input node to the GridPane.
     *
     * @param gp     The GridPane to add the field to
     * @param field  The field to add
     * @param object The object the field belongs to
     * @param row    The row to add the field to
     * @throws IllegalAccessException If there is an error accessing the field
     */
    private void addFieldToGridPane(GridPane gp, Field field, T object, int row) throws IllegalAccessException
    {
        gp.add(new Text(translate(field.getName())), 0, row);
        gp.add(new Text(translate(field.getType().toString())), 1, row);

        Object fieldValue = field.get(object);
        Node inputNode = field.getType().equals(Boolean.TYPE) ? createCheckBox(fieldValue) : createTextField(field, fieldValue);
        gp.add(inputNode, 2, row);
        fieldInputMap.put(field, inputNode);
    }

    /**
     * Creates a CheckBox with a given initial value.
     *
     * @param fieldValue The initial value of the CheckBox
     * @return The created CheckBox
     */
    private CheckBox createCheckBox(Object fieldValue)
    {
        CheckBox cb = new CheckBox();
        if ((boolean) fieldValue) cb.fire();
        return cb;
    }

    /**
     * Creates a TextField with a given initial value.
     *
     * @param field      The field the TextField corresponds to
     * @param fieldValue The initial value of the TextField
     * @return The created TextField
     */
    private TextField createTextField(Field field, Object fieldValue)
    {
        String fieldValueString = field.getType().isArray() && !isDefaultValue(fieldValue)
                ? Arrays.toString((int[]) fieldValue).replaceAll("[\\[\\]]", "")
                : ((fieldValue == null || isDefaultValue(fieldValue)) ? "" : fieldValue).toString();

        TextField ta = new TextField(fieldValueString);
        ta.setMinHeight(35);
        ta.setPrefHeight(35);
        ta.setMaxHeight(35);
        return ta;
    }

    /**
     * Binds the data from the input nodes to the corresponding fields of a class.
     *
     * @param cls The class to bind the data to
     * @throws IllegalAccessException If there is an error accessing a field
     */
    private void bindData(Object cls) throws IllegalAccessException
    {
        for (Map.Entry<Field, Node> entry : fieldInputMap.entrySet())
        {
            Field field = entry.getKey();
            String inputToString = getInputString(entry.getValue());

            switch (field.getType().getName())
            {
                case "int":
                    setIntField(field, cls, inputToString);
                    break;
                case "boolean":
                    field.setBoolean(cls, Boolean.parseBoolean(inputToString));
                    break;
                case "java.lang.String":
                    field.set(cls, inputToString);
                    break;
                case "float":
                    setFloatField(field, cls, inputToString);
                    break;
                case "[I":
                    setIntArrayField(field, cls, inputToString);
                    break;
                case "[Ljava.lang.String;":
                    setStringArrayField(field, cls, inputToString);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + field.getType().getName());
            }
        }
    }

    /**
     * Gets the string representation of the input from a given Node.
     *
     * @param node The Node to get the input from
     * @return The string representation of the input
     */
    private String getInputString(Node node)
    {
        if (node instanceof CheckBox)
            return ((CheckBox) node).isSelected() ? "true" : "false";
        else if (node instanceof ComboBox<?>)
            return ((ComboBox<?>) node).getValue().toString();
        else
            return ((TextField) node).getText();
    }

    /**
     * Sets the value of an int field.
     *
     * @param field       The field to set the value for
     * @param cls         The class the field belongs to
     * @param inputString The string representation of the value
     * @throws IllegalAccessException If there is an error accessing the field
     */
    private void setIntField(Field field, Object cls, String inputString) throws IllegalAccessException
    {
        int intValue = inputString.isEmpty() ? 0 : Integer.parseInt(inputString);
        field.setInt(cls, intValue);
    }

    /**
     * Sets the value of a float field.
     *
     * @param field       The field to set the value for
     * @param cls         The class the field belongs to
     * @param inputString The string representation of the value
     * @throws IllegalAccessException If there is an error accessing the field
     */
    private void setFloatField(Field field, Object cls, String inputString) throws IllegalAccessException
    {
        float floatValue = inputString.isEmpty() ? 0.0f : Float.parseFloat(inputString);
        field.setFloat(cls, floatValue);
    }

    /**
     * Sets the value of an int array field.
     *
     * @param field       The field to set the value for
     * @param cls         The class the field belongs to
     * @param inputString The string representation of the value
     * @throws IllegalAccessException If there is an error accessing the field
     */
    private void setIntArrayField(Field field, Object cls, String inputString) throws IllegalAccessException
    {
        if (inputString.isEmpty())
        {
            field.set(cls, null);
        } else
        {
            String[] numericStrings = inputString.split(",");
            int[] intValues = new int[numericStrings.length];
            for (int i = 0; i < numericStrings.length; i++)
            {
                intValues[i] = Integer.parseInt(numericStrings[i].trim());
            }
            field.set(cls, intValues);
        }
    }

    /**
     * Sets the value of a String array field.
     *
     * @param field       The field to set the value for
     * @param cls         The class the field belongs to
     * @param inputString The string representation of the value
     * @throws IllegalAccessException If there is an error accessing the field
     */
    private void setStringArrayField(Field field, Object cls, String inputString) throws IllegalAccessException
    {
        if (inputString.isEmpty())
        {
            field.set(cls, null);
        } else
        {
            String[] stringValues = inputString.split(",");
            for (int i = 0; i < stringValues.length; i++)
            {
                stringValues[i] = stringValues[i].trim();
            }
            field.set(cls, stringValues);
        }
    }

    /**
     * Closes the dialog when the cancel button is clicked.
     *
     * @param e The ActionEvent
     * @throws IOException If there is an error closing the dialog
     */
    @FXML
    public void cancel(ActionEvent e) throws IOException
    {
        dialogStage.close();
    }

    /**
     * Binds the data and closes the dialog when the ok button is clicked.
     *
     * @param e The ActionEvent
     * @throws IOException If there is an error closing the dialog
     */
    @FXML
    public void ok(ActionEvent e) throws IOException
    {
        ok();
    }

    /**
     * Binds the data and closes the dialog.
     */
    private void ok()
    {
        try
        {
            bindData(object);
            if (model instanceof NeuralModel && object instanceof BaseLayer)
            {
                if (index >= 0)
                {
                    ((NeuralModel) model).getLayers().set(index, (BaseLayer) object);
                    BaseLayer.updateShapeParameters(((NeuralModel) model).getLayers());
                } else
                {
                    ((NeuralModel) model).getLayers().add((BaseLayer) object);
                    BaseLayer.updateShapeParameters(((NeuralModel) model).getLayers());
                }
            }
            dialogStage.close();
        } catch (IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
    }
}
