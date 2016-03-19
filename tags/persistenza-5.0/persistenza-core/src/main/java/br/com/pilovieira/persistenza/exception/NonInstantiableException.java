package br.com.pilovieira.persistenza.exception;

public class NonInstantiableException extends PersistenzaGeneralException {

	private static final long serialVersionUID = -6824396869812754592L;

	public NonInstantiableException(Class<Object> clazz) {
		super(String.format("This class cannot be instantiated: %s", clazz));
	}
}
