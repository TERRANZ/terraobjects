/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.terraobjects.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author terranz
 */
@Entity
@Table(name = "object_fields", catalog = "to2", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ObjectFields.findAll", query = "SELECT o FROM ObjectFields o"),
    @NamedQuery(name = "ObjectFields.findById", query = "SELECT o FROM ObjectFields o WHERE o.id = :id"),
    @NamedQuery(name = "ObjectFields.findByName", query = "SELECT o FROM ObjectFields o WHERE o.name = :name"),
    @NamedQuery(name = "ObjectFields.findByType", query = "SELECT o FROM ObjectFields o WHERE o.type = :type"),
    @NamedQuery(name = "ObjectFields.findByintval", query = "SELECT o FROM ObjectFields o WHERE o.intval = :val"),
    @NamedQuery(name = "ObjectFields.findBylongval", query = "SELECT o FROM ObjectFields o WHERE o.longval = :val"),
    @NamedQuery(name = "ObjectFields.findBystrval", query = "SELECT o FROM ObjectFields o WHERE o.strval = :val"),
    @NamedQuery(name = "ObjectFields.findByfloatval", query = "SELECT o FROM ObjectFields o WHERE o.floatval = :val"),
    @NamedQuery(name = "ObjectFields.findBydateval", query = "SELECT o FROM ObjectFields o WHERE o.dateval = :val"),
    @NamedQuery(name = "ObjectFields.findBylistval", query = "SELECT o FROM ObjectFields o WHERE o.listval = :val")})
public class ObjectFields implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(nullable = false, length = 256)
    private String name;
    @Basic(optional = false)
    @Column(nullable = false, length = 64)
    private String type;
    private Integer intval;
    private Long longval;
    @Column(length = 2048)
    private String strval;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(precision = 22)
    private Double floatval;
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateval;
    @Column(length = 4096)
    private String listval;
    @JoinColumn(name = "object_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false)
    private TObject objectId;

    public ObjectFields() {
    }

    public ObjectFields(Integer id) {
        this.id = id;
    }

    public ObjectFields(Integer id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getIntval() {
        return intval;
    }

    public void setIntval(Integer intval) {
        this.intval = intval;
    }

    public Long getLongval() {
        return longval;
    }

    public void setLongval(Long longval) {
        this.longval = longval;
    }

    public String getStrval() {
        return strval;
    }

    public void setStrval(String strval) {
        this.strval = strval;
    }

    public Double getFloatval() {
        return floatval;
    }

    public void setFloatval(Double floatval) {
        this.floatval = floatval;
    }

    public Date getDateval() {
        return dateval;
    }

    public void setDateval(Date dateval) {
        this.dateval = dateval;
    }

    public String getListval() {
        return listval;
    }

    public void setListval(String listval) {
        this.listval = listval;
    }

    public TObject getTObject() {
        return objectId;
    }

    public void setObjectId(TObject objectId) {
        this.objectId = objectId;
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
        if (!(object instanceof ObjectFields)) {
            return false;
        }
        ObjectFields other = (ObjectFields) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.terraobjects.entity.ObjectFields[ id=" + id + " ]";
    }
    
}
