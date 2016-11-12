package io.github.zkhan93.rkms.controller;

import io.github.zkhan93.rkms.Driver;
import io.github.zkhan93.rkms.task.DiscoveryTask;
import io.github.zkhan93.rkms.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.prefs.Preferences;


public class MainViewController {
    
    Button savePort;
    
    Button toggleBtn;
    
    TextField port;
    
    Label status;
    
    ImageView image;

    private int activePort;
    private Driver driver;
    private boolean serverStarted;
    private Preferences preference;


    {
        driver = new Driver();
        preference = Preferences.userNodeForPackage(MainViewController.class);
        activePort = preference.getInt("port", Constants.PORT);
    }

    
    public void savePort(ActionEvent event) {
        String portStr = port.getText().trim();
        if (!portStr.isEmpty()) {
            try {
                activePort = Integer.parseInt(port.getText());
                preference.putInt("port", activePort);
                driver.stop();
                serverStarted = false;
                toggleState(null);
            } catch (NumberFormatException ex) {
                activePort = Constants.PORT;
            }
        }
    }

    
    public void toggleState(ActionEvent event) {
        serverStarted = !serverStarted;
        boolean error = false;
        if (serverStarted) {
            try {
                driver.go(activePort);
                serverStarted = true;
                //start the discovery thread at the start of application
                Thread thread = new Thread(DiscoveryTask.getInstance());
                thread.start();
            } catch (IOException ex) {
                activePort++;
                serverStarted = false;
                error = true;
            }
        } else {
            driver.stop();
        }
        //update status status
        if (serverStarted) {
            status.setText("Server started and listening on " + activePort);
            toggleBtn.setText("Stop");
            String ip = null;
            try {
                ip = Inet4Address.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                System.out.println("exception cannot get my IP" + ex.getLocalizedMessage());
            }
            image.setImage(new Image(QRCode.from(ip + ":" + activePort).withSize(200, 200).to(ImageType.JPG).file().toURI().toString()));
        } else {
            if (error)
                status.setText("Server not started on port " + activePort + " try different port");
            else
                status.setText("Server stopped");
            toggleBtn.setText("Start");
        }
        System.out.println("start/stop server");
    }

    public void stop() {
        if (driver != null)
            driver.stop();
    }

    public void init() {
        System.out.println("loaded port from preference");
        try {
            activePort = preference.getInt("port", Constants.PORT);
        } catch (Exception ex) {
            System.out.println("invalid port in preferences switched to default");
            activePort = Constants.PORT;
        }
        port.setText(String.valueOf(activePort));
    }
}
