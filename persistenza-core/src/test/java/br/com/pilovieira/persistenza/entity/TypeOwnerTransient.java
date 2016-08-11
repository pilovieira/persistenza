package br.com.pilovieira.persistenza.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
public class TypeOwnerTransient {
	
	@Id
	int id;
	
	@Transient
	private Type att;

	public Type getAtt() {
		return att;
	}
	
}
