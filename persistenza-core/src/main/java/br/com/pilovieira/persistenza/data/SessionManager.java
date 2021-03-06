package br.com.pilovieira.persistenza.data;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;

import br.com.pilovieira.persistenza.PersistenzaHeap;

import com.google.common.base.Function;

class SessionManager {
	
	protected static SessionManager instance;
	
	private SessionFactory sessionFactory;
	
	public SessionManager() {}
	
	SessionManager(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public <T> T execute(Function<Session, T> function){
		Session session = getSessionFactory().openSession();

		try {
			return function.apply(session);
		} finally {
			session.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> list(Class<T> clazz, Criterion... criterions){
		Session session = getSessionFactory().openSession();

		try {
			Criteria criteria = session.createCriteria(clazz).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

			for (Criterion criterion : criterions)
				criteria.add(criterion);

			return (List<T>)criteria.list();
		} finally {
			session.close();
		}
	}
	
	public void commit(Function<Session, Void> function){
		Session session = getSessionFactory().openSession();
		
		try {
			session.beginTransaction();
			function.apply(session);
			session.getTransaction().commit();
		} catch (HibernateException ex) {
			session.getTransaction().rollback();
			throw ex;
		} finally {
			session.close();
		}
	}
	
	public SessionFactory getSessionFactory() {
		if (sessionFactory == null)
			sessionFactory = PersistenzaHeap.getSessionFactory();
		
		if (sessionFactory == null)
			throw new RuntimeException("Session Factory not loaded!");
		
		return sessionFactory;
	}
}
