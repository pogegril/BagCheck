package sql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import bank.Account;
import ledger.Transaction;

/**
 * Class to handle storage and loading of transactions between the program and the database
 * @author pogegril
 */
public class TransactionDAO {

	private Connection connection;

	public TransactionDAO(Connection connection) {
		this.connection = connection;
	}

	/**
	 * Adds a transaction to the database
	 * @param transaction - Transaction to save
	 * @return update - Number of database rows updated (Should be 1)
	 */
	public int add(Transaction transaction) throws SQLException {
		PreparedStatement statement = this.connection.prepareStatement("INSERT INTO transactions(name, description, account_id, date, amount) VALUES (?, ?, ?, ?, ?)");
		statement.setString(1, transaction.getName());
		statement.setString(2, transaction.getDesc());
		statement.setInt(3, AccountDAO.getIDByName(transaction.getAccount().getName(), this.connection));
		statement.setString(4, transaction.getDate().toString());
		statement.setString(5, transaction.getAmount().toString());
		return statement.executeUpdate();
	}

	/**
	 * Deletes the transaction with the received id
	 * @param id - Transaction id
	 * @return update - Number of database rows update (Should be 0 or 1)
	 */
	public int delete(int id) throws SQLException {
		PreparedStatement statement = this.connection.prepareStatement("DELETE FROM transactions WHERE transaction_id = ?");
		statement.setInt(1, id);
		return statement.executeUpdate();
	}

	/**
	 * Returns a transaction by its id
	 * Returns null if not found
	 * @param id - Transaction id
	 * @return transaction
	 */
	public Transaction getTransaction(int id) throws SQLException {
		PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM transactions WHERE transaction_id = ?");
		statement.setInt(1, id);
		ResultSet result = statement.executeQuery();

		if (result.next()) {
			String name = result.getString("name");
			String desc = result.getString("description");
			Account account = AccountDAO.getAccountByID(result.getInt("account_id"), this.connection);
			LocalDate date = LocalDate.parse(result.getString("date"));
			BigDecimal amount = new BigDecimal(result.getString("amount"));
			return new Transaction(name, desc, account, date, amount);
		}
		return null;
	}

	/**
	 * Returns a list with all recorded transactions in the database
	 * @return transactions
	 */
	public ArrayList<Transaction> getTransactions() throws SQLException {
		PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM transactions");
		ResultSet result = statement.executeQuery();

		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		while (result.next()) {
			String name = result.getString("name");
			String desc = result.getString("description");
			Account account = AccountDAO.getAccountByID(result.getInt("account_id"), this.connection);
			LocalDate date = LocalDate.parse(result.getString("date"));
			BigDecimal amount = new BigDecimal(result.getString("amount"));
			transactions.add(new Transaction(name, desc, account, date, amount));
		}
		return transactions;
	}

	/**
	 * Returns a list with all recorded transactions in the database since the set date
	 * @param date - Return transactions since this date
	 * @return transactions
	 */
	public ArrayList<Transaction> getTransactions(LocalDate startDate) throws SQLException {
		PreparedStatement statement = this.connection.prepareStatement("SELECT * FROM transactions WHERE date >= ?");
		statement.setString(1, startDate.toString());
		ResultSet result = statement.executeQuery();

		ArrayList<Transaction> transactions = new ArrayList<Transaction>();
		while (result.next()) {
			String name = result.getString("name");
			String desc = result.getString("description");
			Account account = AccountDAO.getAccountByID(result.getInt("account_id"), this.connection);
			LocalDate date = LocalDate.parse(result.getString("date"));
			BigDecimal amount = new BigDecimal(result.getString("amount"));
			transactions.add(new Transaction(name, desc, account, date, amount));
		}
		return transactions;
	}
}
