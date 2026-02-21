package bank;

/**
 * Enum listing available currencies
 * @author pogegril
 */
public enum Currency {
	
	Euro('€'),
	Dollar('$');

	private final char SIGN;

	/**
	 * Builds the Currency enum with its sign
	 * @param sign - Currency Sign
	 */
	Currency(char sign) {
		this.SIGN = sign;
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
		for (int i = 0; i < values().length; i++) {
			if (this == values()[i]) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the Currency by its ID
	 * @return currency
	 */
	public static Currency getByID(int id) {
		return values()[id];
	}

	/**
	 * Returns the Currency's sign by its ID
	 * @return sign
	 */
	public static char getSignByID(int id) {
		return values()[id].getSign();
	}
}
