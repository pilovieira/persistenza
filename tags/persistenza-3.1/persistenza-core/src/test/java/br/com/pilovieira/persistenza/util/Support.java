package br.com.pilovieira.persistenza.util;

import br.com.pilovieira.persistenza.data.Persistenza;
import br.com.pilovieira.persistenza.entity.Dog;

public class Support {

	public static void createDogs(String... names) {
		for (int i = 0; i < names.length ; i++)
			Persistenza.insert(new Dog(i + 1, names[i]));
	}
	
}
