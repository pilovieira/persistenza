package br.com.pilovieira.persistenza.data;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

class PersistenzaGet {
	
	private SessionManager sessionManager;
	
	public PersistenzaGet(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	public <T> List<T> all(final Class<T> clazz) {
		return sessionManager.list(clazz, new Criterion[]{});
	}
	
	public <T> List<T> like(Class<T> clazz, String attribute, String value) {
		return sessionManager.list(clazz, Restrictions.ilike(attribute, "%" + value + "%"));
	}
	
	public <T> List<T> search(Class<T> clazz, String attribute, Object value) {
		return search(clazz, PersistenzaRestrictions.eq(attribute, value));
	}
	
	public <T> List<T> search(Class<T> clazz, Criterion... criterions) {
		return sessionManager.list(clazz, criterions);
	}

	public <T> List<T> between(final Class<T> clazz, final String attribute, final Date lo, final Date hi) {
		return sessionManager.list(clazz, Restrictions.between(attribute, lo, hi));
	}

	public <T> T get(final Class<T> clazz, final int id) {
		List<T> list = sessionManager.list(clazz, Restrictions.idEq(id));
		return list.isEmpty() ? null : list.get(0);
	}

}