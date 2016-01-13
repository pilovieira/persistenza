package br.com.pilovieira.persistenza.data;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.pilovieira.persistenza.entity.Cat;
import br.com.pilovieira.persistenza.entity.Dog;

@RunWith(MockitoJUnitRunner.class)
public class YoloTest {
	
	@Mock private SessionFactory sessionFactory;
	@Mock private Session session;
	@Mock private Dog dog;
	@Mock private Cat cat;
	@Mock private Transaction transaction;
	
	private Yolo subject;

	@Before
	public void setup() {
		SessionManagerMock.setMock(new SessionManager(sessionFactory));
		when(sessionFactory.openSession()).thenReturn(session);
		when(session.getTransaction()).thenReturn(transaction);
		
		subject = new Yolo();
	}
	
	@AfterClass
	public static void tearDown() {
		SessionManagerMock.clearMock();
	}
	
	@Test
	public void persist() {
		subject.persist(dog);
		
		verify(session).beginTransaction();
		verify(session).saveOrUpdate(dog);
		verify(session).getTransaction();
		verify(transaction).commit();
	}

	@Test
	public void delete() {
		subject.delete(dog);
		
		verify(session).beginTransaction();
		verify(session).delete(dog);
		verify(session).getTransaction();
		verify(transaction).commit();
	}

}
