package br.com.pilovieira.persistenza.entity;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class OneToManySetRelashionship {
	
	@Id
	private int id;
	
	@OneToMany
	private Set<Dog> dogs;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Set<Dog> getDogs() {
		return dogs;
	}

	public void setDogs(Set<Dog> dogs) {
		this.dogs = dogs;
	}

}
