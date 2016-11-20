package io.github.zkhan93.rkms.callbacks;

/**
 * Created by zeeshan on 11/14/2016.
 */
public interface ApplicationCallback {
    void showSettingScene();

    void showAboutScene();

    void showMainScene();

//    void setPort(int port);
//
//    void setName(String name);

    void reset();

    void stopServer();
}
