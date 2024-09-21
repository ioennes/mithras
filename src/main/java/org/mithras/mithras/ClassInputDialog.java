package org.mithras.mithras;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.mithras.machinelearning.decisiontree.DecisionTreeClassifier;
import org.mithras.machinelearning.decisiontree.DecisionTreeRegressor;
import org.mithras.machinelearning.neuralnetwork.layers.BaseLayer;
import org.mithras.machinelearning.svm.LinearSVC;
import org.mithras.machinelearning.svm.LinearSVR;
import org.mithras.machinelearning.svm.SVC;
import org.mithras.structures.Model;
import org.mithras.structures.NeuralModel;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Scene scene = new Scene(root);

        dialogStage.initStyle(StageStyle.UTILITY);

        GridPane gridPane = (GridPane) scene.lookup("#inputgrid");
        gridPane.setPadding(new Insets(20));

        dialogStage.setTitle(object.getClass().getSimpleName() + " Input");
        dialogStage.setScene(scene);

        Field[] fields = object.getClass().getFields();
        if (fields.length == 0)
        {
            ok();
            dialogStage.close();
            return;
        }

        setRowConstraints(gridPane, 40, fields.length + 2);
        boolean hasFields = displayFieldsAndInput(object, gridPane);

        scene.getStylesheets().add(StyleUtil.getCss());

        Platform.runLater(() -> dialogStage.sizeToScene());
        dialogStage.showAndWait();
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
        if (fields.length == 0)
            return false;

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
        Text fieldNameText = new Text(translate(field.getName()));
        fieldNameText.setFocusTraversable(true);
        Tooltip tooltip = new Tooltip(Translator.annotate(translate(field.getName())));
        Tooltip.install(fieldNameText, tooltip);
        gp.add(fieldNameText, 0, row);

        gp.add(new Text(translate(field.getType().toString())), 1, row);

        Object fieldValue = field.get(object);
        Node inputNode = null;
        if (field.getName().equals("optimizer"))
        {
            String[] optimizers = {"SGD", "Adam", "RMSprop", "Adagrad", "Adadelta"};
            inputNode = createComboBox(optimizers, (String) fieldValue);
        }
        else if (field.getName().equals("loss"))
        {
            String[] losses;
            if (object instanceof SVC || object instanceof LinearSVC)
            {
                losses = new String[]{"hinge", "squared_hinge"};
            }
            else if (object instanceof LinearSVR)
            {
                losses = new String[]{"epsilon_insensitive", "squared_epsilon_insensitive"};
            }
            else
            {
                losses = new String[]{"mean_squared_error", "categorical_crossentropy", "binary_crossentropy", "mean_absolute_error"};
            }
            inputNode = createComboBox(losses, (String) fieldValue);
        }
        else if (field.getName().equals("criterion"))
        {
            String[] crits;
            if (object instanceof DecisionTreeRegressor)
            {
                crits = new String[]{"mean_squared_error", "mean_absolute_error", "friedman_mse", "poisson"};
                inputNode = createComboBox(crits, (String) fieldValue);
            }
            else if (object instanceof DecisionTreeClassifier)
            {
                crits = new String[]{"gini", "entropy", "log_loss"};
                inputNode = createComboBox(crits, (String) fieldValue);
            }
        }
        else if (field.getName().equals("splitter"))
        {
            String[] splitters = {"best", "random"};
            inputNode = createComboBox(splitters, (String) fieldValue);
        }
        else if (field.getName().equals("kernel"))
        {
            String[] kernels = {"rbf", "linear", "poly", "sigmoid"};
            inputNode = createComboBox(kernels, (String) fieldValue);
        }
        else if (field.getName().equals("activation"))
        {
            String[] activations = {"relu", "sigmoid", "softmax", "tanh", "elu", "gelu"};
            inputNode = createComboBox(activations, (String) fieldValue);
        }
        else if (field.getName().contains("regularizer"))
        {
            String[] regularizers = {"", "L1", "L1L2", "L2", "OrthogonalRegularizer"};
            inputNode = createComboBox(regularizers, (String) fieldValue);
        }
        else if (field.getName().contains("initializer"))
        {
            String[] initializers = {"", "Constant", "GlorotNormal", "GlorotUniform", "HeNormal", "HeUniform",
                    "Identity", "LecunNormal", "LecunUniform", "Ones", "Orthogonal",};
            inputNode = createComboBox(initializers, (String) fieldValue);
        }
        else if (field.getType().equals(Boolean.TYPE))
        {
            inputNode = createCheckBox(fieldValue);
        }
        else if (field.getType().equals(Path.class))
        {
            inputNode = createFileChooserButton(field, fieldValue);
        }
        else
        {
            inputNode = createTextField(field, fieldValue);
        }
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
        if ((boolean) fieldValue)
            cb.fire();
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
     * Creates a Button to select a file location with a given initial value.
     *
     * @param field      The field the Button corresponds to
     * @param fieldValue The initial value of the Button
     * @return The created Button
     */
    private Button createFileChooserButton(Field field, Object fieldValue)
    {
        Button button = new Button(fieldValue != null ? fieldValue.toString() : "Select File");
        button.setOnAction(event ->
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select File");
            if (fieldValue != null)
            {
                fileChooser.setInitialDirectory(((Path) fieldValue).toFile().getParentFile());
            }
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );
            java.io.File selectedFile = fileChooser.showOpenDialog(button.getScene().getWindow());
            if (selectedFile != null)
            {
                button.setText(selectedFile.toString());
                try
                {
                    field.set(object, selectedFile.toPath());
                } catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
            }
        });
        return button;
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
            System.out.println(field.getType().getName());

            switch (field.getType().getName())
            {
                case "int":
                    setIntField(field, cls, getInputString(entry.getValue()));
                    break;
                case "boolean":
                    field.setBoolean(cls, Boolean.parseBoolean(getInputString(entry.getValue())));
                    break;
                case "java.lang.String":
                    field.set(cls, getInputString(entry.getValue()));
                    break;
                case "float":
                    setFloatField(field, cls, getInputString(entry.getValue()));
                    break;
                case "[I":
                    setIntArrayField(field, cls, getInputString(entry.getValue()));
                    break;
                case "[Ljava.lang.String;":
                    setStringArrayField(field, cls, getInputString(entry.getValue()));
                    break;
                case "java.nio.file.Path":
                    setPathField(field, cls, (Button) entry.getValue());
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
            return ((ComboBox<?>) node).getValue() != null ? ((ComboBox<?>) node).getValue().toString() : "";
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
        }
        else
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

    private void setPathField(Field field, Object cls, Button btn) throws IllegalAccessException
    {
        field.set(cls, Paths.get(btn.getText()));
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
        }
        else
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

    private ComboBox<String> createComboBox(String[] options, String selectedValue)
    {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(options);
        if (selectedValue != null && !selectedValue.isEmpty())
        {
            comboBox.setValue(selectedValue);
        }
        return comboBox;
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
                }
                else
                {
                    ((NeuralModel) model).getLayers().add((BaseLayer) object);
                    BaseLayer.updateShapeParameters(((NeuralModel) model).getLayers());
                }
            }
            System.out.println(object.toString());
            dialogStage.close();
        } catch (IllegalAccessException ex)
        {
            ex.printStackTrace();
        }
    }
}
