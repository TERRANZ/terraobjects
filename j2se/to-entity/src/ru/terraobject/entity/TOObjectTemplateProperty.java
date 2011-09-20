package ru.terraobject.entity;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

/**
 *
 * @author terranz
 */
@Table(name = "object_template_props")
public class TOObjectTemplateProperty
{
    private Integer ObjectTemplateId;    
    private Integer PropId;
    private Integer ObjectTemplatePropsId;

    @Column(name="object_template_id")    
    public Integer getObjectTemplateId()
    {
	return ObjectTemplateId;
    }

    public void setObjectTemplateId(Integer ObjectTemplateId)
    {
	this.ObjectTemplateId = ObjectTemplateId;
    }

    @Column(name="object_template_props_id")
    public Integer getObjectTemplatePropertyId()
    {
	return ObjectTemplatePropsId;
    }

    public void setObjectTemplatePropertyId(Integer ObjectTemplatePropertyId)
    {
	this.ObjectTemplatePropsId = ObjectTemplatePropertyId;
    }

    @Column(name="prop_id")
    public Integer getPropertyId()
    {
	return PropId;
    }

    public void setPropertyId(Integer PropertyId)
    {
	this.PropId = PropertyId;
    }

}
