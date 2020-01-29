package br.com.pilovieira.persistenza.functional;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.pilovieira.persistenza.data.Persistenza;
import br.com.pilovieira.persistenza.entity.TypeImplementer;
import br.com.pilovieira.persistenza.entity.TypeOwner;
import br.com.pilovieira.persistenza.util.DatabaseSetup;
import br.com.pilovieira.persistenza.util.PersistenzaRunner;

@RunWith(PersistenzaRunner.class)
public class InterfacciaFunctionalTest {

	@Before
	public void setup() throws SQLException {
		DatabaseSetup.clear(TypeImplementer.class, TypeOwner.class);
	}
	
	@Test
	public void loadNullAttributeType() {
		TypeImplementer attribute = new TypeImplementer();
		attribute.setId(5);
		
		TypeOwner type = new TypeOwner();
		type.setId(6);
		
		Persistenza.persist(attribute, type);
		
		List<TypeOwner> all = Persistenza.all(TypeOwner.class);
		
		Assert.assertFalse(all.isEmpty());
		Assert.assertNull(all.get(0).getAtt());
	}

	@Test
	public void setAndLoadAttributeType() {
		TypeImplementer attribute = new TypeImplementer();
		attribute.setId(5);
		
		Persistenza.persist(attribute);
		
		TypeOwner type = new TypeOwner(attribute);
		type.setId(6);
		
		Persistenza.persist(type);
		
		type = Persistenza.get(TypeOwner.class, 6);
		
		assertEquals(type.getAtt().getClass(), attribute.getClass());
	}

	@Test
	public void setAndLoadAttributeTypeInSameTransaction() {
		TypeImplementer attribute = new TypeImplementer();
		attribute.setId(5);
		
		TypeOwner type = new TypeOwner(attribute);
		type.setId(6);
		
		Persistenza.persist(attribute, type);
		
		type = Persistenza.get(TypeOwner.class, 6);
		
		assertEquals(type.getAtt().getClass(), attribute.getClass());
	}
	
}
