package io.github.zkhan93.remotekeyboardandmouse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {
	Server s;
	Controller c;
	GetCommands gc;
	static Driver d;

	public static void main(String args[]) throws IOException {
		d = new Driver();
		d.go();
	}

	public void go() {
		setGui();
		setNw();
	}

	public void setGui() {
		// setup gui
	}

	public void setNw() {
		c = new Controller();
		s = new Server();
		if (s.startServer()) {
			s.waitForClient();
		}
	}

	public void startListner() {
		gc = new GetCommands();
		gc.start();
	}

	class GetCommands extends Thread {
		public void run() {

			while (Server.connected == false) {
				System.out.println("waiting getcommand for client");
			}
			System.out.println("connection informed");
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						s.cs.getInputStream()));
				char ch;
				while (!isInterrupted() && Server.connected) {
					ch = br.readLine().toCharArray()[0];
					System.out.println(ch);
					c.typeKey(ch);
				}
			} catch (IOException e) {
				s.disconnectClient();
				e.printStackTrace();
			}
		}
	}
}