package br.com.pilovieira.commerciale.persistenza.core;

import static br.com.pilovieira.commerciale.persistenza.core.Persistenza.delete;
import static br.com.pilovieira.commerciale.persistenza.core.Persistenza.insert;
import static br.com.pilovieira.commerciale.persistenza.core.Persistenza.update;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public abstract class Essere {

	@Id
	@GeneratedValue
	private int id;
	
	public int getId() {
		return id;
	}
	
	public void save() {
		if (id == 0)
			insert(this);
		else
			update(this);
	}
	
	public void fork() {
		delete(this);
	}
}
