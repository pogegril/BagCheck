package tui;

import java.time.LocalDate;
import java.util.Arrays;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.Direction;
import com.googlecode.lanterna.gui2.EmptySpace;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LinearLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.TerminalSize;

import bank.Assets;
import bank.Currency;
import ledger.Ledger;

/**
 * Main menu TUI window
 * @author pogegril
 */
public class MainMenu extends BasicWindow {

	// Overview panel
	private Panel viewPanel;

	/**
	 * Handles the main menu window with the user's data
	 * @param tui - Window TUI object
	 * @param ledger - User ledger
	 */
	public MainMenu(WindowBasedTextGUI tui, Ledger ledger) {
		// Window Details
		super("BagCheck");
		setHints(Arrays.asList(Window.Hint.CENTERED));
		Assets assets = ledger.getAssets();

		// Window panel
		Panel window = new Panel();
		window.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));

		// Main panel
		Panel mainPanel = new Panel();
		mainPanel.setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
		mainPanel.addComponent(new EmptySpace(new TerminalSize(2, 0)));

		// Menu sub-panel
		Panel menuPanel = new Panel();
		menuPanel.addComponent(new Label("╔════ Menu ════╗"));
		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		menuPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));

		// Assets button
		menuPanel.addComponent(new Button(": Assets :", () -> {
			tui.addWindowAndWait(new AssetsManager(tui, ledger));
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

		// Ledger button
		menuPanel.addComponent(new Button(": Ledger :", () -> {
			tui.addWindowAndWait(new LedgerManager(tui, ledger));
			updateOverview(ledger);
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));

		menuPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		menuPanel.addComponent(new Label("╚══════════════╝"));
		mainPanel.addComponent(menuPanel);
		mainPanel.addComponent(new EmptySpace(new TerminalSize(4, 0)));

		// Overview panel
		this.viewPanel = new Panel();
		updateOverview(ledger);
		mainPanel.addComponent(this.viewPanel);
		mainPanel.addComponent(new EmptySpace(new TerminalSize(2, 0)));

		window.addComponent(mainPanel);
		window.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		setComponent(window);
	}

	/**
	 * Updates the panel with the user's data overview
	 * @param ledger - User ledger
	 */
	public void updateOverview(Ledger ledger) {
		Assets assets = ledger.getAssets();
		this.viewPanel.removeAllComponents();
		this.viewPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
		this.viewPanel.addComponent(new Label(getBalance(assets)));
		this.viewPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		this.viewPanel.addComponent(new Label(getMonthlyNet(ledger, assets)));
		this.viewPanel.addComponent(new Label(getYearlyNet(ledger, assets)));
		this.viewPanel.addComponent(new EmptySpace(new TerminalSize(0, 1)));
		this.viewPanel.addComponent(new Button(":  Exit  :", () -> {
			this.close();
		}), LinearLayout.createLayoutData(LinearLayout.Alignment.Center));
	}

	/**
	 * Returns the formatted string for balance labels
	 * @param assets - User assets
	 * @return balanceLabel
	 */
	private String getBalance(Assets assets) {
		Currency currency = assets.getMainCurrency();
		return "Balance: " + assets.getBalance()[currency.getID()].toString() + " " + currency.getSign();
	}

	/**
	 * Returns the formatted string for monthly netflow labels
	 * @param ledger - User ledger
	 * @param assets - User assets
	 * @return montlyLabel
	 */
	private String getMonthlyNet(Ledger ledger, Assets assets) {
		LocalDate firstOfMonth = LocalDate.now().withDayOfMonth(1);
		Currency currency = assets.getMainCurrency();
		return "This month: " + ledger.getAssetsFlow(firstOfMonth)[currency.getID()].toString() + " " + currency.getSign();
	}

	/**
	 * Returns the formatted string for yearly netflow labels
	 * @param ledger - User ledger
	 * @param assets - User assets
	 * @return yearlyLabel
	 */
	private String getYearlyNet(Ledger ledger, Assets assets) {
		LocalDate firstOfYear = LocalDate.now().withDayOfYear(1);
		Currency currency = assets.getMainCurrency();
		return "This year: " + ledger.getAssetsFlow(firstOfYear)[currency.getID()].toString() + " " + currency.getSign();
	}
}
