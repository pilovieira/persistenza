package br.com.pilovieira.persistenza.data;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.google.common.base.Function;

@SuppressWarnings("unchecked")
public class Persistenza {
	
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
	
	public static <T> List<T> like(final Class<T> clazz, final String atributo, final String valor) {
		return sessionManager.list(clazz, Restrictions.ilike(atributo, valor));
	}

	public static <T> List<T> likeString(Class<T> clazz, String atributo, String valor) {
		return like(clazz, atributo, "%" + valor + "%");
	}
	
	public static <T> List<T> search(Class<T> clazz, String atributo, Object valor) {
		return search(clazz, PersistenzaRestrictions.eq(atributo, valor));
	}
	
	public static <T> List<T> search(Class<T> clazz, Criterion... criterions) {
		return sessionManager.list(clazz, criterions);
	}

	public static <T> List<T> between(final Class<T> clazz, final String atributo, final Date dataInicio, final Date dataFim) {
		return sessionManager.list(clazz, Restrictions.between(atributo, dataInicio, dataFim));
	}

	public static <T> T get(final Class<T> clazz, final int id) {
		List<T> list = sessionManager.list(clazz, Restrictions.idEq(id));
		return list.isEmpty() ? null : list.get(0);
	}
	
	public static <T> T unique(final Class<T> clazz) {
		return sessionManager.execute(new Function<Session, T>() {
			@Override
			public T apply(Session session) {
				return (T) session.createCriteria(clazz).uniqueResult();
			}
		});
	}

	public static <T> T singleton(Class<T> clazz) {
		T saved = unique(clazz);
		
		if (saved == null)
			newSingleton(clazz);
		
		return unique(clazz);
	}

	private static <T> void newSingleton(Class<T> clazz) {
		try {
			insert(clazz.newInstance());
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Exception creating new instance.", e);
		}
	}
}
