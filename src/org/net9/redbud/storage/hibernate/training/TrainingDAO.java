package org.net9.redbud.storage.hibernate.training;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Training.
 * @see org.net9.redbud.storage.hibernate.training.Training
 * @author MyEclipse - Hibernate Tools
 */
public class TrainingDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(TrainingDAO.class);

	//property constants
	public static final String NAME = "name";
	public static final String DESCRIPTION = "description";
	public static final String POSTCODE = "postcode";
	public static final String STARTTIME = "starttime";
	public static final String ENDTIME = "endtime";
	public static final String ISPUBLIC = "ispublic";
	public static final String DEADLINE = "deadline";
	public static final String POSTTIME = "posttime";
	public static final String VALIDTIME = "validtime";
	public static final String ISTOKENED = "istokened";
	public static final String FILELIST = "filelist";
	
	protected void initDao() {
		//do nothing
	}
    
    public void save(Training transientInstance) {
        log.debug("saving Training instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Training persistentInstance) {
        log.debug("deleting Training instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
	
	//urong@9#, 2007.08.13
	public void update(Training persistentInstance) {
        log.debug("updating Training instance");
        try {
            getHibernateTemplate().update(persistentInstance);
            log.debug("update successful");
        } catch (RuntimeException re) {
            log.error("update failed", re);
            throw re;
        }
    }
    
    public Training findById( java.lang.Integer id) {
        log.debug("getting Training instance with id: " + id);
        try {
            Training instance = (Training) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.training.Training", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Training instance) {
        log.debug("finding Training instance by example");
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
      log.debug("finding Training instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Training as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}
    
    //by DennisZz
    //返回可验证密码条的活动(当前时间在活动结束之后，并在密码条失效时间之前)
    public List findTheTrainingByCurrentTime(String currentTime) {
        log.debug("finding proper Training instance : " + currentTime);
        try {
  
        	String queryString = "from Training as model where model.endtime <= ?" + " and model.validtime >= ?" ;
        	String[] values={currentTime,currentTime};
  			return getHibernateTemplate().find(queryString, values);
        } catch (RuntimeException re) {
           log.error("find by property name failed", re);
           throw re;
        }
  	}


	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}
	
	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}
	
	public List findByPostcode(Object postcode) {
		return findByProperty(POSTCODE, postcode);
	}
	
	public List findByStarttime(Object starttime) {
		return findByProperty(STARTTIME, starttime);
	}
	
	public List findByEndtime(Object endtime) {
		return findByProperty(ENDTIME, endtime);
	}
	
	public List findByIspublic(Object ispublic) {
		return findByProperty(ISPUBLIC, ispublic);
	}
	
	public List findByDeadline(Object deadline) {
		return findByProperty(DEADLINE, deadline);
	}
	
	public List findByPosttime(Object posttime) {
		return findByProperty(POSTTIME, posttime);
	}
	
	public List findByValidtime (Object validtime){
		return findByProperty(VALIDTIME, validtime);
	}
	
	public List findByFilelist (Object filelist){
		return findByProperty(FILELIST, filelist);
	}
	
	public List findByIstokened(Object istokened) {
		return findByProperty(ISTOKENED, istokened);
	}
	 /**
     * 取出数据库training中的所有的记录
     * written by sillywolf@9#,July, 25,2007
     * @return 数据库training中的所有记录的list
     */
    public List findAll()
    {
    	log.debug("finding all instance from training");
    	try
    	{
    		String queryString="from Training";
    		//Query queryobject=getHibernateTemplate().find(queryString);
    		//return queryobject.list();
    		return getHibernateTemplate().find(queryString);
    	}catch (RuntimeException re)
    	{
    		log.error("find all failed",re);
    		throw re;
    	}
    }
	
    public Training merge(Training detachedInstance) {
        log.debug("merging Training instance");
        try {
            Training result = (Training) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Training instance) {
        log.debug("attaching dirty Training instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Training instance) {
        log.debug("attaching clean Training instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static TrainingDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (TrainingDAO) ctx.getBean("TrainingDAO");
	}
}