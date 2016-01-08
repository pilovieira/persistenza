package br.com.pilovieira.persistenza.data;


class PersistStrategyManager {
	
	protected static PersistStrategyManager instance;
	
	public static PersistStrategyManager getInstance() {
		if (instance == null)
			instance = new PersistStrategyManager();
		return instance;
	}
	
	private PersistStrategy strategy = new Yolo();

	public void buff(boolean buff) {
		strategy = buff ? new Buffer() : new Yolo();
	}
	
	public PersistStrategy get() {
		return strategy;
	}

}
