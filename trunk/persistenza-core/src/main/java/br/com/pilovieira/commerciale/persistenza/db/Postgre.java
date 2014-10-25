package br.com.pilovieira.commerciale.persistenza.db;



public class Postgre extends Database {

	@Override
	public String getDialect() {
		return "org.hibernate.dialect.PostgreSQLDialect";
	}

	@Override
	public String getConnectionDriverClass() {
		return "org.postgresql.Driver";
	}
}
