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
import javax.persistence.criteria.*;
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

    public ObjectFields findByValueSingle(Object value) {
        EntityManager em = getEntityManager();
        String type = value.getClass().getSimpleName();
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

    public List<ObjectFields> findByValue(Object value) {
        EntityManager em = getEntityManager();
        try {
            String queryName = "ObjectFields.findBy";
            queryName += getFieldForValue(value);
            queryName += "val";
            return em.createNamedQuery(queryName, ObjectFields.class).setParameter("val", value).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    private String getFieldForValue(Object value) {
        String type = value.getClass().getSimpleName();
        if (type.equalsIgnoreCase("integer"))
            return "int";
        else if (type.equalsIgnoreCase("string"))
            return "str";
        else
            return type;
    }


    public Long getCountByValue(Object value, String field) {
        EntityManager em = getEntityManager();
        String type = value.getClass().getSimpleName();
        try {
            String queryName = "ObjectFields.findBy";
            if (type.equalsIgnoreCase("integer"))
                queryName += "int";
            else if (type.equalsIgnoreCase("string"))
                queryName += "str";
            else
                queryName += type;
            queryName += "val";
            return Long.valueOf(em.createNamedQuery(queryName, ObjectFields.class).setParameter("val", value).getResultList().size());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public Object getValue(ObjectFields of) {
        switch (of.getType()) {
            case "integer":
                return of.getIntval();
            case "long":
                return of.getLongval();
            case "double":
                return of.getFloatval();
            case "date":
                return of.getDateval();
            case "string":
                return of.getStrval();
        }
        return null;
    }

    public List<ObjectFields> findByObjectNameAndFieldValue(String name, String field, Object value) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<ObjectFields> e = cq.from(ObjectFields.class);
            Join<ObjectFields, TObject> a = e.join("objectId");
            String fieldName = getFieldForValue(value);
            fieldName += "val";
            cq.select(e);
            Predicate fieldNamePredicate = cb.equal(e.get(fieldName), value);
            Predicate namePredicate = cb.equal(e.get("name"), field);
            Predicate joinPredicate = cb.equal(a.get("name"), name);
            cq.where(fieldNamePredicate, namePredicate, joinPredicate);
            Query query = em.createQuery(cq);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
    }

    public List<ObjectFields> findByTObject(TObject tObject) {
        EntityManager em = getEntityManager();
        try {
            em.createNamedQuery("ObjectFields.findByObjectId", ObjectFields.class).setParameter("object", tObject).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            em.close();
        }
        return null;
    }

}
