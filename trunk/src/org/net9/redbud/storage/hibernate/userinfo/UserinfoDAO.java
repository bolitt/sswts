package org.net9.redbud.storage.hibernate.userinfo;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Userinfo.
 * @see org.net9.redbud.storage.hibernate.userinfo.Userinfo
 * @author MyEclipse - Hibernate Tools
 */
public class UserinfoDAO extends HibernateDaoSupport {

    private static final Log log = LogFactory.getLog(UserinfoDAO.class);

	//property constants
	public static final String NAME = "name";
	public static final String GRADE = "grade";
	public static final String GENDER = "gender";
	public static final String NATION = "nation";
	public static final String DEPARTMENT = "department";
	public static final String CLASSNUM = "classnum";
	public static final String POLITICAL = "political";
	public static final String TEL = "tel";
	public static final String MOBILE = "mobile";
	public static final String EMAIL = "email";
	public static final String ADDRESS = "address";
	public static final String ABILITY1 = "ability1";
	public static final String ABILITY2 = "ability2";
	public static final String ABILITY3 = "ability3";
	public static final String ABILITY4 = "ability4";
	public static final String ABILITY5 = "ability5";
	public static final String ABILITY6 = "ability6";
	public static final String POST_REC = "postRec";
	public static final String TRAINING_REC = "trainingRec";
	public static final String ACTIVITY_REC = "activityRec";
	public static final String SPECIALTY = "specialty";

	protected void initDao() {
		//do nothing
	}
    
    public void save(Userinfo transientInstance) {
        log.debug("saving Userinfo instance");
        try {
            getHibernateTemplate().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
    //update method add by DennisZz
    public void update(Userinfo persistentInstance) {
    	log.debug("updating Userinfo instance");
    	try {
    		getHibernateTemplate().update(persistentInstance);
    		log.debug("update successful");
    	} catch(RuntimeException re){
    		log.error("update failed", re);
    		throw re;
    	}
    }
    
	public void delete(Userinfo persistentInstance) {
        log.debug("deleting Userinfo instance");
        try {
            getHibernateTemplate().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Userinfo findById( java.lang.Integer id) {
        log.debug("getting Userinfo instance with id: " + id);
        try {
            Userinfo instance = (Userinfo) getHibernateTemplate()
                    .get("org.net9.redbud.storage.hibernate.userinfo.Userinfo", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Userinfo instance) {
        log.debug("finding Userinfo instance by example");
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
      log.debug("finding Userinfo instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Userinfo as model where model." 
         						+ propertyName + "= ?";
		 return getHibernateTemplate().find(queryString, value);
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}
	
	public List findByGrade(Object grade) {
		return findByProperty(GRADE, grade);
	}
	
	public List findByGender(Object gender) {
		return findByProperty(GENDER, gender);
	}
	
	public List findByNation(Object nation) {
		return findByProperty(NATION, nation);
	}
	
	public List findByDepartment(Object department) {
		return findByProperty(DEPARTMENT, department);
	}
	
	public List findByClassnum(Object classnum) {
		return findByProperty(CLASSNUM, classnum);
	}
	
	public List findByPolitical(Object political) {
		return findByProperty(POLITICAL, political);
	}
	
	public List findByTel(Object tel) {
		return findByProperty(TEL, tel);
	}
	
	public List findByMobile(Object mobile) {
		return findByProperty(MOBILE, mobile);
	}
	
	public List findByEmail(Object email) {
		return findByProperty(EMAIL, email);
	}
	
	public List findByAddress(Object address) {
		return findByProperty(ADDRESS, address);
	}
	
	public List findByAbility1(Object ability1) {
		return findByProperty(ABILITY1, ability1);
	}
	
	public List findByAbility2(Object ability2) {
		return findByProperty(ABILITY2, ability2);
	}
	
	public List findByAbility3(Object ability3) {
		return findByProperty(ABILITY3, ability3);
	}
	
	public List findByAbility4(Object ability4) {
		return findByProperty(ABILITY4, ability4);
	}
	
	public List findByAbility5(Object ability5) {
		return findByProperty(ABILITY5, ability5);
	}
	
	public List findByAbility6(Object ability6) {
		return findByProperty(ABILITY6, ability6);
	}
	
	public List findByPostRec(Object postRec) {
		return findByProperty(POST_REC, postRec);
	}
	
	public List findByTrainingRec(Object trainingRec) {
		return findByProperty(TRAINING_REC, trainingRec);
	}
	
	public List findByActivityRec(Object activityRec) {
		return findByProperty(ACTIVITY_REC, activityRec);
	}
	
	public List findBySpecialty(Object specialty) {
		return findByProperty(SPECIALTY, specialty);
	}
	
    public Userinfo merge(Userinfo detachedInstance) {
        log.debug("merging Userinfo instance");
        try {
            Userinfo result = (Userinfo) getHibernateTemplate()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Userinfo instance) {
        log.debug("attaching dirty Userinfo instance");
        try {
            getHibernateTemplate().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Userinfo instance) {
        log.debug("attaching clean Userinfo instance");
        try {
            getHibernateTemplate().lock(instance, LockMode.NONE);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }

	public static UserinfoDAO getFromApplicationContext(ApplicationContext ctx) {
    	return (UserinfoDAO) ctx.getBean("UserinfoDAO");
	}
}