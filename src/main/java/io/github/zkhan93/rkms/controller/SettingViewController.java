package io.github.zkhan93.rkms.controller;

import io.github.zkhan93.rkms.callbacks.ApplicationCallback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnBack.setOnAction(this);
        btnSave.setOnAction(this);
        btnReset.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == btnBack) {
            //back to main scene
            applicationCallback.showMainScene();
        } else if (event.getSource() == btnSave) {
            //TODO: validate input values if valid then save
            if (validateInput()) {
                applicationCallback.setPort(Integer.parseInt(txtInputPort.getText()));
                applicationCallback.setName(txtInputName.getText());
            }
        } else if (event.getSource() == btnReset) {
            txtInputName.clear();
            txtInputPort.clear();
        } else {
            System.out.println("action not implemented");
        }
    }

    private boolean validateInput() {
        return true;
    }

    public void setApplicationCallback(ApplicationCallback applicationCallback) {
        this.applicationCallback = applicationCallback;
    }

}
