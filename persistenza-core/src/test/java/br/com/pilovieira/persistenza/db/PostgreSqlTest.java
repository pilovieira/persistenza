package br.com.pilovieira.persistenza.db;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostgreSqlTest {
	
	private static final String URL = "url";
	private static final String USER = "user";
	private static final String PASS = "pass";

	@Mock private ConnectionData connectionData;
	
	private PostgreSql subject;
	private Properties properties;

	@Before
	public void setup() {
		when(connectionData.getUrl()).thenReturn(URL);
		when(connectionData.getUsername()).thenReturn(USER);
		when(connectionData.getPassword()).thenReturn(PASS);
		
		properties = System.getProperties();
		System.setProperties(new Properties());
		
		subject = new PostgreSql(connectionData);
	}
	
	@After
	public void tearDown() {
		System.setProperties(properties);
	}
	
	@Test
	public void loadProperties() {
		assertNull(System.getProperty(DatabaseManager.DIALECT));
		assertNull(System.getProperty(DatabaseManager.DRIVER_CLASS));
		assertNull(System.getProperty(DatabaseManager.CONNECTION_URL));
		assertNull(System.getProperty(DatabaseManager.CONNECTION_USERNAME));
		assertNull(System.getProperty(DatabaseManager.CONNECTION_PASSWORD));
		
		subject.loadProperties();
		
		assertEquals("Property", System.getProperty(DatabaseManager.DIALECT), "org.hibernate.dialect.PostgreSQLDialect");
		assertEquals("Property", System.getProperty(DatabaseManager.DRIVER_CLASS), "org.postgresql.Driver");
		assertEquals("Property", System.getProperty(DatabaseManager.CONNECTION_URL), URL);
		assertEquals("Property", System.getProperty(DatabaseManager.CONNECTION_USERNAME), USER);
		assertEquals("Property", System.getProperty(DatabaseManager.CONNECTION_PASSWORD), PASS);
		
		verify(connectionData).getUrl();
		verify(connectionData).getUsername();
		verify(connectionData).getPassword();
	}
	
}
