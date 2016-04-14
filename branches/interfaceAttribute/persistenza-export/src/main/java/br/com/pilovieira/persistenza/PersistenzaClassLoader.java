package br.com.pilovieira.persistenza;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class PersistenzaClassLoader extends URLClassLoader {

	public PersistenzaClassLoader(File loadFile) {
		super(getSystemURLs());
		addURL(loadFile);
	}
	
	public PersistenzaClassLoader(URL[] urls) {
		super(urls);
	}

	private void addURL(File loadFile) {
		try {
			super.addURL(loadFile.toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static URL[] getSystemURLs() {
		return ((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs();
	}
}
