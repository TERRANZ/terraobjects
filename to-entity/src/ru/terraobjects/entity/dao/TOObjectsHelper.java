package ru.terraobjects.entity.dao;

import ru.terraobjects.entity.manager.TOObjectsManager;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.entity.TOObject;
import ru.terraobjects.entity.TOObjectProperty;
import ru.terraobjects.entity.TOObjectPropsId;
import ru.terraobjects.entity.TOProperty;
import ru.terraobjects.entity.annotations.PropGetter;
import ru.terraobjects.entity.annotations.PropSetter;
import ru.terraobjects.entity.annotations.TemplateId;
import ru.terraobjects.entity.manager.TOObjectPropertyManager;
import ru.terraobjects.entity.manager.TOObjectTemplateManager;
import ru.terraobjects.entity.manager.TOPropertyManager;

/**
 *
 * @author terranz
 */
public class TOObjectsHelper<T>
{
    private Connection conn;

    public TOObjectsHelper(Connection conn)
    {
	this.conn = conn;
    }

    public List<T> loadObjects(Class objectClass)
    {
	TOObjectsManager objectsManager = new TOObjectsManager();
	TOObjectTemplateManager templateManger = new TOObjectTemplateManager();
	if (objectClass.isAnnotationPresent(TemplateId.class))
	{
	    Integer tId = new Integer(((TemplateId) (objectClass.getAnnotation(TemplateId.class))).id());
	    if (tId != null)
	    {
		List<TOObject> objects = objectsManager.getAllObjsByTemplate(templateManger.findById(tId));
		ArrayList<T> ret = new ArrayList<T>();
		for (TOObject o : objects)
		{
		    ret.add(loadObject(objectClass, o));
		}
		return ret;
	    }
	}
	return null;
	//throw new RuntimeException("Can't load objects from DB");
    }

    public List<T> loadObjects(Class objectClass, Integer page, Integer perPage)
    {
	TOObjectsManager objectsManager = new TOObjectsManager();
	TOObjectTemplateManager templateManger = new TOObjectTemplateManager();
	if (objectClass.isAnnotationPresent(TemplateId.class))
	{
	    Integer tId = new Integer(((TemplateId) (objectClass.getAnnotation(TemplateId.class))).id());
	    if (tId != null)
	    {
		List<TOObject> objects = objectsManager.getAllObjsByTemplate(templateManger.findById(tId), page, perPage);
		ArrayList<T> ret = new ArrayList<T>();
		for (TOObject o : objects)
		{
		    ret.add(loadObject(objectClass, o));
		}
		return ret;
	    }
	}
	return null;
	//throw new RuntimeException("Can't load objects from DB");
    }

    public T loadObject(Class objectClass, Integer oid)
    {
	TOObject obj = new TOObjectsManager().findById(oid);
	if (obj != null)
	{
	    return loadObject(objectClass, obj);
	} else
	{
	    return null;
	}
    }

    public T loadObject(Class objectClass, TOObject obj)
    {
	T retObj = null;
	try
	{
	    //loading object from db with props and construct new Object
	    retObj = (T) objectClass.newInstance();
	    TOObjectPropertyManager propsManager = new TOObjectPropertyManager();
	    TOPropertyManager propMngr = new TOPropertyManager();
	    if (obj != null)
	    {
		for (Method m : retObj.getClass().getMethods())
		{
		    if (m.isAnnotationPresent(PropSetter.class))
		    {
			//get method setter and set to retObj info from obj
			Integer propId = Integer.valueOf(m.getAnnotation(PropSetter.class).id());
			m.invoke(retObj, propsManager.getPropertyValue(obj, propMngr.findById(propId)));
		    }
		}
	    } else
	    {
		Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE,
			"Can't load obj from DB, id: " + obj.toString(), new Object[]
			{
			});
	    }

	} catch (IllegalArgumentException ex)
	{
	    Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE, null, ex);
	} catch (InvocationTargetException ex)
	{
	    Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE, null, ex);
	} catch (InstantiationException ex)
	{
	    Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE, null, ex);
	} catch (IllegalAccessException ex)
	{
	    Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE, null, ex);
	}
	return retObj;
    }

    public TOObject storeObject(T objToStore, Boolean storedProc)
    {
	//deconstruct object to props and store it into db
	TOObject ret = null;
	TOObjectsManager objManager = new TOObjectsManager();
	TOObjectPropertyManager propsManager = new TOObjectPropertyManager();
	TOPropertyManager propMngr = new TOPropertyManager();
	//get templateId  from object
	TemplateId templateIdAnnotation = objToStore.getClass().getAnnotation(TemplateId.class);
	if (templateIdAnnotation != null)
	{
	    Integer tId = Integer.valueOf(templateIdAnnotation.id());
	    ret = objManager.createNewObject(tId);
	    //prosManager.createDefaultPropsForObject(tId, retId, storedProc);
	    List<TOObjectProperty> props = new ArrayList<TOObjectProperty>();
	    TOObjectTemplateManager templateManger = new TOObjectTemplateManager();
	    for (Method m : objToStore.getClass().getMethods())
	    {
		if (m.isAnnotationPresent(PropGetter.class))
		{
		    //get method getter and set to retObj info from obj
		    Integer propId = m.getAnnotation(PropGetter.class).id();
		    TOProperty prop = propMngr.findById(propId);
		    Integer propType = m.getAnnotation(PropGetter.class).type();
		    Object val = null;
		    if (Boolean.valueOf(m.getAnnotation(PropGetter.class).autoincrement()))
		    {
			Integer start = Integer.valueOf(m.getAnnotation(PropGetter.class).startNum());
			Long count = objManager.getObjectsCountByTemplateId(templateManger.findById(tId));
			if (count == 0)
			{
			    count = new Long(start);
			} else
			{
			    count += 1;
			}
			val = count;
		    } else
		    {
			try
			{
			    val = m.invoke(objToStore, new Object[]
				    {
				    });
			} catch (IllegalAccessException ex)
			{
			    Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE, null, ex);
			} catch (IllegalArgumentException ex)
			{
			    Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE, null, ex);
			} catch (InvocationTargetException ex)
			{
			    Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE, null, ex);
			}
		    }
		    TOObjectPropsId id = new TOObjectPropsId(0, ret.getObjectId(), propId);
		    if (!storedProc)
		    {
			propsManager.createNewObjectPropertyWithValue(ret, prop, val, propType);
		    } else
		    {
			TOObjectProperty newProp = new TOObjectProperty(id, ret, prop, propType);
			TOObjectPropertyManager.setPropValue(newProp, val, propType);
			props.add(newProp);
		    }
		}
	    }
	    if (!props.isEmpty())
	    {
		propsManager.bulkCreateObjectProps(props);
	    }
	}
	return ret;
    }

    public void updateObject(T objToStore, Integer oId)
    {
	TOObjectsManager objManager = new TOObjectsManager();
	TOObjectPropertyManager propsManager = new TOObjectPropertyManager();
	TOPropertyManager propMngr = new TOPropertyManager();
	TemplateId templateIdAnnotation = objToStore.getClass().getAnnotation(TemplateId.class);
	if (templateIdAnnotation != null)
	{
	    //  Integer tId = Integer.valueOf(templateIdAnnotation.id());
	    TOObject obj = objManager.findById(oId);
	    for (Method m : objToStore.getClass().getMethods())
	    {
		if (m.isAnnotationPresent(PropGetter.class))
		{
		    Integer propId = Integer.valueOf(m.getAnnotation(PropGetter.class).id());
		    Integer propType = m.getAnnotation(PropGetter.class).type();
		    Object val = null;
		    try
		    {
			val = m.invoke(objToStore, new Object[]
				{
				});

		    } catch (IllegalAccessException ex)
		    {
			Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (IllegalArgumentException ex)
		    {
			Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE, null, ex);
		    } catch (InvocationTargetException ex)
		    {
			Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE, null, ex);
		    }
		    TOProperty prop = propMngr.findById(propId);
		    propsManager.setPropertyValue(obj, prop, val, propType);
		}
	    }
	}
    }

    public List<Integer> findObjectsByField(Integer propId, Object val, Integer type)
    {
	TOObjectsManager objManager = new TOObjectsManager();
	return objManager.getObjectsByPropAndPropVal(propId, type, val);
    }
}
