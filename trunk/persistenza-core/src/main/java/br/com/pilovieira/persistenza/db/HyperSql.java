package br.com.pilovieira.persistenza.db;

import br.com.pilovieira.persistenza.Database;

public class HyperSql extends Database {

	public HyperSql(String url, String username, String password) {
		super(url, username, password);
	}

	@Override
	protected String getDialect() {
		return "org.hibernate.dialect.HSQLDialect";
	}

	@Override
	protected String getConnectionDriverClass() {
		return "org.hsqldb.jdbcDriver";
	}

	@Override
	protected String getSslFactory() {
		return null;
	}
}
