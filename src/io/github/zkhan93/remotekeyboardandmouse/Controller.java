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

	public void pressNormalKey(int keycode) {
		robo.keyPress(keycode);
		robo.keyRelease(keycode);
	}

	public void pressShiftKey(int keycode) {
		try {
			robo.keyPress(KeyEvent.VK_SHIFT);
			robo.keyPress(keycode);
			robo.keyRelease(keycode);
			robo.keyRelease(KeyEvent.VK_SHIFT);
		} catch (IllegalArgumentException e) {
			System.out.println("No such Key"); // tell app that there is no such
			// key
			robo.keyRelease(KeyEvent.VK_SHIFT);
		}
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
	public void mouseMove(int x,int y){
		robo.mouseMove(MouseInfo.getPointerInfo().getLocation().x + x,
				MouseInfo.getPointerInfo().getLocation().y + y);
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

	public void typeKey(int ch) {
		int keycode = ch;// KeyEvent.getExtendedKeyCodeForChar(ch);
		if (ch >= 'A' && ch <= 'Z') {
			pressShiftKey(keycode);
		} else if (ch >= 'a' && ch <= 'z') {
			ch = KeyEvent.getExtendedKeyCodeForChar((char)ch);
			pressNormalKey(ch);
		} else if ((ch >= '0' && ch <= '9')
		// || (keycode >= KeyEvent.VK_F1 && keycode <=
		// KeyEvent.VK_F9)
				|| (ch == '[' || ch == ']' || ch == ';' || ch == '\''
						|| ch == '`' || ch == '\\' || ch == '-' || ch == '='
						|| ch == ',' || ch == '.' || ch == '/')) {
			ch = KeyEvent.getExtendedKeyCodeForChar(ch);
			pressNormalKey(ch);
		} else {

			switch (ch) {
			case '~':
				ch = KeyEvent.getExtendedKeyCodeForChar('`');
				break;
			case '!':
				ch = KeyEvent.getExtendedKeyCodeForChar('1');
				break;
			case '@':
				ch = KeyEvent.getExtendedKeyCodeForChar('2');
				break;
			case '#':
				ch = KeyEvent.getExtendedKeyCodeForChar('3');
				break;
			case '$':
				ch = KeyEvent.getExtendedKeyCodeForChar('4');
				break;
			case '%':
				ch = KeyEvent.getExtendedKeyCodeForChar('5');
				break;
			case '^':
				ch = KeyEvent.getExtendedKeyCodeForChar('6');
				break;
			case '&':
				ch = KeyEvent.getExtendedKeyCodeForChar('7');
				break;
			case '*':
				ch = KeyEvent.getExtendedKeyCodeForChar('8');
				break;
			case '(':
				ch = KeyEvent.getExtendedKeyCodeForChar('9');
				break;
			case ')':
				ch = KeyEvent.getExtendedKeyCodeForChar('0');
				break;
			case '_':
				ch = KeyEvent.getExtendedKeyCodeForChar('-');
				break;
			case '+':
				ch = KeyEvent.getExtendedKeyCodeForChar('=');
				break;
			case '{':
				ch = KeyEvent.getExtendedKeyCodeForChar('[');
				break;
			case '}':
				ch = KeyEvent.getExtendedKeyCodeForChar(']');
				break;
			case '|':
				ch = KeyEvent.getExtendedKeyCodeForChar('\\');
				break;
			case ':':
				ch = KeyEvent.getExtendedKeyCodeForChar(';');
				break;
			case '"':
				ch = KeyEvent.getExtendedKeyCodeForChar('\'');
				break;
			case '<':
				ch = KeyEvent.getExtendedKeyCodeForChar(',');
				break;
			case '>':
				ch = KeyEvent.getExtendedKeyCodeForChar('.');
				break;
			case '?':
				ch = KeyEvent.getExtendedKeyCodeForChar('/');
				break;
			}
			pressShiftKey(ch);
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
