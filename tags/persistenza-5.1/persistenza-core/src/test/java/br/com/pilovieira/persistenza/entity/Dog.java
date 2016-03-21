package br.com.pilovieira.persistenza.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Dog implements Comparable<Dog> {
	
	public static final String ATR_NAME = "name";
	
	@Id
	int id;
	@Column(name = ATR_NAME)
	private String name;
	
	public Dog() {}
	
	public Dog(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(Dog o) {
		return new Integer(id).compareTo(new Integer(o.id));
	}

}
