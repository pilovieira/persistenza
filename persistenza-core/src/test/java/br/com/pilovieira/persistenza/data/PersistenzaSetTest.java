package br.com.pilovieira.persistenza.data;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PersistenzaSetTest {
	
	@Mock private PersistStrategyManager strategyManager;
	@Mock private PersistStrategy strategy;
	
	private PersistenzaSet subject;

	@Before
	public void setup() {
		when(strategyManager.get()).thenReturn(strategy);
		PersistStrategyManagerMock.setMock(strategyManager);

		subject = new PersistenzaSet();
	}
	
	@AfterClass
	public static void tearDown() {
		PersistStrategyManagerMock.clearMock();
	}
	
	@Test
	public void pause() {
		subject.pause();

		verify(strategyManager).buff(true);
	}

	@Test
	public void play() {
		subject.play();
		
		verify(strategyManager).get();
		verify(strategy).apply();
		verify(strategyManager).buff(false);
	}

	@Test
	public void rewind() {
		subject.rewind();

		verify(strategyManager).buff(false);
	}

	@Test
	public void persist() {
		subject.persist("");
		
		verify(strategyManager).get();
		verify(strategy).persist("");
	}
	
	@Test
	public void delete() {
		subject.delete("");
		
		verify(strategyManager).get();
		verify(strategy).delete("");
	}
	
	

}
