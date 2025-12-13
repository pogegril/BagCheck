package bank;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * Class to handle all assets' details and operations
 * @author pogegril
 */
public class Assets {

	private Currency mainCurrency;
	private Account[] assets;

	public Assets(Currency mainCurrency) {
		this.mainCurrency = mainCurrency;
		this.assets = new Account[9];
	}

	/**
	 * Returns assets' main currency
	 *@return mainCurrency
	 */
	public Currency getMainCurrency() {
		return this.mainCurrency;
	}

	/**
	 * Sets a new main currency
	 * @param mainCurrency - New main currency
	 */
	public void setMainCurrency(Currency currency) {
		this.mainCurrency = currency;
	}

	/**
	 * Returns the list of assets
	 * @returns assets
	 */
	public Account[] getAssets() {
		return this.assets;
	}

	/**
	 * Attempts to add the received account
	 * Returns if the account was added successfully
	 * @param account - Account to add
	 * @return isAdded?
	 */
	public boolean addAccount(Account account) {
		for (int i = 0; i < this.assets.length; i++) {
			if (this.assets[i] == null) {
				this.assets[i] = account;
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes the received account if found
	 * Returns if the account was removed successfully
	 * @param account - Account to remove
	 * @return isRemoved?
	 */
	public boolean remAccount(Account account) {
		boolean rem = false;
		int length = this.assets.length;
		for (int i = 0; i < length; i++) {
			// Compares if the account is the same
			if (this.assets[i].compare(account)) {
				rem = true;	
			}

			if (rem && i < length) {
				this.assets[i] = this.assets[i + 1];
			}
		}	
		if (rem) {
			this.assets[length] = null;
		}
		return rem;
	}

	/**
	 * Returns the assets total balance of each currency
	 * @return balance
	 */
	public BigDecimal[] getBalance() {
		BigDecimal[] balance = new BigDecimal[Currency.values().length];
		Arrays.fill(balance, BigDecimal.ZERO);

		for (Account account : this.assets) {
			int currencyID = account.getCurrency().getID();
			balance[currencyID] = balance[currencyID].add(account.getBalance());
		}
		return balance;
	}

}
