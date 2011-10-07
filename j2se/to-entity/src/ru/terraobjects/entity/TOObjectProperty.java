package ru.terraobjects.entity;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

/**
 *
 * @author terranz
 */
@Table(name = "object_props")
public class TOObjectProperty
{

    private Integer ObjectPropertyId;
    private Integer ObjectId;
    private Integer PropertyId;
    private Integer IntVal;
    private Float FloatVal;
    private String StringVal;
    private String TextVal;

    @Column(name = "floatval")
    public Float getFloatVal()
    {
	return FloatVal;
    }

    public void setFloatVal(Float FloatVal)
    {
	this.FloatVal = FloatVal;
    }

    @Column(name = "intval")
    public Integer getIntVal()
    {
	return IntVal;
    }

    public void setIntVal(Integer IntVal)
    {
	this.IntVal = IntVal;
    }

    @Column(name = "object_id")
    public Integer getObjectId()
    {
	return ObjectId;
    }

    public void setObjectId(Integer ObjectId)
    {
	this.ObjectId = ObjectId;
    }

    @Column(name = "object_props_id")
    public Integer getObjectPropertyId()
    {
	return ObjectPropertyId;
    }

    public void setObjectPropertyId(Integer ObjectPropertyId)
    {
	this.ObjectPropertyId = ObjectPropertyId;
    }

    @Column(name = "prop_id")
    public Integer getPropertyId()
    {
	return PropertyId;
    }

    public void setPropertyId(Integer PropertyId)
    {
	this.PropertyId = PropertyId;
    }

    @Column(name = "strval")
    public String getStringVal()
    {
	return StringVal;
    }

    public void setStringVal(String StringVal)
    {
	this.StringVal = StringVal;
    }

    @Column(name = "textval")
    public String getTextVal()
    {
	return TextVal;
    }

    public void setTextVal(String TextVal)
    {
	this.TextVal = TextVal;
    }
}
