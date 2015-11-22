package br.com.pilovieira.persistenza.db;

import br.com.pilovieira.persistenza.Database;

public final class PostgreSql extends Database {

	public PostgreSql(String url, String username, String password) {
		super(url, username, password);
	}

	@Override
	public String getDialect() {
		return "org.hibernate.dialect.PostgreSQLDialect";
	}

	@Override
	public String getConnectionDriverClass() {
		return "org.postgresql.Driver";
	}
}
