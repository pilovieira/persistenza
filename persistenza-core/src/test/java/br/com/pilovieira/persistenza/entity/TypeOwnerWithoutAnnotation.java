package br.com.pilovieira.persistenza.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TypeOwnerWithoutAnnotation {
	
	@Id
	int id;
	
	private Type att;

	public Type getAtt() {
		return att;
	}
	
}
