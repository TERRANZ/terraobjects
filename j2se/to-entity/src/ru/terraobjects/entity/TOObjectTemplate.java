package ru.terraobjects.entity;

import net.sf.persist.annotations.Table;

/**
 *
 * @author terranz
 */
@Table(name = "object_template")
public class TOObjectTemplate {

    private Integer ObjectTemplateId;
    private String ObjectTemplateName;
    private Integer ParentObjectTemplateId;

    public Integer getParentObjectTemplateId()
    {
	return ParentObjectTemplateId;
    }

    public void setParentObjectTemplateId(Integer ParentObjectTemplateId)
    {
	this.ParentObjectTemplateId = ParentObjectTemplateId;
    }

    public Integer getObjectTemplateId()
    {
	return ObjectTemplateId;
    }

    public void setObjectTemplateId(Integer ObjectTemplateId)
    {
	this.ObjectTemplateId = ObjectTemplateId;
    }

    public String getObjectTemplateName()
    {
	return ObjectTemplateName;
    }

    public void setObjectTemplateName(String ObjectTemplateName)
    {
	this.ObjectTemplateName = ObjectTemplateName;
    }
    
}
