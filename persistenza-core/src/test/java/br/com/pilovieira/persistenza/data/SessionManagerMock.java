package br.com.pilovieira.persistenza.data;

import org.hibernate.SessionFactory;

public class SessionManagerMock extends SessionManager {

	public static void setMock(SessionManager mock) {
		SessionManager.instance = mock;
	}

	public static void clearMock() {
		setMock(null);
	}
	
	SessionManagerMock(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

}
