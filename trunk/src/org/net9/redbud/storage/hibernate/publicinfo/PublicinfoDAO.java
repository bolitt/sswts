package org.net9.redbud.storage.hibernate.publicinfo;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Publicinfo.
 * @see org.net9.redbud.storage.hibernate.publicinfo.Publicinfo
 * @author MyEclipse - Hibernate Tools
 */
public class PublicinfoDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(PublicinfoDAO.class);

	//property constants
	public static final String INFOTITLE = "infotitle";
	public static final String INFOCONTENT = "infocontent";
	public static final String OWNERID = "ownerid";
	public static final String OWENERCATENAME = "ownercatename";
	public static final String PUBLISHTIME = "publishtime";
	public static final String APPROVETIME = "approvetime";
	public static final String APPROVESTATUS = "approvestatus";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Publicinfo transientInstance) {
        log.debug("saving Publicinfo instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Publicinfo persistentInstance) {
        log.debug("deleting Publicinfo instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	
	//urong@9#, 2007.08.04
	public void update(Publicinfo persistentInstance) {
        log.debug("updating Publicinfo instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }
    
    public Publicinfo findById( java.lang.Integer id) {
        log.debug("getting Publicinfo instance with id: " + id);
        try {
            Publicinfo instance = (Publicinfo) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.publicinfo.Publicinfo", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Publicinfo instance) {
        log.debug("finding Publicinfo instance by example");
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
      log.debug("finding Publicinfo instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Publicinfo as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByInfotitle(Object infotitle) {
		return findByProperty(INFOTITLE, infotitle);
	}
	
	public List findByInfocontent(Object infocontent) {
		return findByProperty(INFOCONTENT, infocontent);
	}
	
	public List findByOwnerid(Object ownerid) {
		return findByProperty(OWNERID, ownerid);
	}
	
	public List findByOwnercatename(Object ownercatename) {
		return findByProperty(OWENERCATENAME, ownercatename);
	}
	
	public List findByPublishtime(Object publishtime) {
		return findByProperty(PUBLISHTIME, publishtime);
	}
	
	public List findByApprovetime(Object approvetime) {
		return findByProperty(APPROVETIME, approvetime);
	}
	
	public List findByApprovestatus(Object approvestatus) {
		return findByProperty(APPROVESTATUS, approvestatus);
	}
	
    public Publicinfo merge(Publicinfo detachedInstance) {
        log.debug("merging Publicinfo instance");
        try {
            Publicinfo result = (Publicinfo) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Publicinfo instance) {
        log.debug("attaching dirty Publicinfo instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Publicinfo instance) {
        log.debug("attaching clean Publicinfo instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static PublicinfoDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (PublicinfoDAO) ctx.getBean("PublicinfoDAO");
	}
}