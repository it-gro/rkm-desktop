package io.github.zkhan93.rkms;

import io.github.zkhan93.rkms.controller.MainController;
import io.github.zkhan93.rkms.util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    private MainController mainController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ui/sample.fxml"));
        Parent root = fxmlLoader.load();
        mainController = fxmlLoader.getController();
        primaryStage.setTitle(Constants.APP_NAME);
        primaryStage.setScene(new Scene(root, 320, 320));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        mainController.stop();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
