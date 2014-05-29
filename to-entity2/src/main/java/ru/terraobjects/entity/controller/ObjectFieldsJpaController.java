/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.terraobjects.entity.controller;

import ru.terraobjects.entity.ObjectFields;
import ru.terraobjects.entity.TObject;
import ru.terraobjects.entity.controller.exceptions.NonexistentEntityException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * @author terranz
 */
public class ObjectFieldsJpaController implements Serializable {

    public ObjectFieldsJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ObjectFields objectFields) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TObject objectId = objectFields.getTObject();
            if (objectId != null) {
                objectId = em.getReference(objectId.getClass(), objectId.getId());
                objectFields.setObjectId(objectId);
            }
            em.persist(objectFields);
            if (objectId != null) {
                objectId.getObjectFieldsList().add(objectFields);
                objectId = em.merge(objectId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ObjectFields objectFields) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ObjectFields persistentObjectFields = em.find(ObjectFields.class, objectFields.getId());
            TObject objectIdOld = persistentObjectFields.getTObject();
            TObject objectIdNew = objectFields.getTObject();
            if (objectIdNew != null) {
                objectIdNew = em.getReference(objectIdNew.getClass(), objectIdNew.getId());
                objectFields.setObjectId(objectIdNew);
            }
            objectFields = em.merge(objectFields);
            if (objectIdOld != null && !objectIdOld.equals(objectIdNew)) {
                objectIdOld.getObjectFieldsList().remove(objectFields);
                objectIdOld = em.merge(objectIdOld);
            }
            if (objectIdNew != null && !objectIdNew.equals(objectIdOld)) {
                objectIdNew.getObjectFieldsList().add(objectFields);
                objectIdNew = em.merge(objectIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = objectFields.getId();
                if (findObjectFields(id) == null) {
                    throw new NonexistentEntityException("The objectFields with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ObjectFields objectFields;
            try {
                objectFields = em.getReference(ObjectFields.class, id);
                objectFields.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The objectFields with id " + id + " no longer exists.", enfe);
            }
            TObject objectId = objectFields.getTObject();
            if (objectId != null) {
                objectId.getObjectFieldsList().remove(objectFields);
                objectId = em.merge(objectId);
            }
            em.remove(objectFields);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ObjectFields> findObjectFieldsEntities() {
        return findObjectFieldsEntities(true, -1, -1);
    }

    public List<ObjectFields> findObjectFieldsEntities(int maxResults, int firstResult) {
        return findObjectFieldsEntities(false, maxResults, firstResult);
    }

    private List<ObjectFields> findObjectFieldsEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ObjectFields.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public ObjectFields findObjectFields(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ObjectFields.class, id);
        } finally {
            em.close();
        }
    }

    public int getObjectFieldsCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ObjectFields> rt = cq.from(ObjectFields.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public ObjectFields findByValue(String type, Object value) {
        EntityManager em = getEntityManager();
        try {
            String queryName = "ObjectFields.findBy";
            if (type.equalsIgnoreCase("integer"))
                queryName += "int";
            else if (type.equalsIgnoreCase("string"))
                queryName += "str";
            else
                queryName += type;
            queryName += "val";
            return em.createNamedQuery(queryName, ObjectFields.class).setParameter("val", value).getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

}
