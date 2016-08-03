package br.com.pilovieira.persistenza.loader;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.Modifier;

import javax.persistence.Id;

class SectionId implements Section {
	
	private static final String ID_DECLARATION = "public int id;";
	private static final String SETTER = "setId";
	private static final String GETTER = "getId";

	@Override
	public void decorate(CtClass ctClass) throws CannotCompileException, ClassNotFoundException {
		for (CtField field : ctClass.getDeclaredFields())
			if (field.hasAnnotation(Id.class))
				return;
		
		createDefaultIdentifier(ctClass);
	}
	
	private void createDefaultIdentifier(CtClass ctClass) throws CannotCompileException, ClassNotFoundException {
		CtField ctField = CtField.make(ID_DECLARATION, ctClass);
		ctField.setModifiers(Modifier.PRIVATE);
		
		ArredatoreUtils.addAnnotationsInField(ctClass, ctField, javax.persistence.Id.class);
		
		ctClass.addField(ctField);
		ctClass.addMethod(CtNewMethod.getter(GETTER, ctField));
		ctClass.addMethod(CtNewMethod.setter(SETTER, ctField));
	}

}
