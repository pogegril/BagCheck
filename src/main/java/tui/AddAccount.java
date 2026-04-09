package tui;

import java.math.BigDecimal;
import java.sql.SQLException;
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
import bank.Assets;
import bank.Currency;

/**
 * User input window to create a new account
 */
public class AddAccount extends BasicWindow {

	/**
	 * Creates the account creation window
	 * @param tui - User terminal interface
	 * @param assets - User assets
	 */
	public AddAccount(WindowBasedTextGUI tui, Assets assets) {
		super("Create account");
		setHints(Arrays.asList(Window.Hint.CENTERED));

		// Main window
		Panel window = new Panel();
		window.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Top info
		Label topInfo = new Label("  Register account's information:");
		window.addComponent(topInfo);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Input panel
		Panel inputPanel = new Panel();
		inputPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		inputPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));
		
		// Name input
		inputPanel.addComponent(new Label("Name: "));
		TextBox name = new TextBox(new TerminalSize(12, 1));
		inputPanel.addComponent(name);
		inputPanel.addComponent(new EmptySpace(new TerminalSize(2, 0)));

		// Starter balance
		inputPanel.addComponent(new Label("Balance:"));
		TextBox balance = new TextBox(new TerminalSize(10, 1));
		inputPanel.addComponent(balance);
		inputPanel.addComponent(new EmptySpace(new TerminalSize(2, 0)));

		// Currency picker
		inputPanel.addComponent(new Label("Currency: "));
		ComboBox<Currency> currencyList = new ComboBox<>();
		for (Currency currency : Currency.values()) {
			currencyList.addItem(currency);
		}
		inputPanel.addComponent(currencyList);
		inputPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		window.addComponent(inputPanel);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		Panel buttonPanel = new Panel();
		buttonPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		
		// Save & return
		buttonPanel.addComponent(new Button(": Save :", () -> {
			try {
				BigDecimal starterBalance = BigDecimal.ZERO;
				if (!(balance.getText() == null || balance.getText().isEmpty())) {	
					starterBalance = new BigDecimal(balance.getText());
				}
				Account account = new Account(name.getText(), currencyList.getSelectedItem(), starterBalance);
				assets.addAccount(account);
				this.close();
			} catch (IllegalArgumentException e) {
				topInfo.setText("  Invalid information.");
			} catch (SQLException e) {
				topInfo.setText("  Invalid information.");
			}
		}));
		buttonPanel.addComponent(new EmptySpace(new TerminalSize(2, 0)));

		buttonPanel.addComponent(new Button(":  Back  :", () -> {
			this.close();
		}));

		window.addComponent(buttonPanel, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		setComponent(window);
	}
}
