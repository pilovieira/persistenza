package br.com.pilovieira.commerciale.persistenza;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class CriterioEntre extends Criterio {
	
	private Object valorMaior;
	
	public static CriterioEntre criterioEntre(String atributo, Object valorMenor, Object valorMaior) {
		CriterioEntre criterio = new CriterioEntre();
		criterio.atributo = atributo;
		criterio.valor = valorMenor;
		return criterio;
	}
	
	@Override
	public Criterion produce() {
		return Restrictions.between(atributo, valor, valorMaior);
	}
}
