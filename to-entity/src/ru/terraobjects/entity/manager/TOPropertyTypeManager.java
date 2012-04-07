package ru.terraobjects.entity.manager;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import ru.terraobjects.entity.PersistanceManager;
import ru.terraobjects.entity.TOPropType;

/**
 *
 * @author terranz
 */
public class TOPropertyTypeManager extends PersistanceManager<TOPropType>
{

    @Override
    public TOPropType findById(Integer id)
    {
        return (TOPropType) session.createCriteria(TOPropType.class).add(Restrictions.eq("propTypeId", id)).uniqueResult();
    }

    public List<TOPropType> getPropertyTypes()
    {
        return session.createCriteria(TOPropType.class).list();
    }

    public TOPropType getPropertyType(Integer propId)
    {
        return findById(propId);
    }
}
