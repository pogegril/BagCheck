package sql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import bank.Account;
import bank.Currency;
import ledger.Transaction;

public class AccountDAO {

	private Connection connection;

	public AccountDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Adds an account to the database
	 * @param account - Account to save
	 * @return update - Number of database rows updated (Should be 1)
	 */
	public int add(Account account) throws SQLException {
		PreparedStatement statement = this.connection.prepareStatement("INSERT INTO accounts(name, balance, currency) VALUES (?, ?, ?, ?)");
		statement.setString(1, account.getName());
		statement.setInt(2, account.getID());
		statement.setString(3, account.getBalance().toString());
		statement.setInt(4, account.getCurrency().getID());
		return statement.executeUpdate();
	}

	/**
	 * Deletes the account with the received id
	 * @param id - Account id
	 * @return update - Number of database rows update (Should be 0 or 1)
	 */
	public int delete(int id) throws SQLException {
		PreparedStatement statement = this.connection.prepareStatement("DELETE FROM accounts WHERE account_id = ?");
		statement.setInt(1, id);
		return statement.executeUpdate();
	}

	/**
	 * Returns a list with all recorded accounts in the database
	 * @return accounts
	 */
	public ArrayList<Account> getAccounts() throws SQLException {
		PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM accounts");
		ResultSet result = statement.executeQuery();

		ArrayList<Account> accounts = new ArrayList<Account>();
		while (result.next()) {
			String name = result.getString("name");
			int id = result.getInt("id");
			Currency currency = Currency.getById(result.getInt("currency"));
			BigDecimal balance = new BigDecimal(result.getString("balance"));
			Account account = new Account(name, id, currency);
			account.transaction(balance);
			accounts.add(account);
		}
		return accounts;
	}
}

