package br.com.pilovieira.persistenza.entity.att;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import br.com.pilovieira.persistenza.annotation.Interfaccia;

@Entity
public class TypeOwner {
	
	public static final String ATR_START = "start";
	
	@Id
	int id;
	
	@Transient
	@Interfaccia(name = "mask")
	private Type att;
	
	Integer mask;
	
	public TypeOwner() {}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public TypeOwner(Type att) {
		this.att = att;
	}

	public Type getAtt() {
		return att;
	}
	
}
