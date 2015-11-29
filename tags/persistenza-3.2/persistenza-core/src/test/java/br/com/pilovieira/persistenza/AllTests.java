package br.com.pilovieira.persistenza;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.com.pilovieira.persistenza.data.PersistenzaGetTest;
import br.com.pilovieira.persistenza.data.PersistenzaSetTest;
import br.com.pilovieira.persistenza.data.SessionManagerTest;

@RunWith(Suite.class)
@SuiteClasses(
		{
			PostgreSqlTest.class,
			PersistenzaGetTest.class,
			PersistenzaSetTest.class,
			SessionManagerTest.class
		})
public class AllTests {

}
