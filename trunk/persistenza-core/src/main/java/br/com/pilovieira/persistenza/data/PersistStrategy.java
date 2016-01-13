package br.com.pilovieira.persistenza.data;



abstract class PersistStrategy {
	
	protected SessionManager sessionManager = SessionManager.getInstance();
	
	public abstract void persist(final Object... entities);

	public abstract void delete(final Object... entities);

	public abstract void apply();
	
}
