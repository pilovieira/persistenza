package br.com.pilovieira.commerciale.persistenza;

import static br.com.pilovieira.commerciale.persistenza.PersistenzaManager.isDevelopMode;

import java.io.File;
import java.io.IOException;

import javax.persistence.Entity;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

class SessionFactoryBuilder {
	

	private AnnotationConfiguration config = new AnnotationConfiguration();
	private File root = getRootFile();
	
	public SessionFactory build() {
		loadEntities();
		return config.buildSessionFactory();
	}

	private void loadEntities() {
		try {
			loadEntity(root);
		} catch (IOException | ClassNotFoundException e) {
			throw new RuntimeException("Erro ao carregar entidades", e);
		}
	}
	
	private void loadEntity(File entityFile) throws IOException, ClassNotFoundException {
		if (entityFile.isDirectory())
			for(File subFile : entityFile.listFiles())
				loadEntity(subFile);
		
		addEntity(entityFile);
	}
	
	private void addEntity(File file) throws ClassNotFoundException {
		if (itsATrap(file))
			return;
		
		Class<?> classLoaded = SessionFactoryBuilder.class.getClassLoader().loadClass(convertClassName(file));
		
		if (classLoaded.isAnnotationPresent(Entity.class))
			config.addAnnotatedClass(classLoaded);
	}

	private boolean itsATrap(File file) {
		return !file.getName().endsWith(".class") || file.getAbsolutePath().contains("test");
	}
	
	private String convertClassName(File file) {
		String className = file.getAbsolutePath();
		
		className = className.substring(root.getAbsolutePath().length(), className.length());
		className = className.replace(".class", "");
		className = className.replace('\\', '.');
		className = className.replace('/', '.');
		
		className = convertDevelopClassName(className);	
		
		return className;
	}

	private File getRootFile() {
		File absoluteFile = new File("").getAbsoluteFile();
		return isDevelopMode() ? absoluteFile.getParentFile() : absoluteFile;
	}

	private String convertDevelopClassName(String className) {
		if (className.contains(".target.classes."))
			return className.split(".target.classes.")[1];
		
		return className;
	}
}
