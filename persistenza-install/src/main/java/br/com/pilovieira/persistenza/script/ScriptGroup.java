package br.com.pilovieira.persistenza.script;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ScriptGroup {

	static final String NAME = "name";

	@Id
	@Column(name = NAME)
	private String name;
	
	private int last;
	
	ScriptGroup() {}
	
	public ScriptGroup(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLast() {
		return last;
	}
	
	public void setLast(int last) {
		this.last = last;
	}
	
}
