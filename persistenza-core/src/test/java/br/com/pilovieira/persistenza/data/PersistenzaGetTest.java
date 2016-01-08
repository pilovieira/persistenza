package br.com.pilovieira.persistenza.data;

import static org.mockito.Mockito.verify;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.IlikeExpression;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.pilovieira.persistenza.entity.Dog;

@RunWith(MockitoJUnitRunner.class)
public class PersistenzaGetTest {
	
	@Mock
	private SessionManager sessionManager;
	
	private PersistenzaGet subject;

	@Before
	public void setup() {
		SessionManagerMock.setMock(sessionManager);
		
		subject = new PersistenzaGet();
	}

	@Test
	public void all() {
		subject.all(Dog.class);
		
		verify(sessionManager).list(Dog.class, new Criterion[]{});
	}
	
	@Test
	public void like() {
		subject.like(Dog.class, "att", "value");
		
		verify(sessionManager).list(Matchers.eq(Dog.class), Matchers.argThat(new ArgumentMatcher<Criterion>() {

			@Override
			public boolean matches(Object argument) {
				IlikeExpression expression = (IlikeExpression) argument;
				return expression.toString().equals("att ilike %value%");
			}
		}));
	}

	@Test
	public void search() {
	}

	@Test
	public void searchCriterion() {
	}

	@Test
	public void between() {
	}

	@Test
	public void get() {
	}

}
