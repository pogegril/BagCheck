package tui;

import java.util.Arrays;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.ComboBox;
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
import bank.Currency;

/**
 * Window to display and update account's details
 * @author pogegril
 */
public class EditAccount extends BasicWindow {

	/**
	 * Window to display the account's details and handle any user updates
	 * @param tui - User terminal interface
	 * @param account - Selected account
	 */
	public EditAccount(WindowBasedTextGUI tui, Account account) {
		super("Edit");
		setHints(Arrays.asList(Window.Hint.CENTERED));

		// Main window
		Panel window = new Panel();
		window.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		Label topInfo = new Label("  Account details:");
		window.addComponent(topInfo);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		
		// Main panel
		Panel mainPanel = new Panel();
		mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		mainPanel.addComponent(new Label("Name: "));
		mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		// Name
		TextBox name = new TextBox(new TerminalSize(16, 1));
		name.setText(account.getName());
		mainPanel.addComponent(name);
		mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		// Currency
		mainPanel.addComponent(new Label("Currency: "));
		ComboBox<Currency> currencyList = new ComboBox<>();
		for (Currency currency : Currency.values()) {
			currencyList.addItem(currency);
		}
		currencyList.setSelectedItem(account.getCurrency());
		mainPanel.addComponent(currencyList);
		mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		window.addComponent(mainPanel);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Save & return
		window.addComponent(new Button(": Save :", () -> {
			try {
				if (!name.getText().equals(account.getName())) {
					account.setName(name.getText());
				}
				Currency currency = currencyList.getSelectedItem();
				if (!(currency.getID() == account.getCurrency().getID())) {
					account.setCurrency(currency);
				}
				this.close();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			topInfo.setText("  Invalid details.");
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		setComponent(window);
	}
}
