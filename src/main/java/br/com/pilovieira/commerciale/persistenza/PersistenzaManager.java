package br.com.pilovieira.commerciale.persistenza;

import java.sql.Connection;

import org.hibernate.SessionFactory;

import br.com.pilovieira.commerciale.persistenza.db.Database;

public class PersistenzaManager {
	private static SessionFactory factory;
	private static Database database;
	
	public static void load(Class<? extends Database> databaseClass) {
		loadDatabase(databaseClass);
		loadEntities();
	}
	
	private static void loadDatabase(Class<? extends Database> databaseClass) {
		try {
			database = databaseClass.newInstance();
			database.load();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Problema ao carregar properties da database", e);
		}
	}

	private static void loadEntities() {
		SessionFactoryBuilder sessionFactoryBuilder = new SessionFactoryBuilder();
		factory = sessionFactoryBuilder.build();
	}
	
	public static Connection getConnection() {
		if (database == null)
			throw new RuntimeException("Database not loaded.");
		
		return database.getConnection();
	}

	public static SessionFactory getFactory() {
		return factory;
	}
}
