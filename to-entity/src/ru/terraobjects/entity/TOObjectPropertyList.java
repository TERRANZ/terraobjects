package ru.terraobjects.entity;

import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

/**
 *
 * @author terranz
 */
@Table(name = "object_prop_list")
public class TOObjectPropertyList
{

    private Integer id;
    private Integer objectId;
    private Integer listId;
    private Integer position;

    @Column(name = "list_id")
    public Integer getListId()
    {
	return listId;
    }

    public void setListId(Integer listId)
    {
	this.listId = listId;
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

    @Column(name = "object_property_list_id")
    public Integer getId()
    {
	return id;
    }

    public void setId(Integer objectPropertyListId)
    {
	this.id = objectPropertyListId;
    }

    @Column(name = "position")
    public Integer getPosition()
    {
	return position;
    }

    public void setPosition(Integer position)
    {
	this.position = position;
    }
}
