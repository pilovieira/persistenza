package br.com.pilovieira.persistenza.db;

public final class HyperSql extends DatabaseManager {

	public HyperSql(ConnectionData connectionData) {
		super(connectionData);
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
