package br.com.pilovieira.persistenza.loader;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;

class SectionInterfaccia implements Section {
	
	@Override
	public void decorate(CtClass ctClass) throws CannotCompileException, ClassNotFoundException, NotFoundException {
		for (CtField ctField : ctClass.getDeclaredFields())
			if (ctField.getType().isInterface())
				decorateInterfaceIfNeeded(ctClass, ctField);
	}
	
	private void decorateInterfaceIfNeeded(CtClass ctClass, CtField ctField) throws ClassNotFoundException {
		ArredatoreUtils.addAnnotationsInField(ctClass, ctField, 
				javax.persistence.Transient.class, 
				br.com.pilovieira.persistenza.annotation.Interfaccia.class);
	}

}
