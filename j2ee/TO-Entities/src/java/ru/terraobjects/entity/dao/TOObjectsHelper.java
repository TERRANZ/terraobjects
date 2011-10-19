package ru.terraobjects.entity.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.entity.annotations.PropGetter;
import ru.terraobjects.entity.annotations.PropSetter;
import ru.terraobjects.entity.annotations.TemplateId;

/**
 *
 * @author terranz
 */
public class TOObjectsHelper
{

    public Object loadObject(Class objectClass, Integer id)
    {
        Object retObj = null;
        try
        {
            //loading object from db with props and construct new Object
            retObj = objectClass.newInstance();
            TOPropertiesManager propsManager = new TOPropertiesManager();
            TOObjectsManager objectsManager = new TOObjectsManager();
            Integer objId = objectsManager.getObject(id).getObjectId();
            if (objId != null)
            {
                for (Method m : retObj.getClass().getMethods())
                {
                    if (m.isAnnotationPresent(PropSetter.class))
                    {
                        //get method setter and set to retObj info from obj
                        Integer propId = Integer.valueOf(m.getAnnotation(PropSetter.class).id());
                        m.invoke(retObj, propsManager.getPropertyValue(objId, propId));
                    }
                }
            } else
            {
                Logger.getLogger(TOObjectsHelper.class.getName()).log(Level.SEVERE,
                        "Can't load obj from DB, id: " + id.toString(), new Object[]
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

    public Integer storeObject(Object objToStore)
    {
        //deconstruct object to props and store it into db
        Integer retId = null;
        TOObjectsManager objManager = new TOObjectsManager();
        TOPropertiesManager prosManager = new TOPropertiesManager();
        //get templateId  from object
        TemplateId templateIdAnnotation = objToStore.getClass().getAnnotation(TemplateId.class);
        if (templateIdAnnotation != null)
        {
            Integer tId = Integer.valueOf(templateIdAnnotation.id());
            retId = objManager.createNewObject(tId).getObjectId();
            prosManager.createDefaultPropsForObject(tId, retId);
            for (Method m : objToStore.getClass().getMethods())
            {
                if (m.isAnnotationPresent(PropGetter.class))
                {
                    //get method setter and set to retObj info from obj
                    Integer propId = Integer.valueOf(m.getAnnotation(PropGetter.class).id());
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
                    prosManager.setPropertyValue(retId, propId, val);
                }
            }
        }
        return retId;
    }
}
