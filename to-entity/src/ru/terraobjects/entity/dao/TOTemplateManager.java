package ru.terraobjects.entity.dao;

/**
 *
 * @author terranz
 */
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.persist.*;
import ru.terraobjects.entity.TOObjectTemplate;
import ru.terraobjects.entity.TOObjectTemplateProperty;
import ru.terraobjects.entity.TOProperty;
import ru.terraobjects.entity.TOPropertyType;

public class TOTemplateManager
{
    private Connection conn = null;
    private Persist persist = null;

    public TOTemplateManager(Connection conn)
    {
        this.conn = conn;
        persist = new Persist(conn);
        persist.setUpdateAutoGeneratedKeys(true);
    }

    public TOObjectTemplate getTemplate(Integer templateId)
    {
        return persist.read(TOObjectTemplate.class, DAOConsts.SELECT_OBJECT_TEMPLATE_BY_ID, templateId);
    }

    public List<TOObjectTemplate> getAllTemplates()
    {
        return persist.readList(TOObjectTemplate.class, DAOConsts.SELECT_ALL_OBJECT_TEMPLATE);
    }

    public TOObjectTemplate createTemplate(String name, Integer templateId, Integer parentId)
    {
        //System.out.println("Creating template for: " + name);
        TOObjectTemplate newTemplate = new TOObjectTemplate();
        newTemplate.setId(templateId == null ? 0 : templateId);
        newTemplate.setName(name);
        newTemplate.setParentId(parentId == null ? 0 : parentId);
        if (templateId == 0)
        {
            int added = 0;
            PreparedStatement st = null;
            try
            {
                st = conn.prepareStatement("insert into object_template values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
                st.setInt(1, 0);
                st.setString(2, name);
                st.setInt(3, parentId == null ? 0 : parentId);
                st.execute();
                ResultSet rs = st.getGeneratedKeys();
                if (rs.last())
                {
                    added = rs.getInt(1);
                }
            } catch (SQLException ex)
            {
                Logger.getLogger(TOTemplateManager.class.getName()).log(Level.SEVERE, null, ex);
            } finally
            {
                try
                {
                    st.close();
                } catch (SQLException ex)
                {
                    Logger.getLogger(TOTemplateManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            newTemplate.setId(added);
        } else
        {
            persist.insert(newTemplate);
        }
        return newTemplate;
    }

    public List<TOObjectTemplate> getAllTemplatesFromParent(Integer parentId)
    {
        return persist.readList(TOObjectTemplate.class, DAOConsts.SELECT_OBJECT_TEMPLATES_BY_PARENT_ID);
    }

    public void removeTemplate(Integer templateId, Boolean cascade)
    {
        if (cascade)
        {
            List<TOObjectTemplate> childs = getAllTemplatesFromParent(templateId);
            if (childs != null)
            {
                for (TOObjectTemplate t : childs)
                {
                    removeTemplate(t.getId(), true);
                }
            }
            removeTemplate(templateId, false);
        } else
        {
            PreparedStatement st = null;
            try
            {
                st = conn.prepareStatement(DAOConsts.REMOVE_OBJECT_TEMPLATE_BY_ID);
                st.setInt(1, templateId);
                st.execute();
            } catch (SQLException ex)
            {
                Logger.getLogger(TOTemplateManager.class.getName()).log(Level.SEVERE, null, ex);
            } finally
            {
                try
                {
                    st.close();
                } catch (SQLException ex)
                {
                    Logger.getLogger(TOTemplateManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public TOObjectTemplateProperty createNewProperty(Class propTypeClass, Integer propId, Integer templateId, String name)
    {
        TOObjectTemplateProperty newTemplateProp = getObjectTemplateProp(propId);
        if (newTemplateProp == null)
        {
            newTemplateProp = new TOObjectTemplateProperty();
        } else
        {
            return newTemplateProp;
        }

        TOPropertiesManager propManager = new TOPropertiesManager(conn);
        Integer type = 0;
        if (propTypeClass.equals(String.class))
        {
            type = TOPropertyType.TYPE_STR;
        } else if (propTypeClass.equals(Integer.class))
        {
            type = TOPropertyType.TYPE_INT;
        } else if (propTypeClass.equals(Float.class))
        {
            type = TOPropertyType.TYPE_FLOAT;
        } else if (propTypeClass.equals(Date.class))
        {
            type = TOPropertyType.TYPE_DATE;
        } else if (propTypeClass.equals(Long.class) || propTypeClass.equals(long.class))
        {
            type = TOPropertyType.TYPE_LONG;
        }
        TOProperty newProp = propManager.createNewProperty(type, "", name, propId);

        newTemplateProp.setId(propId);
        newTemplateProp.setObjectTemplateId(templateId);
        newTemplateProp.setPropertyId(newProp.getId());
//	PreparedStatement st = null;
//	int added = 0;
//	try
//	{
//	    st = conn.prepareStatement("insert into object_template_props values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
//	    st.setInt(1, templateId);
//	    st.setInt(2, newProp.getId());
//	    st.setInt(3, 0);
//	    st.execute();
//	    ResultSet rs = st.getGeneratedKeys();
//	    if (rs.last())
//	    {
//		added = rs.getInt(1);
//	    }
//	} catch (SQLException ex)
//	{
//	    Logger.getLogger(TOTemplateManager.class.getName()).log(Level.SEVERE, null, ex);
//	} finally
//	{
//	    try
//	    {
//		st.close();
//	    } catch (SQLException ex)
//	    {
//		Logger.getLogger(TOTemplateManager.class.getName()).log(Level.SEVERE, null, ex);
//	    }
//	}
//
//	newTemplateProp.setId(added);
        persist.insert(newTemplateProp);
        return newTemplateProp;
    }

    public TOObjectTemplateProperty getObjectTemplateProp(Integer propId)
    {
        return persist.read(TOObjectTemplateProperty.class, DAOConsts.SELECT_OBJECT_TEMPLATE_PROPS_BY_ID, propId);
    }

    public List<TOObjectTemplateProperty> getObjectTemplatePropsByTemplate(Integer templateId)
    {
        return persist.readList(TOObjectTemplateProperty.class, DAOConsts.SELECT_OBJECT_TEMPLATE_PROPS_BY_TEMPLATE_ID, templateId);
    }
}
