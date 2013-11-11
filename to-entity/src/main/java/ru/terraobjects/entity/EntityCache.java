package ru.terraobjects.entity;

import java.util.HashMap;
import java.util.List;

/**
 * @author terranz
 */
public class EntityCache {

    private static EntityCache instance = new EntityCache();
    private HashMap<Integer, TOObjectTemplate> templateCache = new HashMap<Integer, TOObjectTemplate>();
    private HashMap<Integer, List<TOObjectTemplateProperty>> templatePropsCache = new HashMap<Integer, List<TOObjectTemplateProperty>>();
    private HashMap<Integer, TOProperty> propertyCache = new HashMap<Integer, TOProperty>();
    private HashMap<Integer, TOPropertyType> propertyTypeCache = new HashMap<Integer, TOPropertyType>();

    public static EntityCache getInstance() {
        return instance;
    }

    public TOObjectTemplate getTemplateFromCache(Integer id) {
        return templateCache.get(id);
    }

    public void addTemplateToCache(Integer id, TOObjectTemplate val) {
        templateCache.put(id, val);
    }

    public List<TOObjectTemplateProperty> getTemplatePropsFromCache(Integer id) {
        return templatePropsCache.get(id);
    }

    public void addTemplatePropsToCache(Integer id, List<TOObjectTemplateProperty> val) {
        templatePropsCache.put(id, val);
    }

    public TOPropertyType getPropertyTypeFromCache(Integer propId) {
        return propertyTypeCache.get(propId);
    }

    public void addPropertyTypeToCache(Integer propertyType, TOPropertyType val) {
        propertyTypeCache.put(propertyType, val);
    }

    public TOProperty getProperty(Integer id) {
        return propertyCache.get(id);
    }

    public void addProperty(Integer id, TOProperty prop) {
        propertyCache.put(id, prop);
    }
}
