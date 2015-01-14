package io.github.zkhan93.remotekeyboardandmouse;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class Controller {
	private Robot robo;
	static int cx, cy, ex, ey, absx, absy;
	static float m, inc, absvx, absvy;
	static int n, denom;

	public Controller() {
		try {

			robo = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void pressNormalKey(int keycode) {
		try {
			robo.keyPress(keycode);

			robo.keyRelease(keycode);
			System.out.println("key " + keycode + " pressed");
		} catch (IllegalArgumentException e) {
			System.out.println("no such key");
		}
	}

	public void pressUni(int keycode) {
		robo.keyPress(KeyEvent.VK_ALT);

		for (int i = 3; i >= 0; --i) {
			// extracts a single decade of the key-code and adds
			// an offset to get the required VK_NUMPAD key-code
			int numpad_kc = keycode / (int) (Math.pow(10, i)) % 10
					+ KeyEvent.VK_NUMPAD0;

			robo.keyPress(numpad_kc);
			robo.keyRelease(numpad_kc);
		}
		robo.keyRelease(KeyEvent.VK_ALT);
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

	public void specialKey(int code) {
		switch (code) {
		case 1:
			pressNormalKey(KeyEvent.VK_PAGE_UP);
			break;
		case 2:
			pressNormalKey(KeyEvent.VK_PAGE_DOWN);
			break;
		case 3:
			pressNormalKey(KeyEvent.VK_CONTEXT_MENU);
			break;
		case 4:
			pressNormalKey(KeyEvent.VK_ESCAPE);
			break;
		case 5:
			pressNormalKey(KeyEvent.VK_TAB);
			break;
		case 6:
			pressNormalKey(KeyEvent.VK_HOME);
			break;
		case 7:
			pressNormalKey(KeyEvent.VK_END);
			break;
		case 8:
			pressNormalKey(KeyEvent.VK_WINDOWS);
			break;
		case 9:
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_X);
			robo.keyRelease(KeyEvent.VK_X);
			robo.keyRelease(KeyEvent.VK_CONTROL);

			break;
		case 10:
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_C);
			robo.keyRelease(KeyEvent.VK_C);
			robo.keyRelease(KeyEvent.VK_CONTROL);

			break;
		case 11:
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_V);
			robo.keyRelease(KeyEvent.VK_V);
			robo.keyRelease(KeyEvent.VK_CONTROL);

			break;
		case 12:
			robo.keyPress(KeyEvent.VK_CONTROL);
			robo.keyPress(KeyEvent.VK_A);
			robo.keyRelease(KeyEvent.VK_A);
			robo.keyRelease(KeyEvent.VK_CONTROL);

			break;
		}
	}

	public void mouseClick(int click) {
		/**
		 * 1 left 2 right 3 middle 4 scroll up 5 scroll down
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
			robo.mouseWheel(-1);
			break;
		case 5:
			// scroll down
			robo.mouseWheel(1);
			break;
		case 6:
			robo.mousePress(MouseEvent.BUTTON1_MASK);
			break;
		case 7:
			robo.mouseRelease(MouseEvent.BUTTON1_MASK);
			break;

		}
	}

	int factor = 500;

	/**
	 * 
	 * @param x
	 * @param y
	 * @param vx
	 * @param vy
	 */
	public void mouseMove(int x, int y, float vx, float vy) {
		cx = MouseInfo.getPointerInfo().getLocation().x;
		cy = MouseInfo.getPointerInfo().getLocation().y;
		// System.out.println("move by " + vx + "," + vy);
		absvx = Math.abs(vx);
		absvy = Math.abs(vy);
		absx = Math.abs(x);
		absy = Math.abs(y);
		denom = absvx > absvy ? Math.round(absvx) : Math.round(absvy);
		n = absx > absy ? absx * factor : absy * factor;
		n /= (denom == 0 ? factor / 2 : denom);
		// System.out.println("steps= " + n);
		float dx = ((float) x) / n;
		float dy = ((float) y) / n;
		for (int step = 1; step <= n; step++) {
			robo.mouseMove((int) (cx + dx * step), (int) (cy + dy * step));

		}
	}

	/**
	 * 
	 * @param ch
	 */
	public void typeKey(int ch) {
		int keycode = ch;// KeyEvent.getExtendedKeyCodeForChar(ch);
		if (ch == KeyEvent.VK_ENTER || ch == KeyEvent.VK_BACK_SPACE)
			pressNormalKey(ch);
		else
			pressUni(keycode);
		/*
		 * 
		 * if (ch >= 'A' && ch <= 'Z') { pressShiftKey(keycode); } else if (ch
		 * >= 'a' && ch <= 'z') { ch = KeyEvent.getExtendedKeyCodeForChar((char)
		 * ch); pressNormalKey(ch); } else if ((ch >= '0' && ch <= '9') // ||
		 * (keycode >= KeyEvent.VK_F1 && keycode <= // KeyEvent.VK_F9) || (ch ==
		 * Constants.CH_OBRACKET || ch == Constants.CH_CBRACKET || ch ==
		 * Constants.CH_SEMICOLON || ch == Constants.CH_SQUOTE || ch ==
		 * Constants.CH_GRAVE || ch == Constants.CH_FSLASH || ch ==
		 * Constants.CH_DASH || ch == Constants.CH_EQUAL || ch ==
		 * Constants.CH_COMMA || ch == Constants.CH_DOT || ch ==
		 * Constants.CH_BSLASH)) { ch = KeyEvent.getExtendedKeyCodeForChar(ch);
		 * pressNormalKey(ch); } else {
		 * 
		 * switch (ch) { case Constants.CH_TILD: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_GRAVE); break; case
		 * Constants.CH_EXCLAMATION: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_ONE); break; case
		 * Constants.CH_AT: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_TWO); break; case
		 * Constants.CH_HASH: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_THREE); break; case
		 * Constants.CH_DOLLAR: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_FOUR); break; case
		 * Constants.CH_PERCENT: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_FIVE); break; case
		 * Constants.CH_CHARET: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_SIX); break; case
		 * Constants.CH_AND: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_SEVEN); break; case
		 * Constants.CH_ASTERICS: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_EIGHT); break; case
		 * Constants.CH_OPAREN: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_NINE); break; case
		 * Constants.CH_CPAREN: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_ZERO); break; case
		 * Constants.CH_UNDERSCORE: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_DASH); break; case
		 * Constants.CH_PLUS: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_EQUAL); break; case
		 * Constants.CH_OBRACES: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_OBRACKET); break;
		 * case Constants.CH_CBRACES: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_CBRACKET); break;
		 * case Constants.CH_PIPE: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_FSLASH); break; case
		 * Constants.CH_COLON: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_SEMICOLON); break;
		 * case Constants.CH_DQUOTES: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_SQUOTE); break; case
		 * Constants.CH_LESS: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_COMMA); break; case
		 * Constants.CH_GREATER: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_DOT); break; case
		 * Constants.CH_QUES: ch =
		 * KeyEvent.getExtendedKeyCodeForChar(Constants.CH_BSLASH); break; }
		 * pressShiftKey(ch); }
		 */
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
