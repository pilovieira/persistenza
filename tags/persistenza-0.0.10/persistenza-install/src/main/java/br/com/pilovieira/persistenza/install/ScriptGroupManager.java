package br.com.pilovieira.persistenza.install;

import static java.util.Collections.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
			
			for (Script script : scripts.get(group.getName()))
				if (script.getNumber() > group.getLast())
					qtd++;

			groupCount.add(String.format("%s | %s scripts", group.getName(), qtd));
		}
		
		return groupCount;
	}

	public Map<ScriptGroup, List<Script>> getInstallScripts() {
		Map<ScriptGroup, List<Script>> map = new HashMap<ScriptGroup, List<Script>>();
		
		for (ScriptGroup group : Persistenza.all(ScriptGroup.class)) {
			List<Script> toInstall = new ArrayList<Script>();
			
			for (Script script : scripts.get(group.getName()))
				if (script.getNumber() > group.getLast())
					toInstall.add(script);
			
			sort(toInstall);
			
			map.put(group, toInstall);
		}
		
		return map;
	}
	
	public void setLast(ScriptGroup group, Script script) {
		group.setLast(script.getNumber());
		Persistenza.update(group);
	}

}
