package br.com.pilovieira.persistenza.data;

import org.hibernate.Session;

import com.google.common.base.Function;

class PersistenzaSingleton {
	
	private SessionManager sessionManager;
	private PersistenzaSet perSet;
	
	public PersistenzaSingleton(SessionManager sessionManager, PersistenzaSet perSet) {
		this.perSet = perSet;
		this.sessionManager = sessionManager;
	}

	public <T> T singleton(final Class<T> clazz) {
		return sessionManager.execute(new Function<Session, T>() {
			@Override
			public T apply(Session session) {
				@SuppressWarnings("unchecked")
				T instance = (T) session.createCriteria(clazz).uniqueResult();
				
				if (instance == null)
					instance = newSingleton(clazz);
				
				return instance;
			}
		});
	}

	private <T> T newSingleton(Class<T> clazz) {
		try {
			T instance = clazz.newInstance();
			perSet.persist(instance);
			return instance;
		} catch (InstantiationException | IllegalAccessException ex) {
			throw new RuntimeException(ex);
		}
	}

}
