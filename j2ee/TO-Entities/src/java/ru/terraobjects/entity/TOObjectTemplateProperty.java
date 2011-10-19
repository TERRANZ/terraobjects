package ru.terraobjects.entity;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

/**
 *
 * @author terranz
 */
@Table(name = "object_template_props")
public class TOObjectTemplateProperty
{
    private Integer objectTemplateId;    
    private Integer propId;
    private Integer objectTemplatePropsId;

    @Column(name="object_template_id")    
    public Integer getObjectTemplateId()
    {
	return objectTemplateId;
    }

    public void setObjectTemplateId(Integer objectTemplateId)
    {
	this.objectTemplateId = objectTemplateId;
    }

    @Column(name="object_template_props_id")
    public Integer getObjectTemplatePropertyId()
    {
	return objectTemplatePropsId;
    }

    public void setObjectTemplatePropertyId(Integer objectTemplatePropertyId)
    {
	this.objectTemplatePropsId = objectTemplatePropertyId;
    }

    @Column(name="prop_id")
    public Integer getPropertyId()
    {
	return propId;
    }

    public void setPropertyId(Integer propertyId)
    {
	this.propId = propertyId;
    }

}