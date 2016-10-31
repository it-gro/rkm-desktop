package io.github.zkhan93.rkms;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket serverSocket;
    Socket clientSocket;
    static boolean connected;
    private GetClientTask getClientTask;
    private ServerCallbacks serverCallbacks;

    public Server(ServerCallbacks serverCallbacks) {
        this.serverCallbacks = serverCallbacks;
    }

    public boolean startServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        connected = false;
        System.out.println("Server started");
        return true;
    }

    public void waitForClient() {
        getClientTask = new GetClientTask();
        getClientTask.start();
    }

    class GetClientTask extends Thread {
        public void run() {
            try {
                System.out.println("waiting for client");
                clientSocket = serverSocket.accept();
                System.out.println("client connected");
                connected = true;
                serverCallbacks.startListener();
            } catch (IOException e) {
                System.out.println("socket closed");
            }
        }
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
