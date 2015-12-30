package br.com.pilovieira.persistenza.data;


public class PersistStrategyManager {
	
	public static final PersistStrategyManager INSTANCE = new PersistStrategyManager();
	
	private PersistStrategy strategy = new Yolo();

	public void buff(boolean buff) {
		strategy = buff ? new Buffer() : new Yolo();
	}
	
	public PersistStrategy get() {
		return strategy;
	}

}
