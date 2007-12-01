package org.net9.redbud.storage.hibernate.organs;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Organs.
 * @see org.net9.redbud.storage.hibernate.organs.Organs
 * @author MyEclipse - Hibernate Tools
 */
public class OrgansDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(OrgansDAO.class);

	//property constants
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Organs transientInstance) {
        log.debug("saving Organs instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Organs persistentInstance) {
        log.debug("deleting Organs instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Organs findById( java.lang.Integer id) {
        log.debug("getting Organs instance with id: " + id);
        try {
            Organs instance = (Organs) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.organs.Organs", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Organs instance) {
        log.debug("finding Organs instance by example");
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
      log.debug("finding Organs instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Organs as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByCode(Object code) {
		return findByProperty(CODE, code);
	}
	
	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}
	
	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}
	
    public Organs merge(Organs detachedInstance) {
        log.debug("merging Organs instance");
        try {
            Organs result = (Organs) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Organs instance) {
        log.debug("attaching dirty Organs instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Organs instance) {
        log.debug("attaching clean Organs instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static OrgansDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (OrgansDAO) ctx.getBean("OrgansDAO");
	}
}