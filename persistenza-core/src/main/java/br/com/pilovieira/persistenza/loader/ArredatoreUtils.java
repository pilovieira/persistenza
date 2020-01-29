package br.com.pilovieira.persistenza.loader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

class ArredatoreUtils {
	
	public static boolean isAutoMapped(CtField field) throws NotFoundException {
		CtClass type = field.getType();
		return type.isPrimitive() || ArredatoreUtils.getAutoMappedTypes().contains(field.getType().getName()); 
	}
	
	private static List<String> getAutoMappedTypes() {
		List<String> types = new ArrayList<>();
		
		types.add(Boolean.class.getName());
		types.add(Character.class.getName());
		types.add(Date.class.getName());
		types.add(Double.class.getName());
		types.add(Float.class.getName());
		types.add(Integer.class.getName());
		types.add(Long.class.getName());
		types.add(Short.class.getName());
		types.add(String.class.getName());
		
		return types;
	}
	
	public static boolean isTransient(CtField ctField) {
		return ctField.hasAnnotation(javax.persistence.Transient.class);
	}
	
	public static boolean hasAnyRelashionshipAnnotations(CtField field) {
		Class<?>[] annotations = {OneToOne.class, OneToMany.class, ManyToOne.class, ManyToMany.class};
		
		for (Class<?> annotation : annotations)
			if (field.hasAnnotation(annotation))
				return true;
		
		return false;
	}
	
	public static boolean isSetListType(CtField field) throws NotFoundException {
		return ArredatoreUtils.getSetListTypes().contains(field.getType().getName()); 
	}
	
	private static List<String> getSetListTypes() {
		List<String> types = new ArrayList<>();
		
		types.add(List.class.getName());
		types.add(Set.class.getName());
		
		return types;
	}

}
