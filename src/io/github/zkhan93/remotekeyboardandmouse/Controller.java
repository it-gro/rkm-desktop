package io.github.zkhan93.remotekeyboardandmouse;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Controller {
	private Robot robo;

	public Controller() {
		try {
			robo = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void pressKey(int keycode) {
		robo.keyPress(keycode);
	}

	public void releaseKey(int keycode) {
		robo.keyRelease(keycode);
	}

	public void typeKey(char ch) {
		try {
			int keycode = KeyEvent.getExtendedKeyCodeForChar(ch);
			if (ch >= 'A' && ch <= 'Z') {
				pressKey(KeyEvent.VK_SHIFT);
				pressKey(keycode);
				releaseKey(keycode);
				releaseKey(KeyEvent.VK_SHIFT);
			} else if ((ch >= '0' && ch <= '9')
					|| (ch >= 'a' && ch <= 'z')
					|| (keycode >= KeyEvent.VK_F1 && keycode <= KeyEvent.VK_F9)
					|| (ch == '[' || ch == ']' || ch == ';' || ch == '\''
							|| ch == '`' || ch == '\\' || ch == '-'
							|| ch == '=' || ch == ',' || ch == '.' || ch == '/')) {
				pressKey(keycode);
				releaseKey(keycode);
			} else {
				switch (keycode) {
				case KeyEvent.VK_LEFT_PARENTHESIS:
					keycode = KeyEvent.VK_9;
					break;
				case KeyEvent.VK_RIGHT_PARENTHESIS:
					keycode = KeyEvent.VK_0;
					break;
				case KeyEvent.VK_BRACELEFT:
					keycode = KeyEvent.VK_OPEN_BRACKET;
					break;
				case KeyEvent.VK_BRACERIGHT:
					keycode = KeyEvent.VK_CLOSE_BRACKET;
					break;
				}
				pressKey(KeyEvent.VK_SHIFT);
				pressKey(keycode);
				releaseKey(keycode);
				releaseKey(KeyEvent.VK_SHIFT);
			}
		} catch (IllegalArgumentException e) {
			System.out.println("No such Key"); //tell app that there is no such key
			releaseKey(KeyEvent.VK_SHIFT);
		}
	}

	public static void shutdown() {
		try {
			String shutdownCommand;
			String operatingSystem = System.getProperty("os.name");
			
			if ("Linux".equals(operatingSystem)
					|| "Mac OS X".equals(operatingSystem)) {
				shutdownCommand = "shutdown -h now";
			} else if ("Windows".equals(operatingSystem)) {
				shutdownCommand = "shutdown.exe -s -t 0";
			} else {
				throw new RuntimeException("Unsupported operating system.");
			}

			Runtime.getRuntime().exec(shutdownCommand);
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
