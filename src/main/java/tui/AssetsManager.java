package tui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Borders;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.dialogs.TextInputDialog;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.RadioBoxList;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.TerminalSize;

import bank.Account;
import bank.Assets;
import ledger.Ledger;
import ledger.Transaction;

/**
 * Terminal window to manage user assets
 * @author pogegril
 */
public class AssetsManager extends BasicWindow {

	// Account selection fields
	private Account selected;
	private RadioBoxList<Account> accList;
	private Panel infoPanel;

	/**
	 * Creates the assets manager window
	 * @param tui - User terminal interface
	 * @param ledger - User ledger
	 */
	public AssetsManager(WindowBasedTextGUI tui, Ledger ledger) {
		super("Assets");
		setHints(Arrays.asList(Window.Hint.CENTERED));
		Assets assets = ledger.getAssets();

		// Window panel
		Panel window = new Panel();
		window.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Main panel
		Panel mainPanel = new Panel();
		mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		// Account panel
		Panel accPanel = new Panel();
		//accPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL)); // Unecessary unless the panel gets more components
		accPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		this.accList = new RadioBoxList<>(new TerminalSize(30, 10));
		updateAccounts(assets);

		this.accList.addListener((selected, lastSelection) -> {
			this.selected = this.accList.getCheckedItem();
			updateInfo();
		});

		accPanel.addComponent(this.accList);
		accPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		Component borderedAccPanel = accPanel.withBorder(Borders.singleLine("Accounts"));
		mainPanel.addComponent(borderedAccPanel);

		// Side panel
		Panel sidePanel = new Panel();
		sidePanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		sidePanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		Panel sideContent = new Panel();
		sideContent.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		sideContent.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		// Account info sub-panel
		this.infoPanel = new Panel();
		this.infoPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		updateInfo();
		sideContent.addComponent(this.infoPanel);

		// Menu sub-panel
		Panel menuPanel = new Panel();
		menuPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		menuPanel.addComponent(new Label("╔════ Menu ════╗"));
		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		menuPanel.addComponent(new Button(": Add :", () -> {
			tui.addWindowAndWait(new AddAccount(tui, assets));
			updateAccounts(assets);
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

		menuPanel.addComponent(new Button(": Edit :", () -> {
			if (this.selected != null) {
				tui.addWindowAndWait(new EditAccount(tui, this.selected));
				updateAccounts(assets);
			}
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

		menuPanel.addComponent(new Button(": Remove :", () -> {
			if (this.selected != null) {
				try {
					ledger.removeAccount(this.selected);
					updateAccounts(assets);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				this.infoPanel.removeAllComponents();
				this.infoPanel.addComponent(new Label("Select an account\n    to remove."), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
			}
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		menuPanel.addComponent(new Button(": Back :", () -> {
			this.close();
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		
		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		menuPanel.addComponent(new Label("╚══════════════╝"));
		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		sideContent.addComponent(menuPanel);
		sidePanel.addComponent(sideContent);
		sidePanel.addComponent(new EmptySpace(new TerminalSize(1, 0)));

		mainPanel.addComponent(sidePanel);
		mainPanel.addComponent(new EmptySpace(new TerminalSize(0, 2)));
		window.addComponent(mainPanel);
		setComponent(window);
	}

	/**
	 * Updates the account's information sub-panel based on the selected account
	 */
	public void updateInfo() {
		this.infoPanel.removeAllComponents();

		if (this.selected == null) {
			this.infoPanel.addComponent(new Label("Select an account."), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		} else {
			this.infoPanel.addComponent(new Label(this.selected.getName()), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
			this.infoPanel.addComponent(new Label("ID: " + this.selected.getID()), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
			this.infoPanel.addComponent(new Label("Balance: " + this.selected.getBalance().toString() + " " + this.selected.getCurrency().getSign()), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
		}
	}

	/**
	 * Updates the account list panel
	 * @param assets - User assets
	 */
	public void updateAccounts(Assets assets) {
		this.accList.clearItems();
		for (Account account : assets.getAssets()) {
			this.accList.addItem(account);
		}
	}
}
