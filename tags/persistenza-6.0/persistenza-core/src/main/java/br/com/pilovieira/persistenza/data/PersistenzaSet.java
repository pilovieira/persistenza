package br.com.pilovieira.persistenza.data;



class PersistenzaSet {
	
	private SessionManager sessionManager;
	PersistStrategy strategy;
	
	public PersistenzaSet(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
		this.strategy = new Yolo(sessionManager);
	}
	
	public void pause() {
		strategy = new Buffer(sessionManager);
	}
	
	public void play() {
		try {
			strategy.apply();
		} finally {
			strategy = new Yolo(sessionManager);
		}
	}

	public void rewind() {
		strategy = new Yolo(sessionManager);
	}
	
	public void persist(final Object... entities) {
		strategy.persist(entities);
	}
	
	public void delete(final Object... entities) {
		strategy.delete(entities);
	}
	
}
