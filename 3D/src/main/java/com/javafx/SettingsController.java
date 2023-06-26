package com.javafx;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SettingsController implements Initializable {
    private boolean cameFromStartMenu;
    private List<TextField> keyBindingFields;
    private List<String> listOfKeys;

    @FXML
    private TextField seedTextField;

    @FXML
    private VBox keyBindingsVBox;

    @FXML
    private AnchorPane anchorPane;

    public SettingsController(boolean cameFromStartMenu) {
        this.cameFromStartMenu = cameFromStartMenu;
    }

    @FXML
    private void resetDefaultKeyBindings() {
        SettingsHandler.setDefaultKeyBindings();
        getKeyBindingsFromSettings();
    }

    @FXML
    private void exitGame() {
        StartPage.closeStage();
    }

    @FXML
    private void onPressedAnchorPane() {
        anchorPane.requestFocus();
    }

    @FXML
    private void onKeyPressedKeyBinding(KeyEvent event) {
        String newKey = event.getCode().toString();
        TextField sourceTextField = (TextField) event.getSource();
        sourceTextField.setText(newKey);

        SettingsHandler.put(sourceTextField.getId(), newKey);
    }

    @FXML
    private void onKeyReleasedKeyBinding() {
        anchorPane.requestFocus();
    }

    @FXML
    private void goToStartMenu() throws IOException {
        StartPage.setScene("startMenu");
    }

    @FXML
    private void goBackScene() throws IOException {
        if (cameFromStartMenu) {
            StartPage.setScene("startMenu");
        } else {
            FlightSimulatorGame game = new FlightSimulatorGame();
            StartPage.setScene(game.startGame(Integer.valueOf(SettingsHandler.getProperty("seed"))));
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        anchorPane.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            // if ESC is pressed in settings
            if (e.getCode().equals(KeyCode.ESCAPE)) {
                Node focusNode = anchorPane.getScene().getFocusOwner();
                // if ESC in keybindings textfield
                if (!(focusNode instanceof TextField)) {
                    try {
                        goBackScene();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        seedTextField.setText(SettingsHandler.getProperty("seed"));

        listOfKeys = new ArrayList<>();
        listOfKeys.add("Key-FlyForward");
        listOfKeys.add("Key-TurnLeft");
        listOfKeys.add("Key-TurnRight");
        listOfKeys.add("Key-FlyUp");
        listOfKeys.add("Key-FlyDown");
        listOfKeys.add("Key-RotateLeft");
        listOfKeys.add("Key-RotateRight");
        listOfKeys.add("Key-RotateUp");
        listOfKeys.add("Key-RotateDown");
        listOfKeys.add("Key-SettingsMenu");

        keyBindingFields = new ArrayList<>();
        getKeyBindingsFromSettings();
    }

    private void getKeyBindingsFromSettings() {
        int i = 0;
        for (Node node : keyBindingsVBox.getChildren()) {
            if (node instanceof TextField) {
                // set saved values to the textfields
                TextField textField = (TextField) node;
                textField.setText(SettingsHandler.getProperty(listOfKeys.get(i)));
                keyBindingFields.add(textField);
                i++;
            }
        }
    }
}
