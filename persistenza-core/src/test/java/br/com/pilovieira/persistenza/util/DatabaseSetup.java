package br.com.pilovieira.persistenza.util;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import org.reflections.util.ClasspathHelper;

import br.com.pilovieira.persistenza.PersistenzaManager;
import br.com.pilovieira.persistenza.db.HyperSql;

@SuppressWarnings("rawtypes")
public class DatabaseSetup {
	
	private static final TestClassLoader classLoader = new TestClassLoader();

	public static void initialize() {
		System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		
		refreshClassLoader();
		
		HyperSql hyperSql = new HyperSql("jdbc:hsqldb:mem:.", "sa", "");
		hyperSql.setShowSql(true);
		
		PersistenzaManager.load(hyperSql);
	}

	public static void refreshClassLoader() {
		try {
			Field scl;
			scl = ClassLoader.class.getDeclaredField("scl");
			scl.setAccessible(true);
			scl.set(null, classLoader);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		Thread.currentThread().setContextClassLoader(classLoader);
	}

	public static void clear(Class... entities) throws SQLException {
		Connection connection = PersistenzaManager.getConnection();
		connection.setAutoCommit(false);
		
		Statement statement = connection.createStatement();
		
		for (Class clazz : entities)
			statement.executeUpdate("delete from " + clazz.getSimpleName().toLowerCase());
		
		connection.commit();
	}
	
	public static class TestClassLoader extends URLClassLoader {

		public TestClassLoader() {
			super(getSystemURLs());
			addUrls();
		}
		
		public TestClassLoader(URL[] urls) {
			super(urls);
		}
		
		private static URL[] getSystemURLs() {
			return ((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs();
		}
		
		private void addUrls() {
			Iterator<URL> iterator = ClasspathHelper.forPackage("br.com.pilovieira.persistenza", ClassLoader.getSystemClassLoader()).iterator();
			
			while(iterator.hasNext())
				super.addURL(iterator.next());
		}
	}

}
