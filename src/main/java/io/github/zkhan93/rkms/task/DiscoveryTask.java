package io.github.zkhan93.rkms.task;

import com.google.gson.Gson;
import io.github.zkhan93.rkms.Main;
import io.github.zkhan93.rkms.models.Host;
import io.github.zkhan93.rkms.util.Constants;

import java.net.*;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

/**
 * Created by Zeeshan Khan on 11/6/2016.
 */
public class DiscoveryTask implements Runnable, PreferenceChangeListener {
	private DatagramSocket socket;
	private byte[] sendData;// = "DISCOVER_RKMS_RESPONSE".getBytes();
	private int activePort;
	private String name, ip;
	private Preferences preferences;
	private Host host;

	public static DiscoveryTask getInstance() {
		return DiscoveryThreadHolder.INSTANCE;
	}

	{
		preferences = Preferences.userNodeForPackage(Main.class);
		preferences.addPreferenceChangeListener(this);
		try {
			InetAddress localHost = Main.getIpAddress();
			ip = localHost.getHostAddress();
			name = preferences.get("name", "");
			if (name.isEmpty()) {
				name = localHost.getHostName();
				if (name.equals(ip))
					try {
						name = Inet4Address.getLocalHost().getHostName();
					} catch (UnknownHostException ex) {

					}
			}
		} catch (SocketException ex) {
			System.out.println("unknown host" + ex.getLocalizedMessage());
		}
		activePort = Preferences.userNodeForPackage(Main.class).getInt("port", Constants.PORT);
		notifyHostDetailsChanged();
	}

	private void notifyHostDetailsChanged() {
		if (host == null)
			host = new Host(name, ip, activePort);
		host.setName(name);
		host.setIp(ip);
		host.setPort(activePort);
		sendData = new Gson().toJson(host, Host.class).getBytes();
	}

	@Override
	public void preferenceChange(PreferenceChangeEvent evt) {
		if (evt.getKey().equals("port"))
			activePort = Integer.parseInt(evt.getNewValue());
		if (evt.getKey().equals("name"))
			name = evt.getNewValue();
		notifyHostDetailsChanged();
	}

	private static class DiscoveryThreadHolder {
		private static final DiscoveryTask INSTANCE = new DiscoveryTask();
	}

	@Override
	public void run() {
		try {
			socket = new DatagramSocket(2222, InetAddress.getByName("0.0.0.0"));
			// socket.setBroadcast(true);
			socket.setSoTimeout(5000);
			byte[] recvBuf = new byte[15000];
			DatagramPacket packet;
			while (!Thread.interrupted()) {
				System.out.println("waiting for client packets");
				packet = new DatagramPacket(recvBuf, recvBuf.length);
				try {
					socket.receive(packet);
					String msg = new String(packet.getData()).trim();
					System.out.println(
							"packet received from -> " + packet.getAddress().getHostAddress() + " data -> " + msg);
					if (msg.startsWith("DISCOVER_RKMS_REQUEST")) {

						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, packet.getAddress(),
								packet.getPort());
						socket.send(sendPacket);
						System.out.println("packet send to " + sendPacket.getAddress().getHostAddress());
					}
				} catch (SocketTimeoutException ex) {
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
