package br.com.pilovieira.persistenza.loader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javassist.CtClass;
import javassist.CtField;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.StringMemberValue;

class ArredatoreAnnotator {
	
	public static void addAnnotationsInField(CtClass ctClass, CtField ctField, Class<?>... annotations) throws ClassNotFoundException {
		List<AnnotationMask> ann = new ArrayList<>();
		for (Class<?> annotation : annotations)
			ann.add(new AnnotationMask(annotation, new HashMap<String, Object>()));
		addAnnotationsInField(ctClass, ctField, ann.toArray(new AnnotationMask[]{}));
	}
	
	public static void addAnnotationsInField(CtClass ctClass, CtField ctField, AnnotationMask... annotations) throws ClassNotFoundException {
		List<AnnotationMask> neededAnnotations = filterNeededAnnotations(ctField, annotations);
		if (neededAnnotations.isEmpty())
			return;
		
		addAnnotations(ctClass, ctField, neededAnnotations);
	}
	
	private static List<AnnotationMask> filterNeededAnnotations(CtField ctField, AnnotationMask[] annotations) throws ClassNotFoundException {
		List<AnnotationMask> needed = new ArrayList<>();
		
		for (AnnotationMask annotationMask : annotations)
			if (ctField.getAnnotation(annotationMask.annotation) == null)
				needed.add(annotationMask);
		
		return needed;
	}

	private static void addAnnotations(CtClass ctClass, CtField ctField, List<AnnotationMask> newAnnotations) throws ClassNotFoundException {
		ClassFile cfile = ctClass.getClassFile();
		ConstPool cpool = cfile.getConstPool();
		
		AnnotationsAttribute attr = new AnnotationsAttribute(cpool, AnnotationsAttribute.visibleTag);
		
		for (Object object : ctField.getAnnotations())
			attr.addAnnotation(new Annotation(object.toString().replace("@", ""), cpool));
		
		for (AnnotationMask annotationMask : newAnnotations) {
			Annotation annotation = new Annotation(annotationMask.annotation.getName(), cpool);
			
			for (Entry<String, Object> entry : annotationMask.memberValues.entrySet())
				annotation.addMemberValue(entry.getKey(), createMemberValue(entry.getValue(), cpool));
			
			attr.addAnnotation(annotation);
		}
		
		ctField.getFieldInfo().addAttribute(attr);
	}

	private static MemberValue createMemberValue(Object value, ConstPool cPool) {
		if (value.getClass().isArray())
			return createArrayMemberValue((Object[]) value, cPool);
		if (value instanceof Integer)
			return new IntegerMemberValue(cPool, (int) value);
		if (value instanceof String)
			return new StringMemberValue((String) value, cPool);
		if (value instanceof Enum)
            return createEnumMemberValue(value, cPool);
		
		return null;
	}

	private static MemberValue createArrayMemberValue(Object[] array,	ConstPool cPool) {
		List<MemberValue> memberValues = new ArrayList<>();
		
		for (Object value : array)
			memberValues.add(createMemberValue(value, cPool));
		
		ArrayMemberValue arrayMemberValue = new ArrayMemberValue(cPool);
		arrayMemberValue.setValue(memberValues.toArray(new MemberValue[]{}));
		return arrayMemberValue;
	}

	private static EnumMemberValue createEnumMemberValue(Object value,	ConstPool cPool) {
		EnumMemberValue emb = new EnumMemberValue(cPool);
		emb.setType(value.getClass().getName());
		emb.setValue(((Enum<?>) value).name());
		return emb;
	}
	
	public static class AnnotationMask {
		private Class<?> annotation;
		private Map<String, Object> memberValues;
		
		public AnnotationMask(Class<?> annotation, Map<String, Object> memberValues) {
			this.annotation = annotation;
			this.memberValues = memberValues;
		}
	}

}
