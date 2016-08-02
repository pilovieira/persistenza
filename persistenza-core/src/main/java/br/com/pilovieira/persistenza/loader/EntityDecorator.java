package br.com.pilovieira.persistenza.loader;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class EntityDecorator {
	
	public Class<?> decorateAndLoad(String className) {
		try {
			return decorateOrCry(className);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Class<?> decorateOrCry(String className) throws NotFoundException, CannotCompileException, ClassNotFoundException {
		String[] subNames = className.split("\\.");
		String simpleName = subNames[subNames.length - 1];
		String packageName = className.replace("." + simpleName, "");
		
		ClassPool pool = ClassPool.getDefault();
		CtClass ctClass = pool.get(className);
		pool.makePackage(pool.getClassLoader(), packageName);
		
		decorateBySections(ctClass);
		
		if (ctClass.isFrozen() || !ctClass.isModified())
			return Class.forName(ctClass.getName());
		
		return (Class<?>) ctClass.toClass();
	}

	private void decorateBySections(CtClass ctClass) throws CannotCompileException, ClassNotFoundException {
		new IdDecorator().createIdentifierIfNeeded(ctClass);
	}
	
}
