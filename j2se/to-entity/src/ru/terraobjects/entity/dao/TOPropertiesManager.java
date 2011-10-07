package ru.terraobjects.entity.dao;

/**
 *
 * @author terranz
 */
import java.util.List;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    private static TOPropertiesManager instance = new TOPropertiesManager();

    private TOPropertiesManager()
    {
    }

    public void setConnection(Connection conn)
    {
        this.conn = conn;
        persist = new Persist(conn);
        persist.setUpdateAutoGeneratedKeys(true);
    }

    public static TOPropertiesManager getInstance()
    {
        return instance;
    }

    public List getPropertyTypes()
    {
        return persist.readList(TOPropertyType.class, DAOConsts.SELECT_ALL_PROP_TYPES);
    }

    public List getProperties()
    {
        return persist.readList(TOProperty.class, DAOConsts.SELECT_ALL_PROPERTIES);
    }

    public List getObjectProperties(Integer oid)
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
        try
        {
            PreparedStatement st = conn.prepareStatement(DAOConsts.REMOVE_OBJECT_PROP_BY_PROP_ID);
            st.setInt(1, propId);
            st.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeObjectPropertiesByObjectId(Integer oId)
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(DAOConsts.REMOVE_OBJECT_PROP_BY_OBJECT_ID);
            st.setInt(1, oId);
            st.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeObjectProperty(Integer oId, Integer propId)
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(DAOConsts.REMOVE_OBJECT_PROP_BY_OBJECT_ID_AND_PROP_ID);
            st.setInt(1, oId);
            st.setInt(2, propId);
            st.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeAllObjectProperties()
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(DAOConsts.REMOVE_ALL_OBJECT_PROPS);
            st.execute();
        } catch (SQLException ex)
        {
            Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
