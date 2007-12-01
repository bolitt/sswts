package org.net9.redbud.storage.hibernate.publicinfologin;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Publicinfologin.
 * @see org.net9.redbud.storage.hibernate.publicinfologin.Publicinfologin
 * @author MyEclipse - Hibernate Tools
 */
public class PublicinfologinDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(PublicinfologinDAO.class);

	//property constants
	public static final String STATUS = "status";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Publicinfologin transientInstance) {
        log.debug("saving Publicinfologin instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Publicinfologin persistentInstance) {
        log.debug("deleting Publicinfologin instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	
	//by echo
	public void update(Publicinfologin persistentInstance) {
        log.debug("updating Publicinfologin instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }
    
    public Publicinfologin findById( java.lang.Integer id) {
        log.debug("getting Publicinfologin instance with id: " + id);
        try {
            Publicinfologin instance = (Publicinfologin) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.publicinfologin.Publicinfologin", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Publicinfologin instance) {
        log.debug("finding Publicinfologin instance by example");
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
      log.debug("finding Publicinfologin instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Publicinfologin as model where model." 
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
	
    public Publicinfologin merge(Publicinfologin detachedInstance) {
        log.debug("merging Publicinfologin instance");
        try {
            Publicinfologin result = (Publicinfologin) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Publicinfologin instance) {
        log.debug("attaching dirty Publicinfologin instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Publicinfologin instance) {
        log.debug("attaching clean Publicinfologin instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static PublicinfologinDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (PublicinfologinDAO) ctx.getBean("PublicinfologinDAO");
	}
}