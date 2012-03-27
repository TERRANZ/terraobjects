package ru.terraobjects.entity.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.entity.TOObject;
import ru.terraobjects.entity.TOObjectProperty;
import ru.terraobjects.entity.annotations.PropGetter;
import ru.terraobjects.entity.annotations.PropSetter;
import ru.terraobjects.entity.annotations.TemplateId;

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
        TOObjectsManager objectsManager = new TOObjectsManager(conn);
        if (objectClass.isAnnotationPresent(TemplateId.class))
        {
            Integer tId = new Integer(((TemplateId) (objectClass.getAnnotation(TemplateId.class))).id());
            if (tId != null)
            {
                List<TOObject> objects = objectsManager.getAllObjsByTemplId(tId);
                ArrayList<T> ret = new ArrayList<T>();
                for (TOObject o : objects)
                {
                    ret.add(loadObject(objectClass, o.getId()));
                }
                return ret;
            }
        }
        return null;
        //throw new RuntimeException("Can't load objects from DB");
    }

    public List<T> loadObjects(Class objectClass, Integer page, Integer perPage)
    {
        TOObjectsManager objectsManager = new TOObjectsManager(conn);
        if (objectClass.isAnnotationPresent(TemplateId.class))
        {
            Integer tId = new Integer(((TemplateId) (objectClass.getAnnotation(TemplateId.class))).id());
            if (tId != null)
            {
                List<TOObject> objects = objectsManager.getAllObjsByTemplId(tId, page, perPage);
                ArrayList<T> ret = new ArrayList<T>();
                for (TOObject o : objects)
                {
                    ret.add(loadObject(objectClass, o.getId()));
                }
                return ret;
            }
        }
        return null;
        //throw new RuntimeException("Can't load objects from DB");
    }

    public T loadObject(Class objectClass, Integer objectId)
    {
        T retObj = null;
        try
        {
            //loading object from db with props and construct new Object
            retObj = (T) objectClass.newInstance();
            TOPropertiesManager propsManager = new TOPropertiesManager(conn);
            TOObjectsManager objectsManager = new TOObjectsManager(conn);
            Integer objId = objectsManager.getObject(objectId).getId();
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
                        "Can't load obj from DB, id: " + objectId.toString(), new Object[]
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

    public Integer storeObject(T objToStore, Boolean storedProc)
    {
        //deconstruct object to props and store it into db
        Integer retId = null;
        TOObjectsManager objManager = new TOObjectsManager(conn);
        TOPropertiesManager prosManager = new TOPropertiesManager(conn);
        //get templateId  from object
        TemplateId templateIdAnnotation = objToStore.getClass().getAnnotation(TemplateId.class);
        if (templateIdAnnotation != null)
        {
            Integer tId = Integer.valueOf(templateIdAnnotation.id());
            retId = objManager.createNewObject(tId).getId();
            prosManager.createDefaultPropsForObject(tId, retId, storedProc);
            for (Method m : objToStore.getClass().getMethods())
            {
                if (m.isAnnotationPresent(PropGetter.class))
                {
                    //get method getter and set to retObj info from obj
                    Integer propId = Integer.valueOf(m.getAnnotation(PropGetter.class).id());
                    Object val = null;
                    if (Boolean.valueOf(m.getAnnotation(PropGetter.class).autoincrement()))
                    {
                        Integer start = Integer.valueOf(m.getAnnotation(PropGetter.class).startNum());
                        Long count = objManager.getObjectsCountByTemplateId(tId);
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
                    prosManager.setPropertyValue(retId, propId, val);
                }
            }
        }
        return retId;
    }

    public void updateObject(T objToStore, Integer oId)
    {
        //TOObjectsManager objManager = new TOObjectsManager(conn);
        TOPropertiesManager prosManager = new TOPropertiesManager(conn);
        TemplateId templateIdAnnotation = objToStore.getClass().getAnnotation(TemplateId.class);
        if (templateIdAnnotation != null)
        {
            //  Integer tId = Integer.valueOf(templateIdAnnotation.id());
            for (Method m : objToStore.getClass().getMethods())
            {
                if (m.isAnnotationPresent(PropGetter.class))
                {
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
                    prosManager.setPropertyValue(oId, propId, val);
                }
            }
        }
    }

    public List<Integer> findObjectsByField(Integer templateId, Integer propId, Object val)
    {
        TOObjectsManager objManager = new TOObjectsManager(conn);
        TOPropertiesManager propManager = new TOPropertiesManager(conn);
        List<TOObject> objectsByTemplate = objManager.getObjectsByTemplateAndProp(templateId, propId);
        List<Integer> ret = new ArrayList<Integer>();
        for (TOObject obj : objectsByTemplate)
        {
            Integer oid = obj.getId();
            if (propManager.getPropertyValue(oid, propId) == val)
            {
                ret.add(oid);
            }
        }
        return ret;
    }
}
