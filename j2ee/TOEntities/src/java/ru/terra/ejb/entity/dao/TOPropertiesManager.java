package ru.terra.ejb.entity.dao;

/**
 *
 * @author terranz
 */
import java.sql.SQLException;
import java.util.List;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import ru.terra.ejb.entity.*;
import net.sf.persist.*;

public class TOPropertiesManager {

private Connection conn = null;
    private Persist persist = null;

    private static TOPropertiesManager instance = new TOPropertiesManager();
    private TOPropertiesManager()
    {
	InitialContext ic = null;
	try
	{
	    ic = new InitialContext();
	} catch (NamingException ex)
	{
	    Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
	}
	DataSource ds = null;
	try
	{
	    ds = (DataSource) ic.lookup("topool");
	} catch (NamingException ex)
	{
	    Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
	}
	try
	{
	    conn = ds.getConnection();
	} catch (SQLException ex)
	{
	    Logger.getLogger(TOObjectsManager.class.getName()).log(Level.SEVERE, null, ex);
	}
	persist = new Persist(conn);
    }

    public static TOPropertiesManager getInstance()
    {
	return instance;
    }

    public List getPropertyTypes()
    {
	return persist.readList(TOPropertyType.class,DAOConsts.SELECT_ALL_PROP_TYPES);
    }

    public List getProperties()
    {
	return persist.readList(TOProperty.class,DAOConsts.SELECT_ALL_PROPERTIES);
    }

    public List getPropsForObjectId(Integer oid)
    {
	return persist.readList(TOObjectProperty.class,DAOConsts.SELECT_OBJECT_PROPS_BY_OBJECT_ID,oid);
    }

    public List getObjectProperties(Integer oid)
    {
	return persist.readList(TOObjectProperty.class,DAOConsts.SELECT_OBJECT_PROPS_BY_OBJECT_ID,oid);
    }
}
