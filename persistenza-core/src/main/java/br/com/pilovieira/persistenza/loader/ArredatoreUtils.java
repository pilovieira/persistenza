package br.com.pilovieira.persistenza.loader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

class ArredatoreUtils {
	
	public static void addAnnotationsInField(CtClass ctClass, CtField ctField, Class<?>... annotations) throws ClassNotFoundException {
		List<Class<?>> neededAnnotations = filterNeededAnnotations(ctField, annotations);
		if (neededAnnotations.isEmpty())
			return;
		
		addAnnotations(ctClass, ctField, neededAnnotations);
	}

	private static List<Class<?>> filterNeededAnnotations(CtField ctField, Class<?>... annotations) throws ClassNotFoundException {
		List<Class<?>> needed = new ArrayList<>();
		
		for (Class<?> annotation : annotations)
			if (ctField.getAnnotation(annotation) == null)
				needed.add(annotation);
		
		return needed;
	}

	private static void addAnnotations(CtClass ctClass, CtField ctField, List<Class<?>> newAnnotations) throws ClassNotFoundException {
		ClassFile cfile = ctClass.getClassFile();
		ConstPool cpool = cfile.getConstPool();
		
		AnnotationsAttribute attr = new AnnotationsAttribute(cpool, AnnotationsAttribute.visibleTag);
		
		for (Object object : ctField.getAnnotations())
			attr.addAnnotation(new Annotation(object.toString().replace("@", ""), cpool));
		
		for (Class<?> annotation : newAnnotations)
			attr.addAnnotation(new Annotation(annotation.getName(), cpool));
		
		ctField.getFieldInfo().addAttribute(attr);
	}
	
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

}
