package br.com.pilovieira.persistenza.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.pilovieira.persistenza.annotation.Interfaccia;
import br.com.pilovieira.persistenza.data.Persistenza;
import br.com.pilovieira.persistenza.entity.EntityWithIdWithoutAnnotation;
import br.com.pilovieira.persistenza.entity.EntityWithoutId;
import br.com.pilovieira.persistenza.entity.ManyToOneRelashionship;
import br.com.pilovieira.persistenza.entity.OneToOneRelashionshipWithoutAnnotation;
import br.com.pilovieira.persistenza.entity.TypeOwnerOnlyInterfacciaAnnotation;
import br.com.pilovieira.persistenza.entity.TypeOwnerTransient;
import br.com.pilovieira.persistenza.entity.TypeOwnerWithoutAnnotation;
import br.com.pilovieira.persistenza.util.DatabaseSetup;
import br.com.pilovieira.persistenza.util.PersistenzaRunner;

@RunWith(PersistenzaRunner.class)
public class ArredatoreFunctionalTest {
	
	@Before
	public void setup() throws SQLException {
		DatabaseSetup.clear(EntityWithoutId.class);
	}
	
	@Test
	public void persistEntityWithIdDecorated() throws Exception {
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

	@Test
	public void registerEntityDecoratingId() throws Exception {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(EntityWithoutId.class.getName());
		
		Field idField = clazz.getDeclaredField("id");
		assertNotNull(idField);
		
		Annotation[] fieldAnnotations = idField.getDeclaredAnnotations();
		assertEquals("Annotations quantity", 1, fieldAnnotations.length);
		assertEquals(Id.class.getName(), fieldAnnotations[0].annotationType().getName());
	}

	@Test
	public void registerEntityAddingIdAnnotation() throws Exception {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(EntityWithIdWithoutAnnotation.class.getName());
		
		Field idField = clazz.getDeclaredField("id");
		assertNotNull(idField);
		
		Annotation[] fieldAnnotations = idField.getDeclaredAnnotations();
		assertEquals("Annotations quantity", 1, fieldAnnotations.length);
		assertEquals(Id.class.getName(), fieldAnnotations[0].annotationType().getName());
	}
	
	@Test
	public void registerEntityDecoratingInterfacciaAnnotations() throws Exception {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(TypeOwnerWithoutAnnotation.class.getName());
		
		Field interfaceField = clazz.getDeclaredField("att");
		assertNotNull(interfaceField);
		
		Annotation[] fieldAnnotations = interfaceField.getDeclaredAnnotations();
		assertEquals("Annotations quantity", 2, fieldAnnotations.length);
		assertEquals(Transient.class.getName(), fieldAnnotations[0].annotationType().getName());
		assertEquals(Interfaccia.class.getName(), fieldAnnotations[1].annotationType().getName());
	}

	@Test
	public void registerEntityDecoratingWithTransientWhenInterfaccia() throws Exception {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(TypeOwnerOnlyInterfacciaAnnotation.class.getName());
		
		Field interfaceField = clazz.getDeclaredField("att");
		assertNotNull(interfaceField);
		
		Annotation[] fieldAnnotations = interfaceField.getDeclaredAnnotations();
		assertEquals("Annotations quantity", 2, fieldAnnotations.length);
		assertEquals(Interfaccia.class.getName(), fieldAnnotations[0].annotationType().getName());
		assertEquals(Transient.class.getName(), fieldAnnotations[1].annotationType().getName());
	}

	@Test
	public void registerEntityIgnoringInterfacciaAnnotationsWhenTransientIsExplicitDeclared() throws Exception {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(TypeOwnerTransient.class.getName());
		
		Field field = clazz.getDeclaredField("att");
		assertNotNull(field);
		
		Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
		assertEquals("Annotations quantity", 1, fieldAnnotations.length);
		assertEquals(Transient.class.getName(), fieldAnnotations[0].annotationType().getName());
	}

	@Test
	public void registerEntityDecoratingOneToOneField() throws Exception {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(OneToOneRelashionshipWithoutAnnotation.class.getName());
		
		Field dogField = clazz.getDeclaredField("dog");
		assertNotNull(dogField);
		
		Annotation[] fieldAnnotations = dogField.getDeclaredAnnotations();
		assertEquals("Annotations quantity", 1, fieldAnnotations.length);
		assertEquals(OneToOne.class.getName(), fieldAnnotations[0].annotationType().getName());
	}

	@Test
	public void registerEntityWithManyToOneFieldWithoutDecorate() throws Exception {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(ManyToOneRelashionship.class.getName());
		
		Field dogField = clazz.getDeclaredField("dog");
		assertNotNull(dogField);
		
		Annotation[] fieldAnnotations = dogField.getDeclaredAnnotations();
		assertEquals("Annotations quantity", 1, fieldAnnotations.length);
		assertEquals(ManyToOne.class.getName(), fieldAnnotations[0].annotationType().getName());
	}

}
