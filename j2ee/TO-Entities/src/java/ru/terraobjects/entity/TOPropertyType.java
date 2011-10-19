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
    private Integer propTypeId;
    private String propTypeName;

    public String getPropTypeName()
    {
        return propTypeName;
    }

    public void setPropTypeName(String propTypeName)
    {
        this.propTypeName = propTypeName;
    }

    public Integer getPropTypeId()
    {
        return propTypeId;
    }

    public TOPropertyType()
    {
        propTypeId = 0;
        propTypeName = "";
    }
}
