package main;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;

import bank.Account;
import bank.Assets;
import bank.Currency;
import ledger.Ledger;
import ledger.Transaction;
import sql.Database;

public class BagTest {

	public static void main(String[] args) {
		// Test ledger
		Database.initialize();
		Ledger ledger = new Ledger();
		Assets assets = ledger.getAssets();

		try {
			Database.loadLedger(ledger);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Account test0 = new Account("Test Account 0", Currency.Euro);
		Account test1 = new Account("Test Account 1", Currency.Euro);
		Account test2 = new Account("Test Account 2", Currency.Euro);
		addAccount(assets, test0);
		addAccount(assets, test1);
		addAccount(assets, test2);

		for (Account account : assets.getAssets()) {
			displayAccount(account);
		} 
		System.out.println("\n");

		// Ledger & Assets handling & Arithmetic tests

		Transaction transaction = new Transaction("Test Transaction", test1.getID(), LocalDate.now(), new BigDecimal("125.12345"));
		addTransaction(ledger, transaction);
		displayAccount(test0);
		displayAccount(test1);
		System.out.println("\n");

		Transaction transaction1 = new Transaction("Test Transaction 1", "Test Description", test1.getID(), LocalDate.now(), new BigDecimal("-20.023"));
		addTransaction(ledger, transaction1);
		displayBalance(ledger);

		displayTransactions(ledger);
	}

	/**
	 * Displays Account details in console
	 */
	public static void displayAccount(Account account) {
		System.out.println("Account Nº" + account.getID() + ": " + account.getName() + "| Balance: " + account.getBalance() + account.getCurrency().getSign());
	}

	/**
	 * Displays all Currencies' balance in console
	 */
	public static void displayBalance(Ledger ledger) {
		BigDecimal[] balance = ledger.getAssets().getBalance();
		for (int i = 0; i < balance.length; i++) {
			BigDecimal currencyBalance = balance[i];
			System.out.println(currencyBalance.toString() + Currency.getSignByID(i));
		}
		System.out.println("\n");
	}

	public static void displayTransactions(Ledger ledger) {
		for (Transaction transaction : ledger.getRecordsByDay(LocalDate.now())) {
			System.out.println("Transaction: " + transaction.getName() + " (" + transaction.getDesc() + ") | Account: " + ledger.getAssets().getAccountByID(transaction.getAccountID()).getName() + " | Amount: " + transaction.getAmount().toString());
		}
	}

	public static void addAccount(Assets assets, Account account)  {
		try {
			assets.addAccount(account);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addTransaction(Ledger ledger, Transaction transaction) {
		try {
			ledger.addTransaction(transaction);	
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
