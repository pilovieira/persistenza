package br.com.pilovieira.persistenza;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.serializers.JavaCodeSerializer;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class PersistenzaHeap {
	
	private static AnnotationConfiguration configuration;
	private static SessionFactory sessionFactory;
	private static Database database;
	private static Reflections typesScanner;
	
	public static AnnotationConfiguration getConfiguration() {
		return configuration;
	}
	
	static void setConfiguration(AnnotationConfiguration configuration) {
		PersistenzaHeap.configuration = configuration;
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	static void setSessionFactory(SessionFactory sessionFactory) {
		PersistenzaHeap.sessionFactory = sessionFactory;
	}
	
	public static Database getDatabase() {
		return database;
	}
	
	static void setDatabase(Database database) {
		PersistenzaHeap.database = database;
	}
	
	public static Reflections getTypesScanner() {
		if (typesScanner == null)
			loadTypesScanner();
		return typesScanner;
	}
	
	static void setTypesScanner(Reflections typesScanner) {
		PersistenzaHeap.typesScanner = typesScanner;
	}
	
	private static void loadTypesScanner() {
		ConfigurationBuilder config = new ConfigurationBuilder();
		config.setUrls(ClasspathHelper.forClassLoader());
		config.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner());
		config.setSerializer(new JavaCodeSerializer());
		
		typesScanner = new Reflections(config);
	}
	
	private PersistenzaHeap() {}
}
