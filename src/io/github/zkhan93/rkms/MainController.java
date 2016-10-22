package io.github.zkhan93.rkms;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController {
    @FXML
    Button savePort;
    @FXML
    Button toggleBtn;
    @FXML
    TextField port;

    @FXML
    public void savePort(ActionEvent event) {
        System.out.println(port.getText());
    }

    @FXML
    public void toggleState(ActionEvent event) {
        System.out.println("start/stop server");
    }
}
