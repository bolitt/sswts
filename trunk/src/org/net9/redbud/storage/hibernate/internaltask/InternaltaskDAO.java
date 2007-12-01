package org.net9.redbud.storage.hibernate.internaltask;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Internaltask.
 * @see org.net9.redbud.storage.hibernate.internaltask.Internaltask
 * @author MyEclipse - Hibernate Tools
 */
public class InternaltaskDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(InternaltaskDAO.class);

	//property constants
	public static final String TASKTITLE = "tasktitle";
	public static final String TASKCONTENT = "taskcontent";
	public static final String PUBLISHTIME = "publishtime";
	public static final String OWNERID = "ownerid";
	public static final String OWENERNAME = "owenername";
	public static final String OWNERPOSNAME = "ownerposname";
	public static final String STATUS = "status";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Internaltask transientInstance) {
        log.debug("saving Internaltask instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Internaltask persistentInstance) {
        log.debug("deleting Internaltask instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	
	public void update(Internaltask persistentInstance) {
        log.debug("updating Internaltask instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }
    
    public Internaltask findById( java.lang.Integer id) {
        log.debug("getting Internaltask instance with id: " + id);
        try {
            Internaltask instance = (Internaltask) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.internaltask.Internaltask", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Internaltask instance) {
        log.debug("finding Internaltask instance by example");
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
      log.debug("finding Internaltask instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Internaltask as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByTasktitle(Object tasktitle) {
		return findByProperty(TASKTITLE, tasktitle);
	}
	
	public List findByTaskcontent(Object taskcontent) {
		return findByProperty(TASKCONTENT, taskcontent);
	}
	
	public List findByPublishtime(Object publishtime) {
		return findByProperty(PUBLISHTIME, publishtime);
	}
	
	public List findByOwnerid(Object ownerid) {
		return findByProperty(OWNERID, ownerid);
	}
	
	public List findByOwenername(Object owenername) {
		return findByProperty(OWENERNAME, owenername);
	}
	
	public List findByOwnerposname(Object ownerposname) {
		return findByProperty(OWNERPOSNAME, ownerposname);
	}
	
	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}
	
    public Internaltask merge(Internaltask detachedInstance) {
        log.debug("merging Internaltask instance");
        try {
            Internaltask result = (Internaltask) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Internaltask instance) {
        log.debug("attaching dirty Internaltask instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Internaltask instance) {
        log.debug("attaching clean Internaltask instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static InternaltaskDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (InternaltaskDAO) ctx.getBean("InternaltaskDAO");
	}
}