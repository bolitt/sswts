package org.net9.redbud.storage.hibernate.internalinfo;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Internalinfo.
 * @see org.net9.redbud.storage.hibernate.internalinfo.Internalinfo
 * @author MyEclipse - Hibernate Tools
 */
public class InternalinfoDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(InternalinfoDAO.class);

	//property constants
	public static final String INFOTITLE = "infotitle";
	public static final String INFOCONTENT = "infocontent";
	public static final String OWNERID = "ownerid";
	public static final String OWNERNAME = "ownername";
	public static final String OWNERPOS = "ownerpos";
	public static final String PUBLISHTIME = "publishtime";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Internalinfo transientInstance) {
        log.debug("saving Internalinfo instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Internalinfo persistentInstance) {
        log.debug("deleting Internalinfo instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Internalinfo findById( java.lang.Integer id) {
        log.debug("getting Internalinfo instance with id: " + id);
        try {
            Internalinfo instance = (Internalinfo) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.internalinfo.Internalinfo", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Internalinfo instance) {
        log.debug("finding Internalinfo instance by example");
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
      log.debug("finding Internalinfo instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Internalinfo as model where model." 
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
	
	public List findByOwnername(Object ownername) {
		return findByProperty(OWNERNAME, ownername);
	}
	
	public List findByOwnerpos(Object ownerpos) {
		return findByProperty(OWNERPOS, ownerpos);
	}
	
	public List findByPublishtime(Object publishtime) {
		return findByProperty(PUBLISHTIME, publishtime);
	}
	
    public Internalinfo merge(Internalinfo detachedInstance) {
        log.debug("merging Internalinfo instance");
        try {
            Internalinfo result = (Internalinfo) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Internalinfo instance) {
        log.debug("attaching dirty Internalinfo instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Internalinfo instance) {
        log.debug("attaching clean Internalinfo instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static InternalinfoDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (InternalinfoDAO) ctx.getBean("InternalinfoDAO");
	}
}