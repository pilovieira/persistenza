package br.com.pilovieira.persistenza.data;

import static org.junit.Assert.assertEquals;

import org.hibernate.criterion.Criterion;
import org.junit.Test;

public class PersistenzaRestrictionsTest {

	@Test
	public void eq() {
		Criterion criterion = PersistenzaRestrictions.eq("att", "value");
		
		assertEquals("Criterion", "att=value", criterion.toString());
	}

	@Test
	public void eqNull() {
		Criterion criterion = PersistenzaRestrictions.eq("att", null);
		
		assertEquals("Criterion", "att is null", criterion.toString());
	}

}
