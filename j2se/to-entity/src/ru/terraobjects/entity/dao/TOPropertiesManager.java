package ru.terraobjects.entity.dao;

/**
 *
 * @author terranz
 */
import java.util.List;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.persist.*;
import ru.terraobjects.entity.TOObjectProperty;
import ru.terraobjects.entity.TOProperty;
import ru.terraobjects.entity.TOPropertyType;

public class TOPropertiesManager
{

    private Connection conn = null;
    private Persist persist = null;

    public TOPropertiesManager(Connection conn)
    {
        this.conn = conn;
        persist = new Persist(conn);
        persist.setUpdateAutoGeneratedKeys(true);
    }

    public List<TOPropertyType> getPropertyTypes()
    {
        return persist.readList(TOPropertyType.class, DAOConsts.SELECT_ALL_PROP_TYPES);
    }

    public List<TOProperty> getProperties()
    {
        return persist.readList(TOProperty.class, DAOConsts.SELECT_ALL_PROPERTIES);
    }

    public List<TOObjectProperty> getObjPropsForObjId(Integer oid)
    {
        return persist.readList(TOObjectProperty.class, DAOConsts.SELECT_OBJECT_PROPS_BY_OBJECT_ID, oid);
    }

    public TOPropertyType getPropertyType(Integer propId)
    {
        TOPropertyType res = EntityCache.getInstance().getPropertyTypeFromCache(propId);
        if (res == null)
        {
            res = persist.read(TOPropertyType.class, DAOConsts.SELECT_PROP_TYPE_BY_ID, propId);
            EntityCache.getInstance().addPropertyTypeToCache(propId, res);
        }
        return res;
    }

    public TOObjectProperty getObjectProperty(Integer oId, Integer propId)
    {
        TOObjectProperty res = persist.read(TOObjectProperty.class, DAOConsts.SELECT_OBJECT_PROP_BY_OBJECT_ID_AND_PROP_ID, oId, propId);
        return res;
    }

    public void removeObjectPropertyByPropId(Integer propId)
    {
        PreparedStatement st = null;
        try
        {
            st = conn.prepareStatement(DAOConsts.REMOVE_OBJECT_PROP_BY_PROP_ID);
            st.setInt(1, propId);
            st.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                st.close();
            } catch (SQLException ex)
            {
                Logger.getLogger(TOPropertiesManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void removeObjPropertiesFromObject(Integer oId)
    {
        PreparedStatement st = null;
        try
        {
            st = conn.prepareStatement(DAOConsts.REMOVE_OBJECT_PROP_BY_OBJECT_ID);
            st.setInt(1, oId);
            st.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                st.close();
            } catch (SQLException ex)
            {
                Logger.getLogger(TOPropertiesManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //todo implement removing list property type 
    }

    public void removeObjectProperty(Integer oId, Integer propId)
    {
        PreparedStatement st = null;
        try
        {
            st = conn.prepareStatement(DAOConsts.REMOVE_OBJECT_PROP_BY_OBJECT_ID_AND_PROP_ID);
            st.setInt(1, oId);
            st.setInt(2, propId);
            st.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                st.close();
            } catch (SQLException ex)
            {
                Logger.getLogger(TOPropertiesManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void removeAllObjectProperties()
    {
        PreparedStatement st = null;
        try
        {
            st = conn.prepareStatement(DAOConsts.REMOVE_ALL_OBJECT_PROPS);
            st.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally
        {
            try
            {
                st.close();
            } catch (SQLException ex)
            {
                Logger.getLogger(TOPropertiesManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //todo implement removing list property type
    }

    public List<Integer> getObjIdsListByPropertyId(Integer listPropId)
    {
        return persist.readList(Integer.class, DAOConsts.SELECT_OBJPROP_LIST_BY_LISTPROP_ID, listPropId);
    }

    public void createDefaultPropsForObject(Integer templateId, Integer oid)
    {
        persist.executeUpdate(DAOConsts.CREATE_PROPS_FOR_OBJECT, new Object[]
                {
                    templateId, oid
                });
    }

    public Object getPropertyValue(Integer oid, Integer pid)
    {
        TOObjectProperty objprop = getObjectProperty(oid, pid);
        TOProperty prop = EntityCache.getInstance().getPropertyFromCache(objprop.getPropertyId());
        if (prop == null)
        {
            System.out.println("property not found in cache, loading");
            prop = persist.read(TOProperty.class, DAOConsts.SELECT_PROPERTY_BY_ID, objprop.getPropertyId());
            EntityCache.getInstance().addPropertyToCache(objprop.getPropertyId(), prop);
        }
        Object ret = null;
        switch (prop.getPropTypeId())
        {
            case TOPropertyType.TYPE_STR:
            {
                ret = objprop.getStringVal();
            }
            break;
            case TOPropertyType.TYPE_INT:
            {
                ret = objprop.getIntVal();
            }
            break;
            case TOPropertyType.TYPE_FLOAT:
            {
                ret = objprop.getFloatVal();
            }
            break;
            case TOPropertyType.TYPE_TEXT:
            {
                ret = objprop.getTextVal();
            }
            break;
            case TOPropertyType.TYPE_DATE:
            {
                ret = objprop.getDateVal();
            }
            break;
            case TOPropertyType.TYPE_LIST:
            {
                ret = getObjIdsListByPropertyId(objprop.getListVal());
            }
            break;
        }
        return ret;
    }

    public void createNewPropertyWithValue(Integer oid, Integer propid, Object value, Integer type)
    {
        TOObjectProperty newProp = new TOObjectProperty();
        newProp.setObjectId(oid);
        newProp.setPropertyId(propid);
        switch (type)
        {
            case TOPropertyType.TYPE_STR:
            {
                newProp.setStringVal(String.valueOf(value));
            }
            break;
            case TOPropertyType.TYPE_INT:
            {
                newProp.setIntVal(Integer.valueOf(String.valueOf(value)));
            }
            break;
            case TOPropertyType.TYPE_FLOAT:
            {
                newProp.setFloatVal(Float.valueOf(String.valueOf(value)));
            }
            break;
            case TOPropertyType.TYPE_TEXT:
            {
                newProp.setStringVal(String.valueOf(value));
            }
            break;
            case TOPropertyType.TYPE_DATE:
            {
                newProp.setDateVal((Date) value);
            }
            break;
            case TOPropertyType.TYPE_LIST:
            {
                newProp.setListVal(Integer.valueOf(String.valueOf(value)));
            }
            break;
        }
        persist.insert(newProp);
    }

    //ставит значение в object_props нужному объекту
    public void setPropertyValue(Integer oid, Integer propid, Object value)
    {
        TOObjectProperty property = getObjectProperty(oid, propid);
        TOProperty prop = EntityCache.getInstance().getPropertyFromCache(property.getPropertyId());
        if (prop == null)
        {
            prop = persist.read(TOProperty.class, DAOConsts.SELECT_PROPERTY_BY_ID, property.getPropertyId());
            EntityCache.getInstance().addPropertyToCache(property.getPropertyId(), prop);
        }

        switch (prop.getPropTypeId())
        {
            case TOPropertyType.TYPE_STR:
            {
                property.setStringVal(String.valueOf(value));
            }
            break;
            case TOPropertyType.TYPE_INT:
            {
                property.setIntVal(Integer.valueOf(String.valueOf(value)));
            }
            break;
            case TOPropertyType.TYPE_FLOAT:
            {
                property.setFloatVal(Float.valueOf(String.valueOf(value)));
            }
            break;
            case TOPropertyType.TYPE_TEXT:
            {
                property.setStringVal(String.valueOf(value));
            }
            break;
            case TOPropertyType.TYPE_DATE:
            {
                property.setDateVal((Date) value);
            }
            break;
            case TOPropertyType.TYPE_LIST:
            {
                property.setListVal(Integer.valueOf(String.valueOf(value)));
            }
            break;
        }
        persist.update(property);
    }

    public void setPropertyValue(Integer oid, Integer propid, Object value, Integer type)
    {
        TOObjectProperty property = getObjectProperty(oid, propid);
        switch (type)
        {
            case TOPropertyType.TYPE_STR:
            {
                property.setStringVal(String.valueOf(value));
            }
            break;
            case TOPropertyType.TYPE_INT:
            {
                property.setIntVal(Integer.valueOf(String.valueOf(value)));
            }
            break;
            case TOPropertyType.TYPE_FLOAT:
            {
                property.setFloatVal(Float.valueOf(String.valueOf(value)));
            }
            break;
            case TOPropertyType.TYPE_TEXT:
            {
                property.setStringVal(String.valueOf(value));
            }
            break;
            case TOPropertyType.TYPE_DATE:
            {
                property.setDateVal((Date) value);
            }
            break;
            case TOPropertyType.TYPE_LIST:
            {
                property.setListVal(Integer.valueOf(String.valueOf(value)));
            }
            break;
        }
        persist.update(property);
    }
}
