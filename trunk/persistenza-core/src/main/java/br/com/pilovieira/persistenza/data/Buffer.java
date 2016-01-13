package br.com.pilovieira.persistenza.data;

import static java.util.Arrays.asList;

import java.util.LinkedList;
import java.util.List;

import org.hibernate.Session;

import com.google.common.base.Function;

class Buffer implements PersistStrategy {
	
	private List<Object> persistEntities = new LinkedList<>();
	private List<Object> deleteEntities = new LinkedList<>();
	private SessionManager sessionManager;

	public Buffer(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}
	
	@Override
	public void persist(Object... entities) {
		persistEntities.addAll(asList(entities));
	}

	@Override
	public void delete(Object... entities) {
		deleteEntities.addAll(asList(entities));
	}

	@Override
	public void apply() {
		commit();
		clear();
	}

	private void commit() {
		sessionManager.commit(new Function<Session, Void>() {
			@Override
			public Void apply(Session session) {
				for (Object entity : persistEntities)
					session.saveOrUpdate(entity);
				for (Object entity : deleteEntities)
					session.delete(entity);
				return null;
			}
		});
	}
	
	private void clear() {
		persistEntities.clear();
		deleteEntities.clear();
	}
}
