package br.com.pilovieira.persistenza.util;

public class DatabaseWithoutSslFactoryFake extends DatabaseFake {

	public DatabaseWithoutSslFactoryFake(String url, String username, String password) {
		super(url, username, password);
	}

	@Override
	protected String getSslFactory() {
		return null;
	}

}
