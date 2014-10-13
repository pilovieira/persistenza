package br.com.pilovieira.commerciale.persistenza;

import java.sql.Connection;

import org.hibernate.SessionFactory;

import br.com.pilovieira.commerciale.persistenza.db.Database;

public class PersistenzaManager {
	
	private static SessionFactory factory;
	private static Database database;
	
	public static void setFactory(SessionFactory factory) {
		PersistenzaManager.factory = factory;
	}
	
	static SessionFactory getFactory() {
		return factory;
	}
	
	public static void loadDatabase(Class<? extends Database> databaseClass) {
		try {
			database = databaseClass.newInstance();
			database.load();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Problema ao carregar properties da database", e);
		}
	}
	
	public static Connection getConnection() {
		if (database == null)
			throw new RuntimeException("Database not loaded.");
		
		return database.getConnection();
	}
}
