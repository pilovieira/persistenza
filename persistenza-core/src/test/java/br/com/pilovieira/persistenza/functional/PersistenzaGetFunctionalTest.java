package br.com.pilovieira.persistenza.functional;

import static br.com.pilovieira.persistenza.entity.Event.ATR_START;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.NOVEMBER;
import static java.util.Calendar.YEAR;
import static java.util.Collections.sort;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import br.com.pilovieira.persistenza.data.Persistenza;
import br.com.pilovieira.persistenza.data.PersistenzaRestrictions;
import br.com.pilovieira.persistenza.entity.Config;
import br.com.pilovieira.persistenza.entity.Dog;
import br.com.pilovieira.persistenza.entity.Event;
import br.com.pilovieira.persistenza.util.DatabaseSetup;
import br.com.pilovieira.persistenza.util.PersistenzaRunner;
import br.com.pilovieira.persistenza.util.Support;

@RunWith(PersistenzaRunner.class)
public class PersistenzaGetFunctionalTest {
	
	@Before
	public void setup() throws SQLException {
		DatabaseSetup.clear(Dog.class, Event.class, Config.class);
		
		Support.createDogs("Doge", "Wow", "Much");
	}
	
	@Test
	public void all() {
		List<Dog> dogs = Persistenza.all(Dog.class);
		
		assertEquals("Dogs size", 3, dogs.size());
	}

	@Test
	public void like() {
		List<Dog> dogs = Persistenza.like(Dog.class, Dog.ATR_NAME, "D");
		
		assertEquals("Dogs size", 1, dogs.size());
		Dog dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
	}
	
	@Test
	public void likeMiddle() {
		List<Dog> dogs = Persistenza.like(Dog.class, Dog.ATR_NAME, "o");
		
		assertEquals("Dogs size", 2, dogs.size());
		sort(dogs);
		Dog dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		dog = dogs.get(1);
		assertEquals("Dog name", "Wow", dog.getName());
	}

	@Test
	public void search() {
		List<Dog> dogs = Persistenza.search(Dog.class, Dog.ATR_NAME, "Doge");
		
		assertEquals("Dogs size", 1, dogs.size());
		Dog dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
	}

	@Test
	public void searchNullAttribute() {
		Persistenza.persist(new Dog(4, null));
		
		List<Dog> dogs = Persistenza.search(Dog.class, Dog.ATR_NAME, null);
		
		assertEquals("Dogs size", 1, dogs.size());
		Dog dog = dogs.get(0);
		assertNull("Name should be null", dog.getName());
	}

	@Test
	public void searchWithCriterion() {
		Persistenza.persist(new Dog(4, null));
		
		Criterion criterion = PersistenzaRestrictions.eq(Dog.ATR_NAME, null);
		List<Dog> dogs = Persistenza.search(Dog.class, criterion);
		
		assertEquals("Dogs size", 1, dogs.size());
		Dog dog = dogs.get(0);
		assertNull("Name should be null", dog.getName());
	}

	@Test
	public void between() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(DAY_OF_MONTH, 21);
		calendar.set(MONTH, NOVEMBER);
		calendar.set(YEAR, 2015);
		
		Persistenza.persist(new Event(4, "Dojo", calendar.getTime()));
		
		Calendar lo = (Calendar)calendar.clone();
		lo.set(DAY_OF_MONTH, 18);
		
		Calendar hi = (Calendar)calendar.clone();
		hi.set(DAY_OF_MONTH, 22);
		
		List<Event> events = Persistenza.between(Event.class, ATR_START, lo.getTime(), hi.getTime());
		
		assertEquals("Dogs size", 1, events.size());
		Event dog = events.get(0);
		assertEquals("Dog name", "Dojo", dog.getName());
	}

	@Test
	public void get() {
		Dog dog = Persistenza.get(Dog.class, 1);
		
		assertNotNull("Dog should not be null", dog);
		assertEquals("Dog name", "Doge", dog.getName());
	}

	@Test
	public void getNoOne() {
		Dog dog = Persistenza.get(Dog.class, 1000);
		
		assertNull("Dog should be null", dog);
	}

	@Test
	public void singletonNonexistent() {
		Config config = Persistenza.singleton(Config.class);
		
		assertNotNull("Dog should not be null", config);
		
		assertEquals("Configs size", 1, Persistenza.all(Config.class).size());
	}

	@Test
	public void singletonExistent() {
		Persistenza.persist(new Config());
		
		Config config = Persistenza.singleton(Config.class);
		
		assertNotNull("Dog should not be null", config);
		assertEquals("Configs size", 1, Persistenza.all(Config.class).size());
	}

}
