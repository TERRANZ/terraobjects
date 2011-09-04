package ru.terra.ejb.entity;

import java.util.List;
import javax.ejb.Stateless;
import net.sf.persist.Persist;
import ru.terra.ejb.entity.dao.*;

/**
 *
 * @author terranz
 */
@Stateless
public class EntityBean implements EntityBeanRemote {

    @Override
    public List<TOObject> findAllObjects()
    {
	return TOObjectsManager.getInstance().getAllObjects();
    }

    @Override
    public List getObjectProps(Integer oid)
    {
	return TOPropertiesManager.getInstance().getObjectProperties(oid);
    }

    @Override
    public List getPropsForObject(Integer oid)
    {
	return TOPropertiesManager.getInstance().getPropsForObjectId(oid);
    }

    @Override
    public Integer createNewObjectByTemplate(Integer tid)
    {
	return TOObjectsManager.getInstance().createNewObject(tid).getObjectId();
    }

    @Override
    public TOObjectProperty getProperty(Integer oid, Integer propId)
    {
	return TOObjectsManager.getInstance().getObjectProperty(oid, propId);
    }

    @Override
    public Object getPropertyValue(Integer oid, Integer propId)
    {
	return TOObjectsManager.getInstance().getPropertyValue(oid, propId);
    }



}
