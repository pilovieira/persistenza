package br.com.pilovieira.persistenza.data;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

class PersistenzaGet {
	
	private SessionManager sessionManager;
	private InterfacciaGet interfaccia;
	
	public PersistenzaGet(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
		this.interfaccia = new InterfacciaGet(sessionManager);
	}
	
	public <T> List<T> all(final Class<T> clazz) {
		List<T> list = sessionManager.list(clazz, new Criterion[]{});
		return interfaccia.list(list);
	}
	
	public <T> List<T> like(Class<T> clazz, String attribute, String value) {
		List<T> list = sessionManager.list(clazz, Restrictions.ilike(attribute, "%" + value + "%"));
		return interfaccia.list(list);
	}
	
	public <T> List<T> search(Class<T> clazz, String attribute, Object value) {
		return search(clazz, PersistenzaRestrictions.eq(attribute, value));
	}
	
	public <T> List<T> search(Class<T> clazz, Criterion... criterions) {
		List<T> list = sessionManager.list(clazz, criterions);
		return interfaccia.list(list);
	}

	public <T> List<T> between(final Class<T> clazz, final String attribute, final Date lo, final Date hi) {
		List<T> list = sessionManager.list(clazz, Restrictions.between(attribute, lo, hi));
		return interfaccia.list(list);
	}

	public <T> T get(final Class<T> clazz, final int id) {
		List<T> list = sessionManager.list(clazz, Restrictions.idEq(id));
		list = interfaccia.list(list);
		return list.isEmpty() ? null : list.get(0);
	}

}
