package ledger;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Class that stores a transaction's details
 * @author pogegril
 */
public class Transaction {

	private String name, desc, tag;
	private int account_id, ID;
	private LocalDate date;
	private BigDecimal amount;

	/**
	 * Builds a Transaction Instance with all of the pertinent details
	 * @param name - Transaction's name
	 * @param tag - Transaction's identifier tag
	 * @param account_id - Transaction's account ID
	 * @param date - Transaction's date
	 * @param amount - Transaction's value
	 */
	public Transaction(String name, String tag, int account_id, LocalDate date, BigDecimal amount) {
		setName(name);
		setTag(tag);
		setAccountID(account_id);
		this.date = date; // Date parser handles date assigning and error catching
		setAmount(amount);
	}

	/**
	 * Builds a Transaction Instance with all of the pertinent details
	 * @param name - Transaction's name
	 * @param desc - Transaction's optional description
	 * @param tag - Transaction's identifier tag
	 * @param account_id - Transaction's account ID
	 * @param date - Transaction's date
	 * @param amount - Transaction's value
	 */
	public Transaction(String name, String desc, String tag, int account_id, LocalDate date, BigDecimal amount) {
		setName(name);
		setDesc(desc);
		setTag(tag);
		setAccountID(account_id);
		this.date = date; // Date parser handles date assigning and error catching
		setAmount(amount);
	}

	/**
	 * Returns transaction's name
	 * @return name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Sets the transaction's name
	 * @param name - Transaction's new name
	 */
	private void setName(String name) {
		if (name == null || name.isEmpty()) { throw new IllegalArgumentException("Transaction name must not be empty."); }
		this.name = name.trim();
	}

	/**
	 * Returns transaction's optional description
	 * Returns null if a description wasn't given
	 * @return description
	 */
	public String getDesc() {
		return this.desc;
	}

	/**
	 * Sets the transaction's description
	 * @param desc - Transaction's new description
	 */
	private void setDesc(String desc) {
		if (desc != null) {
			this.desc = desc.trim();
		}
	}

	/**
	 * Returns transaction's tag
	 * @return tag
	 */
	public String getTag() {
		return this.tag;
	}

	/**
	 * Sets the transaction's tag
	 * @param tag - Transaction's new tag
	 */
	private void setTag(String tag) {
		if (tag == null || tag.isEmpty()) { throw new IllegalArgumentException("Transaction tag must not be empty."); }
		this.tag = tag.trim();
	}

	/**
	 * Returns transaction's account
	 * @return account
	 */
	public int getAccountID() {
		return this.account_id;
	}

	/**
	 * Sets the transaction's account ID
	 * @param account_id - New account ID
	 */
	private void setAccountID(int account_id) {
		this.account_id = account_id;
	}

	/**
	 * Returns transaction's unique ID
	 * @return ID
	 */
	public int getID() {
		return this.ID;
	}

	/**
	 * Sets the database's assigned transaction id
	 * @param id - Database ID
	 */
	public void setID(int id) {
		this.ID = id;
	}

	/**
	 * Returns transaction's date
	 * @return date
	 */
	public LocalDate getDate() {
		return this.date;
	}

	/**
	 * Returns transaction's value
	 * @return value
	 */
	public BigDecimal getAmount() {
		return this.amount;
	}

	/**
	 * Sets the transaction's amount
	 * @param amount - New transaction value
	 */
	private void setAmount(BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) { throw new IllegalArgumentException("Transaction value must not be null."); }
		this.amount = amount;
	}

	/**
	 * Returns a String with the transaction's info to be displayed
	 * @return string
	 */
	@Override
	public String toString() {
		return "[" + getTag() + "] " + getName();
	}
}
