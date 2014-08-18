package br.com.pilovieira.commerciale.persistenza;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class Criterio {
	
	protected String atributo;
	protected Object valor;
	
	public static Criterio criterioEq(String atributo, Object valor) {
		Criterio criterio = new Criterio();
		criterio.atributo = atributo;
		criterio.valor = valor;
		return criterio;
	}
	
	public Criterion produce() {
		if (valor != null)
			return Restrictions.eq(atributo, valor);
		
		return Restrictions.isNull(atributo);
	}
}
