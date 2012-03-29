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
    public static final int TYPE_LONG = 2;
    private Integer id;
    private String name;

    public static String getTypeValById(Integer id)
    {
	switch (id)
	{
	    case TYPE_DATE:
		return "dateval";
	    case TYPE_FLOAT:
		return "floatval";
	    case TYPE_INT:
		return "intval";
	    case TYPE_LIST:
		return "listval";
	    case TYPE_STR:
		return "strval";
	    case TYPE_TEXT:
		return "textval";
	}
	return "strval";
    }

    public String getPropTypeName()
    {
	return name;
    }

    public void setPropTypeName(String propTypeName)
    {
	this.name = propTypeName;
    }

    public Integer getPropTypeId()
    {
	return id;
    }

    public TOPropertyType()
    {
	id = 0;
	name = "";
    }
}
