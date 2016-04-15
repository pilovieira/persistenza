package br.com.pilovieira.persistenza.data;

import static org.apache.commons.lang3.reflect.FieldUtils.getFieldsListWithAnnotation;
import static org.apache.commons.lang3.reflect.FieldUtils.readField;

import java.lang.reflect.Field;
import java.util.List;

import javax.persistence.Id;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;

import br.com.pilovieira.persistenza.PersistenzaHeap;
import br.com.pilovieira.persistenza.annotation.Interfaccia;

class InterfacciaSet {
	
	private static final String SET_ATTRIBUTE_ID_SQL_FORMAT = "update %s set %s = %s where %s = '%s'";
	
	public void set(Session session, List<Object> list) {
		for (Object entity : list)
			for (Field field : getFieldsListWithAnnotation(entity.getClass(), Interfaccia.class))
				markAttribute(session, entity, field);
	}

	private void markAttribute(Session session, Object entity, Field field) {
		try {
			setAttributeId(session, entity, field);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private void setAttributeId(Session session, Object entity, Field field) throws IllegalAccessException {
		Interfaccia annotation = field.getAnnotation(Interfaccia.class);
		final String fieldName = annotation.name().isEmpty() ? field.getName() : annotation.name();
		Object attribute = readField(field, entity, true);
		Object attributeColumnIdValue = readField(getFieldsListWithAnnotation(attribute.getClass(), Id.class).get(0), attribute, true);
		
		PersistentClass classMapping = PersistenzaHeap.getConfiguration().getClassMapping(entity.getClass().getName());
		String entityTable = classMapping.getTable().getName();
		String entityColumnId = ((Column)classMapping.getIdentifier().getColumnIterator().next()).getName();
		Object entityColumnIdValue = readField(getFieldsListWithAnnotation(entity.getClass(), Id.class).get(0), entity, true);
		
		String sqlFormat = SET_ATTRIBUTE_ID_SQL_FORMAT;
		final String sql = String.format(sqlFormat, entityTable, fieldName, attributeColumnIdValue, entityColumnId, entityColumnIdValue);
		
		SQLQuery query = session.createSQLQuery(sql);
		query.executeUpdate();
	}
	
}
