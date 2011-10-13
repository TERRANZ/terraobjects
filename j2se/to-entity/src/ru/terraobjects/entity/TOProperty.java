package ru.terraobjects.entity;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

/**
 *
 * @author terranz
 */
@Table(name = "property")
public class TOProperty
{

    private Integer propId;
    private Integer propTypeId;
    private String propDefValue;

    @Column(name = "prop_defvalue")
    public String getPropDefValue()
    {
	return propDefValue;
    }

    public void setPropDefValue(String propDefValue)
    {
	this.propDefValue = propDefValue;
    }

    @Column(name = "prop_id")
    public Integer getPropId()
    {
	return propId;
    }

    public void setPropId(Integer propId)
    {
	this.propId = propId;
    }

    @Column(name = "prop_type_id")
    public Integer getPropTypeId()
    {
	return propTypeId;
    }

    public void setPropTypeId(Integer propTypeId)
    {
	this.propTypeId = propTypeId;
    }
}
