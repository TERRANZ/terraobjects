package ru.terraobjects.entity.manager;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import ru.terraobjects.entity.*;
import ru.terraobjects.entity.dao.DAOConsts;

import java.util.Date;
import java.util.List;

/**
 * @author terranz
 */
public class TOObjectPropertyManager extends PersistanceManager<TOObjectProperty> {
    public TOObjectPropertyManager() {
        super();
    }

    @Override
    public TOObjectProperty findById(Integer id) {
        return (TOObjectProperty) session.createCriteria(TOObjectProperty.class).add(Restrictions.eq("id", id)).uniqueResult();
    }

    public void removeObjectProperty(Integer oId, Integer propId) {
        session.createSQLQuery(DAOConsts.REMOVE_OBJECT_PROP_BY_OBJECT_ID_AND_PROP_ID).setParameter(1, oId).setParameter(2, propId).executeUpdate();
    }

    public void removeObjectPropertyByPropId(Integer propId) {
        session.createSQLQuery(DAOConsts.REMOVE_OBJECT_PROP_BY_PROP_ID).setParameter(1, propId).executeUpdate();
    }

    public List<TOObjectProperty> getObjPropsForObjId(Integer oid) {
        return session.createCriteria(TOObjectProperty.class).add(Restrictions.eq("object", oid)).list();
    }

    public TOObjectProperty getObjectProperty(TOObject oId, TOProperty propId) {
        return (TOObjectProperty) session.createCriteria(TOObjectProperty.class).add(Restrictions.eq("object", oId)).add(Restrictions.eq("property", propId)).uniqueResult();
    }

    public void removeObjPropertiesFromObject(Integer oId) {
        session.createSQLQuery(DAOConsts.REMOVE_OBJECT_PROP_BY_OBJECT_ID).setParameter(1, oId).executeUpdate();
        //todo implement removing list property type
    }

    public void removeAllObjectProperties() {
        session.createSQLQuery(DAOConsts.REMOVE_ALL_OBJECT_PROPS).executeUpdate();
        //todo implement removing list property type
    }

    //    public List<Integer> getObjIdsListByPropertyId(Integer listPropId)
//    {
//        return persist.readList(Integer.class, DAOConsts.SELECT_OBJPROP_LIST_BY_LISTPROP_ID, listPropId);
//    }
    public void createDefaultPropsForObject(TOObjectTemplate template, TOObject obj, Boolean storedProc) {
        if (storedProc) {
            session.createSQLQuery(DAOConsts.CREATE_PROPS_FOR_OBJECT).setParameter(1, template.getObjectTemplateId()).setParameter(2, obj.getObjectId()).executeUpdate();
        } else {
            TOObjectTemplatePropertyManager tManager = new TOObjectTemplatePropertyManager();
            List<TOObjectTemplateProperty> props = tManager.getObjectTemplatePropsByTemplate(template);
            for (TOObjectTemplateProperty prop : props) {
                createNewObjectPropertyWithValue(obj, prop.getProperty(), "", TOPropertyType.TYPE_STR);
            }
        }
    }

    public void createDeafultPropsForObject(Integer templateId, Integer objectId) {
        session.createSQLQuery(DAOConsts.CREATE_PROPS_FOR_OBJECT).setParameter(0, templateId).setParameter(1, objectId).executeUpdate();
    }

    public Object getPropertyValue(TOObject obj, TOProperty prop) {
        TOObjectProperty objprop = getObjectProperty(obj, prop);
        if (objprop != null) {
            Object ret = null;
            switch (objprop.getPropType()) {
                case TOPropertyType.TYPE_STR: {
                    ret = objprop.getStrval();
                }
                break;
                case TOPropertyType.TYPE_INT: {
                    ret = objprop.getIntval();
                }
                break;
                case TOPropertyType.TYPE_FLOAT: {
                    ret = objprop.getFloatval();
                }
                break;
                case TOPropertyType.TYPE_TEXT: {
                    ret = objprop.getTextval();
                }
                break;
                case TOPropertyType.TYPE_DATE: {
                    ret = objprop.getDateval();
                }
                break;
                case TOPropertyType.TYPE_LIST: {
                    //ret = getObjIdsListByPropertyId(objprop.getListval());
                }
                break;
            }
            return ret;
        }
        throw new RuntimeException("Object " + obj.getObjectId() + " doesn't have property " + prop.getPropId());
    }

    public void createNewObjectPropertyWithValue(TOObject obj, TOProperty prop, Object value, Integer type) {
        TOObjectProperty newProp = generateNewObjectPropertyWithValue(obj, prop, value, type);
        insert(newProp);
    }

    public TOObjectProperty generateNewObjectPropertyWithValue(TOObject obj, TOProperty prop, Object value, Integer type) {
        TOObjectProperty newProp = new TOObjectProperty();
        newProp.setObject(obj);
        newProp.setProperty(prop);
        newProp.setPropType(type);
        TOObjectPropsId id = new TOObjectPropsId(0, obj.getObjectId(), prop.getPropId());
        newProp.setId(id);
        setPropValue(newProp, value, type);
        return newProp;
    }

    public void saveNewObjectProperties(List<TOObjectProperty> props) {
        session.beginTransaction();
        String sql = "insert into object_props (dateval, floatval, intval, listval, prop_type, strval, textval, object_id, object_props_id, prop_id) values ";
        for (TOObjectProperty property : props) {
            sql += " (" + property.getDateval()
                    + ", " + property.getFloatval()
                    + ", " + property.getIntval()
                    + ", " + property.getListval()
                    + ", " + property.getPropType()
                    + ", '" + property.getStrval()
                    + "', '" + property.getTextval()
                    + "', " + property.getObject().getObjectId()
                    + ", 0, " + property.getProperty().getPropId()
                    + ") ";
            sql += ",";
        }
        sql = sql.substring(0, sql.length() - 1);
        Query q = session.createSQLQuery(sql);
        try {
            q.executeUpdate();
            session.getTransaction().commit();
        } catch (HibernateException he) {
            session.getTransaction().rollback();
            logger.error("Unable to save object properties list by single insert", he);
            logger.info("Returning to basic insert method");
            insert(props);
        }
    }
    //ставит значение в object_props нужному объекту

    public void setPropertyValue(TOObject obj, TOProperty prop, Object value, Integer type) {
        TOObjectProperty property = getObjectProperty(obj, prop);
        if (property == null) {
            //throw new RuntimeException("Can't set property: " + propid);
            createNewObjectPropertyWithValue(obj, prop, value, type);
        } else {
            setPropValue(property, value, type);
            update(property);
        }
    }

    public void bulkCreateObjectProps(List<TOObjectProperty> props) {
        Transaction tx = session.beginTransaction();
        Query q = session.createSQLQuery("update object_props set" +
                " dateval = :dateval," +
                "  floatval = :floatval," +
                "  intval = :intval," +
                "  listval = :listval," +
                "  prop_type =:prop_type," +
                "  strval = :strval," +
                "  textval = :textval," +
                "  object_id = :object_id," +
                "  object_props_id = :object_props_id," +
                "  prop_id =:prop_id" +
                "  where object_id = :object_id AND prop_id = :prop_id");
        for (TOObjectProperty o : props) {
            q.setParameter("object_id", o.getObject().getObjectId());
            q.setParameter("prop_id", o.getProperty().getPropId());
            q.setParameter("dateval", o.getDateval());
            q.setParameter("floatval", o.getFloatval());
            q.setParameter("intval", o.getIntval());
            q.setParameter("listval", o.getListval());
            q.setParameter("prop_type", o.getPropType());
            q.setParameter("strval", o.getStrval());
            q.setParameter("textval", o.getTextval());
            q.setParameter("object_props_id", 0);
            q.executeUpdate();
        }
        session.flush();
        session.clear();
        tx.commit();
    }

    public static TOObjectProperty setPropValue(TOObjectProperty prop, Object val, Integer type) {
        try {
            prop.setPropType(type);
            switch (type) {
                case TOPropertyType.TYPE_STR: {
                    prop.setStrval(String.valueOf(val));
                }
                break;
                case TOPropertyType.TYPE_INT: {
                    prop.setIntval(Integer.valueOf(String.valueOf(val)));
                }
                break;
                case TOPropertyType.TYPE_FLOAT: {
                    prop.setFloatval(Float.valueOf(String.valueOf(val)));
                }
                break;
                case TOPropertyType.TYPE_TEXT: {
                    prop.setStrval(String.valueOf(val));
                }
                break;
                case TOPropertyType.TYPE_DATE: {
                    prop.setDateval((Date) val);
                }
                break;
                case TOPropertyType.TYPE_LIST: {
                    prop.setListval(Integer.valueOf(String.valueOf(val)));
                }
                break;
            }
        } catch (NumberFormatException e) {
        }
        return prop;
    }
}
