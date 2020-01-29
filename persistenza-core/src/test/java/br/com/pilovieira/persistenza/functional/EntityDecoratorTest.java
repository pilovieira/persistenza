package br.com.pilovieira.persistenza.functional;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.pilovieira.persistenza.data.Persistenza;
import br.com.pilovieira.persistenza.entity.EntityWithoutId;
import br.com.pilovieira.persistenza.util.DatabaseSetup;

public class EntityDecoratorTest {
	
	@BeforeClass
	public static void initialize() {
		DatabaseSetup.initialize();
	}
	
//	@Before
//	public void setup() throws SQLException {
//		DatabaseSetup.clear(EntityWithoutId.class);
//	}
	
	@Test
	public void registerEntityWithoutId() throws Exception {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(EntityWithoutId.class.getName());
		
		Object entity = clazz.newInstance();
		Method idSetter = MethodUtils.getAccessibleMethod(clazz, "setId", int.class);
		idSetter.invoke(entity, 1);
		Method nameSetter = MethodUtils.getAccessibleMethod(clazz, "setName", String.class);
		nameSetter.invoke(entity, "Joseph");
		
		Persistenza.persist(entity);
		
		List<?> entities = Persistenza.all(EntityWithoutId.class);

		assertEquals("Persisted entities size", 1, entities.size());
		Object savedEntity = entities.get(0);
		
		Method idGetter = MethodUtils.getAccessibleMethod(clazz, "getId");
		Object idValue = idGetter.invoke(savedEntity);
		assertEquals("Id Value", 1, idValue);

		Method nameGetter = MethodUtils.getAccessibleMethod(clazz, "getName");
		Object nameValue = nameGetter.invoke(savedEntity);
		assertEquals("Name Value", "Joseph", nameValue);
	}

}
