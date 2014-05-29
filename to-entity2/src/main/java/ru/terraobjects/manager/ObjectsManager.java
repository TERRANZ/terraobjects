package ru.terraobjects.manager;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.terraobjects.entity.ObjectFields;
import ru.terraobjects.entity.TObject;
import ru.terraobjects.entity.controller.ObjectFieldsJpaController;
import ru.terraobjects.entity.controller.TObjectJpaController;

import javax.persistence.EntityManagerFactory;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Persistence;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Date: 29.05.14
 * Time: 16:27
 */
public class ObjectsManager<T> {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("to2pu");
    private TObjectJpaController objectJpaController = new TObjectJpaController(emf);
    private ObjectFieldsJpaController objectFieldsJpaController = new ObjectFieldsJpaController(emf);
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public ObjectsManager() {
    }

    public void saveOrUpdate(T entity) throws Exception {
        String name = entity.getClass().getName();
        GenerationType generationType = GenerationType.IDENTITY;
        Object id = null;
        String idFieldName = "";
        TObject object = null;
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.getAnnotation(Id.class) != null)
                try {
                    id = PropertyUtils.getProperty(entity, field.getName());
                    idFieldName = field.getName();
//
// GeneratedValue genval = field.getAnnotation(GeneratedValue.class);
//                    if (genval != null)
//                        generationType = genval.strategy();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
        }

        if (id != null)
            object = findById(id);

        if (object == null) {
            object = new TObject();
            object.setId(0);
            object.setParent(0);
            object.setName(name);
            object.setVersion(0);
            object.setUpdated(new Date());
            object.setObjectFieldsList(new ArrayList<>());
            objectJpaController.create(object);
            final List<ObjectFields> objectFieldsList = new ArrayList<>();
            for (Field field : entity.getClass().getDeclaredFields()) {
                if (!field.isAccessible())
                    field.setAccessible(true);
                ObjectFields newObjectField = new ObjectFields(0, field.getName(), field.getType().getSimpleName().toLowerCase());
                newObjectField.setObjectId(object);
                try {
                    String fieldType = field.getType().getSimpleName().toLowerCase();
                    switch (fieldType) {
                        case "integer": {
                            if (idFieldName.equalsIgnoreCase(field.getName())) {
                                Integer newId = 0;
                                if (generationType.equals(GenerationType.IDENTITY)) {
                                    newId = objectJpaController.getCountByName(name).intValue() + 1;
                                    newObjectField.setIntval(newId);
                                    PropertyUtils.setSimpleProperty(entity, idFieldName, newId);
                                }
                            } else
                                newObjectField.setIntval((Integer) PropertyUtils.getProperty(entity, field.getName()));
                        }
                        break;
                        case "long": {
                            if (idFieldName.equalsIgnoreCase(field.getName())) {
                                Long newId = 0l;
                                if (generationType.equals(GenerationType.IDENTITY)) {
                                    newId = objectJpaController.getCountByName(name) + 1;
                                    newObjectField.setLongval(newId);
                                    PropertyUtils.setSimpleProperty(entity, idFieldName, newId);
                                }
                            } else
                                newObjectField.setLongval((Long) PropertyUtils.getProperty(entity, field.getName()));
                        }
                        break;
                        case "double": {
                            newObjectField.setFloatval((Double) PropertyUtils.getProperty(entity, field.getName()));
                        }
                        break;
                        case "date": {
                            newObjectField.setDateval((Date) PropertyUtils.getProperty(entity, field.getName()));
                        }
                        break;
                        case "string": {
                            newObjectField.setStrval((String) PropertyUtils.getProperty(entity, field.getName()));
                        }
                        break;
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                objectFieldsJpaController.create(newObjectField);
                objectFieldsList.add(newObjectField);
            }

            object.setObjectFieldsList(objectFieldsList);
        }
        objectJpaController.edit(object);
    }

    private TObject findById(Object id) {
        ObjectFields objectField = objectFieldsJpaController.findByValue(id.getClass().getSimpleName(), id);
        if (objectField != null)
            return objectField.getTObject();
        return null;
    }

    public T load(Class<T> loadClass, Object id) {
        T ret = null;
        TObject object = null;
        ObjectFields field = objectFieldsJpaController.findByValue(id.getClass().getSimpleName(), id);
        if (field == null)
            return null;
        object = field.getTObject();
        try {
            ret = loadClass.newInstance();

            for (ObjectFields of : object.getObjectFieldsList()) {
                Object value = null;
                switch (of.getType()) {
                    case "integer": {
                        value = of.getIntval();
                    }
                    break;
                    case "long": {
                        value = of.getLongval();
                    }
                    break;
                    case "double": {
                        value = of.getFloatval();
                    }
                    break;
                    case "date": {
                        value = of.getDateval();
                    }
                    break;
                    case "string": {
                        value = of.getStrval();
                    }
                    break;

                }
                PropertyUtils.setSimpleProperty(ret, of.getName(), value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
