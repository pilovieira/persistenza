package br.com.pilovieira.persistenza.data;

public class PersistStrategyManagerMock extends PersistStrategyManager {
	
	public static void setMock(PersistStrategyManager mock) {
		PersistStrategyManager.instance = mock;
	}

}
