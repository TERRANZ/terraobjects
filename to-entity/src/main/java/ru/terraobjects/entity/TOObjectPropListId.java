package ru.terraobjects.entity;
// Generated 07.04.2012 15:38:17 by Hibernate Tools 3.2.1.GA


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ObjectPropListId generated by hbm2java
 */
@Embeddable
public class TOObjectPropListId implements java.io.Serializable {


    private int objectPropertyListId;
    private int objectId;

    public TOObjectPropListId() {
    }

    public TOObjectPropListId(int objectPropertyListId, int objectId) {
        this.objectPropertyListId = objectPropertyListId;
        this.objectId = objectId;
    }


    @Column(name = "object_property_list_id", nullable = false)
    public int getObjectPropertyListId() {
        return this.objectPropertyListId;
    }

    public void setObjectPropertyListId(int objectPropertyListId) {
        this.objectPropertyListId = objectPropertyListId;
    }

    @Column(name = "object_id", nullable = false)
    public int getObjectId() {
        return this.objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }


    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof TOObjectPropListId)) return false;
        TOObjectPropListId castOther = (TOObjectPropListId) other;

        return (this.getObjectPropertyListId() == castOther.getObjectPropertyListId())
                && (this.getObjectId() == castOther.getObjectId());
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getObjectPropertyListId();
        result = 37 * result + this.getObjectId();
        return result;
    }


}


