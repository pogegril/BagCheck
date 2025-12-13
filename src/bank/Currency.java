package bank;

/**
 * Enum listing available currencies
 * @author pogegril
 */
public enum Currency {
	
	Euro('€', 0),
	Dollar('$', 1);

	private final char SIGN;
	private final int ID;
	/**
	 * Builds the Currency enum with its sign
	 * @param sign - Currency Sign
	 */
	Currency(char sign, int id) {
		this.SIGN = sign;
		this.ID = id;
	}

	/**
	 * Returns the currency's sign
	 * @return SIGN
	 */
	public char getSign() {
		return this.SIGN;
	}

	/**
	 * Returns the currency's internal id
	 * @return ID
	 */
	public int getID() {
		return this.ID;
	}
}
