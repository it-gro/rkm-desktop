package io.github.zkhan93.rkms;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    ServerSocket ss;
    Socket cs;
    static boolean connected;
    private GetCient gc;

    public boolean startServer(int port) throws IOException {
        ss = new ServerSocket(port);
        connected = false;
        System.out.println("Server started");
        return true;
    }

    public void waitForClient() {
        gc = new GetCient();
        gc.start();
    }

    class GetCient extends Thread {
        public void run() {
            try {
                System.out.println("waiting for client");
                cs = ss.accept();
                System.out.println("client connected");
                connected = true;
                Driver.d.startListener();
            } catch (IOException e) {
                System.out.println("socket closed");
            }
        }
    }

    public void stopServer() {
        try {
            if (cs != null && !cs.isClosed())
                cs.close();
            if (ss != null && !ss.isClosed())
                ss.close();
            System.out.println("Server stopped");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectClient() {
        try {
            if (cs != null && !cs.isClosed())
                cs.close();
            connected = false;
            waitForClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
