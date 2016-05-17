package br.com.pilovieira.persistenza.data;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.base.Function;

@RunWith(MockitoJUnitRunner.class)
public class SessionManagerTest {
	
	@Rule public ExpectedException thrown = ExpectedException.none();

	@Mock private Function<org.hibernate.Session, Void> commitFunction;
	@Mock private SessionFactory sessionFactory;
	@Mock private Session session;
	@Mock private Transaction transaction;
	@Mock private Function<org.hibernate.Session, Object> executeFunction;
	@Mock private Criterion criterion;
	@Mock private Criteria criteria;
	
	private SessionManager subject;


	@Before
	public void setup() {
		when(sessionFactory.openSession()).thenReturn(session);
		
		subject = new SessionManager(sessionFactory);
	}
	
	@Test
	public void commitFunction() {
		when(session.getTransaction()).thenReturn(transaction);

		subject.commit(commitFunction);
		
		InOrder order = inOrder(sessionFactory, session, commitFunction, transaction);
		order.verify(sessionFactory).openSession();
		order.verify(session).beginTransaction();
		order.verify(commitFunction).apply(session);
		order.verify(session).getTransaction();
		order.verify(transaction).commit();
		order.verify(session).close();
	}
	
	@Test
	public void rollbackTransactionInException() {
		when(session.getTransaction()).thenReturn(transaction);
		doThrow(HibernateException.class).when(transaction).commit();

		try {
			subject.commit(commitFunction);
			fail("Expected HibernateException, but none was thrown.");
		} catch (Exception e) {
			assertEquals("Exception Class", HibernateException.class, e.getClass());
		}
		
		InOrder order = inOrder(sessionFactory, session, commitFunction, transaction);
		order.verify(sessionFactory).openSession();
		order.verify(session).beginTransaction();
		order.verify(commitFunction).apply(session);
		order.verify(session).getTransaction();
		order.verify(transaction).commit();
		order.verify(transaction).rollback();
		order.verify(session).close();
	}

	@Test
	public void executeFunction() {
		subject.execute(executeFunction);
		
		InOrder order = inOrder(sessionFactory, session, executeFunction);
		order.verify(sessionFactory).openSession();
		order.verify(executeFunction).apply(session);
		order.verify(session).close();
	}
	
	@Test
	public void closeSessionWhenThrowExceptionInExecuteFunction() {
		doThrow(HibernateException.class).when(executeFunction).apply(session);
		
		try {
			subject.execute(executeFunction);
			fail("Expected HibernateException, but none was thrown.");
		} catch (Exception e) {
			assertEquals("Exception Class", HibernateException.class, e.getClass());
		}
		
		InOrder order = inOrder(sessionFactory, session, executeFunction);
		order.verify(sessionFactory).openSession();
		order.verify(executeFunction).apply(session);
		order.verify(session).close();
	}
	
	@Test
	public void listObjects() {
		when(session.createCriteria(Class.class)).thenReturn(criteria);
		when(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)).thenReturn(criteria);
		
		subject.list(Class.class, criterion);
		
		InOrder order = inOrder(sessionFactory, session, criteria);
		order.verify(sessionFactory).openSession();
		order.verify(session).createCriteria(Class.class);
		order.verify(criteria).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		order.verify(criteria).add(criterion);
		order.verify(criteria).list();
		order.verify(session).close();
	}
	
	@Test
	public void closeSessionWhenThrowExceptionInList() {
		when(session.createCriteria(Class.class)).thenReturn(criteria);
		when(criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)).thenReturn(criteria);
		doThrow(HibernateException.class).when(criteria).list();
		
		try {
			subject.list(Class.class, criterion);
			fail("Expected HibernateException, but none was thrown.");
		} catch (Exception e) {
			assertEquals("Exception Class", HibernateException.class, e.getClass());
		}
		
		InOrder order = inOrder(sessionFactory, session, criteria);
		order.verify(sessionFactory).openSession();
		order.verify(session).createCriteria(Class.class);
		order.verify(criteria).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		order.verify(criteria).add(criterion);
		order.verify(criteria).list();
		order.verify(session).close();
	}
	
	@Test
	public void errorSessionFactoryNotLoaded() {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Session Factory not loaded!");
		
		SessionManager sessionManager = new SessionManager();
		sessionManager.getSessionFactory();
	}
}
