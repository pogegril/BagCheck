package tui;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.LocalDate;
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
import ledger.Ledger;
import ledger.Transaction;

/**
 * User input window to create a new transaction
 * @author pogegril
 */
public class AddTransaction extends BasicWindow {

	/**
	 * Creates the account creation window
	 * @param tui - User terminal interface
	 * @param ledger - User ledger
	 */
	public AddTransaction(WindowBasedTextGUI tui, Ledger ledger) {
		super("Create transaction");
		setHints(Arrays.asList(Window.Hint.CENTERED));

		// Main window
		Panel window = new Panel();
		window.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Name panel
		Panel nameInput = new Panel();
		nameInput.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		nameInput.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		nameInput.addComponent(new Label("Name: "));
		TextBox name = new TextBox(new TerminalSize(20, 1));
		nameInput.addComponent(name);

		window.addComponent(nameInput);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));
	
		// Input panel
		Panel inputPanel = new Panel();
		inputPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		inputPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		// Amount input
		inputPanel.addComponent(new Label("Amount: "));
		TextBox amount = new TextBox(new TerminalSize(5, 1));
		inputPanel.addComponent(amount);
		inputPanel.addComponent(new EmptySpace(new TerminalSize(2, 0)));

		// Account picker
		inputPanel.addComponent(new Label("Account: "));
		ComboBox<Account> accountList = new ComboBox<>();
		for (Account account : ledger.getAssets().getAssets()) {
			accountList.addItem(account);
		}
		inputPanel.addComponent(accountList);
		
		window.addComponent(inputPanel);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		
		// Second input panel
		Panel inputPanel2 = new Panel();
		inputPanel2.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		inputPanel2.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		// Date input
		inputPanel2.addComponent(new Label("Date: "));
		TextBox dateBox = new TextBox(new TerminalSize(10, 1));
		inputPanel2.addComponent(dateBox);

		// Tag input
		inputPanel2.addComponent(new Label("Tag: "));
		TextBox tag = new TextBox(new TerminalSize(10, 1));
		inputPanel2.addComponent(tag);

		window.addComponent(inputPanel2);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Optional description panel
		Panel descInput = new Panel();
		descInput.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		descInput.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		descInput.addComponent(new Label("(Description): "));
		TextBox desc = new TextBox(new TerminalSize(20, 1));
		descInput.addComponent(desc);

		window.addComponent(descInput);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Save & return
		window.addComponent(new Button(": Save :", () -> {
			try {
				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d/M/yyyy");
				if (dateBox == null || dateBox.getText().isEmpty()) {
					this.close();
				} else {
					LocalDate date = LocalDate.parse(dateBox.getText(), dateFormat);
					if (amount.getText() == null || amount.getText().isEmpty()) {
						this.close();
					} else {
							if (!(desc.getText() == null || desc.getText().equals(""))) {
							Transaction transaction = new Transaction(name.getText(), desc.getText(), tag.getText(), accountList.getSelectedItem().getID(), date, new BigDecimal(amount.getText()));
							ledger.addTransaction(transaction);
						} else {
							Transaction transaction = new Transaction(name.getText(), tag.getText(), accountList.getSelectedItem().getID(), date, new BigDecimal(amount.getText()));
							ledger.addTransaction(transaction);
						}	
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (DateTimeParseException e) {
				e.printStackTrace();
			}
			this.close();
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		setComponent(window);
	}
}
