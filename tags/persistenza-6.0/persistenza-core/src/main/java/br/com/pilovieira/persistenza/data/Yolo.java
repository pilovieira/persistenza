package br.com.pilovieira.persistenza.data;

import java.util.Arrays;

import org.hibernate.Session;

import com.google.common.base.Function;

class Yolo implements PersistStrategy {
	
	private SessionManager sessionManager;
	private InterfacciaSet interfaccia;

	public Yolo(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
		interfaccia = new InterfacciaSet();
	}
	
	public void persist(final Object... entities) {
		sessionManager.commit(new Function<Session, Void>() {
			@Override
			public Void apply(Session session) {
				for (Object entity : entities)
					session.saveOrUpdate(entity);
				
				interfaccia.set(session, Arrays.asList(entities));
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
