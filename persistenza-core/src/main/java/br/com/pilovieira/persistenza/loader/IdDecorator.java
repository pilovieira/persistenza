package br.com.pilovieira.persistenza.loader;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;

import javax.persistence.Id;

class IdDecorator implements Section {
	
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
	
	private void createDefaultIdentifier(CtClass ctClass) throws CannotCompileException {
		ClassFile cfile = ctClass.getClassFile();
		ConstPool cpool = cfile.getConstPool();
		
		CtField ctField = CtField.make(ID_DECLARATION, ctClass);
		ctField.setModifiers(Modifier.PRIVATE);
		
		AnnotationsAttribute attr = new AnnotationsAttribute(cpool, AnnotationsAttribute.visibleTag);
		Annotation annot = new Annotation(Id.class.getName(), cpool);
		attr.addAnnotation(annot);
		ctField.getFieldInfo().addAttribute(attr);
		
		ctClass.addField(ctField);
		ctClass.addMethod(CtNewMethod.getter(GETTER, ctField));
		ctClass.addMethod(CtNewMethod.setter(SETTER, ctField));
	}

}
