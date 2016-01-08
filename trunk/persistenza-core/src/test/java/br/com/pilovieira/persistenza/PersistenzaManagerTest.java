package br.com.pilovieira.persistenza;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PersistenzaManagerTest {
	
	@Rule public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void errorWithNullDatabase() {
		thrown.expect(RuntimeException.class);
		thrown.expectMessage("Database not loaded.");
		
		PersistenzaManager.setDatabase(null);
	}

}
