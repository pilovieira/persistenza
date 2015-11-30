package br.com.pilovieira.persistenza.db;

import br.com.pilovieira.persistenza.Database;

public final class PostgreSql extends Database {

	public PostgreSql(String url, String username, String password) {
		super(url, username, password);
	}

	@Override
	protected String getDialect() {
		return "org.hibernate.dialect.PostgreSQLDialect";
	}

	@Override
	protected String getConnectionDriverClass() {
		return "org.postgresql.Driver";
	}

	@Override
	protected String getSslFactory() {
		return "org.postgresql.ssl.NonValidatingFactory";
	}
}
