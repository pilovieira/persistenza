package br.com.pilovieira.persistenza.loader;

import static com.google.common.collect.Iterables.concat;

import java.util.Iterator;

import javax.persistence.Entity;

import org.hibernate.cfg.AnnotationConfiguration;
import org.reflections.Store;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import br.com.pilovieira.persistenza.PersistenzaHeap;

public class EntityLoader {
	
	private AnnotationConfiguration config;
	
	public EntityLoader(AnnotationConfiguration config) {
		this.config = config;
	}
	
	public void load() {
		ClassArredatore arredatore = new ClassArredatore();
		Iterator<String> iterator = getEntitiesNames().iterator();
		
		while(iterator.hasNext()) {
			String className = iterator.next();
			Class<?> clazz = arredatore.arredate(className);
			config.addAnnotatedClass(clazz);
		}
	}
	
	private Iterable<String> getEntitiesNames() {
		Store store = PersistenzaHeap.getTypesScanner().getStore();
		Iterable<String> annotated = store.get(TypeAnnotationsScanner.class.getSimpleName(), Entity.class.getName());
		Iterable<String> subTypes = concat(annotated, store.getAll(TypeAnnotationsScanner.class.getSimpleName(), annotated));
		return concat(subTypes, store.getAll(SubTypesScanner.class.getSimpleName(), subTypes));
	}
}
