package br.com.pilovieira.persistenza.loader;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import br.com.pilovieira.persistenza.PersistenzaHeap;

class Arredatore {
	
	private Section[] sections;

	public Arredatore() {
		this(
				new SectionId(),
				new SectionInterfaccia(),
				new SectionOneToOne(),
				new SectionOneToMany()
			);
	}

	Arredatore(Section... sections) {
		this.sections = sections;
	}
	
	public Class<?> arredate(String className) {
		try {
			return decorateOrCry(className);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Class<?> decorateOrCry(String className) throws Exception {
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

	private void decorateBySections(CtClass ctClass) throws Exception {
		for (Section section : sections)
			section.decorate(ctClass);
	}
	
}
