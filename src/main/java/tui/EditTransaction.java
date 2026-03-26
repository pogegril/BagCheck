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
import bank.Assets;
import ledger.Ledger;
import ledger.Transaction;

/**
 * Window to rewrite transactions with edited information
 * @author pogegril
 */
public class EditTransaction extends BasicWindow {

	/**
	 * Creates the transaction editor window
	 * @param tui - User terminal interface
	 * @param ledger - User ledger
	 * @param transaction - Selected transaction
	 */
	public EditTransaction(WindowBasedTextGUI tui, Ledger ledger, Transaction transaction) {
		super("Edit Transaction");
		Assets assets = ledger.getAssets();
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
		name.setText(transaction.getName());
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
		amount.setText(transaction.getAmount().toString());
		inputPanel.addComponent(amount);
		inputPanel.addComponent(new EmptySpace(new TerminalSize(2, 0)));

		// Account picker
		inputPanel.addComponent(new Label("Account: "));
		ComboBox<Account> accountList = new ComboBox<>();
		for (Account account : ledger.getAssets().getAssets()) {
			accountList.addItem(account);
		}
		Account account = assets.getAccountByID(transaction.getAccountID());
		accountList.setSelectedItem(account);
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
		String date = transaction.getDate().format(DateTimeFormatter.ofPattern("d/M/yyyy"));
		dateBox.setText(date);
		inputPanel2.addComponent(dateBox);

		// Tag input
		inputPanel2.addComponent(new Label("Tag: "));
		TextBox tag = new TextBox(new TerminalSize(10, 1));
		tag.setText(transaction.getTag());
		inputPanel2.addComponent(tag);

		window.addComponent(inputPanel2);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Optional description panel
		Panel descInput = new Panel();
		descInput.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		descInput.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		descInput.addComponent(new Label("(Description): "));
		TextBox desc = new TextBox(new TerminalSize(20, 1));
		if (transaction.getDesc() != null) {
			desc.setText(transaction.getDesc());
		}
		descInput.addComponent(desc);

		window.addComponent(descInput);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Save & return
		window.addComponent(new Button(": Save :", () -> {
			try {
				if (amount.getText() == null || amount.getText().isEmpty()) {
					this.close();
				}

				DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("d/M/yyyy");
				LocalDate newDate = LocalDate.parse(dateBox.getText(), dateFormat);
				BigDecimal newAmount = new BigDecimal(amount.getText());

				// Checks if any detail changed and rewrites transaction if so
				if (
						!transaction.getName().equals(name.getText().trim()) ||
						!transaction.getDesc().equals(desc.getText().trim()) || 
						!transaction.getTag().equals(tag.getText().trim()) || 
						!(accountList.getSelectedItem().getID() == transaction.getAccountID()) ||
						!newDate.isEqual(transaction.getDate()) ||
						!(newAmount.compareTo(transaction.getAmount()) == 0)
				   ) {
					Transaction newTransaction = new Transaction(name.getText(), desc.getText(), tag.getText(), accountList.getSelectedItem().getID(), newDate, newAmount);
					ledger.addTransaction(newTransaction);
					ledger.removeTransaction(transaction);
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
