package br.com.pilovieira.persistenza;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.pilovieira.persistenza.data.BufferTest;
import br.com.pilovieira.persistenza.data.PersistStrategyManagerTest;
import br.com.pilovieira.persistenza.data.PersistenzaGetTest;
import br.com.pilovieira.persistenza.data.PersistenzaSetTest;
import br.com.pilovieira.persistenza.data.PersistenzaTest;
import br.com.pilovieira.persistenza.data.SessionManagerTest;
import br.com.pilovieira.persistenza.data.YoloTest;
import br.com.pilovieira.persistenza.db.HyperSqlTest;
import br.com.pilovieira.persistenza.db.PostgreSqlTest;

@RunWith(Suite.class)
@SuiteClasses(
		{
			DatabaseTest.class,
			PersistenzaManagerTest.class,
			BufferTest.class,
			PersistenzaGetTest.class,
			PersistenzaSetTest.class,
			PersistenzaTest.class,
			PersistStrategyManagerTest.class,
			SessionManagerTest.class,
			YoloTest.class,
			HyperSqlTest.class,
			PostgreSqlTest.class
		})
public class AllTests {

}
