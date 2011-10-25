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

    private Integer id;
    private Integer parentId = 0;
    private Integer templateId;
    private Date createdAt;
    private Date updatedAt;

    @Column(name = "object_created_at")
    public Date getCreatedAt()
    {
	return createdAt;
    }

    @Column(name = "object_id")
    public Integer getId()
    {
	return id;
    }

    @Column(name = "parent_object_id")
    public Integer getParentId()
    {
	return parentId;
    }

    @Column(name = "object_template_id")
    public Integer getTemplateId()
    {
	return templateId;
    }

    @Column(name = "object_updated_at")
    public Date getUpdatedAt()
    {
	return updatedAt;
    }

    public void setCreatedAt(Date objectCreatedAt)
    {
	this.createdAt = objectCreatedAt;
    }

    public void setId(Integer objectId)
    {
	this.id = objectId;
    }

    public void setParentId(Integer objectParentId)
    {
	this.parentId = objectParentId;
    }

    public void setTemplateId(Integer objectTemplatePropsId)
    {
	this.templateId = objectTemplatePropsId;
    }

    public void setUpdatedAt(Date objectUpdatedAt)
    {
	this.updatedAt = objectUpdatedAt;
    }
}
