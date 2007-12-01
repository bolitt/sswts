package org.net9.redbud.storage.hibernate.posrecord;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Posrecord.
 * 
 * @see org.net9.redbud.storage.hibernate.posrecord.Posrecord
 * @author MyEclipse - Hibernate Tools
 */
public class PosrecordDAO extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(PosrecordDAO.class);

	// property constants
	public static final String POSNAME = "posname";

	public static final String ABILITY1 = "ability1";

	public static final String ABILITY2 = "ability2";

	public static final String ABILITY3 = "ability3";

	public static final String ABILITY4 = "ability4";

	public static final String ABILITY5 = "ability5";

	public static final String ABILITY6 = "ability6";

	public static final String REMARK = "remark";

	public static final String STARTTIME = "starttime";

	public static final String ENDTIME = "endtime";

	public static final String POSCODE = "poscode";

	public static final String STUNUM = "stunum";

	protected void initDao() {
		// do nothing
	}

    

	public void save(Posrecord transientInstance) {
		log.debug("saving Posrecord instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}


	public void delete(Posrecord persistentInstance) {
		log.debug("deleting Posrecord instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	// by echo
	public void update(Posrecord persistentInstance) {
		log.debug("updating Posrecord instance");
		try {
			getHibernateTemplate().update(persistentInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	// by echo
	// find by full code and student num;
	public List findByFullCodeStudentNum(String fullCode, String StudentNum) {
		log.debug("finding Posts instance with fullcode: " + fullCode
				+ " and studentnum: " + StudentNum);
		try {
			String preparedQuery = "FROM Posrecord AS model WHERE model.poscode = ?"
					+ " AND model.stunum = ?";
			String[] values = { fullCode, StudentNum };
			return getHibernateTemplate().find(preparedQuery, values);
		} catch (RuntimeException re) {
			log.error("find by fullcode and studentnum failed", re);
			throw re;
		}
	}

	public Posrecord findById(java.lang.Integer id) {
		log.debug("getting Posrecord instance with id: " + id);
		try {
			Posrecord instance = (Posrecord) getHibernateTemplate()
					.get(
							"org.net9.redbud.storage.hibernate.posrecord.Posrecord",
							id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Posrecord instance) {
		log.debug("finding Posrecord instance by example");
		try {
			List results = getHibernateTemplate().findByExample(instance);
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Posrecord instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Posrecord as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByPosname(Object posname) {
		return findByProperty(POSNAME, posname);
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

	public List findByRemark(Object remark) {
		return findByProperty(REMARK, remark);
	}

	public List findByStarttime(Object starttime) {
		return findByProperty(STARTTIME, starttime);
	}

	public List findByEndtime(Object endtime) {
		return findByProperty(ENDTIME, endtime);
	}

	public List findByPoscode(Object poscode) {
		return findByProperty(POSCODE, poscode);
	}

	public List findByStunum(Object stunum) {
		return findByProperty(STUNUM, stunum);
	}

	public Posrecord merge(Posrecord detachedInstance) {
		log.debug("merging Posrecord instance");
		try {
			Posrecord result = (Posrecord) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Posrecord instance) {
		log.debug("attaching dirty Posrecord instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Posrecord instance) {
		log.debug("attaching clean Posrecord instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static PosrecordDAO getFromApplicationContext(ApplicationContext ctx) {
		return (PosrecordDAO) ctx.getBean("PosrecordDAO");
	}
}