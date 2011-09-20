package ru.terraobject.entity;

/**
 *
 * @author terranz
 */
public class TOPropertyType
{

    private Integer PropTypeId;
    private String PropTypeName;

    public String getPropTypeName()
    {
	return PropTypeName;
    }

    public void setPropTypeName(String PropTypeName)
    {
	this.PropTypeName = PropTypeName;
    }

    public Integer getPropTypeId()
    {
	return PropTypeId;
    }

    public void setPropTypeId(Integer PropTypeId)
    {
	this.PropTypeId = PropTypeId;
    }

    public TOPropertyType()
    {
	PropTypeId = 0;
	PropTypeName = "";
    }
}
