package br.com.pilovieira.persistenza.entity.att;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TypeImplementer implements Type {

	@Id
	int id;
	
	public void setId(int id) {
		this.id = id;
	}
	
}
