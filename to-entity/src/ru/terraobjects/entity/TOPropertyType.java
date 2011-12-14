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
    private Integer id;
    private String name;

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
