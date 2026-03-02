package ledger;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

import bank.Account;
import bank.Assets;
import bank.Currency;
import sql.Database;

/**
 * Class to mantain an orderly Ledger
 * @author pogegril
 */
public class Ledger {

	private Assets assets;
	private NavigableMap<LocalDate, ArrayList<Transaction>> ledger;

	/**
	 * Creates a new ledger to mantain track of assets and transactions
	 */
	public Ledger() {
		// Assets object to process banking transactions
		this.assets = new Assets(); 
		// TreeMap by dates containing transaction lists
		this.ledger = new TreeMap<LocalDate, ArrayList<Transaction>>();
	}

	/**
	 * Returns the user's tracked assets
	 * @return assets
	 */
	public Assets getAssets() {
		return this.assets;
	}

	/**
	 * Returns the user's ledger
	 * @return ledger
	 */
	public NavigableMap<LocalDate, ArrayList<Transaction>> getLedger() {
		return this.ledger;
	}

	/**
	 * Removes an account from the ledger's assets
	 * Removes all transactions with this ID
	 * @param account - Account to remove
	 */
	public void removeAccount(Account account) throws SQLException {
		int id = account.getID();

		// Filter transactions to remove
		List<Transaction> toRemove = new ArrayList<>();
		for (ArrayList<Transaction> dayRecords : this.ledger.values()) {
			for (Transaction transaction : dayRecords) {
				if (transaction.getAccountID() == account.getID()) {
					toRemove.add(transaction);
				}
			}
		}

		// Remove transactons from account to be removed
		for (Transaction transaction : toRemove) {
			removeTransaction(transaction);
		}

		this.assets.remAccount(account);
	}


	/**
	 * Adds a new transaction to the ledger if it's not a duplicate
	 * @param transaction - New transaction to track
	 * @return isAdded?
	 */
	public boolean addTransaction(Transaction transaction) throws SQLException {
		LocalDate date = transaction.getDate();
		this.ledger.computeIfAbsent(date, newRecords -> new ArrayList<Transaction>());

		ArrayList<Transaction> dateRecords = this.ledger.get(date);
		if (dateRecords.contains(transaction)) {
			return false;
		}

		Database.addTransaction(transaction);
		Account account = this.assets.getAccountByID(transaction.getAccountID());
		account.transaction(transaction.getAmount());
		Database.updateBalance(account.getID(), account.getBalance());

		dateRecords.add(transaction);
		return true;
	}

	/**
	 * Loads an account into the ledger
	 * To be used specifically to load an existing account since it doesnt update the assets balance
	 * @param transaction - Transaction to load
	 * @return isAdded?
	 */
	public boolean loadTransaction(Transaction transaction) {
		LocalDate date = transaction.getDate();
		this.ledger.computeIfAbsent(date, newRecords -> new ArrayList<Transaction>());

		ArrayList<Transaction> dateRecords = this.ledger.get(date);
		if (dateRecords.contains(transaction)) {
			return false;
		}
		dateRecords.add(transaction);
		return true;
	}

	/**
	 * Removes the transaction from the ledger
	 * @param transaction - Transaction to remove
	 * @return isRemoved?
	 */
	public boolean removeTransaction(Transaction transaction) throws SQLException {
		Database.remTransaction(transaction.getID());
		ArrayList<Transaction> dateRecords = this.ledger.get(transaction.getDate());
		if (dateRecords != null && dateRecords.remove(transaction)) {
			// Cleans dateRecords if this was the last transaction present
			if (dateRecords.isEmpty()) {
				this.ledger.remove(transaction.getDate());
			}
			Account account = this.assets.getAccountByID(transaction.getAccountID());
			BigDecimal revertTransaction = transaction.getAmount().negate();
			account.transaction(revertTransaction);
			Database.updateBalance(account.getID(), account.getBalance());
			return true;
		}
		return false;
	}

	/**
	 * Returns all transactions from the received date
	 * @param date - Date of requested transactions
	 */
	public ArrayList<Transaction> getRecordsByDay(LocalDate date) {
		return this.ledger.get(date);
	}

	/**
	 * Returns all transactions from the requested number of previous months
	 * @param months - Number of months' records to return
	 */
	public NavigableMap<LocalDate, ArrayList<Transaction>> getRecordsByMonths(long months) {
		LocalDate currentDate = LocalDate.now();
		LocalDate startDate = currentDate.minusMonths(months);
		return this.ledger.subMap(startDate, true, currentDate, true);
	}

	/**
	 * Returns the assets flow summary by currency since the received date
	 * @param date - Date to start tracking assets flow
	 */
	public BigDecimal[] getAssetsFlow(LocalDate date) {
		BigDecimal[] assetsFlow = new BigDecimal[Currency.values().length];
		Arrays.fill(assetsFlow, BigDecimal.ZERO);

		LocalDate currentDate = LocalDate.now();
		NavigableMap<LocalDate, ArrayList<Transaction>> transactions = this.ledger.subMap(date, true, currentDate, true);

		for (ArrayList<Transaction> dayRecords : transactions.values()) {
			for (Transaction transaction : dayRecords) {
				Account account = this.assets.getAccountByID(transaction.getAccountID());
				assetsFlow[account.getCurrency().getID()] = assetsFlow[account.getCurrency().getID()].add(transaction.getAmount());
			}
		}
		return assetsFlow;
	}
}
