package ru.terraobjects.entity;

import net.sf.persist.annotations.Table;

/**
 *
 * @author terranz
 */
@Table(name = "object_template")
public class TOObjectTemplate {

    private Integer objectTemplateId;
    private String objectTemplateName;
    private Integer parentObjectTemplateId;

    public Integer getParentObjectTemplateId()
    {
	return parentObjectTemplateId;
    }

    public void setParentObjectTemplateId(Integer ParentObjectTemplateId)
    {
	this.parentObjectTemplateId = ParentObjectTemplateId;
    }

    public Integer getObjectTemplateId()
    {
	return objectTemplateId;
    }

    public void setObjectTemplateId(Integer ObjectTemplateId)
    {
	this.objectTemplateId = ObjectTemplateId;
    }

    public String getObjectTemplateName()
    {
	return objectTemplateName;
    }

    public void setObjectTemplateName(String ObjectTemplateName)
    {
	this.objectTemplateName = ObjectTemplateName;
    }
    
}
