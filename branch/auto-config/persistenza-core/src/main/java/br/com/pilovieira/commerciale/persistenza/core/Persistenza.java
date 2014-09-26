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
	
	private static SessionFactory getFactory() {
		if (factory == null)
			factory = new EssereLauncher().prepareFactory();
		return factory;
	}
	
	private static Session openSession() {
		return getFactory().openSession();
	}

	static void insert(Essere ser) {
		Session session = prepare();
		session.save(ser);
		perform(session);
	}

	static void update(Essere ser) {
		Session session = prepare();
		session.update(ser);
		perform(session);
	}

	static void delete(Essere ser) {
		Session session = prepare();
		session.delete(ser);
		perform(session);
	}

	private static Session prepare() {
		Session session = openSession();
		session.getTransaction().begin();
		return session;
	}

	private static void perform(Session session) {
		session.getTransaction().commit();
		session.close();
	}

	public static <T> List<T> all(Class<T> clazz) {
		Session session = openSession();
		List<T> list = (List<T>) session.createCriteria(clazz).list();
		session.close();
		return asSet(list);
	}
	
	public static <T> List<T> like(Class<T> clazz, String atributo, String valor) {
		Session session = openSession();
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
		Session session = openSession();
		Criteria criteria = session.createCriteria(clazz);
		for (Criterio criterio : criterios)
			criteria.add(criterio.produce());
		List<T> list = (List<T>) criteria.list();
		session.close();
		return asSet(list);
	}

	public static <T> List<T> between(Class<T> clazz, String atributo, Date dataInicio, Date dataFim) {
		Session session = openSession();
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
		Session session = openSession();
		Criteria criteria = session.createCriteria(clazz);
		criteria.add(Restrictions.idEq(id));
		List<T> list = (List<T>) criteria.list();
		session.close();
		return list.isEmpty() ? null : list.get(0);
	}
	
	public static <T> T singleton(Class<T> clazz) {
		Session session = openSession();
		T singleton = (T) session.createCriteria(clazz).uniqueResult();
		session.close();
		return singleton;
	}
	
	private static <T> List<T> asSet(List<T> list) {
		return new ArrayList<T>(new HashSet<T>(list));
	}
}
