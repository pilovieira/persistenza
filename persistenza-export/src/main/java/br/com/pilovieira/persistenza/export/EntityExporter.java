package br.com.pilovieira.persistenza.export;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import br.com.pilovieira.persistenza.EntityLoader;

public class EntityExporter {

	private File selectedFile;
	private AnnotationConfiguration config;
	private SchemaExport schemaExport; 

	public EntityExporter(File selectedFile) {
		this.selectedFile = selectedFile;
		load();
	}

	private void load() {
		config = new AnnotationConfiguration();
		
		new EntityLoader(selectedFile, config).load();
		schemaExport = new SchemaExport(config);
	}
	
	List<String> getEntities() {
		@SuppressWarnings("unchecked")
		List<String> values = new ArrayList<String>(new HashSet<String>(config.getImports().values()));
		Collections.sort(values);
		return values;
	}
	
	void drop() {
		schemaExport.drop(true, true);
	}
	
	void create() {
		schemaExport.create(true, true);
	}	
}
