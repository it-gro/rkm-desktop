package io.github.zkhan93.rkms.controller;

import com.sun.deploy.util.Property;
import com.sun.javafx.iio.ImageStorage;
import io.github.zkhan93.rkms.Driver;
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

import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Properties;

public class MainController {
    @FXML
    Button savePort;
    @FXML
    Button toggleBtn;
    @FXML
    TextField port;
    @FXML
    Label status;
    @FXML
    ImageView image;

    private int activePort;
    private Driver driver;
    private boolean serverStarted;
    private Properties properties;
    private FileOutputStream fileOutputStream;
    private FileInputStream fileInputStream;

    {
        driver = new Driver();
        try {
            File file = new File("config.properties");
            fileOutputStream = new FileOutputStream(file);
            fileInputStream = new FileInputStream(file);
            properties = new Properties();
            properties.load(fileInputStream);
        } catch (IOException ex) {
            System.out.println("cannot get properties file: " + ex.getLocalizedMessage());
        }
//        activePort = Integer.parseInt(properties.getProperty("port"));
    }

    @FXML
    public void savePort(ActionEvent event) {
        String portStr = port.getText().trim();
        if (!portStr.isEmpty()) {
            try {
                activePort = Integer.parseInt(port.getText());
                properties.put("port", String.valueOf(activePort));
                properties.store(fileOutputStream, null);
                driver.stop();
                serverStarted = false;
                toggleState(null);
            } catch (NumberFormatException ex) {
                activePort = Constants.PORT;
            } catch (IOException ex) {
                System.out.println("cannot save properties");
            }
        }
    }

    @FXML
    public void toggleState(ActionEvent event) {
        serverStarted = !serverStarted;
        boolean error = false;
        if (serverStarted) {
            try {
                driver.go(activePort);
                serverStarted = true;
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
        port.setText(properties.getProperty("port"));
    }
}
