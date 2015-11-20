package br.com.pilovieira.persistenza.db;

import static java.lang.System.setProperty;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class DatabaseManager {
	
	static final String PROPERTY_DIALECT = "hibernate.dialect";
	static final String PROPERTY_DRIVER_CLASS = "hibernate.connection.driver_class";
	static final String PROPERTY_CONNECTION_URL = "hibernate.connection.url";
	static final String PROPERTY_CONNECTION_USERNAME = "hibernate.connection.username";
	static final String PROPERTY_CONNECTION_PASSWORD = "hibernate.connection.password";
	private static final String PROPERTY_SHOW_SQL = "hibernate.show_sql";

	private ConnectionData connectionData;
	private boolean showSql;
	
	public DatabaseManager(ConnectionData connectionData) {
		this.connectionData = connectionData;
	}
	
	public void loadProperties() {
		setProperty(PROPERTY_SHOW_SQL, String.valueOf(showSql));
		setProperty(PROPERTY_DIALECT, getDialect());
		setProperty(PROPERTY_DRIVER_CLASS, getConnectionDriverClass());
		setProperty(PROPERTY_CONNECTION_URL, connectionData.getUrl());
		setProperty(PROPERTY_CONNECTION_USERNAME, connectionData.getUsername());
		setProperty(PROPERTY_CONNECTION_PASSWORD, connectionData.getPassword());
	}
	
	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}
	
	public Connection getConnection() {
		try {
			Class.forName(getConnectionDriverClass());
			return DriverManager.getConnection(connectionData.getUrl(), connectionData.getUsername(), connectionData.getPassword());			
		} catch (Exception e) {
			throw new RuntimeException("Connection not established", e);
		}
	}

	public abstract String getDialect();

	public abstract String getConnectionDriverClass();
}