/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.terraobjects.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author terranz
 */
@Entity
@Table(name = "object", catalog = "to2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TObject.findAll", query = "SELECT t FROM TObject t"),
    @NamedQuery(name = "TObject.findById", query = "SELECT t FROM TObject t WHERE t.id = :id"),
    @NamedQuery(name = "TObject.findByName", query = "SELECT t FROM TObject t WHERE t.name = :name"),
    @NamedQuery(name = "TObject.findByCreated", query = "SELECT t FROM TObject t WHERE t.created = :created"),
    @NamedQuery(name = "TObject.findByVersion", query = "SELECT t FROM TObject t WHERE t.version = :version"),
    @NamedQuery(name = "TObject.findByParent", query = "SELECT t FROM TObject t WHERE t.parent = :parent"),
    @NamedQuery(name = "TObject.findByUpdated", query = "SELECT t FROM TObject t WHERE t.updated = :updated")})
public class TObject implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 512)
    private String name;
    @Basic(optional = false)
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @Basic(optional = false)
    @Column(nullable = false)
    private int version;
    private Integer parent;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "objectId")
    private List<ObjectFields> objectFieldsList;

    public TObject() {
    }

    public TObject(Integer id) {
        this.id = id;
    }

    public TObject(Integer id, String name, Date created, int version) {
        this.id = id;
        this.name = name;
        this.created = created;
        this.version = version;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @XmlTransient
    public List<ObjectFields> getObjectFieldsList() {
        return objectFieldsList;
    }

    public void setObjectFieldsList(List<ObjectFields> objectFieldsList) {
        this.objectFieldsList = objectFieldsList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TObject)) {
            return false;
        }
        TObject other = (TObject) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.terraobjects.entity.TObject[ id=" + id + " ]";
    }
    
}
