package ru.terraobjects.entity.dao;

/**
 *
 * @author terranz
 */
import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.persist.*;
import ru.terraobjects.entity.TOObject;

public class TOObjectsManager
{
    private Connection conn = null;
    private Persist persist = null;

    public TOObjectsManager(Connection conn)
    {
        this.conn = conn;
        persist = new Persist(conn);
        persist.setUpdateAutoGeneratedKeys(true);
    }

    public List<TOObject> getAllObjects()
    {
        return persist.readList(TOObject.class, DAOConsts.SELECT_ALL_OBJECTS);
    }

    public List<TOObject> getAllObjsByTemplId(Integer templateId)
    {
        return persist.readList(TOObject.class, DAOConsts.SELECT_OBJECTS_BY_TEMPLATE_ID, templateId);
    }

    public List<TOObject> getAllObjsByTemplId(Integer templateId, Integer page, Integer perPage)
    {
        return persist.readList(TOObject.class, DAOConsts.SELECT_OBJECTS_BY_TEMPLATE_ID_PAGING, templateId, page * perPage, perPage);
    }

    public List<TOObject> getAllObjectsByParentId(Integer parentId)
    {
        return persist.readList(TOObject.class, DAOConsts.SELECT_OBJECTS_BY_PARENT_ID, parentId);
    }

    public List<TOObject> getAllObjectsByParentId(Integer parentId, Integer page, Integer perPage)
    {
        return persist.readList(TOObject.class, DAOConsts.SELECT_OBJECTS_BY_PARENT_ID_PAGING, parentId, page * perPage, perPage);
    }

    public TOObject getObject(Integer id)
    {
        return persist.read(TOObject.class, DAOConsts.SELECT_OBJECT_BY_ID, id);
    }

    public TOObject createNewObject(Integer templateId)
    {
        TOObject newobj = new TOObject();
        newobj.setCreatedAt(new Date());
        newobj.setUpdatedAt(new Date());
        newobj.setId(0);
        newobj.setParentId(0);
        newobj.setTemplateId(templateId);
        int added = 0;
        PreparedStatement st = null;
        try
        {
            //persist.insert(newobj);
            st = conn.prepareStatement("insert into object values (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            st.setInt(1, 0);
            st.setInt(2, 0);
            st.setInt(3, templateId);
            st.setDate(4, new java.sql.Date(newobj.getCreatedAt().getTime()));
            st.setDate(5, new java.sql.Date(newobj.getUpdatedAt().getTime()));
            st.execute();
            ResultSet rs = st.getGeneratedKeys();
            if (rs.last())
            {
                added = rs.getInt(1);
            }
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
                Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //createDefaultPropsForObject(templateId, added);
        newobj.setId(added);
        return newobj;
    }

    public Long getObjectsCountByTemplateId(Integer templateId)
    {
        try
        {
            PreparedStatement st = conn.prepareStatement(DAOConsts.SELECT_OBJECTS_COUNT_BY_TEMPLATE_ID);
            st.setInt(1, templateId);
            st.execute();
            ResultSet rs = st.getResultSet();
            if (rs.last())
            {
                return rs.getLong(1);
            }
        } catch (SQLException ex)
        {
            Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Long(0);
    }

    public void removeObjectWithProps(Integer objId)
    {
        TOPropertiesManager propMngr = new TOPropertiesManager(conn);
        propMngr.removeObjPropertiesFromObject(objId);
        PreparedStatement st = null;
        try
        {
            st = conn.prepareStatement(DAOConsts.REMOVE_OBJECT_BY_ID);
            st.setInt(1, objId);
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
                Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void removeObjectsByTemplate(Integer templateId)
    {
        PreparedStatement st = null;
        try
        {
            st = conn.prepareStatement(DAOConsts.REMOVE_OBJECT_BY_TEMPLATE_ID);
            st.setInt(1, templateId);
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
                Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void removeAllObjects()
    {
        TOPropertiesManager propMngr = new TOPropertiesManager(conn);
        propMngr.removeAllObjectProperties();
        PreparedStatement st = null;
        try
        {
            st = conn.prepareStatement(DAOConsts.REMOVE_ALL_OBJECTS);
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
                Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public List<TOObject> getObjectsByTemplateAndProp(Integer templateId, Integer propId)
    {
        return persist.readList(TOObject.class, DAOConsts.SELECT_OBJECT_BY_TEMPLATE_ID_AND_PROP_ID, templateId, propId);
    }
}
