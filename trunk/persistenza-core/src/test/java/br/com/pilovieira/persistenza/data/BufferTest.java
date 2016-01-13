package br.com.pilovieira.persistenza.data;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.pilovieira.persistenza.entity.Cat;
import br.com.pilovieira.persistenza.entity.Dog;

@RunWith(MockitoJUnitRunner.class)
public class BufferTest {
	
	@Mock private SessionFactory sessionFactory;
	@Mock private Session session;
	@Mock private Dog dog;
	@Mock private Cat cat;
	@Mock private Transaction transaction;
	
	private Buffer subject;

	@Before
	public void setup() {
		when(sessionFactory.openSession()).thenReturn(session);
		when(session.getTransaction()).thenReturn(transaction);
		
		subject = new Buffer(new SessionManager(sessionFactory));
	}
	
	@Test
	public void persist() {
		subject.persist(dog);
		
		verify(session, never()).beginTransaction();
		verify(session, never()).saveOrUpdate(dog);
		verify(session, never()).getTransaction();
		verify(transaction, never()).commit();
		
		subject.apply();
		
		verify(session).beginTransaction();
		verify(session).saveOrUpdate(dog);
		verify(session).getTransaction();
		verify(transaction).commit();
	}

	@Test
	public void delete() {
		subject.delete(dog);

		verify(session, never()).beginTransaction();
		verify(session, never()).delete(dog);
		verify(session, never()).getTransaction();
		verify(transaction, never()).commit();

		subject.apply();
		
		verify(session).beginTransaction();
		verify(session).delete(dog);
		verify(session).getTransaction();
		verify(transaction).commit();
	}

	@Test
	public void both() {
		subject.persist(dog);
		subject.delete(cat);

		verify(session, never()).beginTransaction();
		verify(session, never()).saveOrUpdate(dog);
		verify(session, never()).delete(cat);
		verify(session, never()).getTransaction();
		verify(transaction, never()).commit();

		subject.apply();
		
		verify(session).beginTransaction();
		verify(session).saveOrUpdate(dog);
		verify(session).delete(cat);
		verify(session).getTransaction();
		verify(transaction).commit();
	}
	
}
