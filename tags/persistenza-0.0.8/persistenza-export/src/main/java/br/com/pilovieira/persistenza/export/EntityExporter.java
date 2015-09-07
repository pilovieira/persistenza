package br.com.pilovieira.persistenza.export;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import br.com.pilovieira.persistenza.EntityLoader;

public class EntityExporter {

	private AnnotationConfiguration config;
	private SchemaExport schemaExport; 

	public EntityExporter() {
		load();
	}

	private void load() {
		config = new AnnotationConfiguration();
		
		new EntityLoader(config).load();
		schemaExport = new SchemaExport(config);
		checkStatus();
	}
	
	public List<String> getEntities() {
		@SuppressWarnings("unchecked")
		List<String> values = new ArrayList<String>(new HashSet<String>(config.getImports().values()));
		Collections.sort(values);
		return values;
	}
	
	public void export(boolean drop, boolean create) {
		if (drop)
			schemaExport.drop(true, true);
		if (create)
			schemaExport.create(true, true);
		
		checkStatus();
	}
	
	private void checkStatus() {
		if (schemaExport.getExceptions().isEmpty())
			return;
		
		throwExceptions();
	}

	private void throwExceptions() {
		String exceptions = "Executado com as seguintes exceptions:\r\n";
		for (Object ex : schemaExport.getExceptions())
			exceptions += ex.toString() + "\r\n";
			
		throw new RuntimeException(exceptions);
	}	
}
