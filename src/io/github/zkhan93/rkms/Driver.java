package io.github.zkhan93.rkms;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {
    Server s = new Server();
    Controller c = new Controller();
    GetCommands gc;
    static Driver d;

    public void go() {
        setNw();
    }

    public void setNw() {
        if (s.startServer()) {
            s.waitForClient();
        } else {
            System.out.println("failed to start server");
        }
    }

    public void startListner() {
        gc = new GetCommands();
        gc.start();
    }

    class GetCommands extends Thread {
        public void run() {
            System.out.println("connection informed");
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        s.cs.getInputStream()));
                int ch;
                String str;
                String stra[];
                while (!isInterrupted() && (str = br.readLine()) != null
                        && Server.connected) {
                    //	System.out.println(str);
                    if (str.startsWith("0")) {
                        // keyboard event
                        str = str.substring(2, str.length());
                        if (str.startsWith("0")) {
                            ch = Integer
                                    .parseInt(str.substring(2, str.length()));
                            //	System.out.println((char) ch);
                            c.typeKey(ch);
                        } else {
                            // if starts with 1 special key
                            ch = Integer.parseInt(str.substring(2,
                                    str.length()));
                            c.specialKey(ch);
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
                            c.mouseMove(Integer.parseInt(stra[1]),
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
                            c.mouseClick(Integer.parseInt(str));
                        }

                    } else if (str.startsWith("2")) {
                        str = str.substring(1, str.length());
                        //	System.out.println("special: " + str);
                    }
                }
                System.out.println("client disconnected");
                s.disconnectClient();
            } catch (IOException e) {
                s.disconnectClient();
                e.printStackTrace();
            }
        }
    }
}