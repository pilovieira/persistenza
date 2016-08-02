package br.com.pilovieira.persistenza.util;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.reflections.util.ClasspathHelper;

import br.com.pilovieira.persistenza.PersistenzaManager;
import br.com.pilovieira.persistenza.db.HyperSql;

@SuppressWarnings("rawtypes")
public class DatabaseSetup {
	
	public static void initialize(Class... entities) {
		System.setProperty("hibernate.hbm2ddl.auto", "create-drop");
		
		TestClassLoader classLoader = new TestClassLoader();
		classLoader.addUrls(entities);
		
		Thread.currentThread().setContextClassLoader(classLoader);
//		try {
//			Field scl;
//			scl = ClassLoader.class.getDeclaredField("scl");
//			scl.setAccessible(true); // Set accessible
//			scl.set(null, classLoader);
//		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} // Get system class loader
		
		HyperSql hyperSql = new HyperSql("jdbc:hsqldb:mem:.", "sa", "");
		hyperSql.setShowSql(true);
		
		PersistenzaManager.load(hyperSql);
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
		}
		
		public TestClassLoader(URL[] urls) {
			super(urls);
		}
		
		private static URL[] getSystemURLs() {
			return ((URLClassLoader)ClassLoader.getSystemClassLoader()).getURLs();
		}
		
		public void addUrls(Class... classes) {
			for (Class clazz : classes)
				super.addURL(ClasspathHelper.forClass(clazz, ClassLoader.getSystemClassLoader()));
		}
	}
	
}
