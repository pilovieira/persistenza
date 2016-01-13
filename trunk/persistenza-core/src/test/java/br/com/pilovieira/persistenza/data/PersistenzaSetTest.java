package br.com.pilovieira.persistenza.data;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.pilovieira.persistenza.entity.Dog;

import com.google.common.base.Function;

@SuppressWarnings("unchecked")
@RunWith(MockitoJUnitRunner.class)
public class PersistenzaSetTest {
	
	@Mock private SessionManager sessionManager;
	
	private PersistenzaSet subject;

	@Before
	public void setup() {
		subject = new PersistenzaSet(sessionManager);
	}
	
	@Test
	public void defaultStrategy() {
		assertEquals("Strategy should be Yolo", Yolo.class, subject.strategy.getClass());
	}
	
	@Test
	public void pause() {
		subject.pause();
		
		assertEquals("Strategy should be Buffer", Buffer.class, subject.strategy.getClass());
	}

	@Test
	public void play() {
		subject.pause();
		assertEquals("Strategy should be Buffer", Buffer.class, subject.strategy.getClass());
		
		subject.persist(new Dog());
		verify(sessionManager, never()).commit(Matchers.any(Function.class));
		
		subject.play();

		verify(sessionManager).commit(Matchers.any(Function.class));
		assertEquals("Strategy should be Yolo", Yolo.class, subject.strategy.getClass());
	}

	@Test
	public void rewind() {
		subject.pause();

		assertEquals("Strategy should be Buffer", Buffer.class, subject.strategy.getClass());
		
		subject.rewind();

		assertEquals("Strategy should be Yolo", Yolo.class, subject.strategy.getClass());
	}

	@Test
	public void persist() {
		subject.persist(new Dog());
		
		verify(sessionManager).commit(Matchers.any(Function.class));
	}
	
	@Test
	public void delete() {
		subject.delete(new Dog());
		
		verify(sessionManager).commit(Matchers.any(Function.class));
	}
	
	

}
