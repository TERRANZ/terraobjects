package ru.terraobjects.entity.manager;

import java.util.Date;
import java.util.List;
import org.hibernate.criterion.Restrictions;
import ru.terraobjects.entity.PersistanceManager;
import ru.terraobjects.entity.TOObjectTemplate;
import ru.terraobjects.entity.TOObjectTemplateProperty;
import ru.terraobjects.entity.TOProperty;
import ru.terraobjects.entity.TOPropertyType;

/**
 *
 * @author terranz
 */
public class TOObjectTemplatePropertyManager extends PersistanceManager<TOObjectTemplateProperty>
{

    @Override
    public TOObjectTemplateProperty findById(Integer id)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TOObjectTemplateProperty createNewTemplateProperty(Class propTypeClass, Integer propId, TOObjectTemplate template, String name)
    {
        TOObjectTemplateProperty newTemplateProp = getObjectTemplateProp(propId);
        if (newTemplateProp == null)
        {
            newTemplateProp = new TOObjectTemplateProperty();
        }
        else
        {
            return newTemplateProp;
        }

        Integer type = 0;
        if (propTypeClass.equals(String.class))
        {
            type = TOPropertyType.TYPE_STR;
        }
        else if (propTypeClass.equals(Integer.class))
        {
            type = TOPropertyType.TYPE_INT;
        }
        else if (propTypeClass.equals(Float.class))
        {
            type = TOPropertyType.TYPE_FLOAT;
        }
        else if (propTypeClass.equals(Date.class))
        {
            type = TOPropertyType.TYPE_DATE;
        }
        else if (propTypeClass.equals(Long.class) || propTypeClass.equals(long.class))
        {
            type = TOPropertyType.TYPE_LONG;
        }
        TOPropertyManager mngr = new TOPropertyManager();
        TOProperty newProp = mngr.createNewProperty(new TOPropertyTypeManager().findById(type), "", name, propId);

        newTemplateProp.setObjectTemplatePropsId(propId);
        newTemplateProp.setObjectTemplate(template);
        newTemplateProp.setProperty(newProp);
        insert(newTemplateProp);
        return newTemplateProp;
    }

    public TOObjectTemplateProperty getObjectTemplateProp(Integer prop)
    {
        return (TOObjectTemplateProperty) session.createCriteria(TOObjectTemplateProperty.class).add(Restrictions.eq("property.propId", prop)).uniqueResult();
    }

    public List<TOObjectTemplateProperty> getObjectTemplatePropsByTemplate(TOObjectTemplate template)
    {
        return session.createCriteria(TOObjectTemplateProperty.class).add(Restrictions.eq("objectTemplate", template)).list();
    }
}
