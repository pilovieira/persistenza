package br.com.pilovieira.persistenza.update;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import br.com.pilovieira.persistenza.PersistenzaManager;
import br.com.pilovieira.persistenza.Script;
import br.com.pilovieira.persistenza.ScriptGroup;
import br.com.pilovieira.persistenza.ScriptGroupManager;
import br.com.pilovieira.persistenza.ScriptLoader;

public class SystemUpdateManager {
	
	private ScriptGroupManager groupManager;
	private String[] groupPriority;

	public SystemUpdateManager(String... groupPriority) {
		this.groupPriority = groupPriority;
	}
	
	public boolean start() {
		installUpdater();
		loadScripts();
		
		boolean needInstall = groupManager.needInstall();
		
		if (needInstall)
			openView();
		
		return needInstall;
	}
	
	private void installUpdater() {
		if (isUpdaterInstalled())
			return;
		
		AnnotationConfiguration config = new AnnotationConfiguration();
		config.addAnnotatedClass(ScriptGroup.class);
		SchemaExport exporter = new SchemaExport(config);
		exporter.create(true, true);
	}
	
	private void loadScripts() {
		Map<String, Set<Script>> scripts = new ScriptLoader().loadByResources();
		groupManager = new ScriptGroupManager(scripts);
		groupManager.setGroupOrder(Arrays.asList(groupPriority));
	}
	
	private void openView() {
		if (groupManager.needInstall())
			new SystemUpdateView(groupManager);
	}
	
	private boolean isUpdaterInstalled() {
		try {
			Connection c = PersistenzaManager.getConnection();
			DatabaseMetaData dbm = c.getMetaData();  
			ResultSet rs = dbm.getTables(null, null, ScriptGroup.class.getSimpleName().toLowerCase(), null);
			return rs.next();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
