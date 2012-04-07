package ru.terraobjects.entity;
// Generated 07.04.2012 15:38:17 by Hibernate Tools 3.2.1.GA


import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * PropType generated by hbm2java
 */
@Entity
@Table(name="prop_type"
    ,catalog="terraobjects"
)
public class TOPropType  implements java.io.Serializable {


     private Integer propTypeId;
     private String propTypeName;
     private Set<TOProperty> properties = new HashSet<TOProperty>(0);

    public TOPropType() {
    }

    public TOPropType(String propTypeName, Set<TOProperty> properties) {
       this.propTypeName = propTypeName;
       this.properties = properties;
    }
   
     @Id @GeneratedValue(strategy=IDENTITY)
    
    @Column(name="prop_type_id", unique=true, nullable=false)
    public Integer getPropTypeId() {
        return this.propTypeId;
    }
    
    public void setPropTypeId(Integer propTypeId) {
        this.propTypeId = propTypeId;
    }
    
    @Column(name="prop_type_name", length=20)
    public String getPropTypeName() {
        return this.propTypeName;
    }
    
    public void setPropTypeName(String propTypeName) {
        this.propTypeName = propTypeName;
    }
@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="propType")
    public Set<TOProperty> getProperties() {
        return this.properties;
    }
    
    public void setProperties(Set<TOProperty> properties) {
        this.properties = properties;
    }




}

