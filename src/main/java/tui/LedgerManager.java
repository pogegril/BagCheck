package tui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.TerminalSize;

import bank.Assets;
import ledger.Ledger;
import ledger.Transaction;

/**
 * Terminal window to manage user's ledger
 * @author pogegril
 */
public class LedgerManager extends BasicWindow {

	// Transaction selection
	private Transaction selected;
	private TextBox filterBox;
	private RadioBoxList<Transaction> transList;
	private Panel infoPanel;

	/**
	 * Creates the ledger manager window
	 * @param tui - User terminal interface
	 * @param ledger - User ledger
	 */
	public LedgerManager(WindowBasedTextGUI tui, Ledger ledger) {
		super("Ledger");
		setHints(Arrays.asList(Window.Hint.CENTERED));
		Assets assets = ledger.getAssets();

		// Window panel
		Panel window = new Panel();
		window.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		Panel topPanel = new Panel();
		topPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		topPanel.addComponent(new EmptySpace(new TerminalSize(3, 0)));
		topPanel.addComponent(new Label("Filter: "));		
		this.filterBox = new TextBox(new TerminalSize(20, 1));
		topPanel.addComponent(this.filterBox);

		topPanel.addComponent(new EmptySpace(new TerminalSize(3, 0)));
		topPanel.addComponent(new Button(":  Apply  :", () -> {
			updateTransactions(ledger);
		}));

		window.addComponent(topPanel);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Main panel
		Panel mainPanel = new Panel();
		mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		// Transactions panel
		Panel transPanel = new Panel();
		//transPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL)); // Unecessary unless the panel gets more components
		transPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		this.transList = new RadioBoxList<>(new TerminalSize(30, 10));
		this.updateTransactions(ledger);

		this.transList.addListener((selected, lastSelection) -> {
			this.selected = this.transList.getCheckedItem();
			updateInfo(assets);
		});

		transPanel.addComponent(this.transList);
		transPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		Component borderedTransPanel = transPanel.withBorder(Borders.singleLine("Transactions"));
		mainPanel.addComponent(borderedTransPanel);

		// Side panel
		Panel sidePanel = new Panel();
		sidePanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		sidePanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		Panel sideContent = new Panel();
		sideContent.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		sideContent.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Transaction info sub-panel
		this.infoPanel = new Panel();
		this.infoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		updateInfo(assets);
		sideContent.addComponent(this.infoPanel, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

		// Menu sub-panel
		Panel menuPanel = new Panel();
		menuPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		menuPanel.addComponent(new Label("╔════ Menu ════╗"));
		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		menuPanel.addComponent(new Button(": Add :", () -> {
			tui.addWindowAndWait(new AddTransaction(tui, ledger));
			updateTransactions(ledger);
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

		menuPanel.addComponent(new Button(": Edit :", () -> {
			if (!(this.selected == null)) {
				tui.addWindowAndWait(new EditTransaction(tui, ledger, this.selected));
				updateTransactions(ledger);
			} else {
				this.infoPanel.removeAllComponents();
				this.infoPanel.addComponent(new Label("Select a transaction\n    to edit."), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
			}
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

		menuPanel.addComponent(new Button(": Remove :", () -> {
			if (!(this.selected == null)) {
				try {
					ledger.removeTransaction(this.selected);
					updateTransactions(ledger);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				this.infoPanel.removeAllComponents();
				this.infoPanel.addComponent(new Label("Select a transaction\n    to remove."), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
			}
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

		menuPanel.addComponent(new Button(": Transfer :", () -> {
			tui.addWindowAndWait(new Transfer(tui, ledger));
			updateTransactions(ledger);
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		menuPanel.addComponent(new Button(": Back :", () -> {
			this.close();
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		
		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		menuPanel.addComponent(new Label("╚══════════════╝"));
		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		sideContent.addComponent(menuPanel, LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		sidePanel.addComponent(sideContent);
		sidePanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		mainPanel.addComponent(sidePanel);
		mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 2)));
		window.addComponent(mainPanel);
		setComponent(window);
	}

	/**
	 * Updates the transaction's information sub-panel based on the selected account
	 */
	public void updateInfo(Assets assets) {
		this.infoPanel.removeAllComponents();

		if (this.selected == null) {
			this.infoPanel.addComponent(new Label("Select a transaction."), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		} else {
			this.infoPanel.addComponent(new Label(this.selected.getName()), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
			if (this.selected.getDesc() != null && this.selected.getDesc() != "") {
				this.infoPanel.addComponent(new Label("(" + this.selected.getDesc() + ")"), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
			}
			this.infoPanel.addComponent(new Label("Amount: " + this.selected.getAmount().toString()), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
			this.infoPanel.addComponent(new Label("Account: " + assets.getAccountByID(this.selected.getAccountID()).getName()), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
			String date = this.selected.getDate().getDayOfWeek() + " - " + this.selected.getDate().getDayOfMonth() + "/" + this.selected.getDate().getMonthValue() + "/" + this.selected.getDate().getYear();
			this.infoPanel.addComponent(new Label(date), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		}
	}

	/**
	 * Updates the transaction list panel
	 * Applies filters if there are any
	 * @param ledger - User ledger
	 */
	public void updateTransactions(Ledger ledger) {
		this.transList.clearItems();
		if (this.filterBox.getText() == null || this.filterBox.getText().isEmpty()) {
			for (ArrayList<Transaction> dayRecords : ledger.getLedger().descendingMap().values()) {
				for (Transaction transaction : dayRecords) {
					this.transList.addItem(transaction);
				}

			}
		} else {
			for (ArrayList<Transaction> dayRecords : ledger.getLedger().descendingMap().values()) {
				for (Transaction transaction : dayRecords) {
					if (transaction.getTag().equals(this.filterBox.getText().trim())) {
						this.transList.addItem(transaction);
					}
				}
			}
		}
	}


}
