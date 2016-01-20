package br.com.pilovieira.persistenza;

import java.util.List;

public class Script implements Comparable<Script> {
	
	private String group;
	private Integer sequence;
	private List<String> queries;
	
	public Script(String group, int sequence, List<String> queries) {
		this.group = group;
		this.sequence = sequence;
		this.queries = queries;
	}
	
	public String getGroup() {
		return group;
	}
	
	public Integer getSequence() {
		return sequence;
	}
	
	public List<String> getQueries() {
		return queries;
	}

	@Override
	public int compareTo(Script o) {
		return sequence.compareTo(o.getSequence());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Script))
			return false;
		
		Script other = (Script) obj;
		
		return group.equals(other.getGroup()) && sequence.equals(other.sequence); 
	}
	
	@Override
	public int hashCode() {
		return group.hashCode() * sequence.hashCode() * 31;
	}

}
