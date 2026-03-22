package tui;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.ComboBox;
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
 * User window for inter-account transfers
 * @author pogegril
 */
public class Transfer extends BasicWindow {

	public Transfer(WindowBasedTextGUI tui, Ledger ledger) {
		super("Transfer");
		setHints(Arrays.asList(Window.Hint.CENTERED));

		// Main window
		Panel window = new Panel();
		window.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		Panel inputPanel = new Panel();
		inputPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		inputPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		inputPanel.addComponent(new Label("From: "));
		ComboBox<Account> senderPicker = new ComboBox<>();
		for (Account account : ledger.getAssets().getAssets()) {
			senderPicker.addItem(account);
		}
		inputPanel.addComponent(senderPicker);
		inputPanel.addComponent(new EmptySpace(new TerminalSize(4, 0)));

		inputPanel.addComponent(new Label("To: "));
		ComboBox<Account> receiverPicker = new ComboBox<>();
		for (Account account : ledger.getAssets().getAssets()) {
			receiverPicker.addItem(account);
		}
		inputPanel.addComponent(receiverPicker);
		inputPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		inputPanel.addComponent(new Label("Amount"));
		TextBox amount = new TextBox(new TerminalSize(8, 1));
		inputPanel.addComponent(amount);
		inputPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		window.addComponent(inputPanel);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		Panel descPanel = new Panel();
		descPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		descPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));
		descPanel.addComponent(new Label("(Description)"));
		TextBox desc = new TextBox(new TerminalSize(15, 1));
		descPanel.addComponent(desc);
		descPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		window.addComponent(descPanel);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		window.addComponent(new Button(": Save :", () -> {
			Account sender = senderPicker.getSelectedItem();
			Account receiver = receiverPicker.getSelectedItem();
			if (sender.compare(receiver) || sender.getCurrency().getID() != receiver.getCurrency().getID()) {
				this.close();
			}
			try {
				String value = amount.getText().trim();
				if (!(value == null || value.isEmpty())) {

					String description = desc.getText();
					String senderName = "Tranfer to " + receiver.getName();
					String receiverName = "Transfer from " + sender.getName();

					if (!(description == null || description.isEmpty())) {
						Transaction sent = new Transaction(senderName, description, "Transfer", sender.getID(), LocalDate.now(), new BigDecimal(amount.getText()).negate());
						Transaction received = new Transaction(receiverName, description, "Transfer", receiver.getID(), LocalDate.now(), new BigDecimal(amount.getText()));
						ledger.addTransaction(sent);
						ledger.addTransaction(received);
					} else {
						Transaction sent = new Transaction(senderName, "Transfer", sender.getID(), LocalDate.now(), new BigDecimal(amount.getText()).negate());
						Transaction received = new Transaction(receiverName, "Transfer", receiver.getID(), LocalDate.now(), new BigDecimal(amount.getText()));
						ledger.addTransaction(sent);
						ledger.addTransaction(received);
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			this.close();
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		setComponent(window);
	}
}
