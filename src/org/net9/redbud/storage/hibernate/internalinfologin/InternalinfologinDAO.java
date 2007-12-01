package org.net9.redbud.storage.hibernate.internalinfologin;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Internalinfologin.
 * @see org.net9.redbud.storage.hibernate.internalinfologin.Internalinfologin
 * @author MyEclipse - Hibernate Tools
 */
public class InternalinfologinDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(InternalinfologinDAO.class);

	//property constants
	public static final String STATUS = "status";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Internalinfologin transientInstance) {
        log.debug("saving Internalinfologin instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Internalinfologin persistentInstance) {
        log.debug("deleting Internalinfologin instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	
	//by echo
	public void update(Internalinfologin persistentInstance) {
        log.debug("updating Internalinfologin instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }
    
    public Internalinfologin findById( java.lang.Integer id) {
        log.debug("getting Internalinfologin instance with id: " + id);
        try {
            Internalinfologin instance = (Internalinfologin) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.internalinfologin.Internalinfologin", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Internalinfologin instance) {
        log.debug("finding Internalinfologin instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding Internalinfologin instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Internalinfologin as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}
	
    public Internalinfologin merge(Internalinfologin detachedInstance) {
        log.debug("merging Internalinfologin instance");
        try {
            Internalinfologin result = (Internalinfologin) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Internalinfologin instance) {
        log.debug("attaching dirty Internalinfologin instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Internalinfologin instance) {
        log.debug("attaching clean Internalinfologin instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static InternalinfologinDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (InternalinfologinDAO) ctx.getBean("InternalinfologinDAO");
	}
}