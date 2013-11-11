package ru.terraobjects.entity.manager;

import org.hibernate.criterion.Restrictions;
import ru.terraobjects.entity.*;

import java.util.Date;
import java.util.List;

/**
 * @author terranz
 */
public class TOObjectTemplatePropertyManager extends PersistanceManager<TOObjectTemplateProperty> {

    public TOObjectTemplatePropertyManager() {
        super();
    }

    @Override
    public TOObjectTemplateProperty findById(Integer id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public TOObjectTemplateProperty createNewTemplateProperty(TOProperty newProp, Integer propId, TOObjectTemplate template) {
        TOObjectTemplateProperty newTemplateProp = getObjectTemplateProp(propId);
        if (newTemplateProp == null) {
            newTemplateProp = new TOObjectTemplateProperty();
        } else {
            return newTemplateProp;
        }

        newTemplateProp.setObjectTemplatePropsId(propId);
        newTemplateProp.setObjectTemplate(template);
        newTemplateProp.setProperty(newProp);
        insert(newTemplateProp);
        return newTemplateProp;
    }

    public TOObjectTemplateProperty getObjectTemplateProp(Integer prop) {

        return (TOObjectTemplateProperty) session.createCriteria(TOObjectTemplateProperty.class).add(Restrictions.eq("property.propId", prop)).uniqueResult();
    }

    public List<TOObjectTemplateProperty> getObjectTemplatePropsByTemplate(TOObjectTemplate template) {
        return session.createCriteria(TOObjectTemplateProperty.class).add(Restrictions.eq("objectTemplate", template)).list();
    }

}
