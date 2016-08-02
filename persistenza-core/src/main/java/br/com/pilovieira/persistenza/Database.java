package br.com.pilovieira.persistenza;

import static java.lang.System.setProperty;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class Database {
	
	private static final String PROPERTY_DIALECT = "hibernate.dialect";
	private static final String PROPERTY_DRIVER_CLASS = "hibernate.connection.driver_class";
	private static final String PROPERTY_CONNECTION_URL = "hibernate.connection.url";
	private static final String PROPERTY_CONNECTION_USERNAME = "hibernate.connection.username";
	private static final String PROPERTY_CONNECTION_PASSWORD = "hibernate.connection.password";
	private static final String PROPERTY_SHOW_SQL = "hibernate.show_sql";

	private String url;
	private String username;
	private String password;
	private boolean showSql;
	private boolean ssl;
	private OptionalConfigs configs = new OptionalConfigs();
	
	public Database(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public void setShowSql(boolean showSql) {
		this.showSql = showSql;
	}
	
	public void setSsl(boolean ssl) {
		this.ssl = ssl;
	}
	
	public void setConfigs(OptionalConfigs configs) {
		this.configs = configs;
	}
	
	public OptionalConfigs getConfigs() {
		return configs;
	}
	
	public Connection getConnection() {
		try {
			Class.forName(getConnectionDriverClass());
			return DriverManager.getConnection(prepareUrl(), username, password);			
		} catch (Exception e) {
			throw new RuntimeException("Connection not established", e);
		}
	}

	private String prepareUrl() {
		return ssl ? url + getSslOptions() : url;
	}
	
	private String getSslOptions() {
		String ssl = "?ssl=true";
		String factory = getSslFactory();
		return factory != null ? ssl + "&sslfactory=" + factory : ssl;
	}

	protected void loadProperties() {
		setProperty(PROPERTY_SHOW_SQL, String.valueOf(showSql));
		setProperty(PROPERTY_DIALECT, getDialect());
		setProperty(PROPERTY_DRIVER_CLASS, getConnectionDriverClass());
		setProperty(PROPERTY_CONNECTION_URL, prepareUrl());
		setProperty(PROPERTY_CONNECTION_USERNAME, username);
		setProperty(PROPERTY_CONNECTION_PASSWORD, password);
	}

	protected abstract String getDialect();

	protected abstract String getConnectionDriverClass();

	protected abstract String getSslFactory();
}