package bank;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Class to handle all assets' details and operations
 * @author pogegril
 */
public class Assets {

	private NavigableMap<Integer, Account> assets;

	public Assets() {
		this.assets = new TreeMap<Integer, Account>();
	}

	/**
	 * Returns the array list of assets
	 * @returns assets
	 */
	public NavigableMap<Integer, Account> getAssets() {
		return this.assets;
	}

	/**
	 * Attempts to add the received account
	 * Returns if the account was added successfully
	 * @param account - Account to add
	 * @return isAdded?
	 */
	public boolean addAccount(Account account) {
		if (this.assets.get(account.getID()) == null) {
			this.assets.put(account.getID(), account);
			return true;
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
		return this.assets.remove(account.getID()) != null;
	}

	/**
	 * Returns the assets total balance of each currency
	 * @return balance
	 */
	public BigDecimal[] getBalance() {
		BigDecimal[] balance = new BigDecimal[Currency.values().length];
		Arrays.fill(balance, BigDecimal.ZERO);

		for (Account account : this.assets.values()) {
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
	 * Returns smallest unused id
	 * @return id
	 */
	public int getUniqueID() {
		int id = 0;
		while (this.assets.containsKey(id)) {
			id = this.assets.ceilingKey(id) + 1;
		}
		return id;
	}

	/**
	 * Returns the account by its ID
	 * @return account
	 */
	public Account getAccountByID(int id) {
		return this.assets.get(id);
	}
}

