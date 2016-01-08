package br.com.pilovieira.persistenza.data;

import br.com.pilovieira.persistenza.PersistenzaManager;


abstract class PersistStrategy {
	
	protected SessionManager sessionManager = new SessionManager(PersistenzaManager.getFactory());
	
	public abstract void persist(final Object... entities);

	public abstract void delete(final Object... entities);

	public abstract void apply();
	
}
