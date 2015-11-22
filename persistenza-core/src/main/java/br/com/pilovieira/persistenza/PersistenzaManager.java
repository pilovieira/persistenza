package br.com.pilovieira.persistenza;

import java.sql.Connection;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class PersistenzaManager {
	
	private static SessionFactory factory;
	private static Database database;
	
	public static void setDatabaseManager(Database database) {
		validate(database);
		PersistenzaManager.database = database;
		database.loadProperties();
	}
	
	public static void load() {
		validate(database);
		loadEntities();
	}
	
	private static void loadEntities() {
		AnnotationConfiguration config = new AnnotationConfiguration();
		new EntityLoader(config).load();
		factory = config.buildSessionFactory();
	}
	
	public static Connection getConnection() {
		validate(database);
		return database.getConnection();
	}

	private static void validate(Database databaseManager) {
		if (databaseManager == null)
			throw new RuntimeException("Database not loaded.");
	}

	public static SessionFactory getFactory() {
		return factory;
	}
}
