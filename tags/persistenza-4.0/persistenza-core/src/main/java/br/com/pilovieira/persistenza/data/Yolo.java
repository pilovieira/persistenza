package br.com.pilovieira.persistenza.data;

import org.hibernate.Session;

import com.google.common.base.Function;

class Yolo implements PersistStrategy {
	
	private SessionManager sessionManager;

	public Yolo(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	public void persist(final Object... entities) {
		sessionManager.commit(new Function<Session, Void>() {
			@Override
			public Void apply(Session session) {
				for (Object entity : entities)
					session.saveOrUpdate(entity);
				return null;
			}
		});
	}

	public void delete(final Object... entities) {
		sessionManager.commit(new Function<Session, Void>() {
			@Override
			public Void apply(Session session) {
				for (Object entity : entities)
					session.delete(entity);
				return null;
			}
		});
	}

	@Override
	public void apply() {}

}
