package br.com.pilovieira.persistenza.db;

import static java.lang.System.getProperty;
import static java.lang.System.setProperty;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class Database {
	
	public abstract String getDialect();
	
	public abstract String getConnectionDriverClass();
	
	public static final String CONNECTION_URL = "persistenza.url";
	public static final String CONNECTION_USERNAME = "persistenza.username";
	public static final String CONNECTION_PASSWORD = "persistenza.password";
	
	public void load() {
		setProperty("hibernate.dialect", getDialect());
		setProperty("hibernate.connection.driver_class", getConnectionDriverClass());
		setProperty("hibernate.connection.url", getProperty(CONNECTION_URL));
		setProperty("hibernate.connection.username", getProperty(CONNECTION_USERNAME));
		setProperty("hibernate.connection.password", getProperty(CONNECTION_PASSWORD));
	}
	
	public Connection getConnection() {
		try {
			Class.forName(getConnectionDriverClass());
			
			return DriverManager.getConnection(
					getProperty(CONNECTION_URL),
					getProperty(CONNECTION_USERNAME),
					getProperty(CONNECTION_PASSWORD));			
		} catch (Exception e) {
			throw new RuntimeException("Problema ao carregar connection", e);
		}
	}
}