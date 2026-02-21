package bank;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.NavigableMap;
import java.util.TreeMap;

import sql.Database;

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
	 * Returns a collection view of the assets
	 * @returns assets
	 */
	public Collection<Account> getAssets() {
		return this.assets.values();
	}

	/**
	 * Attempts to add an account with the received details if not present already
	 * Returns if the account was added successfully
	 * @param account - Account to add
	 * @return isAdded?
	 */
	public boolean addAccount(Account account) throws SQLException {
		for (Account acc : this.assets.values()) {
			if (account.compare(acc) == true) {
				return false;
			}
		}
		Database.addAccount(account);
		this.assets.put(account.getID(), account);
		return true;
	}

	/**
	 * Removes the received account if found
	 * Returns if the account was removed successfully
	 * @param account - Account to remove
	 * @return isRemoved?
	 */
	public boolean remAccount(Account account) throws SQLException {
		Database.remAccount(account.getID());
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
		return Currency.getByID(maxIndex);
	}

	/**
	 * Returns the account by its ID
	 * @return account
	 */
	public Account getAccountByID(int id) {
		return this.assets.get(id);
	}
}

