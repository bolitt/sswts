package org.net9.redbud.storage.hibernate.trainrecord;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Trainrecord.
 * @see org.net9.redbud.storage.hibernate.trainrecord.Trainrecord
 * @author MyEclipse - Hibernate Tools
 */
public class TrainrecordDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TrainrecordDAO.class);

	//property constants
	public static final String TRAINID = "trainid";
	public static final String LOGINID = "loginid";
	public static final String SCORE = "score";
	public static final String ISATTEND = "isattend";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Trainrecord transientInstance) {
        log.debug("saving Trainrecord instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Trainrecord persistentInstance) {
        log.debug("deleting Trainrecord instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Trainrecord findById( java.lang.Integer id) {
        log.debug("getting Trainrecord instance with id: " + id);
        try {
            Trainrecord instance = (Trainrecord) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.trainrecord.Trainrecord", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Trainrecord instance) {
        log.debug("finding Trainrecord instance by example");
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
      log.debug("finding Trainrecord instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Trainrecord as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByTrainid(Object trainid) {
		return findByProperty(TRAINID, trainid);
	}
	
	public List findByLoginid(Object loginid) {
		return findByProperty(LOGINID, loginid);
	}
	
	public List findByScore(Object score) {
		return findByProperty(SCORE, score);
	}
	
	public List findByIsattend(Object isattend) {
		return findByProperty(ISATTEND, isattend);
	}
	
    public Trainrecord merge(Trainrecord detachedInstance) {
        log.debug("merging Trainrecord instance");
        try {
            Trainrecord result = (Trainrecord) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Trainrecord instance) {
        log.debug("attaching dirty Trainrecord instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Trainrecord instance) {
        log.debug("attaching clean Trainrecord instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TrainrecordDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TrainrecordDAO) ctx.getBean("TrainrecordDAO");
	}
}