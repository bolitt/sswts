package org.net9.redbud.storage.hibernate.login;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Login.
 * 
 * @see org.net9.redbud.storage.hibernate.login.Login
 * @author MyEclipse - Hibernate Tools
 */
public class LoginDAO extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(LoginDAO.class);

	// property constants
	public static final String STUDENTNUM = "studentnum";

	public static final String PASSWORD = "password";

	protected void initDao() {
		// do nothing
	}

	public void save(Login transientInstance) {
		log.debug("saving Login instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	// update method by DennisZz
	public void update(Login persistentInstance) {
		log.debug("updating Login instance");
		try {
			getHibernateTemplate().update(persistentInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	//by urong@9#, 2007.08.03
	public List findAllLogin() {
		log.debug("finding all Login instance");
		try {
			String queryString = "from Login as model";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("return all Login failed", re);
			throw re;
		}
	}

	public void delete(Login persistentInstance) {
		log.debug("deleting Login instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Login findById(java.lang.Integer id) {
		log.debug("getting Login instance with id: " + id);
		try {
			Login instance = (Login) getHibernateTemplate().get(
					"org.net9.redbud.storage.hibernate.login.Login", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Login instance) {
		log.debug("finding Login instance by example");
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
		log.debug("finding Login instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Login as model where model."
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

	public List findByPassword(Object password) {
		return findByProperty(PASSWORD, password);
	}

	public Login merge(Login detachedInstance) {
		log.debug("merging Login instance");
		try {
			Login result = (Login) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Login instance) {
		log.debug("attaching dirty Login instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Login instance) {
		log.debug("attaching clean Login instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static LoginDAO getFromApplicationContext(ApplicationContext ctx) {
		return (LoginDAO) ctx.getBean("LoginDAO");
	}

	/**
	 * Written by sillywolf@9#,May,23,2007
	 * 
	 * @author zhangduo
	 * @param studentNum
	 * @return
	 */
	public Login getLoginByStudentNum(String studentNum) {
		List list = findByStudentnum(studentNum);
		if (list.size() == 0)
			return null;
		else
			return (Login) (list.get(0));

	}

}