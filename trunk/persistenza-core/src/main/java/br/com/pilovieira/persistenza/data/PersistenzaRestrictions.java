package br.com.pilovieira.persistenza.data;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;


public abstract class PersistenzaRestrictions {
	
	public static Criterion eq(String atributo, Object valor) {
		if (valor != null)
			return Restrictions.eq(atributo, valor);
		
		return Restrictions.isNull(atributo);
	}

}
