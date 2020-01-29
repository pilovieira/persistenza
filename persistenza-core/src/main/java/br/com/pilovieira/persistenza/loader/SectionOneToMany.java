package br.com.pilovieira.persistenza.loader;

import java.util.HashMap;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.IndexColumn;

import br.com.pilovieira.persistenza.loader.ArredatoreAnnotator.AnnotationMask;

class SectionOneToMany implements Section {
	
	@Override
	public void decorate(CtClass ctClass) throws CannotCompileException, ClassNotFoundException, NotFoundException {
		for (CtField ctField : ctClass.getDeclaredFields())
			if (isElegible(ctField))
				decorateOneToOne(ctClass, ctField);
	}

	private boolean isElegible(CtField ctField) throws NotFoundException {
		return !ArredatoreUtils.isTransient(ctField) &&
				ArredatoreUtils.isSetListType(ctField) &&
				!ArredatoreUtils.hasAnyRelashionshipAnnotations(ctField);
	}

	private void decorateOneToOne(CtClass ctClass, CtField ctField) throws ClassNotFoundException {
		ArredatoreAnnotator.addAnnotationsInField(ctClass, ctField, 
				createAnnotationOneToMany(),
				createAnnotationCascade(),
				createAnnotationIndexColumn());
	}

	private AnnotationMask createAnnotationOneToMany() {
		Map<String, Object> memberValues = new HashMap<String, Object>();
		memberValues.put("fetch", FetchType.EAGER);
		return new ArredatoreAnnotator.AnnotationMask(OneToMany.class, memberValues);
	}

	private AnnotationMask createAnnotationCascade() {
		Map<String, Object> memberValues = new HashMap<String, Object>();
		memberValues.put("value", new CascadeType[]{CascadeType.ALL});
		return new ArredatoreAnnotator.AnnotationMask(Cascade.class, memberValues);
	}

	private AnnotationMask createAnnotationIndexColumn() {
		Map<String, Object> memberValues = new HashMap<String, Object>();
		memberValues.put("name", "INDEX");
		return new ArredatoreAnnotator.AnnotationMask(IndexColumn.class, memberValues);
	}

}
