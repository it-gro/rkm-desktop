package io.github.zkhan93.rkms;

import io.github.zkhan93.rkms.callbacks.ApplicationCallback;
import io.github.zkhan93.rkms.controller.MainViewController;
import io.github.zkhan93.rkms.controller.SettingViewController;
import io.github.zkhan93.rkms.util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application implements ApplicationCallback {

    private MainViewController mainViewController;
    private SettingViewController settingViewController;

    private Stage mainStage;

    @Override
    public void init() throws Exception {
        super.init();
        loadFonts();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        showMainScene();
    }

    @Override
    public void stop() throws Exception {
        mainViewController.stop();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadFonts() {
        try {
            Font.loadFont(Main.class.getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 14);
        } catch (Exception ex) {

        }
    }

    @Override
    public void showSettingScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("setting.fxml"));
            Parent root = fxmlLoader.load();
            mainStage.setTitle("Settings");
            mainStage.setScene(new Scene(root));
            settingViewController = fxmlLoader.getController();
            settingViewController.setApplicationCallback(this);
        } catch (IOException ex) {
            System.out.println("error" + ex.getLocalizedMessage());
        }
    }

    @Override
    public void showInfoScene() {

    }

    @Override
    public void showMainScene() throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
        Parent root = fxmlLoader.load();
        mainStage.setTitle(Constants.APP_NAME);
        mainStage.setScene(new Scene(root, 620, 350));
        mainStage.show();
        mainViewController = fxmlLoader.getController();
        mainViewController.setApplicationCallback(this);
    }
}
