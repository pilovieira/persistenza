package br.com.pilovieira.persistenza.loader;

import javassist.CtClass;

interface Section {
	
	void decorate(CtClass ctClass) throws Exception ;

}
