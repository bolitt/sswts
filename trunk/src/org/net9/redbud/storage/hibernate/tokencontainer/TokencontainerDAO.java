package org.net9.redbud.storage.hibernate.tokencontainer;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Tokencontainer.
 * @see org.net9.redbud.storage.hibernate.tokencontainer.Tokencontainer
 * @author MyEclipse - Hibernate Tools
 */
public class TokencontainerDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TokencontainerDAO.class);

	//property constants
	public static final String PASSWORD = "password";
	public static final String OBJECTID = "objectid";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Tokencontainer transientInstance) {
        log.debug("saving Tokencontainer instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Tokencontainer persistentInstance) {
        log.debug("deleting Tokencontainer instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Tokencontainer findById( java.lang.Integer id) {
        log.debug("getting Tokencontainer instance with id: " + id);
        try {
            Tokencontainer instance = (Tokencontainer) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.tokencontainer.Tokencontainer", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Tokencontainer instance) {
        log.debug("finding Tokencontainer instance by example");
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
      log.debug("finding Tokencontainer instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Tokencontainer as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}
	
	
	public List findByObjectid(Object objectid) {
		return findByProperty(OBJECTID, objectid);
	}
	
    public Tokencontainer merge(Tokencontainer detachedInstance) {
        log.debug("merging Tokencontainer instance");
        try {
            Tokencontainer result = (Tokencontainer) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Tokencontainer instance) {
        log.debug("attaching dirty Tokencontainer instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Tokencontainer instance) {
        log.debug("attaching clean Tokencontainer instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TokencontainerDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TokencontainerDAO) ctx.getBean("TokencontainerDAO");
	}
}