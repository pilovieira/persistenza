package br.com.pilovieira.persistenza.install;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ScriptLoader {
	
	private static final String SEPARATOR = "-";
	private static final String JAR_EXTENSION = ".jar";
	private static final String SCRIPT_EXTENSION = ".install";
	
	private Map<String, Set<Script>> scripts = new HashMap<String, Set<Script>>();

	public Map<String, Set<Script>> load(File file) {
		try {
			if (!isJar(file))
				throw new RuntimeException("Arquivo selecionado deve ser jar!");
			
			loadZipFile(file);
		} catch (IOException ex) {
			throw new RuntimeException("Erro ao carregar scripts", ex);
		}
		
		return scripts;
	}
	
	private boolean isJar(File propertyFile) {
		return propertyFile.getName().endsWith(JAR_EXTENSION);
	}

	private void loadZipFile(File file) throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)entries.nextElement();
			
			if (zipEntry.getName().endsWith(SCRIPT_EXTENSION))
				loadScript(zipEntry, zipFile.getInputStream(zipEntry));
		}
	}
	
	private void loadScript(ZipEntry zipEntry, InputStream inputStream) throws IOException {
		String[] split = zipEntry.getName().replace(SCRIPT_EXTENSION, "").split(SEPARATOR);
		
		add(new Script(split[0], Integer.parseInt(split[1]), inputStream));
	}

	private void add(Script script) {
		Set<Script> group = getGroup(script.getGroup());
		group.add(script);
	}
	
	private Set<Script> getGroup(String group) {
		Set<Script> set = scripts.get(group);
		
		if (set == null) {
			set = new HashSet<Script>();
			scripts.put(group, set);
		}
		
		return set;
	}
}
