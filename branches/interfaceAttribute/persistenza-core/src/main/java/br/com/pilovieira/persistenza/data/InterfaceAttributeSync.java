package br.com.pilovieira.persistenza.data;

import static org.apache.commons.lang3.reflect.FieldUtils.getFieldsListWithAnnotation;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;
import static org.apache.commons.lang3.reflect.FieldUtils.writeField;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

import javax.persistence.Id;

import org.hibernate.Session;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;

import br.com.pilovieira.persistenza.PersistenzaHeap;
import br.com.pilovieira.persistenza.annotation.InterfaceAttribute;

import com.google.common.base.Function;

class InterfaceAttributeSync {
	
	private static final String GET_ATTRIBUTE_ID_SQL_FORMAT = "select %s from %s where %s = '%s'";
	
	private SessionManager sessionManager;
	
	public InterfaceAttributeSync(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	public <T> List<T> syncList(List<T> list) {
		for (T entity : list)
			for (Field field : getFieldsListWithAnnotation(entity.getClass(), InterfaceAttribute.class))
				loadAttribute(entity, field);
		
		return list;
	}

	private <T> void loadAttribute(T entity, Field field) {
		try {
			Integer attributeId = getAttributeId(entity, field);
			Object value = getAttributeValue(attributeId, field.getType());
			
			writeField(field, entity, value, true);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private <T> Integer getAttributeId(T entity, Field field) throws IllegalAccessException {
		InterfaceAttribute annotation = field.getAnnotation(InterfaceAttribute.class);
		final String fieldName = annotation.name().isEmpty() ? field.getName() : annotation.name();
		
		PersistentClass classMapping = PersistenzaHeap.getConfiguration().getClassMapping(entity.getClass().getName());
		String entityTable = classMapping.getTable().getName();
		String entityColumnId = ((Column)classMapping.getIdentifier().getColumnIterator().next()).getName();
		Object entityColumnIdValue = readField(getFieldsListWithAnnotation(entity.getClass(), Id.class).get(0), entity, true);
		
		final String sql = String.format(GET_ATTRIBUTE_ID_SQL_FORMAT, fieldName, entityTable, entityColumnId, entityColumnIdValue);
		
		return sessionManager.execute(new Function<Session, Integer>() {
			@Override
			public Integer apply(Session session) {
				return (Integer)session.createSQLQuery(sql).uniqueResult();
			}
		});
	}

	private Object getAttributeValue(Integer id, Class<?> type) {
		if (id == null)
			return null;
		
		for (Class<?> subtype : getSubTypes(type)) {
			Object entity = Persistenza.get(subtype, id);
			if (entity != null)
				return entity;
		}
		
		return null;
	}

	private <T> Set<Class<? extends T>> getSubTypes(Class<T> type) {
		return PersistenzaHeap.getTypesScanner().getSubTypesOf(type);
	}

}
