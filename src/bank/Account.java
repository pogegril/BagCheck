package bank;

import java.math.BigDecimal;

/**
 * Class to handle an account's details and operations
 * @author pogegril
 */
public class Account {

	private String name;
	private BigDecimal balance;
	private Currency currency;

	/**
	 * Creates a new account
	 * @param name - Account's identification
	 * @param currency - Account's currency
	 */
	public Account(String name, Currency currency) {
		this.name = name;
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
	 * Updates the account's balance with the deposit value
	 * @param deposit - Value to deposit
	 */
	public void deposit(BigDecimal deposit) {
		this.balance = this.balance.add(deposit);	
	}

	/**
	 * Updates the account's balance with the expense value
	 * @param expense - Value to charge
	 */
	public void expense(BigDecimal expense) {
		this.balance = this.balance.subtract(expense);
	}

	/**
	 * Returns if the account is the same as this instance
	 * @param account - Account to compare
	 * @return isEqual?
	 */
	public boolean compare(Account account) {
		return this.name.equals(account.getName());
	}
}
