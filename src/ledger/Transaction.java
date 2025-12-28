package ledger;

import bank.Account;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Class that stores a transaction's details
 * @author pogegril
 */
public class Transaction {

	private String name, desc;
	private int account_id;
	private LocalDate date;
	private BigDecimal amount;

	/**
	 * Builds a Transaction Instance with all of the pertinent details
	 * @param name - Transaction's name
	 * @param account_id - Transaction's account ID
	 * @param date - Transaction's date
	 * @param amount - Transaction's value
	 */
	public Transaction(String name, int account_id, LocalDate date, BigDecimal amount) {
		this.name = name;
		this.account_id = account_id;
		this.date = date;
		this.amount = amount;
	}

	/**
	 * Builds a Transaction Instance with all of the pertinent details
	 * @param name - Transaction's name
	 * @param desc - Transaction's optional description
	 * @param account_id - Transaction's account ID
	 * @param date - Transaction's date
	 * @param amount - Transaction's value
	 */
	public Transaction(String name, String desc, int account_id, LocalDate date, BigDecimal amount) {
		this.name = name;
		this.desc = desc;
		this.account_id = account_id;
		this.date = date;
		this.amount = amount;
	}

	/**
	 * Returns transaction's name
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns transaction's optional description
	 * Returns null if a description wasn't given
	 * @return name
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * Returns transaction's account
	 * @return account
	 */
	public int getAccountID() {
		return this.account_id;
	}

	/**
	 * Returns transaction's date
	 * @return date
	 */
	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * Returns transaction's value
	 * @return value
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}
}
