package br.com.pilovieira.persistenza;

import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.cfg.AnnotationConfiguration;

class EntityLoader {
	
	private AnnotationConfiguration config;
	
	public EntityLoader(AnnotationConfiguration config) {
		this.config = config;
	}
	
	public void load() {
		for (Class<?> clazz : getEntities())
			config.addAnnotatedClass(clazz);
	}
	
	private Set<Class<?>> getEntities() {
		return PersistenzaHeap.getTypesScanner().getTypesAnnotatedWith(Entity.class);
	}
}
