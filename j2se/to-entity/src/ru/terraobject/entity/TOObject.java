package ru.terraobject.entity;

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

    private Integer ObjectId;
    private Integer ObjectParentId;
    private Integer ObjectTemplateId;
    private Date ObjectCreatedAt;
    private Date ObjectUpdatedAt;

    @Column(name = "object_created_at")
    public Date getObjectCreatedAt()
    {
	return ObjectCreatedAt;
    }

    @Column(name = "object_id")
    public Integer getObjectId()
    {
	return ObjectId;
    }

    @Column(name = "parent_object_id")
    public Integer getObjectParentId()
    {
	return ObjectParentId;
    }

    @Column(name = "object_template_id")
    public Integer getObjectTemplateId()
    {
	return ObjectTemplateId;
    }

    @Column(name = "object_updated_at")
    public Date getObjectUpdatedAt()
    {
	return ObjectUpdatedAt;
    }

    public void setObjectCreatedAt(Date ObjectCreatedAt)
    {
	this.ObjectCreatedAt = ObjectCreatedAt;
    }

    public void setObjectId(Integer ObjectId)
    {
	this.ObjectId = ObjectId;
    }

    public void setObjectParentId(Integer ObjectParentId)
    {
	this.ObjectParentId = ObjectParentId;
    }

    public void setObjectTemplateId(Integer ObjectTemplatePropsId)
    {
	this.ObjectTemplateId = ObjectTemplatePropsId;
    }

    public void setObjectUpdatedAt(Date ObjectUpdatedAt)
    {
	this.ObjectUpdatedAt = ObjectUpdatedAt;
    }
}
