package main;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import bank.Assets;
import ledger.Ledger;
import sql.Database;
import tui.TUI;

/**
 * BagCheck program launcher
 * @author pogegril
 */
public class BagCheck {

	/**
	 * Main program process
	 * @param args - Java arguments
	 */
	public static void main(String[] args) {
		// Initialization & Loading database
		Database.initialize();
		Ledger ledger = new Ledger();
		loadLedger(ledger);

		try {
			new TUI().start(ledger);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Attempts to load the database information into the ledger
	 * @param ledger - User's ledger
	 */
	public static void loadLedger(Ledger ledger) {
		try {
			Database.loadLedger(ledger);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Attempts to load the database information into the ledger
	 * Only loads transactions since the received date
	 * @param ledger - User's ledger
	 */
	public static void loadLedger(Ledger ledger, LocalDate date) {
		try {
			Database.loadLedger(ledger, date);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
