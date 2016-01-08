package br.com.pilovieira.persistenza.data;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PersistStrategyManagerTest {

	private PersistStrategyManager subject;

	@Before
	public void setup() {
		subject = new PersistStrategyManager();
	}
	
	@Test
	public void defaultStrategy() {
		subject = new PersistStrategyManager();
		
		assertEquals("Strategy should be Yolo", Yolo.class, subject.get().getClass());
	}
	
	@Test
	public void withBuff() {
		subject.buff(true);
		
		assertEquals("Strategy should be Buffer", Buffer.class, subject.get().getClass());
	}

	@Test
	public void withoutBuff() {
		subject.buff(false);
		
		assertEquals("Strategy should be Yolo", Yolo.class, subject.get().getClass());
	}

}
