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
				String str;
				while (!isInterrupted() && Server.connected) {
					str = br.readLine();
					if (str.startsWith("0")) {
						// keyboard event
						ch = str.toCharArray()[1];
						System.out.println(ch);
						c.typeKey(ch);
					} else if (str.startsWith("1")) {
						// mouse event
						str = str.substring(2, str.length());
						if(str.startsWith("0")){
							// mouse move event
							str=str.substring(2, str.length());
							System.out.println("mouse move : " + str);
							c.mouseMove(Integer.parseInt(str));
						}
						else{
							// mouse click event
							str=str.substring(2, str.length());
							System.out.println("mouse click : " + str);
							c.mouseClick(Integer.parseInt(str));
						}
						
					} else {
						str = str.substring(1, str.length());
						System.out.println("special: " + str);
					}

				}
			} catch (IOException e) {
				s.disconnectClient();
				e.printStackTrace();
			}
		}
	}
}