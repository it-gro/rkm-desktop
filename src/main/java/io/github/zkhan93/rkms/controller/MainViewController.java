package io.github.zkhan93.rkms.controller;

import io.github.zkhan93.rkms.Driver;
import io.github.zkhan93.rkms.callbacks.ApplicationCallback;
import io.github.zkhan93.rkms.task.DiscoveryTask;
import io.github.zkhan93.rkms.util.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;


public class MainViewController implements Initializable, EventHandler<ActionEvent> {
    @FXML
    Button btnSetting;

    @FXML
    Button btnReset;

    @FXML
    Button btnInfo;

    @FXML
    ImageView imgQrcode;

    @FXML
    Label txtIp;

    @FXML
    Label txtPort;

    @FXML
    Label txtStatus;

    private int activePort;
    private Driver driver;
    private boolean serverStarted;
    private Preferences preference;
    private Thread discoveryThread;
    private ApplicationCallback applicationCallback;

    {
        driver = new Driver();
        preference = Preferences.userNodeForPackage(MainViewController.class);
        activePort = preference.getInt("port", Constants.PORT);
    }

    /**
     * set activePort value from preferences and starts the discovery server
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("loaded port from preference");
        try {
            activePort = preference.getInt("port", Constants.PORT);
        } catch (Exception ex) {
            System.out.println("invalid port in preferences switched to default");
            activePort = Constants.PORT;
        }
        driver.stop();
        serverStarted = false;
        toggleState();
        txtPort.setText("Port " + String.valueOf(activePort));

        btnSetting.setOnAction(this);
        btnReset.setOnAction(this);
        btnInfo.setOnAction(this);
    }

    public void setApplicationCallback(ApplicationCallback applicationCallback) {
        this.applicationCallback = applicationCallback;
    }


    public void savePort() {
        String portStr = txtPort.getText().trim();
        if (!portStr.isEmpty()) {
            try {
                activePort = Integer.parseInt(txtPort.getText());
                preference.putInt("port", activePort);
            } catch (NumberFormatException ex) {
                activePort = Constants.PORT;
            }
        }
    }


    public void toggleState() {
        serverStarted = !serverStarted;
        boolean error = false;
        if (serverStarted) {
            try {
                driver.go(activePort);
                serverStarted = true;
                //start the discovery thread at the start of application
                discoveryThread = new Thread(DiscoveryTask.getInstance());
                discoveryThread.start();
            } catch (IOException ex) {
                activePort++;
                serverStarted = false;
                error = true;
            }
        } else {
            driver.stop();
        }
        //update txtStatus txtStatus
        if (serverStarted) {
            txtStatus.setText("Server ready to connect");
            String ip = null;
            try {
                ip = Inet4Address.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                System.out.println("exception cannot get my IP" + ex.getLocalizedMessage());
            }
            imgQrcode.setImage(new Image(QRCode.from(ip + ":" + activePort).withSize(200, 200).to(ImageType.JPG).file().toURI().toString()));
        } else {
            if (error)
                txtStatus.setText("Server stopped by error: " + error);
            else
                txtStatus.setText("Server stopped");
        }
        System.out.println("start/stop server");
    }

    public void stop() {
        if (driver != null)
            driver.stop();
        if (discoveryThread != null)
            discoveryThread.interrupt();
    }


    @Override
    public void handle(ActionEvent event) {

        if (event.getSource() == btnSetting) {
            //switch to setting scene
            try {
                applicationCallback.showSettingScene();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (event.getSource() == btnInfo) {
            //switch to info scene
            try {
                applicationCallback.showInfoScene();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (event.getSource() == btnReset) {
            driver.stop();
            serverStarted = false;
            toggleState();
        } else {
            //not implemented
        }
    }
}
