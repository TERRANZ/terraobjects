package ru.terraobjects.entity.manager;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import ru.terraobjects.entity.EntityCache;
import ru.terraobjects.entity.PersistanceManager;
import ru.terraobjects.entity.TOObjectTemplate;

/**
 *
 * @author terranz
 */
public class TOObjectTemplateManager extends PersistanceManager<TOObjectTemplate>
{

    @Override
    public TOObjectTemplate findById(Integer id)
    {
        TOObjectTemplate ret = EntityCache.getInstance().getTemplateFromCache(id);
        if (ret == null)
        {
             ret = (TOObjectTemplate) session.createCriteria(TOObjectTemplate.class).add(Restrictions.eq("objectTemplateId", id)).uniqueResult();
             EntityCache.getInstance().addTemplateToCache(id, ret);
        }
        return ret;
    }

    public TOObjectTemplate getTemplate(Integer templateId)
    {
        return findById(templateId);
    }

    public List<TOObjectTemplate> getAllTemplates()
    {
        return session.createCriteria(TOObjectTemplate.class).list();
    }

    public TOObjectTemplate createTemplate(String name, Integer templateId, Integer parentId)
    {
        TOObjectTemplate newTemplate = new TOObjectTemplate();
        newTemplate.setObjectTemplateId(templateId == null ? 0 : templateId);
        newTemplate.setParentObjectTemplateId(parentId == null ? 0 : parentId);
        newTemplate.setObjectTemplateName(name);
        insert(newTemplate);
        return newTemplate;
    }

    public List<TOObjectTemplate> getAllTemplatesFromParent(Integer parentId)
    {
        return session.createCriteria(TOObjectTemplate.class).add(Restrictions.eq("parentObjectTemplateId", parentId)).list();
    }

    public void removeTemplate(Integer templateId, Boolean cascade)
    {
        if (cascade)
        {
            List<TOObjectTemplate> childs = getAllTemplatesFromParent(templateId);
            if (childs != null)
            {
                for (TOObjectTemplate t : childs)
                {
                    removeTemplate(t.getObjectTemplateId(), true);
                }
            }
            removeTemplate(templateId, false);
        }
        else
        {
            delete(findById(templateId));
        }
    }
}
