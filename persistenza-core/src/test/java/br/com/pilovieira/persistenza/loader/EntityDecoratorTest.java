package br.com.pilovieira.persistenza.loader;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.pilovieira.persistenza.Database;
import br.com.pilovieira.persistenza.OptionalConfigs;
import br.com.pilovieira.persistenza.entity.Dog;

@RunWith(MockitoJUnitRunner.class)
public class EntityDecoratorTest {

	@Mock private Database database;
	@Mock private OptionalConfigs configs;
	
	private EntityDecorator subject;


	@Before
	public void setup() {
		PersistenzaHeapMock.setDatabase(database);
		when(database.getConfigs()).thenReturn(configs);
		
		subject = new EntityDecorator();
	}
	
	@Test
	public void notDecorateWhenOff() {
		when(configs.isAutoDecorate()).thenReturn(false);
		
		subject.decorateAndLoad(Dog.class.getName());
		
		verify(database).getConfigs();
		verify(configs).isAutoDecorate();
	}
	
	@Test
	public void decorateClass() {
		when(configs.isAutoDecorate()).thenReturn(true);
		
		subject.decorateAndLoad("br.com.pilovieira.persistenza.loader.EntityDecoratorTest$AwayClass");
		
		verify(database).getConfigs();
		verify(configs).isAutoDecorate();
	}
	
	private class AwayClass {
		
	}

}
