package io.github.zkhan93.rkms.controller;

import io.github.zkhan93.rkms.Driver;
import io.github.zkhan93.rkms.util.Constants;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class MainController {
    @FXML
    Button savePort;
    @FXML
    Button toggleBtn;
    @FXML
    TextField port;
    @FXML
    Label status;

    private int activePort;
    private Driver driver;
    private boolean serverStarted;

    {
        activePort = Constants.PORT;
        driver = new Driver();
    }

    @FXML
    public void savePort(ActionEvent event) {
        String portStr = port.getText().trim();
        if (!portStr.isEmpty()) {
            try {
                activePort = Integer.parseInt(port.getText());
                driver.stop();
                serverStarted = false;
                toggleState(null);
            } catch (NumberFormatException ex) {
                activePort = Constants.PORT;
            }
        }
    }

    @FXML
    public void toggleState(ActionEvent event) {
        serverStarted = !serverStarted;
        if (serverStarted) {
            try {
                driver.go(activePort);
                serverStarted = true;
            } catch (IOException ex) {
                activePort++;
                serverStarted = false;
            }
        } else {
            driver.stop();
        }
        //update status status
        if (serverStarted) {
            status.setText("Server started and listening on " + activePort);
            toggleBtn.setText("Stop");
        } else {
            status.setText("Server not started on port " + activePort + " try different port");
            toggleBtn.setText("Start");
        }
        System.out.println("start/stop server");
    }

    public void stop() {
        if (driver != null)
            driver.stop();
    }


}
