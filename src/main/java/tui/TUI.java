package tui;

import java.io.IOException;

import com.googlecode.lanterna.graphics.SimpleTheme;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.TextColor;

import ledger.Ledger;

/**
 * Terminal User Interface manager
 * @author pogegril
 */
public class TUI {

	/**
	 * Starts the multi-window TUI interface on the main menu
	 * @param ledger - User's ledger
	 */
	public void start(Ledger ledger) throws IOException {
		Terminal terminal = new DefaultTerminalFactory().createTerminal();	
		Screen screen = new TerminalScreen(terminal);
		try {
			screen.startScreen();
			MultiWindowTextGUI tui = new MultiWindowTextGUI(screen);

			tui.setTheme(new SimpleTheme(new TextColor.RGB(145, 121, 153), new TextColor.RGB(25, 18, 29)));
			tui.addWindowAndWait(new MainMenu(tui, ledger));
		} finally {
			screen.stopScreen();
		}
	}
}
