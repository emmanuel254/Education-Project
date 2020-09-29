package com.test.educationproject.jpa;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author litem
 */
@Stateless
public class TransactionProvider {

    @PersistenceContext
    private EntityManager em;

    public EntityManager getEM() {
        return em;
    }

    public boolean createEntity(Object o) {
        boolean res = false;
        try {
            if (o != null) {
                em.persist(o);
                em.flush();
            }
            res = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return res;
        }
    }

    public boolean createMultipleEntities(List objects) {
        boolean res = false;
        try {
            if (objects != null && !objects.isEmpty()) {
                for (Object o : objects) {
                    /*ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
                Validator validator = factory.getValidator();

                Set<ConstraintViolation<Object>> constraintViolations = validator.validate(o);

                if (constraintViolations.size() > 0) {
                    System.out.println("Constraint Violations occurred..");
                    for (ConstraintViolation<Object> contraints : constraintViolations) {
                        System.out.println(contraints.getRootBeanClass().getSimpleName()
                                + "." + contraints.getPropertyPath() + " " + contraints.getMessage());
                    }
                }*/
                    em.persist(o);
                }
                em.flush();
            }
            res = true;
        } catch (PersistenceException ex) {
            ex.printStackTrace();
            //System.out.print(ex.getCause());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return res;
        }
    }

    public boolean updateEntity(Object o) {
        boolean res = false;
        try {
            if (o != null) {
                em.merge(o);
                em.flush();
            }
            res = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return res;
        }
    }

    public boolean updateMultipleEntities(List objects) {
        boolean res = false;
        try {
            if (objects != null && !objects.isEmpty()) {
                for (Object o : objects) {
                    em.merge(o);
                }
                em.flush();
            }
            res = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return res;
        }
    }

    public <T extends Object> T getEntity(Class<T> object_class, Object id) {
        T res = null;
        try {
            if (id != null) {
                res = em.find(object_class, id);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return res;
        }
    }

    public <T extends Object> T getSingleResult(Query qry) {
        T res = null;
        try {
            res = (T) qry.getSingleResult();
        } catch (NoResultException ex) {

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return res;

    }

    public List getManyFromQuery(Query q) {
        List res = new ArrayList();
        try {
            if (q != null) {
                res = q.getResultList();
            }
        } catch (Exception ex) {

        } finally {
            if (res == null) {
                res = new ArrayList();
            }
            return res;
        }
    }

    public boolean deleteEntity(Object o) {
        boolean res = false;
        try {
            if (o != null) {
                em.remove(o);
                em.flush();
            }
            res = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return res;
        }
    }

    public boolean deleteMultipleEnties(List objects) {
        boolean res = false;
        try {
            if (objects != null && !objects.isEmpty()) {
                for (Object o : objects) {
                    em.remove(o);
                }
                em.flush();
            }
            res = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return res;
        }
    }
}
