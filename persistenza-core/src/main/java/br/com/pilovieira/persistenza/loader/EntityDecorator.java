package br.com.pilovieira.persistenza.loader;

import br.com.pilovieira.persistenza.PersistenzaHeap;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

class EntityDecorator {
	
	public Class<?> decorateAndLoad(String className) {
		try {
			return decorateOrCry(className);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Class<?> decorateOrCry(String className) throws NotFoundException, CannotCompileException, ClassNotFoundException {
		if (!PersistenzaHeap.getOptionalConfigs().isAutoDecorate())
			return forName(className);
		
		CtClass ctClass = loadCtClass(className);
		
		decorateBySections(ctClass);
		
		if (ctClass.isFrozen() || !ctClass.isModified())
			return forName(ctClass.getName());
		
		return (Class<?>) ctClass.toClass();
	}

	private CtClass loadCtClass(String className) throws NotFoundException, CannotCompileException {
		String[] subNames = className.split("\\.");
		String simpleName = subNames[subNames.length - 1];
		String packageName = className.replace("." + simpleName, "");
		
		ClassPool pool = ClassPool.getDefault();
		CtClass ctClass = pool.get(className);
		pool.makePackage(pool.getClassLoader(), packageName);
		return ctClass;
	}

	private Class<?> forName(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}

	private void decorateBySections(CtClass ctClass) throws CannotCompileException, ClassNotFoundException {
		new IdDecorator().createIdentifierIfNeeded(ctClass);
	}
	
}
