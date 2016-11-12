package io.github.zkhan93.rkms.callbacks;

import java.net.Socket;

/**
 * Created by Zeeshan Khan on 11/6/2016.
 */
public interface GetClientTaskCallback {
    void clientConnected(Socket socket);
}
