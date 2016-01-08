package br.com.pilovieira.persistenza.data;

import org.hibernate.SessionFactory;

public class SessionManagerMock extends SessionManager {

	public static void setMock(SessionManager mock) {
		SessionManager.instance = mock;
	}
	
	SessionManagerMock(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

}
