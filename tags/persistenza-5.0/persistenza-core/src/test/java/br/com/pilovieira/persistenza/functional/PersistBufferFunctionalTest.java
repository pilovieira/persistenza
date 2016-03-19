package br.com.pilovieira.persistenza.functional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.pilovieira.persistenza.data.Persistenza;
import br.com.pilovieira.persistenza.entity.Cat;
import br.com.pilovieira.persistenza.entity.Dog;
import br.com.pilovieira.persistenza.entity.Man;

public class PersistBufferFunctionalTest extends PersistenzaSetFunctionalTest {
	
	@Before
	public void rewind() {
		Persistenza.rewind();
	}
	
	@Test
	public void persistEntity() {
		Persistenza.pause();
		
		Persistenza.persist(new Dog(1, "Doge"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 0, dogs.size());
		
		Persistenza.play();

		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		Dog savedDog = dogs.get(0);
		
		assertEquals("Dog name", "Doge", savedDog.getName());
	}
	
	@Test
	public void persistRewind() {
		Persistenza.pause();
		
		Persistenza.persist(new Dog(1, "Doge"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 0, dogs.size());
		
		Persistenza.rewind();
		Persistenza.play();

		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 0, dogs.size());
	}

	@Test
	public void updateEntity() {
		Persistenza.persist(new Dog(1, "Doge"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		Dog savedDog = dogs.get(0);
		
		Persistenza.pause();
		
		savedDog.setName("HueHue BRBR");
		
		Persistenza.persist(savedDog);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dog name", "Doge", dogs.get(0).getName());
		
		Persistenza.play();
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dog name", "HueHue BRBR", savedDog.getName());
	}

	@Test
	public void updateRewind() {
		Persistenza.persist(new Dog(1, "Doge"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		Dog savedDog = dogs.get(0);
		
		Persistenza.pause();
		
		savedDog.setName("HueHue BRBR");
		
		Persistenza.persist(savedDog);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dog name", "Doge", dogs.get(0).getName());
		
		Persistenza.rewind();
		Persistenza.play();
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dog name", "Doge", dogs.get(0).getName());
	}

	@Test
	public void deleteEntity() {
		Persistenza.persist(new Dog(1, "Doge"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		
		assertEquals("Dogs size", 1, dogs.size());
		
		Dog savedDog = dogs.get(0);

		Persistenza.pause();
		Persistenza.delete(savedDog);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());

		Persistenza.play();
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 0, dogs.size());
	}

	@Test
	public void deleteRewind() {
		Persistenza.persist(new Dog(1, "Doge"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		
		assertEquals("Dogs size", 1, dogs.size());
		
		Dog savedDog = dogs.get(0);
		
		Persistenza.pause();
		Persistenza.delete(savedDog);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		
		Persistenza.rewind();
		Persistenza.play();
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
	}
	
	@Test
	public void persistMultipleEntities() {
		Persistenza.pause();
		
		Persistenza.persist(new Dog(1, "Doge"), new Dog(2, "Wow"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 0, dogs.size());
		
		Persistenza.play();

		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 2, dogs.size());
		assertEquals("Dog name", "Doge", dogs.get(0).getName());
		assertEquals("Dog name", "Wow", dogs.get(1).getName());
	}

	@Test
	public void persistMultipleEntitiesDifferentTypes() {
		Persistenza.pause();
		
		Persistenza.persist(new Dog(1, "Doge"), new Cat(2, "Bart"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 0, dogs.size());
		List<Cat> cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 0, cats.size());

		Persistenza.play();
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		assertEquals("Dog name", "Doge", dogs.get(0).getName());
		cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 1, cats.size());
		assertEquals("Cat name", "Bart", cats.get(0).getName());
	}

	@Test
	public void persistMultipleRelatedEntities() {
		Persistenza.pause();
		
		Dog dog = new Dog(1, "Doge");
		Man man = new Man(2, "Derp", dog);
		Persistenza.persist(man, dog);
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 0, dogs.size());
		List<Man> mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 0, mans.size());

		Persistenza.play();
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		assertEquals("Dog name", "Doge", dogs.get(0).getName());
		
		mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 1, mans.size());
		man = mans.get(0);
		assertEquals("Man name", "Derp", man.getName());
		assertNotNull("Best friend should not be null", man.getFriend());
		assertEquals("Dog ID", dog.getId(), man.getFriend().getId());
	}
	
	@Test
	public void updateMultipleEntitiesDifferentTypes() {
		Persistenza.persist(new Dog(1, "Doge"), new Cat(2, "Bart"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		Dog dog = dogs.get(0);
		assertEquals("Dog name", "Doge",  dogs.get(0).getName());
		
		List<Cat> cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 1, cats.size());
		Cat cat = cats.get(0);
		assertEquals("Cat name", "Bart", cat.getName());
		
		Persistenza.pause();

		dog.setName("Wow");
		cat.setName("Garf");
		
		Persistenza.persist(dog, cat);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 1, cats.size());
		cat = cats.get(0);
		assertEquals("Cat name", "Bart", cat.getName());

		Persistenza.play();
		
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
		Persistenza.persist(man, dog);
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		List<Man> mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 1, mans.size());
		man = mans.get(0);
		assertEquals("Man name", "Derp", man.getName());

		Persistenza.pause();
		
		dog.setName("Wow");
		man.setName("Gusto");

		Persistenza.persist(dog, man);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 1, mans.size());
		man = mans.get(0);
		assertEquals("Man name", "Derp", man.getName());
		
		Persistenza.play();
		
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
		Persistenza.persist(man, dog);
		
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

		Persistenza.pause();

		man.setFriend(null);
		
		Persistenza.persist(man);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 1, mans.size());
		man = mans.get(0);
		assertEquals("Man name", "Derp", man.getName());
		assertNotNull("Best friend should not be null", man.getFriend());
		assertEquals("Dog ID", dog.getId(), man.getFriend().getId());
		
		Persistenza.play();
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		
		mans = Persistenza.all(Man.class);
		assertEquals("Mans size", 1, mans.size());
		Assert.assertNull("Best friend", mans.get(0).getFriend());
	}
	
	@Test
	public void updateEntityWithNewEntity() {
		Persistenza.persist(new Dog(1, "Doge"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		Dog dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());

		Persistenza.pause();

		dog.setName("Wow");
		Cat cat = new Cat(2, "Bart");
		
		Persistenza.persist(dog, cat);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		List<Cat> cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 0, cats.size());
		
		Persistenza.play();
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Wow", dog.getName());
		
		cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 1, cats.size());
		cat = cats.get(0);
		assertEquals("Cat name", "Bart", cat.getName());
	}
	
	@Test
	public void deleteMultipleEntitiesDifferentTypes() {
		Persistenza.persist(new Dog(1, "Doge"), new Cat(2, "Bart"));
		
		List<Dog> dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		Dog dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		List<Cat> cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 1, cats.size());
		Cat cat = cats.get(0);
		assertEquals("Cat name", "Bart", cat.getName());
		
		Persistenza.pause();
		
		Persistenza.delete(dog, cat);
		
		dogs = Persistenza.all(Dog.class);
		assertEquals("Dogs size", 1, dogs.size());
		dog = dogs.get(0);
		assertEquals("Dog name", "Doge", dog.getName());
		
		cats = Persistenza.all(Cat.class);
		assertEquals("Cats size", 1, cats.size());
		cat = cats.get(0);
		assertEquals("Cat name", "Bart", cat.getName());
		
		Persistenza.play();
		
		dogs = Persistenza.all(Dog.class);
		assertTrue("Dogs should be empty", dogs.isEmpty());
		
		cats = Persistenza.all(Cat.class);
		assertTrue("Dogs should be empty", cats.isEmpty());
	}
	
}
