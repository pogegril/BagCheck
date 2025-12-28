CREATE TABLE accounts(
	name TEXT NOT NULL,
	id INTEGER PRIMARY KEY,
	balance TEXT NOT NULL,
	currency INTEGER NOT NULL
);

CREATE TABLE transactions(
	transaction_id INTEGER PRIMARY KEY,
	name TEXT NOT NULL,
	description TEXT,
	account_id INTEGER NOT NULL,
	date TEXT NOT NULL,
	amount TEXT NOT NULL,
	
	FOREIGN KEY(account_id) REFERENCES accounts(account_id)
);
