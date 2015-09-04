package br.com.pilovieira.persistenza.db;

import static java.lang.System.setProperty;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class DatabaseManager {
	
	static final String DIALECT = "hibernate.dialect";
	static final String DRIVER_CLASS = "hibernate.connection.driver_class";
	static final String CONNECTION_URL = "hibernate.connection.url";
	static final String CONNECTION_USERNAME = "hibernate.connection.username";
	static final String CONNECTION_PASSWORD = "hibernate.connection.password";

	private ConnectionData connectionData;
	
	public DatabaseManager(ConnectionData connectionData) {
		this.connectionData = connectionData;
	}
	
	public void loadProperties() {
		setProperty(DIALECT, getDialect());
		setProperty(DRIVER_CLASS, getConnectionDriverClass());
		setProperty(CONNECTION_URL, connectionData.getUrl());
		setProperty(CONNECTION_USERNAME, connectionData.getUsername());
		setProperty(CONNECTION_PASSWORD, connectionData.getPassword());
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