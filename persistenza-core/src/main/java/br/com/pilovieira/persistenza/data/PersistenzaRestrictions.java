package br.com.pilovieira.persistenza.data;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;


public class PersistenzaRestrictions {
	
	public static Criterion eq(String attribute, Object value) {
		if (value != null)
			return Restrictions.eq(attribute, value);
		
		return Restrictions.isNull(attribute);
	}
	
	public static Criterion like(String attribute, String value) {
		return Restrictions.ilike(attribute, "%" + value + "%");
	}

	private PersistenzaRestrictions() {}
}
