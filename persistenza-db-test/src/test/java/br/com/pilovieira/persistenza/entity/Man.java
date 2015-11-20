package br.com.pilovieira.persistenza.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Man {
	
	@Id
	int id;
	private String name;
	@OneToOne
	private Dog friend;
	
	public Man() {}
	
	public Man(int id, String name, Dog friend) {
		this.id = id;
		this.name = name;
		this.friend = friend;
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
	
	public Dog getFriend() {
		return friend;
	}
	
	public void setFriend(Dog friend) {
		this.friend = friend;
	}

}
