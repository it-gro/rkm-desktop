package io.github.zkhan93.rkms.task;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import static io.github.zkhan93.rkms.util.Constants.DISCOVERY_PORT;

/**
 * Created by Zeeshan Khan on 11/6/2016.
 */
public class DiscoveryTask implements Runnable {
    DatagramSocket socket;
    byte[] sendData = "DISCOVER_RKMS_RESPONSE".getBytes();
    public static DiscoveryTask getInstance() {
        return DiscoveryThreadHolder.INSTANCE;
    }

    private static class DiscoveryThreadHolder {
        private static final DiscoveryTask INSTANCE = new DiscoveryTask();
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(2222,InetAddress.getByName("0.0.0.0"));
//            socket.setBroadcast(true);
            socket.setSoTimeout(5000);
            byte[] recvBuf = new byte[15000];
            DatagramPacket packet;
            while (!Thread.interrupted()) {
                System.out.println("waiting for client packets");
                packet = new DatagramPacket(recvBuf, recvBuf.length);
                try {
                    socket.receive(packet);
                    String msg = new String(packet.getData()).trim();
                    System.out.println("packet received from -> " + packet.getAddress().getHostAddress() + " data -> " + msg);
                    if (msg.startsWith("DISCOVER_RKMS_REQUEST")) {

                        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                        socket.send(sendPacket);
                        System.out.println("packet send to " + sendPacket.getAddress().getHostAddress());
                    }
                } catch (SocketTimeoutException ex) {
                }
            }
        } catch (Exception ex) {
            System.out.printf("discovery exception " + ex.getLocalizedMessage());
        }
    }
}
