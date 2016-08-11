package br.com.pilovieira.persistenza.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import br.com.pilovieira.persistenza.annotation.Interfaccia;

@Entity
public class TypeOwnerOnlyInterfacciaAnnotation {
	
	@Id
	int id;
	
	@Interfaccia
	private Type att;

	public Type getAtt() {
		return att;
	}
	
}
