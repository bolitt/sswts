package org.net9.redbud.storage.hibernate.internaltasklogin;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Internaltasklogin.
 * @see org.net9.redbud.storage.hibernate.internaltasklogin.Internaltasklogin
 * @author MyEclipse - Hibernate Tools
 */
public class InternaltaskloginDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(InternaltaskloginDAO.class);

	//property constants
	public static final String READSTATUS = "readstatus";
	public static final String ECHOSTATUS = "echostatus";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Internaltasklogin transientInstance) {
        log.debug("saving Internaltasklogin instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
    //by echo 
    public void update(Internaltasklogin persistentInstance) {
        log.debug("updating Internaltasklogin instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }
    
	public void delete(Internaltasklogin persistentInstance) {
        log.debug("deleting Internaltasklogin instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Internaltasklogin findById( java.lang.Integer id) {
        log.debug("getting Internaltasklogin instance with id: " + id);
        try {
            Internaltasklogin instance = (Internaltasklogin) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.internaltasklogin.Internaltasklogin", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Internaltasklogin instance) {
        log.debug("finding Internaltasklogin instance by example");
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
      log.debug("finding Internaltasklogin instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Internaltasklogin as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByReadStatus(Object readstatus) {
		return findByProperty(READSTATUS, readstatus);
	}
	
	public List findByEchoStatus(Object echostatus) {
		return findByProperty(ECHOSTATUS, echostatus);
	}
	
    public Internaltasklogin merge(Internaltasklogin detachedInstance) {
        log.debug("merging Internaltasklogin instance");
        try {
            Internaltasklogin result = (Internaltasklogin) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Internaltasklogin instance) {
        log.debug("attaching dirty Internaltasklogin instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Internaltasklogin instance) {
        log.debug("attaching clean Internaltasklogin instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static InternaltaskloginDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (InternaltaskloginDAO) ctx.getBean("InternaltaskloginDAO");
	}
}