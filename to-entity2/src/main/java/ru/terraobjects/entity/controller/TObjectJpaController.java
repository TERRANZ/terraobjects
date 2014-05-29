/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.terraobjects.entity.controller;

import ru.terraobjects.entity.ObjectFields;
import ru.terraobjects.entity.TObject;
import ru.terraobjects.entity.controller.exceptions.IllegalOrphanException;
import ru.terraobjects.entity.controller.exceptions.NonexistentEntityException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author terranz
 */
public class TObjectJpaController implements Serializable {

    public TObjectJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TObject TObject) {
        if (TObject.getObjectFieldsList() == null) {
            TObject.setObjectFieldsList(new ArrayList<ObjectFields>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ObjectFields> attachedObjectFieldsList = new ArrayList<ObjectFields>();
            for (ObjectFields objectFieldsListObjectFieldsToAttach : TObject.getObjectFieldsList()) {
                objectFieldsListObjectFieldsToAttach = em.getReference(objectFieldsListObjectFieldsToAttach.getClass(), objectFieldsListObjectFieldsToAttach.getId());
                attachedObjectFieldsList.add(objectFieldsListObjectFieldsToAttach);
            }
            TObject.setObjectFieldsList(attachedObjectFieldsList);
            em.persist(TObject);
            for (ObjectFields objectFieldsListObjectFields : TObject.getObjectFieldsList()) {
                TObject oldObjectIdOfObjectFieldsListObjectFields = objectFieldsListObjectFields.getTObject();
                objectFieldsListObjectFields.setObjectId(TObject);
                objectFieldsListObjectFields = em.merge(objectFieldsListObjectFields);
                if (oldObjectIdOfObjectFieldsListObjectFields != null) {
                    oldObjectIdOfObjectFieldsListObjectFields.getObjectFieldsList().remove(objectFieldsListObjectFields);
                    oldObjectIdOfObjectFieldsListObjectFields = em.merge(oldObjectIdOfObjectFieldsListObjectFields);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TObject TObject) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TObject persistentTObject = em.find(TObject.class, TObject.getId());
            List<ObjectFields> objectFieldsListOld = persistentTObject.getObjectFieldsList();
            List<ObjectFields> objectFieldsListNew = TObject.getObjectFieldsList();
            List<String> illegalOrphanMessages = null;
            for (ObjectFields objectFieldsListOldObjectFields : objectFieldsListOld) {
                if (!objectFieldsListNew.contains(objectFieldsListOldObjectFields)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ObjectFields " + objectFieldsListOldObjectFields + " since its objectId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ObjectFields> attachedObjectFieldsListNew = new ArrayList<ObjectFields>();
            for (ObjectFields objectFieldsListNewObjectFieldsToAttach : objectFieldsListNew) {
                objectFieldsListNewObjectFieldsToAttach = em.getReference(objectFieldsListNewObjectFieldsToAttach.getClass(), objectFieldsListNewObjectFieldsToAttach.getId());
                attachedObjectFieldsListNew.add(objectFieldsListNewObjectFieldsToAttach);
            }
            objectFieldsListNew = attachedObjectFieldsListNew;
            TObject.setObjectFieldsList(objectFieldsListNew);
            TObject = em.merge(TObject);
            for (ObjectFields objectFieldsListNewObjectFields : objectFieldsListNew) {
                if (!objectFieldsListOld.contains(objectFieldsListNewObjectFields)) {
                    TObject oldObjectIdOfObjectFieldsListNewObjectFields = objectFieldsListNewObjectFields.getTObject();
                    objectFieldsListNewObjectFields.setObjectId(TObject);
                    objectFieldsListNewObjectFields = em.merge(objectFieldsListNewObjectFields);
                    if (oldObjectIdOfObjectFieldsListNewObjectFields != null && !oldObjectIdOfObjectFieldsListNewObjectFields.equals(TObject)) {
                        oldObjectIdOfObjectFieldsListNewObjectFields.getObjectFieldsList().remove(objectFieldsListNewObjectFields);
                        oldObjectIdOfObjectFieldsListNewObjectFields = em.merge(oldObjectIdOfObjectFieldsListNewObjectFields);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = TObject.getId();
                if (findTObject(id) == null) {
                    throw new NonexistentEntityException("The tObject with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            TObject TObject;
            try {
                TObject = em.getReference(TObject.class, id);
                TObject.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The TObject with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ObjectFields> objectFieldsListOrphanCheck = TObject.getObjectFieldsList();
            for (ObjectFields objectFieldsListOrphanCheckObjectFields : objectFieldsListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TObject (" + TObject + ") cannot be destroyed since the ObjectFields " + objectFieldsListOrphanCheckObjectFields + " in its objectFieldsList field has a non-nullable objectId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(TObject);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TObject> findTObjectEntities() {
        return findTObjectEntities(true, -1, -1);
    }

    public List<TObject> findTObjectEntities(int maxResults, int firstResult) {
        return findTObjectEntities(false, maxResults, firstResult);
    }

    private List<TObject> findTObjectEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TObject.class));
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

    public TObject findTObject(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TObject.class, id);
        } finally {
            em.close();
        }
    }

    public int getTObjectCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TObject> rt = cq.from(TObject.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Long getCountByName(String name) {
        EntityManager em = getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery cq = cb.createQuery();
            Root<TObject> rt = cq.from(TObject.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            cq.where(cb.equal(rt.get("name"), name));
            Query q = em.createQuery(cq);
            return (Long) q.getSingleResult();
        } finally {
            em.close();
        }
    }

}
