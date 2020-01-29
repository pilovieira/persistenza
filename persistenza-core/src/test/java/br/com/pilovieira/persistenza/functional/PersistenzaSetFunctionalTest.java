package br.com.pilovieira.persistenza.functional;

import java.sql.SQLException;

import org.junit.Before;
import org.junit.runner.RunWith;

import br.com.pilovieira.persistenza.entity.Cat;
import br.com.pilovieira.persistenza.entity.Dog;
import br.com.pilovieira.persistenza.entity.Man;
import br.com.pilovieira.persistenza.util.DatabaseSetup;
import br.com.pilovieira.persistenza.util.PersistenzaRunner;

@RunWith(PersistenzaRunner.class)
public abstract class PersistenzaSetFunctionalTest {
	
	@Before
	public void setup() throws SQLException {
		DatabaseSetup.clear(Man.class, Dog.class, Cat.class);
	}
	
	public abstract void persistEntity();

	public abstract void updateEntity();

	public abstract void deleteEntity();
	
	public abstract void persistMultipleEntities();

	public abstract void persistMultipleEntitiesDifferentTypes();

	public abstract void persistMultipleRelatedEntities();
	
	public abstract void updateMultipleEntitiesDifferentTypes();

	public abstract void updateMultipleRelatedEntities();
	
	public abstract void updateMultipleUnlinkEntites();
	
	public abstract void updateEntityWithNewEntity();
	
	public abstract void deleteMultipleEntitiesDifferentTypes();
	
}
