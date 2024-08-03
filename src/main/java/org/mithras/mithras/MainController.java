package org.mithras.mithras;

import javafx.event.ActionEvent;

import java.io.IOException;

public class MainController
{
    public void switchToMain(ActionEvent e) throws IOException
    {
        SceneManager.switchToMain();
    }

    public void switchToModelSelection(ActionEvent e) throws IOException
    {
        SceneManager.switchToModelSelection();
    }

    public void switchToCreateSVM(ActionEvent e) throws IOException
    {
        SceneManager.switchToCreateSVM();
    }

    public void switchToCreateDNN(ActionEvent e) throws IOException
    {
        SceneManager.switchToCreateDNN();
    }

    public void switchToCreateTree(ActionEvent e) throws IOException
    {
        SceneManager.switchToCreateTree();
    }

    public void openCSVBrowser(ActionEvent e) throws IOException
    {
        SceneManager.openCSVBrowser();
    }

    public void openTranscriber(ActionEvent e) throws IOException
    {
        SceneManager.openTranscriber();
    }

    public void openImporter(ActionEvent e) throws IOException, InterruptedException
    {
        SceneManager.openImporter();
    }
}
