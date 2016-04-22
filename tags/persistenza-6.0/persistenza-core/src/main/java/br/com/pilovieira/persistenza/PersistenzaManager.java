package br.com.pilovieira.persistenza;

import java.sql.Connection;

import org.hibernate.cfg.AnnotationConfiguration;

public final class PersistenzaManager {
	
	public static void load(Database database) {
		setDatabase(database);
		load();
	}
	
	static void setDatabase(Database database) {
		PersistenzaHeap.setDatabase(database);
		validateDatabase();
		database.loadProperties();
	}
	
	static void load() {
		validateDatabase();
		loadEntities();
	}
	
	private static void loadEntities() {
		AnnotationConfiguration config = new AnnotationConfiguration();
		new EntityLoader(config).load();
		PersistenzaHeap.setConfiguration(config);
		PersistenzaHeap.setSessionFactory(config.buildSessionFactory());
	}
	
	public static Connection getConnection() {
		validateDatabase();
		return PersistenzaHeap.getDatabase().getConnection();
	}

	private static void validateDatabase() {
		if (PersistenzaHeap.getDatabase() == null)
			throw new RuntimeException("Database not loaded.");
	}

	private PersistenzaManager() {}
}
