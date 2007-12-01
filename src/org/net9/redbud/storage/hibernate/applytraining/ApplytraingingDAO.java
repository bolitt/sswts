package org.net9.redbud.storage.hibernate.applytraining;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Applytrainging.
 * @see org.net9.redbud.storage.hibernate.applytraining.Applytrainging
 * @author MyEclipse - Hibernate Tools
 */
public class ApplytraingingDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(ApplytraingingDAO.class);

	//property constants
	public static final String STUDENTNUM = "studentnum";
	public static final String TRAININGID = "trainingid";
	public static final String APPLYREASON = "applyreason";
	public static final String APPLYSTATUS = "applystatus";
	public static final String APPLYTIME = "applytime";
	public static final String TRAININGNAME = "trainingname";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Applytrainging transientInstance) {
        log.debug("saving Applytrainging instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
    //urong@9#,2007.07.28
    public void update(Applytrainging persistentInstance) {
        log.debug("updating Applytrainging instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }
    
	public void delete(Applytrainging persistentInstance) {
        log.debug("deleting Applytrainging instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Applytrainging findById( java.lang.Integer id) {
        log.debug("getting Applytrainging instance with id: " + id);
        try {
            Applytrainging instance = (Applytrainging) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.applytraining.Applytrainging", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    public List findByExample(Applytrainging instance) {
        log.debug("finding Applytrainging instance by example");
        try {
            List results = getHibernateTemplate().findByExample(instance);
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }
    
    //by urong@9#, 2007.09.28
    public List findByTrainingIdAndStudentNum(int trainingID, String studentNum){
	   log.debug("finding Applytrainging instance with trainingID and studentNum: " + trainingID + studentNum);
	   
	   try{
		   String queryString = "from Applytrainging as model where model."
			   + TRAININGID + "= ? and " + STUDENTNUM + "= ?";
		   Object values[] = {trainingID, studentNum};
		   return getHibernateTemplate().find(queryString, values);
	   } catch(RuntimeException re){
		   log.error("find by property name failed", re);
		   throw re;
	   }
   	}
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding Applytrainging instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Applytrainging as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByStudentnum(Object studentnum) {
		return findByProperty(STUDENTNUM, studentnum);
	}
	
	public List findByTrainingid(Object trainingid) {
		return findByProperty(TRAININGID, trainingid);
	}
	
	public List findByApplyreason(Object applyreason) {
		return findByProperty(APPLYREASON, applyreason);
	}
	
	public List findByApplystatus(Object applystatus) {
		return findByProperty(APPLYSTATUS, applystatus);
	}
	
	public List findByApplytime(Object applytime) {
		return findByProperty(APPLYTIME, applytime);
	}
	
	public List findByTrainingname(Object trainingname) {
		return findByProperty(TRAININGNAME, trainingname);
	}
	
    public Applytrainging merge(Applytrainging detachedInstance) {
        log.debug("merging Applytrainging instance");
        try {
            Applytrainging result = (Applytrainging) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Applytrainging instance) {
        log.debug("attaching dirty Applytrainging instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Applytrainging instance) {
        log.debug("attaching clean Applytrainging instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static ApplytraingingDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (ApplytraingingDAO) ctx.getBean("ApplytraingingDAO");
	}
}