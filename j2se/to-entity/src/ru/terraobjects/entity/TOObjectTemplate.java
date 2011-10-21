package ru.terraobjects.entity;

import net.sf.persist.annotations.Table;

/**
 *
 * @author terranz
 */
@Table(name = "object_template")
public class TOObjectTemplate
{

    private Integer id;
    private String name;
    private Integer parentId;

    public Integer getParentId()
    {
        return parentId;
    }

    public void setParentId(Integer ParentObjectTemplateId)
    {
        this.parentId = ParentObjectTemplateId;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer ObjectTemplateId)
    {
        this.id = ObjectTemplateId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String ObjectTemplateName)
    {
        this.name = ObjectTemplateName;
    }
}
