package br.com.pilovieira.persistenza.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OneToManyListRelashionshipWithoutAnnotation {
	
	@Id
	private int id;
	
	private List<Dog> dogs;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Dog> getDogs() {
		return dogs;
	}

	public void setDogs(List<Dog> dogs) {
		this.dogs = dogs;
	}

}
