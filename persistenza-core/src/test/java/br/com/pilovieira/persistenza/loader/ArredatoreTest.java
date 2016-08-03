package br.com.pilovieira.persistenza.loader;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import javassist.CtClass;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.pilovieira.persistenza.Database;
import br.com.pilovieira.persistenza.OptionalConfigs;
import br.com.pilovieira.persistenza.PersistenzaHeap;
import br.com.pilovieira.persistenza.entity.Dog;

@RunWith(MockitoJUnitRunner.class)
public class ArredatoreTest {

	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Mock private Database database;
	@Mock private OptionalConfigs configs;
	@Mock private SectionImpl section;
	
	private Arredatore subject;

	@Before
	public void setup() {
		PersistenzaHeapMock.setDatabase(database);
		when(database.getConfigs()).thenReturn(configs);
		
		subject = new Arredatore(section);
	}
	
	@Test
	public void notDecorateWhenOff() throws Exception {
		when(configs.isAutoDecorate()).thenReturn(false);
		
		subject.arredate(Dog.class.getName());
		
		verify(database).getConfigs();
		verify(configs).isAutoDecorate();
		verify(section, never()).decorate(any(CtClass.class));
	}
	
	@Test
	public void decorateClass() throws Exception {
		when(configs.isAutoDecorate()).thenReturn(true);
		
		subject.arredate("br.com.pilovieira.persistenza.loader.ArredatoreTest$AwayClass");
		
		verify(database).getConfigs();
		verify(configs).isAutoDecorate();
		verify(section).decorate(any(CtClass.class));
	}

	@Test
	public void runtimeExceptionWhenFailDecorate() throws Exception {
		when(configs.isAutoDecorate()).thenReturn(true);
		doThrow(ClassNotFoundException.class).when(section).decorate(any(CtClass.class));
		
		thrown.expect(RuntimeException.class);
		
		subject.arredate("br.com.pilovieira.persistenza.loader.ClassArredatoreTest$AwayClass");
	}
	
	public static class PersistenzaHeapMock extends PersistenzaHeap {
		public static void setDatabase(Database database) {
			PersistenzaHeap.setDatabase(database);
		}
		
	}
	
	public class SectionImpl implements Section {
		@Override
		public void decorate(CtClass ctClass) throws Exception {}
	}
	
	class AwayClass {}

}
