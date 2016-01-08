package br.com.pilovieira.persistenza.data;


class PersistenzaSet {
	
	private PersistStrategyManager strategy = PersistStrategyManager.getInstance();
	
	public void pause() {
		strategy.buff(true);
	}
	
	public void play() {
		try {
			strategy.get().apply();
		} finally {
			strategy.buff(false);
		}
	}

	public void rewind() {
		strategy.buff(false);
	}
	
	public void persist(final Object... entities) {
		strategy.get().persist(entities);
	}
	
	public void delete(final Object... entities) {
		strategy.get().delete(entities);
	}

}
