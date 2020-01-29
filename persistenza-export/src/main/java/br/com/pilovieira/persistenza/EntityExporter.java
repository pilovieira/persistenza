package br.com.pilovieira.persistenza;

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
		PersistenzaHeap.setTypesScanner(null);
		load();
	}

	private void load() {
		config = new AnnotationConfiguration();
		
		new EntityLoader(config).load();
		schemaExport = new SchemaExport(config);
	}
	
	public List<String> getEntities() {
		@SuppressWarnings("unchecked")
		List<String> values = new ArrayList<String>(new HashSet<String>(config.getImports().values()));
		Collections.sort(values);
		return values;
	}
	
	public void export(String outputFileName) {
		if (!outputFileName.isEmpty())
			schemaExport.setOutputFile(outputFileName);
		
		schemaExport.execute(true, false, false, true);
	}
}
