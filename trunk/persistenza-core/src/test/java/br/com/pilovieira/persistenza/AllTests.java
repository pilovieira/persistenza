package br.com.pilovieira.persistenza;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.pilovieira.persistenza.data.BufferTest;
import br.com.pilovieira.persistenza.data.PersistStrategyManagerTest;
import br.com.pilovieira.persistenza.data.PersistenzaSingletonTest;
import br.com.pilovieira.persistenza.data.PersistenzaSetTest;
import br.com.pilovieira.persistenza.data.SessionManagerTest;
import br.com.pilovieira.persistenza.data.YoloTest;
import br.com.pilovieira.persistenza.db.HyperSqlTest;
import br.com.pilovieira.persistenza.db.PostgreSqlTest;
import br.com.pilovieira.persistenza.functional.PersistenzaGetFunctionalTest;
import br.com.pilovieira.persistenza.functional.PersistYoloFunctionalTest;

@RunWith(Suite.class)
@SuiteClasses(
		{
			DatabaseTest.class,
			PersistenzaManagerTest.class,
			BufferTest.class,
			PersistenzaGetFunctionalTest.class,
			PersistenzaSetTest.class,
			PersistenzaSingletonTest.class,
			PersistYoloFunctionalTest.class,
			PersistStrategyManagerTest.class,
			SessionManagerTest.class,
			YoloTest.class,
			HyperSqlTest.class,
			PostgreSqlTest.class
		})
public class AllTests {

}
