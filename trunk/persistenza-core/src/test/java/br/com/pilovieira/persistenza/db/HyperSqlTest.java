package br.com.pilovieira.persistenza.db;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class HyperSqlTest {

	private HyperSql subject;

	@Before
	public void setup() {
		subject = new HyperSql("url", "user", "pass");
	}
	
	@Test
	public void getDialect() {
		assertEquals("Dialect", "org.hibernate.dialect.HSQLDialect", subject.getDialect());
	}

	@Test
	public void getConnectionDriverClass() {
		assertEquals("Driver", "org.hsqldb.jdbcDriver", subject.getConnectionDriverClass());
	}

	@Test
	public void getSslFactory() {
		assertNull("SSL Factory must be null", subject.getSslFactory());
	}

}
