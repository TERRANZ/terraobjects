package ru.terraobjects.entity;

import java.util.Date;
import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

/**
 *
 * @author terranz
 */
@Table(name = "object_props")
public class TOObjectProperty
{

    private Integer objectPropertyId;
    private Integer objectId;
    private Integer propertyId;
    private Integer intVal;
    private Float floatVal;
    private String stringVal;
    private String textVal;
    private Date dateVal;
    private Integer listVal;

    @Column(name = "dateval")
    public Date getDateVal()
    {
	return dateVal;
    }

    public void setDateVal(Date dateVal)
    {
	this.dateVal = dateVal;
    }

    @Column(name = "listval")
    public Integer getListVal()
    {
	return listVal;
    }

    public void setListVal(Integer listVal)
    {
	this.listVal = listVal;
    }

    @Column(name = "floatval")
    public Float getFloatVal()
    {
	return floatVal;
    }

    public void setFloatVal(Float floatVal)
    {
	this.floatVal = floatVal;
    }

    @Column(name = "intval")
    public Integer getIntVal()
    {
	return intVal;
    }

    public void setIntVal(Integer intVal)
    {
	this.intVal = intVal;
    }

    @Column(name = "object_id")
    public Integer getObjectId()
    {
	return objectId;
    }

    public void setObjectId(Integer objectId)
    {
	this.objectId = objectId;
    }

    @Column(name = "object_props_id")
    public Integer getObjectPropertyId()
    {
	return objectPropertyId;
    }

    public void setObjectPropertyId(Integer objectPropertyId)
    {
	this.objectPropertyId = objectPropertyId;
    }

    @Column(name = "prop_id")
    public Integer getPropertyId()
    {
	return propertyId;
    }

    public void setPropertyId(Integer propertyId)
    {
	this.propertyId = propertyId;
    }

    @Column(name = "strval")
    public String getStringVal()
    {
	return stringVal;
    }

    public void setStringVal(String stringVal)
    {
	this.stringVal = stringVal;
    }

    @Column(name = "textval")
    public String getTextVal()
    {
	return textVal;
    }

    public void setTextVal(String textVal)
    {
	this.textVal = textVal;
    }
}
