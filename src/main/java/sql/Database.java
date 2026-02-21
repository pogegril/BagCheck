package sql;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import bank.Account;
import bank.Assets;
import ledger.Ledger;
import ledger.Transaction;

/**
 * Database class to initialize, load and update the program's database
 * @author pogegril
 */
public class Database {

	private static final String DB_URL = "jdbc:sqlite:database.db";
	private static final String SCHEMA_PATH = "db/database.sql";

	/**
	 * Returns JDBC's connection
	 * @return connection
	 */
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(DB_URL);
	}

	/**
	 * Static method to initialize the program's database
	 */
	public static void initialize() {
		File dbFile = new File("database.db");
		boolean exists = dbFile.exists();
		
		try (Connection connection = getConnection()) {
			if (!exists) {
				createDatabase(connection);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a database file if non existant
	 * @param connection - JDBC's connection
	 */
	private static void createDatabase(Connection connection) throws IOException, SQLException {
		// Loading Schema
		InputStream inStream = Database.class.getClassLoader().getResourceAsStream(SCHEMA_PATH);
		if (inStream == null) {
			throw new RuntimeException("Database Schema not found.");
		}
		
		// Reading Schema
		StringBuilder schema = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inStream))) {
			String line;
			while ((line = reader.readLine()) != null) {
				schema.append(line).append("\n");
			}
		}

		// Building Database
		try (Statement statement = connection.createStatement()) {
			String[] statements = schema.toString().split(";");
			for (String line : statements) {
				if (!line.trim().isEmpty()) {
					statement.execute(line);
				}
			}
		}
	}

	/**
	 * Loads the received ledger with the database's contents
	 * @param ledger - Empty ledger
	 */
	public static void loadLedger(Ledger ledger) throws SQLException {
		Assets assets = ledger.getAssets();
		try (Connection connection = getConnection()) {
			AccountDAO accDao = new AccountDAO(connection);
			TransactionDAO transDao = new TransactionDAO(connection);

			for (Account account : accDao.getAccounts()) {
				if (!assets.addAccount(account)) {
					throw new IllegalStateException("Duplicate account entry error.");
				}
			}

			for (Transaction transaction : transDao.getTransactions()) {
				if (!ledger.loadTransaction(transaction)) {
					throw new IllegalStateException("Duplicate transaction entry error.");
				}
			}
		}
	}

	/**
	 * Writes an account into the database
	 * @param account - Account to save
	 */
	public static void addAccount(Account account) throws SQLException {
		try (Connection connection = getConnection()) {
			AccountDAO accDao = new AccountDAO(connection);
			accDao.add(account);
		}
	}

	/**
	 * Deletes an account from the database
	 * @param id - Account's id
	 */
	public static void remAccount(int id) throws SQLException {
		try (Connection connection = getConnection()) {
			AccountDAO accDao = new AccountDAO(connection);
			if (accDao.delete(id) == 0) {
				throw new IllegalStateException("Attempted to delete non-existant account.");
			}
		}
	}

	/**
	 * Connects the AccountDAO to update an account's database entry's balance
	 * @param id - Account ID
	 * @param balance - Updated balance
	 */
	public static void updateBalance(int id, BigDecimal balance) throws SQLException {
		try (Connection connection = getConnection()) {
			AccountDAO accDao = new AccountDAO(connection);
			accDao.updateBalance(id, balance);
		}
	}

	/**
	 * Writes a transaction's info to the database
	 * @param transaction - Transaction to save
	 */
	public static void addTransaction(Transaction transaction) throws SQLException {
		try (Connection connection = getConnection()) {
			TransactionDAO transDao = new TransactionDAO(connection);
			transDao.add(transaction);
		}
	} 

	/**
	 * Deletes a transaction from the database
	 * @param id - Transaction's id
	 */
	public static void remTransaction(int id) throws SQLException {
		try (Connection connection = getConnection()) {
			TransactionDAO transDao = new TransactionDAO(connection);
			if (transDao.delete(id) == 0) {
				throw new IllegalStateException("Attempted to delete non-existant transaction.");
			}
		}
	}
}
