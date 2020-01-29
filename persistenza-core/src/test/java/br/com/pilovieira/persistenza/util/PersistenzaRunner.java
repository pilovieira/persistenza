package br.com.pilovieira.persistenza.util;

import java.lang.reflect.InvocationTargetException;

import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.mockito.runners.MockitoJUnitRunner;

public class PersistenzaRunner extends MockitoJUnitRunner {
	
	private static boolean init = false;

	public PersistenzaRunner(Class<?> clazz) throws InvocationTargetException {
		super(clazz);
	}

	@Override
	public Description getDescription() {
		return super.getDescription();
	}

	@Override
	public void run(RunNotifier notifier) {
		if (!init) {
			DatabaseSetup.initialize();
			init = true;
		}
		super.run(notifier);
	}

}
