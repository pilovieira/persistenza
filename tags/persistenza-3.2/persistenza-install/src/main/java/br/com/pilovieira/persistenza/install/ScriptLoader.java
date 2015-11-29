package br.com.pilovieira.persistenza.install;

import static com.google.common.io.Closeables.closeQuietly;
import static java.util.Arrays.asList;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ScriptLoader {
	
	private static final String QUERY_SEPARATOR = ";";
	private static final String MSG_WITHOUT_HEADER = "Script without a valid header!";
	private static final String SEQUENCE_IDENTIFIER = "#";
	private static final String SCRIPT_EXTENSION = ".install";
	
	private Map<String, Set<Script>> scripts = new HashMap<String, Set<Script>>();

	public Map<String, Set<Script>> load(File file) {
		try {
			loadZipFile(file);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
		
		return scripts;
	}
	
	private void loadZipFile(File file) throws ZipException, IOException {
		ZipFile zipFile = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zipFile.entries();

		while (entries.hasMoreElements()) {
			ZipEntry zipEntry = (ZipEntry)entries.nextElement();
			
			if (zipEntry.getName().endsWith(SCRIPT_EXTENSION))
				loadScript(zipEntry, zipFile.getInputStream(zipEntry));
		}
		
		zipFile.close();
	}
	
	private void loadScript(ZipEntry zipEntry, InputStream stream) throws IOException {
		InputStreamReader is = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(is);

		try {
			String[] header = getHeader(br);
			List<String> queries = loadQueries(br);

			add(new Script(header[0].trim(), Integer.parseInt(header[1].trim()), queries));
		} finally {
			closeQuietly(br);
			closeQuietly(is);
		}
	}

	private List<String> loadQueries(BufferedReader br) throws IOException {
		StringBuilder sb = new StringBuilder();
		String buffer = br.readLine();
		
		while (buffer != null) {
			sb.append(buffer);
			buffer = br.readLine();
		}
		
		return asList(sb.toString().split(QUERY_SEPARATOR));
	}

	private String[] getHeader(BufferedReader br) throws IOException {
		String read = br.readLine();
		String[] split = read.split(SEQUENCE_IDENTIFIER);
		
		if (split.length != 2)
			throw new RuntimeException(MSG_WITHOUT_HEADER);
		return split;
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
