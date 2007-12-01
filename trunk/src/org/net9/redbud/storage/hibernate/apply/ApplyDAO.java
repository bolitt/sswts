package org.net9.redbud.storage.hibernate.apply;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.net9.redbud.util.PermissionValidate;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

/**
 * Data access object (DAO) for domain model class Apply.
 * 
 * @see org.net9.redbud.storage.hibernate.apply.Apply
 * @author MyEclipse - Hibernate Tools
 */
public class ApplyDAO extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(ApplyDAO.class);

	// property constants
	public static final String STUDENTNUM = "studentnum";

	public static final String POSCODE = "poscode";

	public static final String APPLYREASON = "applyreason";

	public static final String POSCATEGORY = "poscategory";

	public static final String APPLYTIME = "applytime";

	public static final String APPLYSTATUS = "applystatus";

	protected void initDao() {
		// do nothing
	}

	public void save(Apply transientInstance) {
		log.debug("saving Apply instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Apply persistentInstance) {
		log.debug("deleting Apply instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	// by echo
	public void update(Apply persistentInstance) {
		log.debug("updating Apply instance");
		try {
			getHibernateTemplate().update(persistentInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	public Apply findById(java.lang.Integer id) {
		log.debug("getting Apply instance with id: " + id);
		try {
			Apply instance = (Apply) getHibernateTemplate().get(
					"org.net9.redbud.storage.hibernate.apply.Apply", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Apply instance) {
		log.debug("finding Apply instance by example");
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
		log.debug("finding Apply instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Apply as model where model."
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

	public List findByPoscode(Object poscode) {
		return findByProperty(POSCODE, poscode);
	}

	public List findByApplyreason(Object applyreason) {
		return findByProperty(APPLYREASON, applyreason);
	}

	public List findByPoscategory(Object poscategory) {
		return findByProperty(POSCATEGORY, poscategory);
	}

	public List findByApplytime(Object applytime) {
		return findByProperty(APPLYTIME, applytime);
	}

	public List findByApplystatus(Object applystatus) {
		return findByProperty(APPLYSTATUS, applystatus);
	}

	public Apply merge(Apply detachedInstance) {
		log.debug("merging Apply instance");
		try {
			Apply result = (Apply) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Apply instance) {
		log.debug("attaching dirty Apply instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Apply instance) {
		log.debug("attaching clean Apply instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static ApplyDAO getFromApplicationContext(ApplicationContext ctx) {
		return (ApplyDAO) ctx.getBean("ApplyDAO");
	}

	/**
	 * 
	 * @param fullcode
	 * @return
	 * @author zhangduo modified by echo 2007.10.12
	 */
	@SuppressWarnings("unchecked")
	public List<Apply> getAllMyApprovableApply(String fullcode) {
		if (PermissionValidate.isOrganizationBoss(fullcode)) {
			// for the head of Organ, CS's studentunion president,402301 101101
			// for example
			// 402 301 ******
			String cateCode = fullcode.substring(0, 3);
			List<Apply> allCategoryApply = findByPoscategory(cateCode);
			ArrayList<Apply> result = new ArrayList<Apply>();
			if (allCategoryApply.isEmpty() == false) {
				getMyDeptApply(allCategoryApply, result, fullcode);
			}
			return result;
		} else if (PermissionValidate.isCategoryBoss(fullcode)) {
			List<Apply> result = new ArrayList<Apply>();
			String cateCode = fullcode.substring(0, 3);
			List<Apply> allCategoryApply = findByPoscategory(cateCode);
			int point = 0;
			if (allCategoryApply.isEmpty() == false) {
				while (point < allCategoryApply.size()) {
					Apply a = allCategoryApply.get(point);
					int temp = Integer.parseInt(a.getApplystatus());
					if (temp == 2)
						result.add(allCategoryApply.get(point));
					point++;
				}
				return result;
			}
		}
		return new ArrayList<Apply>();
	}

	/**
	 * @author echo
	 * @param allApply
	 * @param result
	 * @param fullcode
	 */
	private void getMyDeptApply(List<Apply> allApply, List<Apply> result,
			String fullcode) {
		// for the boss of some category,402 101101101 , for example
		// he or she could approve 402 101 ******
		int point = 0;
		String prefix = fullcode.substring(0, 6);
		while (point < allApply.size()) {
			String tempFullCode = allApply.get(point).getPoscode();
			if ((tempFullCode.equals(fullcode) == false)
					&& (tempFullCode.substring(0, 6).equals(prefix) == true)) {
				Apply a = allApply.get(point);
				int temp = Integer.parseInt(a.getApplystatus());
				if (temp == 2)
					result.add(allApply.get(point));
			}
			point++;
		}
	}

	@SuppressWarnings("unused")
	private void getExactSubOneApply(List<Apply> allApply, List<Apply> result,
			String fullcode, int prelen) {
		int point = 0;
		int suflen = 9 - prelen;
		String suffix = "";
		for (int i = 0; i < suflen / 3; i++)
			suffix = suffix + "101";
		while (point < allApply.size()) {
			String tempFullcode = allApply.get(point).getPoscode();
			if ((tempFullcode.equals(fullcode) == false)
					&& (tempFullcode.substring(12 - suflen, 12).equals(suffix) == true)
					&& (tempFullcode.substring(0, prelen).equals(
							fullcode.substring(0, prelen)) == true)) {
				Apply a = allApply.get(point);
				if (!result.contains(a)
						&& Integer.parseInt(a.getApplystatus()) == 2)
					result.add(allApply.get(point));
			}
			point++;
		}
	}
}