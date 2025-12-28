package bank;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

/**
 * Class to handle all assets' details and operations
 * @author pogegril
 */
public class Assets {

	private List<Account> assets;

	public Assets() {
		this.assets = new ArrayList<Account>();
	}

	/**
	 * Returns the array list of assets
	 * @returns assets
	 */
	public List<Account> getAssets() {
		return this.assets;
	}

	/**
	 * Attempts to add the received account
	 * Returns if the account was added successfully
	 * @param account - Account to add
	 * @return isAdded?
	 */
	public boolean addAccount(Account account) {
		for (int i = 0; i < this.assets.size(); i++) {
			if (this.assets.get(i).compare(account)) {
				return false;
			}
		}
		return this.assets.add(account);
		
	}

	/**
	 * Removes the received account if found
	 * Returns if the account was removed successfully
	 * @param account - Account to remove
	 * @return isRemoved?
	 */
	public boolean remAccount(Account account) {
		return this.assets.remove(account);
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

	/**
	 * Returns assets' main currency
	 * @return mainCurrency
	 */
	public Currency getMainCurrency() {
		BigDecimal[] balance = getBalance();
		int maxIndex = 0;

		for (int i = 1; i < balance.length; i++) {
			if (balance[maxIndex].compareTo(balance[i]) == 1) {
				maxIndex = i;
			}
		}
		return Currency.getById(maxIndex);
	}

	/**
	 * Returns the smallest unused unique id
	 * @return id
	 */
	public int getUniqueID() {
		int maxID = -1;
		for (Account account : this.assets) {
			maxID = Math.max(maxID, account.getID());
		}
		return maxID + 1;
	}

	/**
	 * Returns the account by its ID
	 * @return account
	 */
	public Account getAccountByID(int id) {
		for (Account account : this.assets) {
			if (account.getID() == id) {
				return account;
			}
		}
		return null;
	}
}

