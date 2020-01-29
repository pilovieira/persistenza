package br.com.pilovieira.persistenza.loader;

import java.util.ArrayList;
import java.util.List;

import javassist.CtClass;
import javassist.CtField;
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

	private static void addAnnotations(CtClass ctClass, CtField ctField, List<Class<?>> neededAnnotations) {
		ClassFile cfile = ctClass.getClassFile();
		ConstPool cpool = cfile.getConstPool();
		
		AnnotationsAttribute attr = new AnnotationsAttribute(cpool, AnnotationsAttribute.visibleTag);
		for (Class<?> annotation : neededAnnotations)
			attr.addAnnotation(new Annotation(annotation.getName(), cpool));
		
		ctField.getFieldInfo().addAttribute(attr);
	}

}
