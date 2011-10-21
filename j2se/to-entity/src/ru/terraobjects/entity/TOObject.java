package ru.terraobjects.entity;

import java.util.Date;
import net.sf.persist.annotations.Column;
import net.sf.persist.annotations.Table;

/**
 *
 * @author terranz
 */
@Table(name = "object")
public class TOObject
{

    private Integer objectId;
    private Integer objectParentId;
    private Integer objectTemplateId;
    private Date objectCreatedAt;
    private Date objectUpdatedAt;

    @Column(name = "object_created_at")
    public Date getObjectCreatedAt()
    {
	return objectCreatedAt;
    }

    @Column(name = "object_id")
    public Integer getObjectId()
    {
	return objectId;
    }

    @Column(name = "parent_object_id")
    public Integer getObjectParentId()
    {
	return objectParentId;
    }

    @Column(name = "object_template_id")
    public Integer getObjectTemplateId()
    {
	return objectTemplateId;
    }

    @Column(name = "object_updated_at")
    public Date getObjectUpdatedAt()
    {
	return objectUpdatedAt;
    }

    public void setObjectCreatedAt(Date objectCreatedAt)
    {
	this.objectCreatedAt = objectCreatedAt;
    }

    public void setObjectId(Integer objectId)
    {
	this.objectId = objectId;
    }

    public void setParentId(Integer objectParentId)
    {
	this.objectParentId = objectParentId;
    }

    public void setObjectTemplateId(Integer objectTemplatePropsId)
    {
	this.objectTemplateId = objectTemplatePropsId;
    }

    public void setObjectUpdatedAt(Date objectUpdatedAt)
    {
	this.objectUpdatedAt = objectUpdatedAt;
    }
}
