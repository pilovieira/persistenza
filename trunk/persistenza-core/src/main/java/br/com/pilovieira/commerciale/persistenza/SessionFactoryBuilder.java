package br.com.pilovieira.commerciale.persistenza;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

class SessionFactoryBuilder {
	
	private AnnotationConfiguration config = new AnnotationConfiguration();
	
	public SessionFactory build() {
		new EntityLoader(config).load();
		return config.buildSessionFactory();
	}
}
