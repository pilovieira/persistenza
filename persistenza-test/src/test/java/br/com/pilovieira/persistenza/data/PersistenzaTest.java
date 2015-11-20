package br.com.pilovieira.persistenza.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.pilovieira.persistenza.entity.Cat;
import br.com.pilovieira.persistenza.entity.Dog;
import br.com.pilovieira.persistenza.entity.Man;
import br.com.pilovieira.persistenza.util.DatabaseSetup;

public class PersistenzaTest {
	
	@BeforeClass
	public static void initialize() {
		DatabaseSetup.initialize(Dog.class, Man.class, Cat.class);
	}
	
	@Before
	public void setup() throws SQLException {
		DatabaseSetup.clear(Man.class, Dog.class, Cat.class);
	}
	
	@Test
	public void insertEntity() {
		Persistenza.insert(new Dog(1, "Doge"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);

		assertEquals("Dogs size", 1, dogs.size());
		Dog savedDog = dogs.get(0);
		
		assertEquals("Dog name", "Doge", savedDog.getName());
	}

	@Test
	public void updateEntity() {
		Persistenza.insert(new Dog(1, "Doge"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		
		assertEquals("Dogs size", 1, dogs.size());
		
		Dog savedDog = dogs.get(0);
		savedDog.setName("HueHue BRBR");
		Persistenza.update(savedDog);
		
		dogs = Persistenza.all(Dog.class);
		
		assertEquals("Dog name", "HueHue BRBR", savedDog.getName());
	}

	@Test
	public void deleteEntity() {
		Persistenza.insert(new Dog(1, "Doge"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		
		assertEquals("Dogs size", 1, dogs.size());
		
		Dog savedDog = dogs.get(0);
		Persistenza.delete(savedDog);
		
		dogs = Persistenza.all(Dog.class);
		
		assertEquals("Dogs size", 0, dogs.size());
	}
	
	@Test
	public void insertMultipleEntities() {
		Persistenza.insert(new Dog(1, "Doge"), new Dog(2, "Wow"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);

		assertEquals("Dogs size", 2, dogs.size());
		assertEquals("Dog name", "Doge", dogs.get(0).getName());
		assertEquals("Dog name", "Wow", dogs.get(1).getName());
	}

	@Test
	public void insertMultipleEntitiesDifferentTypes() {
		Persistenza.insert(new Dog(1, "Doge"), new Cat(2, "Bart"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		
		assertEquals("Dogs size", 1, dogs.size());
		assertEquals("Dog name", "Doge", dogs.get(0).getName());
		
		List<Cat> cats = Persistenza.all(Cat.class);
		assertEquals("Cat name", "Bart", cats.get(0).getName());
	}

	@Test
	public void insertMultipleRelatedEntities() {
		Dog dog = new Dog(1, "Doge");
		Man man = new Man(2, "Derp", dog);
		Persistenza.insert(man, dog);
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		
		assertEquals("Dogs size", 1, dogs.size());
		assertEquals("Dog name", "Doge", dogs.get(0).getName());
		
		List<Man> mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 1, mans.size());
		man = mans.get(0);
		assertEquals("Man name", "Derp", man.getName());
		assertNotNull("Best friend should not be null", man.getFriend());
		assertEquals("Dog ID", dog.getId(), man.getFriend().getId());
	}
	
	@Test
	public void updateMultipleEntitiesDifferentTypes() {
		Persistenza.insert(new Dog(1, "Doge"), new Cat(2, "Bart"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		Dog dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		List<Cat> cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 1, cats.size());
		Cat cat = cats.get(0);
		assertEquals("Cat name", "Bart", cat.getName());
		
		dog.setName("Wow");
		cat.setName("Garf");
		
		Persistenza.update(dog, cat);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Wow", dog.getName());
		
		cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 1, cats.size());
		cat = cats.get(0);
		assertEquals("Cat name", "Garf", cat.getName());
	}

	@Test
	public void updateMultipleRelatedEntities() {
		Dog dog = new Dog(1, "Doge");
		Man man = new Man(2, "Derp", dog);
		Persistenza.insert(man, dog);
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		List<Man> mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 1, mans.size());
		man = mans.get(0);
		assertEquals("Man name", "Derp", man.getName());
		
		dog.setName("Wow");
		man.setName("Gusto");
		
		Persistenza.update(dog, man);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Wow", dog.getName());
		
		mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 1, mans.size());
		man = mans.get(0);
		assertEquals("Man name", "Gusto", man.getName());
	}
	
	@Test
	public void updateMultipleUnlinkEntites() {
		Dog dog = new Dog(1, "Doge");
		Man man = new Man(2, "Derp", dog);
		Persistenza.insert(man, dog);
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		List<Man> mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 1, mans.size());
		man = mans.get(0);
		assertEquals("Man name", "Derp", man.getName());
		assertNotNull("Best friend should not be null", man.getFriend());
		assertEquals("Dog ID", dog.getId(), man.getFriend().getId());
		
		man.setFriend(null);
		
		Persistenza.update(man);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		
		mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 1, mans.size());
		Assert.assertNull("Best friend", mans.get(0).getFriend());
	}
	
	@Test
	public void deleteMultipleEntitiesDifferentTypes() {
		Persistenza.insert(new Dog(1, "Doge"), new Cat(2, "Bart"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		Dog dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		List<Cat> cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 1, cats.size());
		Cat cat = cats.get(0);
		assertEquals("Cat name", "Bart", cat.getName());
		
		Persistenza.delete(dog, cat);
		
		dogs = Persistenza.all(Dog.class);
		assertTrue("Dogs should be empty", dogs.isEmpty());
		
		cats = Persistenza.all(Cat.class);
		assertTrue("Dogs should be empty", cats.isEmpty());
	}

}
