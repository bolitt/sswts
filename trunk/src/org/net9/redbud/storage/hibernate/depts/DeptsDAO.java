package org.net9.redbud.storage.hibernate.depts;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Depts.
 * @see org.net9.redbud.storage.hibernate.depts.Depts
 * @author MyEclipse - Hibernate Tools
 */
public class DeptsDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(DeptsDAO.class);

	//property constants
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Depts transientInstance) {
        log.debug("saving Depts instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Depts persistentInstance) {
        log.debug("deleting Depts instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Depts findById( java.lang.Integer id) {
        log.debug("getting Depts instance with id: " + id);
        try {
            Depts instance = (Depts) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.depts.Depts", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Depts instance) {
        log.debug("finding Depts instance by example");
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
      log.debug("finding Depts instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Depts as model where model." 
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
	
    public Depts merge(Depts detachedInstance) {
        log.debug("merging Depts instance");
        try {
            Depts result = (Depts) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Depts instance) {
        log.debug("attaching dirty Depts instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Depts instance) {
        log.debug("attaching clean Depts instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static DeptsDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (DeptsDAO) ctx.getBean("DeptsDAO");
	}
}