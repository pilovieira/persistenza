package br.com.pilovieira.persistenza.db;

import br.com.pilovieira.persistenza.Database;

public final class HyperSql extends Database {

	public HyperSql(String url, String username, String password) {
		super(url, username, password);
	}

	@Override
	public String getDialect() {
		return "org.hibernate.dialect.HSQLDialect";
	}

	@Override
	public String getConnectionDriverClass() {
		return "org.hsqldb.jdbcDriver";
	}
}
