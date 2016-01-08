package br.com.pilovieira.persistenza.data;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PersistenzaSingletonTest {

	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private PersistenzaSet perSet;
	@Mock
	private SessionFactory sessionFactory;
	
	private PersistenzaSingleton subject;

	@Mock
	private Session session;

	@Mock
	private Criteria criteria;

	@Before
	public void setup() {
		when(sessionFactory.openSession()).thenReturn(session);
		when(session.createCriteria(Matchers.any(Class.class))).thenReturn(criteria);
		
		subject = new PersistenzaSingleton(perSet, new SessionManager(sessionFactory));
	}
	
	@Test
	public void singletonWithUnassecibleClass() {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("java.lang.IllegalAccessException: Class br.com.pilovieira.persistenza.data.PersistenzaSingleton can not access a member of class br.com.pilovieira.persistenza.data.UnnacessibleClass with modifiers \"private\"");
		
		subject.singleton(UnnacessibleClass.class);
	}

	@Test
	public void persistedSingleton() {
		String sing = new String();
		when(criteria.uniqueResult()).thenReturn(sing);
		
		String singleton = subject.singleton(String.class);
		
		assertEquals("Persisted Singleton", sing, singleton);
	}

	@Test
	public void newSingleton() {
		when(criteria.uniqueResult()).thenReturn(null);
		
		String singleton = subject.singleton(String.class);
		
		assertEquals("New Singleton", new String(), singleton);
	}
	
}
