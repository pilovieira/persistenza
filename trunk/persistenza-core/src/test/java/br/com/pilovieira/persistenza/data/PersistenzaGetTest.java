package br.com.pilovieira.persistenza.data;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.hibernate.criterion.BetweenExpression;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.IdentifierEqExpression;
import org.hibernate.criterion.IlikeExpression;
import org.hibernate.criterion.SimpleExpression;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.pilovieira.persistenza.entity.Dog;

@RunWith(MockitoJUnitRunner.class)
public class PersistenzaGetTest {
	
	@Mock private SessionManager sessionManager;
	
	private PersistenzaGet subject;

	@Before
	public void setup() {
		subject = new PersistenzaGet(sessionManager);
	}
	
	@Test
	public void all() {
		subject.all(Dog.class);
		
		verify(sessionManager).list(Dog.class, new Criterion[]{});
	}
	
	@Test
	public void like() {
		subject.like(Dog.class, "att", "value");
		
		verify(sessionManager).list(eq(Dog.class), argThat(new ArgumentMatcher<Criterion>() {

			@Override
			public boolean matches(Object argument) {
				IlikeExpression expression = (IlikeExpression) argument;
				return expression.toString().equals("att ilike %value%");
			}
		}));
	}

	@Test
	public void search() {
		subject.search(Dog.class, "att", "value");
		
		verify(sessionManager).list(eq(Dog.class), argThat(new ArgumentMatcher<Criterion>() {

			@Override
			public boolean matches(Object argument) {
				SimpleExpression expression = (SimpleExpression) argument;
				return expression.toString().equals("att=value");
			}
		}));
	}

	@Test
	public void between() {
		subject.between(Dog.class, "att", new Date(), new Date());
		
		verify(sessionManager).list(eq(Dog.class), any(BetweenExpression.class));
	}

	@Test
	public void get() {
		subject.get(Dog.class, 1);
		
		verify(sessionManager).list(eq(Dog.class), argThat(new ArgumentMatcher<Criterion>() {

			@Override
			public boolean matches(Object argument) {
				IdentifierEqExpression expression = (IdentifierEqExpression) argument;
				return expression.toString().equals("id = 1");
			}
		}));
		
	}

}
