package br.com.pilovieira.persistenza;

import java.sql.Connection;

import org.hibernate.SessionFactory;

import br.com.pilovieira.persistenza.db.DatabaseManager;

public class PersistenzaManager {
	
	private static SessionFactory factory;
	private static DatabaseManager database;
	
	public static void setDatabaseManager(DatabaseManager databaseManager) {
		validateDatabaseManager(databaseManager);
		database = databaseManager;
		database.loadProperties();
	}
	
	public static void load() {
		loadEntities();
	}
	
	private static void loadEntities() {
		SessionFactoryBuilder sessionFactoryBuilder = new SessionFactoryBuilder();
		factory = sessionFactoryBuilder.build();
	}
	
	public static Connection getConnection() {
		validateDatabaseManager(database);
		return database.getConnection();
	}

	private static void validateDatabaseManager(DatabaseManager databaseManager) {
		if (databaseManager == null)
			throw new RuntimeException("Database not loaded.");
	}

	public static SessionFactory getFactory() {
		return factory;
	}
}
