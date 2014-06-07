package br.com.pilovieira.commerciale.persistenza;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class Criterio {
	
	private String atributo;
	private Object valor;
	
	public static Criterio criterioEq(String atributo, Object valor) {
		return new Criterio(atributo, valor);
	}
	
	private Criterio(String atributo, Object valor) {
		this.atributo = atributo;
		this.valor = valor;
	}
	
	public Criterion produce() {
		if (valor != null)
			return Restrictions.eq(atributo, valor);
		
		return Restrictions.isNull(atributo);
	}
}
