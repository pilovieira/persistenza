package br.com.pilovieira.persistenza.data;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.google.common.base.Function;

public final class Persistenza {
	
	private static SessionManager sessionManager = SessionManager.getInstance();

	public static void insert(final Object... entities) {
		sessionManager.commit(new Function<Session, Void>() {
			@Override
			public Void apply(Session session) {
				for (Object entity : entities)
					session.save(entity);
				return null;
			}
		});
	}

	public static void update(final Object... entities) {
		sessionManager.commit(new Function<Session, Void>() {
			@Override
			public Void apply(Session session) {
				for (Object entity : entities)
					session.update(entity);
				return null;
			}
		});
	}

	public static void delete(final Object... entities) {
		sessionManager.commit(new Function<Session, Void>() {
			@Override
			public Void apply(Session session) {
				for (Object entity : entities)
					session.delete(entity);
				return null;
			}
		});
	}

	public static <T> List<T> all(final Class<T> clazz) {
		return sessionManager.list(clazz, new Criterion[]{});
	}
	
	public static <T> List<T> like(Class<T> clazz, String attribute, String value) {
		return sessionManager.list(clazz, Restrictions.ilike(attribute, "%" + value + "%"));
	}
	
	public static <T> List<T> search(Class<T> clazz, String attribute, Object value) {
		return search(clazz, PersistenzaRestrictions.eq(attribute, value));
	}
	
	public static <T> List<T> search(Class<T> clazz, Criterion... criterions) {
		return sessionManager.list(clazz, criterions);
	}

	public static <T> List<T> between(final Class<T> clazz, final String attribute, final Date lo, final Date hi) {
		return sessionManager.list(clazz, Restrictions.between(attribute, lo, hi));
	}

	public static <T> T get(final Class<T> clazz, final int id) {
		List<T> list = sessionManager.list(clazz, Restrictions.idEq(id));
		return list.isEmpty() ? null : list.get(0);
	}
	
	public static <T> T singleton(final Class<T> clazz) {
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

	private static <T> T newSingleton(Class<T> clazz) {
		try {
			T instance = clazz.newInstance();
			insert(instance);
			return instance;
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Exception creating new instance.", e);
		}
	}
}
