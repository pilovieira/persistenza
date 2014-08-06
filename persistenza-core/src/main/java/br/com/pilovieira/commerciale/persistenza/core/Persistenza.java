package br.com.pilovieira.commerciale.persistenza.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class Persistenza {

	private static SessionFactory factory;
	
	public static void setFactory(SessionFactory factory) {
		Persistenza.factory = factory;
	}
	
	private static Session createSession() {
		return factory.openSession();
	}

	public static void insert(Object entidade) {
		Session session = createSession();
		session.getTransaction().begin();
		session.save(entidade);
		session.getTransaction().commit();
		session.close();
	}

	public static void update(Object entidade) {
		Session session = createSession();
		session.getTransaction().begin();
		session.update(entidade);
		session.getTransaction().commit();
		session.close();
	}

	public static void delete(Object entidade) {
		Session session = createSession();
		session.getTransaction().begin();
		session.delete(entidade);
		session.getTransaction().commit();
		session.close();
	}

	public static <T> List<T> all(Class<T> clazz) {
		Session session = createSession();
		List<T> list = (List<T>) session.createCriteria(clazz).list();
		session.close();
		return asSet(list);
	}
	
	public static <T> List<T> like(Class<T> clazz, String atributo, String valor) {
		Session session = createSession();
		Criteria criteria = session.createCriteria(clazz);
		criteria.add(Restrictions.ilike(atributo, valor));
		List<T> list = (List<T>) criteria.list();
		session.close();
		return asSet(list);
	}
	
	public static <T> List<T> search(Class<T> clazz, String atributo, Object valor) {
		return search(clazz, Arrays.asList(Criterio.criterioEq(atributo, valor)));
	}
	
	public static <T> List<T> search(Class<T> clazz, List<Criterio> criterios) {
		Session session = createSession();
		Criteria criteria = session.createCriteria(clazz);
		for (Criterio criterio : criterios)
			criteria.add(criterio.produce());
		List<T> list = (List<T>) criteria.list();
		session.close();
		return asSet(list);
	}

	public static <T> List<T> between(Class<T> clazz, String atributo, Date dataInicio, Date dataFim) {
		Session session = createSession();
		Criteria criteria = session.createCriteria(clazz);
		criteria.add(Restrictions.between(atributo, dataInicio, dataFim));
		List<T> list = (List<T>) criteria.list();
		session.close();
		return asSet(list);
	}

	public static <T> List<T> searchLike(Class<T> clazz, String atributo, String valor) {
		return like(clazz, atributo, "%" + valor + "%");
	}
	
	public static <T> T get(Class<T> clazz, int id) {
		Session session = createSession();
		Criteria criteria = session.createCriteria(clazz);
		criteria.add(Restrictions.idEq(id));
		List<T> list = (List<T>) criteria.list();
		session.close();
		return list.isEmpty() ? null : list.get(0);
	}
	
	public static <T> T singleton(Class<T> clazz) {
		Session session = createSession();
		T singleton = (T) session.createCriteria(clazz).uniqueResult();
		session.close();
		return singleton;
	}
	
	private static <T> List<T> asSet(List<T> list) {
		return new ArrayList<T>(new HashSet<T>(list));
	}
}
