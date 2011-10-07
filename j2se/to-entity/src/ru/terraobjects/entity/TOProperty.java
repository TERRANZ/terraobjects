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

    private Integer PropId;
    private Integer PropTypeId;
    private String PropDefValue;

    @Column(name = "prop_defvalue")
    public String getPropDefValue()
    {
	return PropDefValue;
    }

    public void setPropDefValue(String PropDefValue)
    {
	this.PropDefValue = PropDefValue;
    }

    @Column(name = "prop_id")
    public Integer getPropId()
    {
	return PropId;
    }

    public void setPropId(Integer PropId)
    {
	this.PropId = PropId;
    }

    @Column(name = "prop_type_id")
    public Integer getPropTypeId()
    {
	return PropTypeId;
    }

    public void setPropTypeId(Integer PropTypeId)
    {
	this.PropTypeId = PropTypeId;
    }
}
