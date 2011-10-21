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

    private Integer id;
    private Integer typeId;
    private String defValue;

    @Column(name = "prop_defvalue")
    public String getDefValue()
    {
        return defValue;
    }

    public void setDefValue(String propDefValue)
    {
        this.defValue = propDefValue;
    }

    @Column(name = "prop_id")
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer propId)
    {
        this.id = propId;
    }

    @Column(name = "prop_type_id")
    public Integer getTypeId()
    {
        return typeId;
    }

    public void setTypeId(Integer propTypeId)
    {
        this.typeId = propTypeId;
    }
}