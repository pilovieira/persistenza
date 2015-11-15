package br.com.pilovieira.persistenza.install;

import java.io.InputStream;

public class Script implements Comparable<Script> {
	
	private String group;
	private Integer number;
	private InputStream scriptStream;
	
	public Script(String group, int number, InputStream scriptStream) {
		this.group = group;
		this.number = number;
		this.scriptStream = scriptStream;
	}
	
	public String getGroup() {
		return group;
	}
	
	public Integer getNumber() {
		return number;
	}
	
	public InputStream getScriptStream() {
		return scriptStream;
	}

	@Override
	public int compareTo(Script o) {
		return number.compareTo(o.getNumber());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Script))
			return false;
		
		Script other = (Script) obj;
		
		return group.equals(other.getGroup()) && number.equals(other.number); 
	}
	
	@Override
	public int hashCode() {
		return group.hashCode() * number.hashCode() * 31;
	}

}
