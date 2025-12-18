package ledger;

import bank.Account;
import bank.Assets;
import bank.Currency;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.NavigableMap;
import java.util.TreeMap;

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
	 * Adds a new transaction to the ledger if it's not a duplicate
	 * @param transaction - New transaction to track
	 */
	public boolean addTransaction(Transaction transaction) {
		LocalDate date = transaction.getDate();
		this.ledger.computeIfAbsent(date, newRecords -> new ArrayList<Transaction>());

		ArrayList<Transaction> dateRecords = this.ledger.get(date);
		if (dateRecords.contains(transaction)) {
			return false;
		}

		Account account = transaction.getAccount();
		account.transaction(transaction.getAmmount());

		dateRecords.add(transaction);
		return true;

		
	}

	/**
	 * Removes the transaction from the ledger
	 * @param transaction - Transaction to remove
	 */
	public boolean removeTransaction(Transaction transaction) {
		ArrayList<Transaction> dateRecords = this.ledger.get(transaction.getDate());
		if (dateRecords != null && dateRecords.remove(transaction)) {
			Account account = transaction.getAccount();
			BigDecimal revertTransaction = transaction.getAmmount().negate();
			account.transaction(revertTransaction);

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
}
