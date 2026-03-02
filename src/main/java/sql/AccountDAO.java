package sql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import bank.Account;
import bank.Currency;

/**
 * Handles account operations between the database and the program
 * @author pogegril
 */
public class AccountDAO {

	private Connection connection;

	/**
	 * Accounts Data Access Object
	 * @param connection - JDBC connection
	 */
	public AccountDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Adds an account to the database
	 * @param account - Account to save
	 */
	public void add(Account account) throws SQLException {
		String sqlStatement = "INSERT INTO accounts(name, balance, currency) VALUES (?, ?, ?)";
		try (PreparedStatement statement = this.connection.prepareStatement(sqlStatement, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, account.getName());
			statement.setBigDecimal(2, account.getBalance());
			statement.setInt(3, account.getCurrency().getID());
			statement.executeUpdate();

			try (ResultSet id = statement.getGeneratedKeys()) {
				if (id.next()) {
					account.setID(id.getInt(1));	
				} else {
					throw new  SQLException("Failed to retrieve account's auto-generated ID.");
				}
			}
		}
	}

	/**
	 * Deletes the account with the received id
	 * @param id - Account id
	 * @return update - Number of database rows update (Should be 0 or 1)
	 */
	public int delete(int id) throws SQLException {
		try (PreparedStatement statement = this.connection.prepareStatement("DELETE FROM accounts WHERE id = ?")) {
			statement.setInt(1, id);
			return statement.executeUpdate();
		}
	}

	/**
	 * Updates the database's account entry's balance
	 * @param id - Account ID
	 * @param balance - Updated balance
	 */
	public void updateBalance(int id, BigDecimal balance) throws SQLException {
		try (PreparedStatement statement = this.connection.prepareStatement("UPDATE accounts SET balance = ? WHERE id = ?")) {
			statement.setBigDecimal(1, balance);
			statement.setInt(2, id);
			statement.executeUpdate();
		}
	}

	/**
	 * Returns a list with all recorded accounts in the database
	 * @return accounts
	 */
	public ArrayList<Account> getAccounts() throws SQLException {
		try (PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM accounts")) {
			try (ResultSet result = statement.executeQuery()) {

				ArrayList<Account> accounts = new ArrayList<Account>();
				while (result.next()) {
					String name = result.getString("name");
					int id = result.getInt("id");
					Currency currency = Currency.getByID(result.getInt("currency"));
					BigDecimal balance = result.getBigDecimal("balance");
					Account account = new Account(name, currency);
					account.setID(id);
					account.transaction(balance);
					accounts.add(account);
				}
				return accounts;
			}	
		}
	}
}

