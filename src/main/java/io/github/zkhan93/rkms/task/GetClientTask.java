package io.github.zkhan93.rkms.task;

import io.github.zkhan93.rkms.callbacks.GetClientTaskCallback;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Zeeshan Khan on 11/6/2016.
 */
public class GetClientTask extends Thread {
    private WeakReference<GetClientTaskCallback> GetClientTaskCallbackRef;
    private ServerSocket serverSocket;

    public GetClientTask(ServerSocket serverSocket, GetClientTaskCallback getClientTaskCallback) {
        this.serverSocket = serverSocket;
        GetClientTaskCallbackRef = new WeakReference<>(getClientTaskCallback);
    }

    @Override
    public void run() {
        try {
            System.out.println("waiting for client");
            Socket clientSocket = serverSocket.accept();
            GetClientTaskCallbackRef.get().clientConnected(clientSocket);
        } catch (IOException e) {
            System.out.println("socket closed ungracefully " + e.getLocalizedMessage());
        }
    }
}
