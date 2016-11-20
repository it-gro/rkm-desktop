package io.github.zkhan93.rkms.controller;

import io.github.zkhan93.rkms.Main;
import io.github.zkhan93.rkms.callbacks.ApplicationCallback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

/**
 * Created by zeeshan on 11/14/2016.
 */
public class SettingViewController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    Button btnBack;

    @FXML
    Button btnSave;

    @FXML
    Button btnReset;

    @FXML
    TextField txtInputPort;

    @FXML
    TextField txtInputName;

    private ApplicationCallback applicationCallback;
    private Preferences preferences;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnBack.setOnAction(this);
        btnSave.setOnAction(this);
        btnReset.setOnAction(this);
        preferences = Preferences.userNodeForPackage(Main.class);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == btnBack) {
            //back to main scene
            applicationCallback.showMainScene();
        } else if (event.getSource() == btnSave) {
            if (validateInput()) {
                preferences.put("name",txtInputName.getText());
                preferences.putInt("port",Integer.parseInt(txtInputPort.getText()));
            }
        } else if (event.getSource() == btnReset) {
            txtInputName.clear();
            txtInputPort.clear();
        } else {
            System.out.println("action not implemented");
        }
    }

    private boolean validateInput() {
        //TODO: validate input values if valid then save in preferences
        return true;
    }

    public void setApplicationCallback(ApplicationCallback applicationCallback) {
        this.applicationCallback = applicationCallback;
    }

}
