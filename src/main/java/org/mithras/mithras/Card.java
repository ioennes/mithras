package org.mithras.mithras;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.mithras.structures.NeuralModel;
import org.mithras.structures.SVMModel;
import org.mithras.structures.State;
import org.mithras.structures.TreeModel;

import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;

/**
 * The Card class extends StackPane and represents a card in the application.
 * Each card has a type, which is defined by the CardType enum.
 */
public class Card extends StackPane
{

    private final CardType cardType;
    private String modelName;

    /**
     * The constructor for the Card class.
     *
     * @param modelName The name of the model.
     * @param modelType The type of the model.
     * @param cardType  The type of the card.
     */
    public Card(String modelName, String modelType, CardType cardType)
    {
        this.cardType = cardType;
        try
        {
            Node root = loadFXML();

            configureCard(root, modelName, modelType);
            this.modelName = modelName;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Loads the FXML file for the card.
     *
     * @return The root node of the loaded FXML file.
     * @throws IOException If an I/O error occurs.
     */
    private Node loadFXML() throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
        loader.setController(this);
        return loader.load();
    }

    /**
     * Configures the card.
     *
     * @param root      The root node of the loaded FXML file.
     * @param modelName The name of the model.
     * @param modelType The type of the model.
     */
    private void configureCard(Node root, String modelName, String modelType)
    {
        this.getChildren().add(root);

        configureMainPane(root);
        configureModelName(root, modelName);
        configureButtons(root);
        configureModelType(root, modelType);
    }

    /**
     * Configures the main pane of the card.
     *
     * @param root The root node of the loaded FXML file.
     */
    private void configureMainPane(Node root)
    {
        StackPane sp = (StackPane) root.lookup("#mainpane");
        sp.setMaxWidth(1000);
        sp.setPrefWidth(800);
        sp.setPrefHeight(200);
        sp.setMinHeight(200);
        sp.setMaxHeight(200);
    }

    /**
     * Configures the model name of the card.
     *
     * @param root      The root node of the loaded FXML file.
     * @param modelName The name of the model.
     */
    private void configureModelName(Node root, String modelName)
    {
        Text name = (Text) root.lookup("#modelname");
        name.setText(modelName);
    }

    private void configureButtons(Node root)
    {
        Button edit = (Button) root.lookup("#editbtn");
        edit.setOnAction(e -> {
            try
            {
                edit();
            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });

        Button view = (Button) root.lookup("#viewbtn");
        view.setOnAction(e -> {
            try
            {
                State.setModelName(modelName);
                view();
            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });

        Button configure = (Button) root.lookup("#configurebtn");
        configure.setOnAction(e -> {
            try
            {
                configure();
            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });

        Button copy = (Button) root.lookup("#copybtn");
        copy.setOnAction(e -> {
            try
            {
                copy();
            } catch (IOException ex)
            {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Configures the model type of the card.
     *
     * @param root      The root node of the loaded FXML file.
     * @param modelType The type of the model.
     */
    private void configureModelType(Node root, String modelType)
    {
        Text type = (Text) root.lookup("#modeltype");
        type.setText(modelType);
    }

    /**
     * Returns the type of the card.
     *
     * @return The type of the card.
     */
    public CardType getCardtype()
    {
        return cardType;
    }

    /**
     * Runs the card.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void edit() throws IOException
    {
        if (getCardtype() == CardType.NeuralNetwork)
        {
            SceneManager.switchToNNLS(modelName);
        } else if (getCardtype() == CardType.SVM)
        {
            new ClassInputDialog<>().start(((SVMModel) ModelManager.models.get(modelName)).getSvm(), modelName, -1);
            System.out.println(ModelManager.models.get(modelName).toString());
        } else if (getCardtype() == CardType.DecisionTreeClassifier || getCardtype() == CardType.DecisionTreeRegressor)
        {
            new ClassInputDialog<>().start(((TreeModel) ModelManager.models.get(modelName)).getTree(), modelName, -1);
            System.out.println(ModelManager.models.get(modelName).toString());
        } else
        {
            SceneManager.switchToMain();
        }
    }

    public void configure() throws IOException
    {
        if (getCardtype() == CardType.NeuralNetwork)
        {
            new ClassInputDialog<>().start(((NeuralModel) ModelManager.models.get(modelName)).getCompilationData(),
                    modelName, -1);
        }
        else if (getCardtype() == CardType.SVM)
        {
            new ClassInputDialog<>().start(((SVMModel) ModelManager.models.get(modelName)).getConfigData(), modelName,
                    -1);
        }
        else if (getCardtype() == CardType.DecisionTreeClassifier || getCardtype() == CardType.DecisionTreeRegressor)
        {
            new ClassInputDialog<>().start(((TreeModel) ModelManager.models.get(modelName)).getConfigData(), modelName,
                    -1);
        }
    }

    public void view() throws IOException
    {
        if (getCardtype() == CardType.NeuralNetwork)
        {
            SceneManager.switchToDNNView(modelName);
        }
    }

    public void copy() throws IOException
    {
        StringBuilder sb = new StringBuilder();
        PyTranscriber.writeImports(sb);
        sb.append("\n");
        sb.append(ModelManager.models.get(modelName).toString());
        StringSelection selection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
    }

    /**
     * The CardType enum represents the type of the card.
     */
    public enum CardType
    {
        NeuralNetwork, SVM, DecisionTreeClassifier, DecisionTreeRegressor
    }
}