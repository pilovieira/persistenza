package br.com.pilovieira.persistenza.data;

interface PersistStrategy {
	
	void persist(final Object... entities);

	void delete(final Object... entities);

	void apply();
	
}
