package br.com.pilovieira.persistenza;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.pilovieira.persistenza.data.BufferTest;
import br.com.pilovieira.persistenza.data.PersistenzaGetTest;
import br.com.pilovieira.persistenza.data.PersistenzaRestrictionsTest;
import br.com.pilovieira.persistenza.data.PersistenzaSetTest;
import br.com.pilovieira.persistenza.data.PersistenzaSingletonTest;
import br.com.pilovieira.persistenza.data.SessionManagerTest;
import br.com.pilovieira.persistenza.data.YoloTest;
import br.com.pilovieira.persistenza.db.HyperSqlTest;
import br.com.pilovieira.persistenza.db.PostgreSqlTest;
import br.com.pilovieira.persistenza.functional.InterfacciaFunctionalTest;
import br.com.pilovieira.persistenza.functional.PersistBufferFunctionalTest;
import br.com.pilovieira.persistenza.functional.PersistYoloFunctionalTest;
import br.com.pilovieira.persistenza.functional.PersistenzaGetFunctionalTest;

@RunWith(Suite.class)
@SuiteClasses(
		{
			DatabaseTest.class,
			PersistenzaManagerTest.class,
			
			BufferTest.class,
			PersistenzaGetTest.class,
			PersistenzaRestrictionsTest.class,
			PersistenzaSetTest.class,
			PersistenzaSingletonTest.class,
			SessionManagerTest.class,
			YoloTest.class,
			
			HyperSqlTest.class,
			PostgreSqlTest.class,
			
			InterfacciaFunctionalTest.class,
			PersistenzaGetFunctionalTest.class,
			PersistYoloFunctionalTest.class,
			PersistBufferFunctionalTest.class
		})
public class AllTests {

}
