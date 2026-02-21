package bank;

import java.math.BigDecimal;

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
	 */
	public Account(String name, Currency currency) {
		if (name == null || name.length() == 0) {throw new IllegalArgumentException("The Account's name must not be empty.");}
		this.name = name.trim();
		this.balance = new BigDecimal(0);
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
	 * Returns the account's current balance
	 * @return balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * Updates the account's balance with the transaction's value
	 * @param deposit - Transaction's value
	 */
	public void transaction(BigDecimal ammount) {
		this.balance = this.balance.add(ammount);
	}

	/**
	 * Returns if the account is the same as this instance
	 * @param account - Account to compare
	 * @return isEqual?
	 */
	public boolean compare(Account account) {
		return this.ID == account.getID() || this.name.equals(account.getName());
	}
}
