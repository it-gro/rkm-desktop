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
 * Created by zeeshan on 11/16/2016.
 */
public class AboutViewController implements Initializable, EventHandler<ActionEvent> {

    @FXML
    Button btnBack;

    private ApplicationCallback applicationCallback;

    @Override
    public void handle(ActionEvent event) {
        if (event.getSource() == btnBack) {
            applicationCallback.showMainScene();
        } else {
            System.out.println("action not implemented");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.btnBack.setOnAction(this);
    }

    public void setApplicationCallback(ApplicationCallback applicationCallback) {
        this.applicationCallback = applicationCallback;
    }
}
