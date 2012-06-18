package ru.terraobjects.entity.dao;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import ru.terraobjects.entity.TOObjectTemplate;
import ru.terraobjects.entity.annotations.PropGetter;
import ru.terraobjects.entity.annotations.TemplateId;
import ru.terraobjects.entity.manager.TOObjectTemplateManager;
import ru.terraobjects.entity.manager.TOObjectTemplatePropertyManager;

/**
 *
 * @author terranz
 */
public class TOTemplateHelper
{
    private TOObjectTemplatePropertyManager templatePropertyManager;
    private TOObjectTemplateManager templateManager;

    public TOTemplateHelper()
    {
        templatePropertyManager = new TOObjectTemplatePropertyManager();
        templateManager = new TOObjectTemplateManager();
    }

    //TODO: class -> template+props
    public TOObjectTemplate createTemplateFromClass(Class dtoClass) throws InstantiationException, IllegalAccessException
    {
        //load class, go thru annotations and create template with templateProps
        if (dtoClass.isAnnotationPresent(TemplateId.class))
        {
            //ok, template id is set
            Integer templateId = new Integer(((TemplateId) dtoClass.getAnnotation(TemplateId.class)).id());
            System.out.println("TOTemplateHelper: templateid is " + templateId);
            if (templateManager.getTemplate(templateId) != null)
            {
                Logger.getLogger(TOTemplateHelper.class.getName()).log(Level.SEVERE, null,
                        "Template with id " + templateId + " already exists!");
                return null;
            } else
            {
                TOObjectTemplate newTemplate = templateManager.createTemplate(dtoClass.getSimpleName(), templateId, null);
                //System.out.println("NewTemplate id " + newTemplate.getId());
                Object dto = dtoClass.newInstance();
                for (Method m : dto.getClass().getMethods())
                {

                    if (m.isAnnotationPresent(PropGetter.class))
                    {
                        System.out.println(m.getName());
                        Integer propId = new Integer(m.getAnnotation(PropGetter.class).id());
                        System.out.println("prop id " + propId);
                        String name = "".equals(m.getAnnotation(PropGetter.class).name()) ? m.getName() : m.getAnnotation(PropGetter.class).name();
                        System.out.println("name: " + name);
                        System.out.println("New Property: " + templatePropertyManager.createNewTemplateProperty(m.getReturnType(), propId, newTemplate, name).getObjectTemplatePropsId());
                    }
                }
                return newTemplate;
            }
        }
        return null;
    }

    //TODO: template+props -> class file
    public void templateToClass(Integer templateId, String fileName)
    {
    }

    //TODO: template+props -> class string
    public String templateToClass(Integer templateId)
    {
        return "";
    }
}
