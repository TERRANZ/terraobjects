package ru.terraobjects.entity;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

/**
 *
 * @author terranz
 */
public abstract class PersistanceManager<T>
{
    protected Session session = HibernateUtil.getSessionFactory().openSession();

    public List<T> findAll(Class entity)
    {
        Criteria c = session.createCriteria(entity);
        return c.list();
    }

    public void delete(T o)
    {
        try
        {
            session.beginTransaction();
            session.delete(o);
            session.getTransaction().commit();
        } catch (HibernateException he)
        {
            he.printStackTrace();
        }
    }

    public void update(T o)
    {
        try
        {
            //session.beginTransaction();
            session.update(o);
            session.flush();
        } catch (HibernateException he)
        {
            he.printStackTrace();
        }
    }

    public void insert(T o)
    {
	try
        {
            session.beginTransaction();
            session.save(o);
            session.getTransaction().commit();
        } catch (HibernateException he)
        {
            he.printStackTrace();
        }
    }

    public abstract T findById(Integer id);
}
