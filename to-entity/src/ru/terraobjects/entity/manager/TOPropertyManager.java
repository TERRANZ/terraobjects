package ru.terraobjects.entity.manager;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import ru.terraobjects.entity.EntityCache;
import ru.terraobjects.entity.PersistanceManager;
import ru.terraobjects.entity.TOPropType;
import ru.terraobjects.entity.TOProperty;

/**
 *
 * @author terranz
 */
public class TOPropertyManager extends PersistanceManager<TOProperty>
{

    @Override
    public TOProperty findById(Integer id)
    {
        TOProperty ret = EntityCache.getInstance().getProperty(id);
        if (ret == null)
        {
            ret = (TOProperty) session.createCriteria(TOProperty.class).add(Restrictions.eq("propId", id)).uniqueResult();
            EntityCache.getInstance().addProperty(id, ret);
        }
        return ret;
    }

    public List<TOProperty> getProperties()
    {
        return session.createCriteria(TOProperty.class).list();
    }

    public TOProperty getProperty(Integer propId)
    {
        return findById(propId);
    }

    public TOProperty createNewProperty(TOPropType propType, String defVal, String name, Integer propId)
    {
        TOProperty newProp = new TOProperty();
        newProp.setPropType(propType);
        newProp.setPropDefvalue(defVal);
        newProp.setPropName(name);
        insert(newProp);
        return newProp;
    }
}
