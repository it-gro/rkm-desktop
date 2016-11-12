package io.github.zkhan93.rkms;

import io.github.zkhan93.rkms.callbacks.GetClientTaskCallback;
import io.github.zkhan93.rkms.callbacks.ServerCallbacks;
import io.github.zkhan93.rkms.task.GetClientTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements GetClientTaskCallback {
    ServerSocket serverSocket;
    Socket clientSocket;
    static boolean connected;
    private GetClientTask getClientTask;
    private ServerCallbacks serverCallbacks;

    public Server(ServerCallbacks serverCallbacks) {
        this.serverCallbacks = serverCallbacks;
    }

    public boolean initServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        connected = false;
        System.out.println("Server initialized");
        return true;
    }

    public void waitForClient() {
        getClientTask = new GetClientTask(serverSocket, this);
        getClientTask.start();
    }

    @Override
    public void clientConnected(Socket socket) {
        clientSocket = socket;
        System.out.println("client connected");
        connected = true;
        serverCallbacks.startListener();
    }

    public void stopServer() {
        try {
            if (clientSocket != null && !clientSocket.isClosed())
                clientSocket.close();
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();
            System.out.println("Server stopped");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectClient() {
        try {
            if (clientSocket != null && !clientSocket.isClosed())
                clientSocket.close();
            connected = false;
            waitForClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
