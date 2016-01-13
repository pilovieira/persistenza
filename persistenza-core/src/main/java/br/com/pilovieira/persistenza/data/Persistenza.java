package br.com.pilovieira.persistenza.data;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Criterion;

public final class Persistenza {
	
	private static final SessionManager sessionManager = new SessionManager();
	private static final PersistenzaSet perSet = new PersistenzaSet(sessionManager);
	private static final PersistenzaGet perGet = new PersistenzaGet(sessionManager);
	private static final PersistenzaSingleton perSing = new PersistenzaSingleton(sessionManager, perSet);
	
	public static void pause() {
		perSet.pause();
	}
	
	public static void play() {
		perSet.play();
	}

	public static void rewind() {
		perSet.rewind();
	}
	
	public static void persist(final Object... entities) {
		perSet.persist(entities);
	}
	
	public static void delete(final Object... entities) {
		perSet.delete(entities);
	}
	
	public static <T> List<T> all(final Class<T> clazz) {
		return perGet.all(clazz);
	}
	
	public static <T> List<T> like(Class<T> clazz, String attribute, String value) {
		return perGet.like(clazz, attribute, value);
	}
	
	public static <T> List<T> search(Class<T> clazz, String attribute, Object value) {
		return perGet.search(clazz, attribute, value);
	}
	
	public static <T> List<T> search(Class<T> clazz, Criterion... criterions) {
		return perGet.search(clazz, criterions);
	}

	public static <T> List<T> between(final Class<T> clazz, final String attribute, final Date lo, final Date hi) {
		return perGet.between(clazz, attribute, lo, hi);
	}

	public static <T> T get(final Class<T> clazz, final int id) {
		return perGet.get(clazz, id);
	}
	
	public static <T> T singleton(final Class<T> clazz) {
		return perSing.singleton(clazz);
	}

}
