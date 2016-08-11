package br.com.pilovieira.persistenza.loader;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

class SectionInterfaccia implements Section {
	
	@Override
	public void decorate(CtClass ctClass) throws CannotCompileException, ClassNotFoundException, NotFoundException {
		for (CtField ctField : ctClass.getDeclaredFields())
			if (isElegible(ctField))
				decorateInterfaceIfNeeded(ctClass, ctField);
	}

	private boolean isElegible(CtField ctField) throws NotFoundException {
		return !ArredatoreUtils.isTransient(ctField) && 
				ctField.getType().isInterface() &&
				!ArredatoreUtils.isSetListType(ctField);
	}
	
	private void decorateInterfaceIfNeeded(CtClass ctClass, CtField ctField) throws ClassNotFoundException {
		ArredatoreAnnotator.addAnnotationsInField(ctClass, ctField, 
				javax.persistence.Transient.class, 
				br.com.pilovieira.persistenza.annotation.Interfaccia.class);
	}

}
