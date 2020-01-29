package br.com.pilovieira.persistenza.loader;

import br.com.pilovieira.persistenza.Database;
import br.com.pilovieira.persistenza.PersistenzaHeap;

public class PersistenzaHeapMock extends PersistenzaHeap {
	
	public static void setDatabase(Database database) {
		PersistenzaHeap.setDatabase(database);
	}
	
}
