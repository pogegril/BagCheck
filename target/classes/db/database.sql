CREATE TABLE accounts(
	name TEXT NOT NULL,
	id INTEGER PRIMARY KEY,
	balance NUMERIC NOT NULL,
	currency INTEGER NOT NULL
);

CREATE TABLE transactions(
	name TEXT NOT NULL,
	description TEXT,
	account_id INTEGER NOT NULL,
	id INTEGER PRIMARY KEY,
	date TEXT NOT NULL,
	amount NUMERIC NOT NULL,
	
	FOREIGN KEY(account_id) REFERENCES accounts(id)
);
