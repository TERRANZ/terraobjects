package ru.terraobject.entity.dao;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import ru.terraobject.entity.TOObjectProperty;
import ru.terraobject.entity.TOObjectTemplate;
import ru.terraobject.entity.TOObjectTemplateProperty;
import ru.terraobject.entity.TOProperty;

/**
 *
 * @author terranz
 */
public class EntityCache
{

    private static EntityCache instance = new EntityCache();
    private HashMap<Integer, TOObjectTemplate> templateCache = new HashMap<Integer, TOObjectTemplate>();
    private HashMap<Integer, List<TOObjectTemplateProperty>> templatePropsCache = new HashMap<Integer, List<TOObjectTemplateProperty>>();
    private HashMap<Integer, TOProperty> propertyCache = new HashMap<Integer, TOProperty>();
    private HashMap<Pair, TOObjectProperty> objectPropsCache = new HashMap<Pair, TOObjectProperty>();

    public static EntityCache getInstance()
    {
	return instance;
    }

    public TOObjectTemplate getTemplateFromCache(Integer id)
    {
	return templateCache.get(id);
    }

    public void addTemplateToCache(Integer id, TOObjectTemplate val)
    {
	templateCache.put(id, val);
    }

    public List<TOObjectTemplateProperty> getTemplatePropsFromCache(Integer id)
    {
	return templatePropsCache.get(id);
    }

    public void addTemplatePropsToCache(Integer id, List<TOObjectTemplateProperty> val)
    {
	templatePropsCache.put(id, val);
    }

    public TOProperty getPropertyFromCache(Integer id)
    {
	return propertyCache.get(id);
    }

    public void addPropertyToCache(Integer id, TOProperty val)
    {
	propertyCache.put(id, val);
    }

    public TOObjectProperty getObjectPropertyFromCache(Pair key)
    {
	return objectPropsCache.get(key);
    }

    public void addObjectPropertyToCache(Pair key, TOObjectProperty val)
    {
	objectPropsCache.put(key, val);
    }
}