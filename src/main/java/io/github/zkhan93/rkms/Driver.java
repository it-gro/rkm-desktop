package io.github.zkhan93.rkms;

import io.github.zkhan93.rkms.callbacks.ServerCallbacks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver implements ServerCallbacks {
    Server server;
    Controller controller;
    GetCommands getCommands;

    public Driver() {
        server = new Server(this);
        controller = new Controller();
    }

    public void go(int port) throws IOException {
        setNetwork(port);
    }

    public void stop() {
        server.stopServer();
    }

    public void setNetwork(int port) throws IOException {
        if (server.initServer(port)) {
            server.waitForClient();
        } else {
            System.out.println("failed to start server");
        }
    }

    public void startListener() {
        getCommands = new GetCommands();
        getCommands.setPriority(Thread.MAX_PRIORITY);
        getCommands.start();
    }

    class GetCommands extends Thread {
        public void run() {
            System.out.println("connection informed");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        server.clientSocket.getInputStream()));
                int ch;
                String str;
                String stra[];
                while (!isInterrupted() && (str = br.readLine()) != null
                        && Server.connected) {
                    	System.out.println(str);
                    if (str.startsWith("0")) {
                        // keyboard event
                        str = str.substring(2, str.length());
                        if (str.startsWith("0")) {
                            ch = Integer
                                    .parseInt(str.substring(2, str.length()));
                            	System.out.println((char) ch);
                            controller.typeKey(ch);
                        } else {
                            // if starts with 1 special key
                            ch = Integer.parseInt(str.substring(2,
                                    str.length()));
                            controller.specialKey(ch);
                            //	System.out.println("received skey "+ch);

                        }
                    } else if (str.startsWith("1")) {
                        // 1 mouse event
                        str = str.substring(2, str.length());
                        if (str.startsWith("0")) {
                            // 0 mouse move event
                            stra = str.split(":");
                            // System.out.println("mouse move : " +
                            // stra[1]+","+stra[2]);
                            controller.mouseMove(Integer.parseInt(stra[1]),
                                    Integer.parseInt(stra[2]),
                                    Float.parseFloat(stra[3]),
                                    Float.parseFloat(stra[4]));
                        } else {
                            // 1 mouse click event
                            /**
                             * 1 left 2 middle 3 right 4 scroll up 5 scroll down
                             */
                            str = str.substring(2, str.length());
                            // System.out.println("mouse click : " + str);
                            controller.mouseClick(Integer.parseInt(str));
                        }

                    } else if (str.startsWith("2")) {
                        str = str.substring(1, str.length());
                        //	System.out.println("special: " + str);
                    }
                }
                System.out.println("client disconnected");
                server.disconnectClient();
            } catch (IOException e) {
                server.disconnectClient();
                e.printStackTrace();
            }
        }
    }
}