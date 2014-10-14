package br.com.pilovieira.commerciale.persistenza;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.persistence.Entity;

import org.hibernate.cfg.AnnotationConfiguration;

public class EntityLoader {
	
	private static final String ANNOTATION_ENTITY = "javax/persistence/Entity";
	private static final String EXTENSION_JAR = ".jar";
	
	private AnnotationConfiguration config;
	private File root = new File("").getAbsoluteFile();
	
	public EntityLoader(AnnotationConfiguration config) {
		this.config = config;
	}
	
	void load() {
		try {
			loadEntity(root);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao carregar entidades", e);
		}
	}
	
	private void loadEntity(File entityFile) throws IOException {
		if (entityFile.isDirectory())
			for(File subFile : entityFile.listFiles())
				loadEntity(subFile);
		
		if (entityFile.getName().endsWith(EXTENSION_JAR))
			loadZipFile(entityFile);
	}
	
	private void loadZipFile(File file) throws IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)entries.nextElement();
			
			if (isPossibleEntity(zipFile, zipEntry))
				addEntity(convertClassName(zipEntry.getName()));	
		}
		
		zipFile.close();
	}
	
	private boolean isPossibleEntity(ZipFile zipFile, ZipEntry zipEntry) throws IOException {
		if (!zipEntry.getName().endsWith(".class"))
			return false;
		
		InputStream inputStream = zipFile.getInputStream(zipEntry);
		
		StringBuffer buffer = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = inputStream.read(b)) != -1;)
			buffer.append(new String(b, 0, n));

		return buffer.toString().contains(ANNOTATION_ENTITY);
	}

	private String convertClassName(String className) {
		className = className.replace(".class", "");
		className = className.replace('\\', '.');
		className = className.replace('/', '.');
		return className;
	}

	private void addEntity(String className) {
		try {
			Class<?> classLoaded = SessionFactoryBuilder.class.getClassLoader().loadClass(className);
			if (classLoaded.isAnnotationPresent(Entity.class)) {
				config.addAnnotatedClass(classLoaded);
				System.out.println("adicionando entity " + className);
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}		
	}
}
