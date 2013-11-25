package ru.terraobjects.entity;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

/**
 * @author terranz
 */
public abstract class PersistanceManager<T> {
    protected Session session = null;
    protected Logger logger = Logger.getLogger(this.getClass());

    public PersistanceManager() {
        Logger.getLogger(this.getClass()).info("Creating persistance manager");
        try {
            session = HibernateUtil.getSessionFactory().getCurrentSession();
        } catch (HibernateException e) {
            Logger.getLogger(this.getClass()).info("Can't get current session");
            session = HibernateUtil.getSessionFactory().openSession();
        }
    }


    public List<T> findAll(Class entity) {
        Criteria c = session.createCriteria(entity);
        return c.list();
    }

    public void delete(T o) {
        try {
            session.beginTransaction();
            session.delete(o);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            session.getTransaction().rollback();
            he.printStackTrace();
        }
    }

    public void update(T o) {
        try {
            //session.beginTransaction();
            session.update(o);
            session.flush();
        } catch (HibernateException he) {
            session.getTransaction().rollback();
            he.printStackTrace();
        }
    }

    public void insert(T o) {
        try {
            session.beginTransaction();
            session.save(o);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            session.getTransaction().rollback();
            he.printStackTrace();
        }
    }

    public abstract T findById(Integer id);

    public void insert(List<T> os) {
        try {
            session.beginTransaction();
            for (T o : os)
                session.save(o);
            session.getTransaction().commit();
        } catch (HibernateException he) {
            session.getTransaction().rollback();
            he.printStackTrace();
        }
    }
}
