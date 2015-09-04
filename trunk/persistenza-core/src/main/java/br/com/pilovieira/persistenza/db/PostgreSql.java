package br.com.pilovieira.persistenza.db;

public final class PostgreSql extends DatabaseManager {

	public PostgreSql(ConnectionData connectionData) {
		super(connectionData);
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
