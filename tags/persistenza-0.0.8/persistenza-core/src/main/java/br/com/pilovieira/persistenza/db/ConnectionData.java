package br.com.pilovieira.persistenza.db;

public class ConnectionData {
	
	private String url;
	private String username;
	private String password;

	public ConnectionData(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}

}
