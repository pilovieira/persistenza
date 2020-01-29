package br.com.pilovieira.persistenza.loader;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

class SectionOneToOne implements Section {
	
	@Override
	public void decorate(CtClass ctClass) throws CannotCompileException, ClassNotFoundException, NotFoundException {
		for (CtField ctField : ctClass.getDeclaredFields())
			if (isElegible(ctField))
				decorateOneToOne(ctClass, ctField);
	}

	private boolean isElegible(CtField ctField) throws NotFoundException {
		return !ArredatoreUtils.isTransient(ctField) &&
				!ctField.getType().isInterface() &&
				!ArredatoreUtils.hasAnyRelashionshipAnnotations(ctField) &&
				!ArredatoreUtils.isAutoMapped(ctField);
	}

	private void decorateOneToOne(CtClass ctClass, CtField ctField) throws ClassNotFoundException {
		ArredatoreAnnotator.addAnnotationsInField(ctClass, ctField, javax.persistence.OneToOne.class);
	}

}
