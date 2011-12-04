package ru.terraobjects.entity;

import net.sf.persist.annotations.Column;
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

    @Column(name = "parent_object_template_id")
    public Integer getParentId()
    {
	return parentId;
    }

    public void setParentId(Integer ParentObjectTemplateId)
    {
	this.parentId = ParentObjectTemplateId;
    }

    @Column(name = "object_template_id")
    public Integer getId()
    {
	return id;
    }

    public void setId(Integer ObjectTemplateId)
    {
	this.id = ObjectTemplateId;
    }

    @Column(name = "Object_Template_Name")
    public String getName()
    {
	return name;
    }

    public void setName(String ObjectTemplateName)
    {
	this.name = ObjectTemplateName;
    }
}
