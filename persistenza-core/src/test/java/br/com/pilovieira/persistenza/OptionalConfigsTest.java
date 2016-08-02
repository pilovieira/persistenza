package br.com.pilovieira.persistenza;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class OptionalConfigsTest {

	private OptionalConfigs subject;

	@Before
	public void setup() {
		subject = new OptionalConfigs();
	}
	
	@Test
	public void defaultValues() {
		assertTrue("Auto Decorate", subject.isAutoDecorate());
	}

	@Test
	public void shutdownAutoDecorate() {
		subject.setAutoDecorate(false);
		assertFalse("Auto Decorate", subject.isAutoDecorate());
	}

}
