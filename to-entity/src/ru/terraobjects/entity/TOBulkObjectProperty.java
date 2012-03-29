package ru.terraobjects.entity;

/**
 *
 * @author terranz
 */
public class TOBulkObjectProperty
{
    private Integer type, objectId, propId;
    private Object value;

    public Integer getObjectId()
    {
	return objectId;
    }

    public void setObjectId(Integer objectId)
    {
	this.objectId = objectId;
    }

    public Integer getPropId()
    {
	return propId;
    }

    public void setPropId(Integer propId)
    {
	this.propId = propId;
    }

    public Integer getType()
    {
	return type;
    }

    public void setType(Integer type)
    {
	this.type = type;
    }

    public Object getValue()
    {
	return value;
    }

    public void setValue(Object value)
    {
	this.value = value;
    }

    public TOBulkObjectProperty(Integer type, Integer objectId, Integer propId, Object value)
    {
	this.type = type;
	this.objectId = objectId;
	this.propId = propId;
	this.value = value;
    }
}
