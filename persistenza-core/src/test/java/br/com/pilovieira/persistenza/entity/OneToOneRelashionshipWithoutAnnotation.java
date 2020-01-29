package br.com.pilovieira.persistenza.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OneToOneRelashionshipWithoutAnnotation {
	
	@Id
	private int id;
	
	private Integer number;
	
	private Dog dog;

	public Dog getDog() {
		return dog;
	}

	public void setDog(Dog dog) {
		this.dog = dog;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

}
