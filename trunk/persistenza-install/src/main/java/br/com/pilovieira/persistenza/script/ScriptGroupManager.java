package br.com.pilovieira.persistenza.script;

import static java.util.Collections.sort;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.pilovieira.persistenza.PersistenzaManager;
import br.com.pilovieira.persistenza.data.Persistenza;

public class ScriptGroupManager {
	
	private static final Logger LOGGER = Logger.getLogger(ScriptGroupManager.class);
	
	private Map<String, Set<Script>> scripts;
	private List<String> groupOrder;
	private LinkedHashMap<ScriptGroup, List<Script>> installScripts;
	
	public ScriptGroupManager(Map<String, Set<Script>> scripts) {
		this.scripts = scripts;
		refreshGroups();
	}
	
	public void setGroupOrder(List<String> groupOrder) {
		this.groupOrder = groupOrder;
	}
	
	private void refreshGroups() {
		for (String groupName : scripts.keySet())
			if (Persistenza.search(ScriptGroup.class, ScriptGroup.NAME, groupName).isEmpty())
				Persistenza.persist(new ScriptGroup(groupName));
	}
	
	public List<String> countScripts() {
		List<String> groupCount = new ArrayList<String>();
		
		Map<ScriptGroup, List<Script>> installScripts = getInstallScripts();
		for (ScriptGroup group : installScripts.keySet())
			groupCount.add(String.format("%s | %s scripts", group.getName(), installScripts.get(group).size()));
		
		return groupCount;
	}

	public void installScripts(ScriptLogger logger) {
		Map<ScriptGroup, List<Script>> installScripts = getInstallScripts();
		
		for (ScriptGroup group : installScripts.keySet())
			for (Script script : installScripts.get(group)) {
				execute(script, logger);
				setLast(group, script);
			}
	}
	
	public boolean needInstall() {
		for (List<Script> script : getInstallScripts().values())
			if (!script.isEmpty())
				return true;
		
		return false;
	}

	private LinkedHashMap<ScriptGroup, List<Script>> getInstallScripts() {
		if (installScripts == null)
			this.installScripts = loadInstallScripts();
		return installScripts;
	}
	
	private LinkedHashMap<ScriptGroup, List<Script>> loadInstallScripts() {
		LinkedHashMap<ScriptGroup, List<Script>> map = new LinkedHashMap<ScriptGroup, List<Script>>();
		
		for (ScriptGroup group : getScriptGroups()) {
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
	
	private void execute(Script script, ScriptLogger logger) {
		try {
			Connection connection = PersistenzaManager.getConnection();
	        
	        connection.setAutoCommit(false);
	        Statement statement = connection.createStatement();
	
	        for (String query : script.getQueries()) {
	        	LOGGER.info(query);
	        	logger.log(query);
	        	statement.executeUpdate(query);
	        }
	        
	        connection.commit();
	    } catch (Exception ex) {
	        throw new RuntimeException(ex);
	    }
	}

	private void setLast(ScriptGroup group, Script script) {
		group.setLast(script.getSequence());
		Persistenza.persist(group);
	}
	
	private List<ScriptGroup> getScriptGroups() {
		List<PriorityGroup> priorities = new ArrayList<>();
		List<ScriptGroup> unpriorities = new ArrayList<>();
		
		categorize(priorities, unpriorities);
		
		sort(priorities);
		
		List<ScriptGroup> groups = new ArrayList<>();
		for (PriorityGroup priority : priorities)
			groups.add(priority.getGroup());
		
		groups.addAll(unpriorities);
		
		return groups;
	}

	private void categorize(List<PriorityGroup> priorities, List<ScriptGroup> unpriorities) {
		for (ScriptGroup group : Persistenza.all(ScriptGroup.class)) {
			int index = groupOrder.indexOf(group.getName());
			
			if (index != -1)
				priorities.add(new PriorityGroup(index, group));
			else
				unpriorities.add(group);
		}
	}

}
