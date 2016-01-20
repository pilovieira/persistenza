package br.com.pilovieira.persistenza.util;

import br.com.pilovieira.persistenza.Database;

public class DatabaseFake extends Database {

	public DatabaseFake(String url, String username, String password) {
		super(url, username, password);
	}

	@Override
	protected String getDialect() {
		return "dialectFake";
	}

	@Override
	protected String getConnectionDriverClass() {
		return "driverFake";
	}

	@Override
	protected String getSslFactory() {
		return "sslFactoryFake";
	}

}
