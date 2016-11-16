package io.github.zkhan93.rkms;

import io.github.zkhan93.rkms.callbacks.ApplicationCallback;
import io.github.zkhan93.rkms.controller.AboutViewController;
import io.github.zkhan93.rkms.controller.MainViewController;
import io.github.zkhan93.rkms.controller.SettingViewController;
import io.github.zkhan93.rkms.task.DiscoveryTask;
import io.github.zkhan93.rkms.util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.prefs.Preferences;

public class Main extends Application implements ApplicationCallback {

    private MainViewController mainViewController;
    private SettingViewController settingViewController;
    private AboutViewController aboutViewController;

    private Stage mainStage;

    private int activePort;
    private Driver driver;
    private boolean serverStarted;
    private Preferences preference;
    private Thread discoveryThread;
    private String name;

    @Override
    public void init() throws Exception {
        super.init();
        loadFonts();
        System.out.println("loaded port from preference");
        driver = new Driver();
        preference = Preferences.userNodeForPackage(MainViewController.class);
        activePort = preference.getInt("port", Constants.PORT);
        try {
            activePort = preference.getInt("port", Constants.PORT);
        } catch (Exception ex) {
            System.out.println("invalid port in preferences switched to default");
            activePort = Constants.PORT;
        }
        driver.stop();
        serverStarted = false;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        showMainScene();
        mainViewController.setPort(activePort);
        toggleState();
    }

    @Override
    public void stop() throws Exception {
        stopServer();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void loadFonts() {
        try {
            Font.loadFont(Main.class.getResource("/fonts/Roboto-Regular.ttf").toExternalForm(), 14);
        } catch (Exception ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
        }
    }

    @Override
    public void showAboutScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("about.fxml"));
            Parent root = fxmlLoader.load();
            mainStage.setTitle("About");
            mainStage.setScene(new Scene(root));
            aboutViewController = fxmlLoader.getController();
            aboutViewController.setApplicationCallback(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void showMainScene() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("main.fxml"));
            Parent root = fxmlLoader.load();
            mainStage.setTitle(Constants.APP_NAME);
            mainStage.setScene(new Scene(root, 620, 350));
            mainStage.show();
            mainViewController = fxmlLoader.getController();
            mainViewController.setApplicationCallback(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void setPort(int port) {
        activePort = port;
        preference.putInt("port", activePort);
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void reset() {
        driver.stop();
        discoveryThread.interrupt();
        serverStarted = false;
        toggleState();
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
            mainViewController.setStatus("Server ready to connect");
            String ip = null;
            try {
                ip = Inet4Address.getLocalHost().getHostAddress();
            } catch (UnknownHostException ex) {
                System.out.println("exception cannot get my IP" + ex.getLocalizedMessage());
            }
            mainViewController.setQrcode(ip, activePort);
//            imgQrcode.setImage(new Image(QRCode.from(ip + ":" + activePort).withSize(200, 200).to(ImageType.JPG).file().toURI().toString()));
        } else {
            if (error)
                mainViewController.setStatus("Server stopped by error: " + error);
            else
                mainViewController.setStatus("Server stopped");
        }
        System.out.println("start/stop server");
    }

    @Override
    public void stopServer() {
        if (driver != null)
            driver.stop();
        if (discoveryThread != null)
            discoveryThread.interrupt();
    }
}
