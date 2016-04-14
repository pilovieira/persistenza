package br.com.pilovieira.persistenza.data;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.serializers.JavaCodeSerializer;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import br.com.pilovieira.persistenza.annotation.InterfaceAttribute;

class InterfaceAttributeSync {
	
	public <T> List<T> list(List<T> list) {
		for (T entity : list) {
			for (Field field : entity.getClass().getDeclaredFields()) {
				if (field.isAnnotationPresent(InterfaceAttribute.class)) {
					field.setAccessible(true);
					
					Class<?> type = field.getType();
					
//					field.set(entity, getValue());
				}
			}
			
		}
		
		
		return list;
	}

	private Object getValue(Class<?> clazz) {
		Set<?> subtypes = getEntities(clazz);
		
		// TODO Auto-generated method stub
		return null;
	}
	
	private Set<?> getEntities(Class<?> clazz) {
		ConfigurationBuilder config = new ConfigurationBuilder();
		config.setUrls(ClasspathHelper.forClassLoader());
		config.setScanners(new TypeAnnotationsScanner(), new SubTypesScanner());
		config.setSerializer(new JavaCodeSerializer());
		
		Reflections reflections = new Reflections(config);
		return reflections.getSubTypesOf(clazz);
	}

}
