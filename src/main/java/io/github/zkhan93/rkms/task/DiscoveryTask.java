package io.github.zkhan93.rkms.task;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static io.github.zkhan93.rkms.util.Constants.DISCOVERY_PORT;

/**
 * Created by Zeeshan Khan on 11/6/2016.
 */
public class DiscoveryTask implements Runnable {
    DatagramSocket socket;

    public static DiscoveryTask getInstance() {
        return DiscoveryThreadHolder.INSTANCE;
    }

    private static class DiscoveryThreadHolder {
        private static final DiscoveryTask INSTANCE = new DiscoveryTask();
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket(DISCOVERY_PORT, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            while (true) {
                System.out.println("ready to announce");
                byte[] recvBuf = new byte[15000];
                DatagramPacket packet = new DatagramPacket(recvBuf, recvBuf.length);
                socket.receive(packet);

                String msg = new String(packet.getData()).trim();
                System.out.println("packet recived from -> " + packet.getAddress().getHostAddress() + " data -> " + msg);
                if (msg.startsWith("DISCOVER_RKMS_REQUEST")) {
                    byte[] sendData = "DISCOVER_RKMS_RESPONSE".getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(), packet.getPort());
                    socket.send(sendPacket);
                    System.out.println("packet send to " + sendPacket.getAddress().getHostAddress());
                }
            }
        } catch (Exception ex) {
            System.out.printf("discovert exception " + ex.getLocalizedMessage());
        }
    }
}
