package bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;

import sql.Database;

/**
 * Class to handle an account's details and operations
 * @author pogegril
 */
public class Account {

	private String name;
	private int ID;
	private BigDecimal balance;
	private Currency currency;

	/**
	 * Creates a new account
	 * @param name - Account's identification
	 * @param currency - Account's currency
	 * @param balance - Current balance
	 */
	public Account(String name, Currency currency, BigDecimal balance) {
		if (name == null || name.isEmpty()) { throw new IllegalArgumentException("The Account's name must not be empty."); }
		this.name = name.trim();
		this.balance = balance;
		if (currency == null) { throw new IllegalArgumentException("Currency must not be null"); }
		this.currency = currency;
	}

	/**
	 * Returns the account's name
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Updates the account's name
	 * @param name - Account's new name
	 */
	public void setName(String name) throws SQLException {
		if (name == null || name.isEmpty()) { throw new IllegalArgumentException("The Account's name must not be empty."); }
		this.name = name.trim();
		Database.updateName(this.ID, name);
	}

	/**
	 * Returns the account's unique id
	 * @return ID
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * Assigns the database's assigned unique ID
	 * @param id
	 */
	public void setID(int id) {
		this.ID = id;
	}

	/**
	 * Returns the account's currency
	 * @return currency
	 */
	public Currency getCurrency() {
		return this.currency;
	}

	/**
	 * Updates the account's currency
	 * @param currency - Account's new currency
	 */
	public void setCurrency(Currency currency) throws SQLException {
		if (currency == null) { throw new IllegalArgumentException("Currency must not be null"); }
		this.currency = currency;
		Database.updateCurrency(this.ID, currency.getID());
	}

	/**
	 * Returns the account's current balance
	 * @return balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * Updates the current balance
	 * Unlike transactions it doesnt log the update nor
	 * ledger overview information
	 * @param balance - New balance
	 */
	public void setBalance(BigDecimal balance) throws SQLException {
		this.balance = balance;
		Database.updateBalance(this.ID, this.balance);
	}

	/**
	 * Updates the account's balance with the transaction's value
	 * @param amount - Transaction's value
	 */
	public void transaction(BigDecimal amount) throws SQLException {
		this.balance = this.balance.add(amount);
		Database.updateBalance(this.ID, this.balance);
	}

	/**
	 * Returns if the account is the same as this instance
	 * @param account - Account to compare
	 * @return isEqual?
	 */
	public boolean compare(Account account) {
		return this.ID == account.getID() || this.name.equals(account.getName());
	}

	/**
	 * Returns a String with the account's info to be displayed
	 * @return string
	 */
	@Override
	public String toString() {
		BigDecimal roundBalance = getBalance().setScale(2, RoundingMode.HALF_UP);
		return "[" + roundBalance.toString() + " " + getCurrency().getSign() + "] " + getName();
	}
}
