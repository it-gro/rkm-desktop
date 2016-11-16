package io.github.zkhan93.rkms.controller;

import io.github.zkhan93.rkms.callbacks.ApplicationCallback;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;


public class MainViewController implements Initializable, EventHandler<ActionEvent>, PreferenceChangeListener {
    @FXML
    Button btnSetting;

    @FXML
    Button btnReset;

    @FXML
    Button btnAbout;

    @FXML
    ImageView imgQrcode;

    @FXML
    Label txtIp;

    @FXML
    Label txtPort;

    @FXML
    Label txtStatus;

    private ApplicationCallback applicationCallback;


    /**
     * set activePort value from preferences and starts the discovery server
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnSetting.setOnAction(this);
        btnReset.setOnAction(this);
        btnAbout.setOnAction(this);
    }

    public void setApplicationCallback(ApplicationCallback applicationCallback) {
        this.applicationCallback = applicationCallback;
    }

    public void setPort(int port) {
        txtPort.setText("Port " + String.valueOf(port));
    }

    public void setStatus(String status) {
        txtStatus.setText(status);
    }

    public void setQrcode(String ip, int port) {
        imgQrcode.setImage(new Image(QRCode.from(ip + ":" + port).withSize(200, 200).to(ImageType.JPG).file().toURI().toString()));
    }


    @Override
    public void handle(ActionEvent event) {

        if (event.getSource() == btnSetting) {
            //switch to setting scene
            applicationCallback.showSettingScene();
        } else if (event.getSource() == btnAbout) {
            //switch to info scene
            applicationCallback.showAboutScene();
        } else if (event.getSource() == btnReset) {
            applicationCallback.reset();
        } else {
            //not implemented
        }
    }

    @Override
    public void preferenceChange(PreferenceChangeEvent evt) {
        if (evt.getKey() == "port") {
            setPort(Integer.parseInt(evt.getNewValue()));
        } else {
            //unknown preference changed
        }
    }
}
