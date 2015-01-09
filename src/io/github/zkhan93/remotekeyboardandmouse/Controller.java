package io.github.zkhan93.remotekeyboardandmouse;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

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

	public void mouseClick(int click) {
		/**
		 * 1 left 2 right 3 middle
		 */
		switch (click) {
		case 1:
			robo.mousePress(MouseEvent.BUTTON1_MASK);
			robo.mouseRelease(MouseEvent.BUTTON1_MASK);
			break;
		case 2:
			robo.mousePress(MouseEvent.BUTTON2_MASK);
			robo.mouseRelease(MouseEvent.BUTTON2_MASK);
			break;
		case 3:
			robo.mousePress(MouseEvent.BUTTON3_MASK);
			robo.mouseRelease(MouseEvent.BUTTON3_MASK);
			break;
		case 4: // scroll up
			robo.mouseWheel(-2);
			break;
		case 5:
			// scroll down
			robo.mouseWheel(2);
			break;
		}
	}

	public void mouseMove(int action) {
		int x, y;
		switch (action) {
		case 1: // moving up by 10 pixels
			x = 0;
			y = -10;
			break;
		case 2:
			x = 10;
			y = -10;
			break;
		case 3:
			x = 10;
			y = 0;
			break;
		case 4:
			x = 10;
			y = 10;
			break;
		case 5:
			x = 0;
			y = 10;
			break;
		case 6:
			x = -10;
			y = 10;
			break;
		case 7:
			x = -10;
			y = 0;
			break;
		case 8:
			x = -10;
			y = -10;
			break;
		default:
			x = 0;
			y = 0;
			break;
		}
		robo.mouseMove(MouseInfo.getPointerInfo().getLocation().x + x,
				MouseInfo.getPointerInfo().getLocation().y + y);

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
			System.out.println("No such Key"); // tell app that there is no such
												// key
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
