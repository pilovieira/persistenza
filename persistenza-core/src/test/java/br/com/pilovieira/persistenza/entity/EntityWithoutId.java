package br.com.pilovieira.persistenza.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EntityWithoutId {
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
