package br.com.pilovieira.persistenza;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseTest {
	
	private static final String PROPERTY_DIALECT = "hibernate.dialect";
	private static final String PROPERTY_DRIVER_CLASS = "hibernate.connection.driver_class";
	private static final String PROPERTY_CONNECTION_URL = "hibernate.connection.url";
	private static final String PROPERTY_CONNECTION_USERNAME = "hibernate.connection.username";
	private static final String PROPERTY_CONNECTION_PASSWORD = "hibernate.connection.password";
	private static final String PROPERTY_SHOW_SQL = "hibernate.show_sql";
	
	private static final String URL_VALUE = "localhost:5432";
	private static final String USER_VALUE = "user";
	private static final String PASS_VALUE = "pass";

	private Properties properties;
	private DatabaseFake subject;

	@Before
	public void setup() {
		properties = System.getProperties();
		System.setProperties(new Properties());
		
		subject = new DatabaseFake(URL_VALUE, USER_VALUE, PASS_VALUE);
	}
	
	@After
	public void tearDown() {
		System.setProperties(properties);
	}
	
	@Test
	public void loadProperties() {
		assertNull(System.getProperty(PROPERTY_DIALECT));
		assertNull(System.getProperty(PROPERTY_DRIVER_CLASS));
		assertNull(System.getProperty(PROPERTY_CONNECTION_URL));
		assertNull(System.getProperty(PROPERTY_CONNECTION_USERNAME));
		assertNull(System.getProperty(PROPERTY_CONNECTION_PASSWORD));
		
		subject.loadProperties();
		
		assertEquals("Property Dialect", System.getProperty(PROPERTY_DIALECT), "dialectFake");
		assertEquals("Property Driver", System.getProperty(PROPERTY_DRIVER_CLASS), "driverFake");
		assertEquals("Property Url", System.getProperty(PROPERTY_CONNECTION_URL), URL_VALUE);
		assertEquals("Property User", System.getProperty(PROPERTY_CONNECTION_USERNAME), USER_VALUE);
		assertEquals("Property Pass", System.getProperty(PROPERTY_CONNECTION_PASSWORD), PASS_VALUE);
	}
	
	@Test
	public void showSql() {
		assertNull(System.getProperty(PROPERTY_SHOW_SQL));
		
		subject.setShowSql(true);
		subject.loadProperties();
		
		assertEquals("Property Pass", System.getProperty(PROPERTY_SHOW_SQL), "true");
	}
	
	@Test
	public void withSsl() {
		subject.setSsl(true);
		subject.loadProperties();
		
		assertEquals("Property Url", System.getProperty(PROPERTY_CONNECTION_URL), "localhost:5432?ssl=true&sslfactory=sslFactoryFake");
	}

	@Test
	public void withSslWithoutFactory() {
		subject = new DatabaseWithoutSslFactoryFake(URL_VALUE, USER_VALUE, PASS_VALUE);
		subject.setSsl(true);
		subject.loadProperties();
		
		assertEquals("Property Url", System.getProperty(PROPERTY_CONNECTION_URL), "localhost:5432?ssl=true");
	}
	
}
