package br.com.pilovieira.persistenza.db;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class PostgreSqlTest {

	private PostgreSql subject;

	@Before
	public void setup() {
		subject = new PostgreSql("url", "user", "pass");
	}
	
	@Test
	public void getDialect() {
		assertEquals("Dialect", "org.hibernate.dialect.PostgreSQLDialect", subject.getDialect());
	}

	@Test
	public void getConnectionDriverClass() {
		assertEquals("Driver", "org.postgresql.Driver", subject.getConnectionDriverClass());
	}

	@Test
	public void getSslFactory() {
		assertEquals("SSL Factory", "org.postgresql.ssl.NonValidatingFactory", subject.getSslFactory());
	}


}
