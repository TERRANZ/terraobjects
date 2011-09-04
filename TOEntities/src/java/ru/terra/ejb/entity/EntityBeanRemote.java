package ru.terra.ejb.entity;

import java.util.List;
import javax.ejb.Remote;

/**
 *
 * @author terranz
 */
@Remote
public interface EntityBeanRemote {

    List<TOObject> findAllObjects();

    List getObjectProps(Integer oid);

    List getPropsForObject(Integer oid);

    Integer createNewObjectByTemplate(Integer tid);

    TOObjectProperty getProperty(Integer oid, Integer propId);

    Object getPropertyValue(Integer oid, Integer propId);
}
