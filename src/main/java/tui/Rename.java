package tui;

import java.util.Arrays;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.TerminalSize;

import bank.Account;
public class Rename extends BasicWindow {

	public Rename(WindowBasedTextGUI tui, Account account) {
		super("Rename");
		setHints(Arrays.asList(Window.Hint.CENTERED));

		// Main window
		Panel window = new Panel();
		window.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		window.addComponent(new EmptySpace(new TerminalSize(1, 0)));
		
		Panel mainPanel = new Panel();
		mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		Label prompt = new Label("Enter the new name: ");
		mainPanel.addComponent(prompt);
		mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		TextBox name = new TextBox(new TerminalSize(16, 1));
		name.setText(account.getName());
		mainPanel.addComponent(name);
		mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Save & return
		mainPanel.addComponent(new Button(": Save :", () -> {
			try {
				account.setName(name.getText());
				this.close();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			prompt.setText("Invalid name.");
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

		window.addComponent(mainPanel);
		window.addComponent(new EmptySpace(new TerminalSize(1, 0)));
		setComponent(window);
	}
}
