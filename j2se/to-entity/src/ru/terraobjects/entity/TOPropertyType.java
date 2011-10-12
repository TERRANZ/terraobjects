package ru.terraobjects.entity;

/**
 *
 * @author terranz
 */
public class TOPropertyType
{

    public static final int TYPE_STR = 1;
    public static final int TYPE_INT = 2;
    public static final int TYPE_FLOAT = 3;
    public static final int TYPE_TEXT = 4;
    public static final int TYPE_DATE = 5;
    public static final int TYPE_LIST = 6;
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

    public TOPropertyType()
    {
	PropTypeId = 0;
	PropTypeName = "";
    }
}
