package br.com.pilovieira.persistenza.loader;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.Modifier;

class SectionId implements Section {
	
	private static final String ID_NAME = "id";
	private static final String ID_DECLARATION = "public int id;";
	private static final String SETTER = "setId";
	private static final String GETTER = "getId";

	@Override
	public void decorate(CtClass ctClass) throws CannotCompileException, ClassNotFoundException {
		CtField idField = null;

		for (CtField field : ctClass.getDeclaredFields()) {
			if (field.hasAnnotation(javax.persistence.Id.class))
				return;
			if (ID_NAME.equals(field.getName()))
				idField = field;
		}

		if (idField == null)
			idField = createDefaultIdentifierField(ctClass);
		
		ArredatoreAnnotator.addAnnotationsInField(ctClass, idField, javax.persistence.Id.class);
	}
	
	private CtField createDefaultIdentifierField(CtClass ctClass) throws CannotCompileException, ClassNotFoundException {
		CtField idField = CtField.make(ID_DECLARATION, ctClass);
		idField.setModifiers(Modifier.PRIVATE);
		
		ctClass.addField(idField);
		ctClass.addMethod(CtNewMethod.getter(GETTER, idField));
		ctClass.addMethod(CtNewMethod.setter(SETTER, idField));
		
		return idField;
	}

}
