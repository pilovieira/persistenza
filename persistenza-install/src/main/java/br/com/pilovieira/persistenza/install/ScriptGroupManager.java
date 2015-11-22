package br.com.pilovieira.persistenza.install;

import static java.util.Collections.sort;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.com.pilovieira.persistenza.PersistenzaManager;
import br.com.pilovieira.persistenza.data.Persistenza;

public class ScriptGroupManager {
	
	private Map<String, Set<Script>> scripts;
	
	public ScriptGroupManager(Map<String, Set<Script>> scripts) {
		this.scripts = scripts;
	}
	
	public void refreshGroups() {
		for (String groupName : scripts.keySet())
			if (Persistenza.search(ScriptGroup.class, ScriptGroup.NAME, groupName).isEmpty())
				Persistenza.insert(new ScriptGroup(groupName));
	}
	
	public List<String> countScripts() {
		List<String> groupCount = new ArrayList<String>();
		
		for (ScriptGroup group : Persistenza.all(ScriptGroup.class)) {
			int qtd = 0;
			
			Set<Script> set = scripts.get(group.getName());
			if (set == null)
				groupCount.add(String.format("%s | no scripts found", group.getName()));
			else {
				for (Script script : set)
					if (script.getSequence() > group.getLast())
						qtd++;
				
				groupCount.add(String.format("%s | %s scripts", group.getName(), qtd));
			}

		}
		
		return groupCount;
	}

	public void installScripts() {
		Map<ScriptGroup, List<Script>> installScripts = getInstallScripts();
		
		for (ScriptGroup group : installScripts.keySet())
			for (Script script : installScripts.get(group)) {
				execute(script);
				setLast(group, script);
			}
	}

	private Map<ScriptGroup, List<Script>> getInstallScripts() {
		Map<ScriptGroup, List<Script>> map = new HashMap<ScriptGroup, List<Script>>();
		
		for (ScriptGroup group : Persistenza.all(ScriptGroup.class)) {
			List<Script> toInstall = new ArrayList<Script>();
			
			Set<Script> set = scripts.get(group.getName());
			if (set == null)
				continue;
			for (Script script : set)
				if (script.getSequence() > group.getLast())
					toInstall.add(script);
			
			sort(toInstall);
			
			map.put(group, toInstall);
		}
		
		return map;
	}
	
	private void execute(Script script) {
		try {
			Connection connection = PersistenzaManager.getConnection();
	        
	        connection.setAutoCommit(false);
	        Statement statement = connection.createStatement();
	
	        for (String query : script.getQueries()) 
	        	statement.executeUpdate(query);
	        
	        connection.commit();
	    } catch (Exception ex) {
	        throw new RuntimeException(ex);
	    }
	}

	private void setLast(ScriptGroup group, Script script) {
		group.setLast(script.getSequence());
		Persistenza.update(group);
	}

}
