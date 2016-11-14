package io.github.zkhan93.rkms.controller;

import io.github.zkhan93.rkms.callbacks.ApplicationCallback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by zeeshan on 11/14/2016.
 */
public class SettingViewController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    Button btnBack;

    private ApplicationCallback applicationCallback;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnBack.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == btnBack) {
            //back to main scene
            try {
                applicationCallback.showMainScene();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            //not implemented
        }
    }

    public void setApplicationCallback(ApplicationCallback applicationCallback) {
        this.applicationCallback = applicationCallback;
    }
}
