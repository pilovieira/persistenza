package br.com.pilovieira.commerciale.persistenza.core;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

public class EssereLauncher {
	
	private Set<String> classNames = new HashSet<String>();
	
	SessionFactory prepareFactory() {
		AnnotationConfiguration  config = new AnnotationConfiguration();
		
		config.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
		config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
		config.setProperty("hibernate.connection.url", "jdbc:postgresql://localhost:5432/hibernate");
		config.setProperty("hibernate.connection.username", "postgres");
		config.setProperty("hibernate.connection.password", "");
		config.setProperty("hibernate.show_sql", "true");
		
		loadEsseres(config);
		
		return config.buildSessionFactory();
		
	}

	private void loadEsseres(AnnotationConfiguration config) {
		try {
			findClasses(new File("..").getCanonicalFile());
			addEsseres(config);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}

	private void findClasses(File root) throws ClassNotFoundException, IOException {
		for (File file : root.listFiles()) {
			if (file.isDirectory())
				findClasses(file);
			else
				if (file.getName().endsWith(".class"))
					classNames.add(cleanName(file.getCanonicalPath()));
		}
	}

	private String cleanName(String nome) {
		if (nome.contains("target"))
			nome = nome.split("target")[1].split("classes")[1];
		
		nome = nome.replace(".class", "");
		
		return nome.replace("\\", ".").replace("/", ".").substring(1);
	}

	private void addEsseres(AnnotationConfiguration config) throws ClassNotFoundException {
		for (String name : classNames) {
			Class<?> clazz = Class.forName(name);
			if (clazz != Essere.class && Essere.class.isAssignableFrom(clazz))
				config.addAnnotatedClass(clazz);
		}
	}
}
