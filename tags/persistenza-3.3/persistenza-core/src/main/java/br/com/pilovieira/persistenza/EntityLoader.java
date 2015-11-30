package br.com.pilovieira.persistenza;

import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.cfg.AnnotationConfiguration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.serializers.JavaCodeSerializer;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

public class EntityLoader {
	
	private AnnotationConfiguration config;
	
	public EntityLoader(AnnotationConfiguration config) {
		this.config = config;
	}
	
	public void load() {
		for (Class<?> clazz : getEntities())
			config.addAnnotatedClass(clazz);
	}
	
	private Set<Class<?>> getEntities() {
		ConfigurationBuilder config = new ConfigurationBuilder();
		config.setUrls(ClasspathHelper.forClassLoader());
		config.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner());
		config.setSerializer(new JavaCodeSerializer());
		
		Reflections reflections = new Reflections(config);
		return reflections.getTypesAnnotatedWith(Entity.class);
	}
}
