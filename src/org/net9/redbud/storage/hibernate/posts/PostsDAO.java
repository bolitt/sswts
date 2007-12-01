package org.net9.redbud.storage.hibernate.posts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.net9.redbud.util.PermissionValidate;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Posts.
 * 
 * @see org.net9.redbud.storage.hibernate.posts.Posts
 * @author MyEclipse - Hibernate Tools
 */
public class PostsDAO extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(PostsDAO.class);

	// property constants
	public static final String CODE = "code";

	public static final String NAME = "name";

	public static final String FULLCODE = "fullcode";

	public static final String FULLNAME = "fullname";

	public static final String DESCRIPTION = "description";

	public static final String WEIGHT = "weight";

	public static final String COEFFICIENT = "coefficient";

	public static final String CATEGORY_CODE = "categoryCode";

	public static final String ORGAN_CODE = "organCode";

	public static final String DEPT_CODE = "deptCode";

	public static final String POS_REQ = "posReq";

	public static final String POS_ABILITY1 = "posAbility1";

	public static final String POS_ABILITY2 = "posAbility2";

	public static final String POS_ABILITY3 = "posAbility3";

	public static final String POS_ABILITY4 = "posAbility4";

	public static final String POS_ABILITY5 = "posAbility5";

	public static final String POS_ABILITY6 = "posAbility6";

	public static final String POS_COST = "posCost";

	public static final String RELATED_POS = "relatedPos";

	protected void initDao() {
		// do nothing
	}

	public void save(Posts transientInstance) {
		log.debug("saving Posts instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	// by DennisZz
	public void update(Posts persistentInstance) {
		log.debug("udpating Posts instance");
		try {
			getHibernateTemplate().update(persistentInstance);
			log.debug("update successful");
		} catch (RuntimeException re) {
			log.error("update failed", re);
			throw re;
		}
	}

	public void delete(Posts persistentInstance) {
		log.debug("deleting Posts instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Posts findById(java.lang.Integer id) {
		log.debug("getting Posts instance with id: " + id);
		try {
			Posts instance = (Posts) getHibernateTemplate().get(
					"org.net9.redbud.storage.hibernate.posts.Posts", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Posts instance) {
		log.debug("finding Posts instance by example");
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

	// by DennisZz
	public List findByCatOrganCode(String catCode, String organCode) {
		log.debug("finding Posts instance with category: " + catCode
				+ " and organ: " + organCode);
		try {
			String preparedQuery = "FROM Posts AS model WHERE model.categoryCode = ?"
					+ " AND model.organCode = ?";
			String[] values = { catCode, organCode };
			return getHibernateTemplate().find(preparedQuery, values);
		} catch (RuntimeException re) {
			log.error("find by category and organ failed", re);
			throw re;
		}
	}

	/**
	 * Find Post List By Category,Organs and Depts written by sillywolf@9#,May
	 * 15th,2007
	 * 
	 * @param catCode:Category
	 *            Code
	 * @param organCode:Organ
	 *            code
	 * @param deptCode:Dept
	 *            code
	 * @return:The Position List according to the category code,organ code and
	 *             dept code
	 */
	public List findByCatOrganDeptCode(String catCode, String organCode,
			String deptCode) {
		log.debug("finding Posts instance with category: " + catCode
				+ " ,organ " + organCode + " and depts" + deptCode);
		try {
			String preparedQuery = "FROM Posts AS model WHERE model.categoryCode = ?"
					+ " AND model.organCode = ? AND model.deptCode = ?";
			String[] values = { catCode, organCode, deptCode };
			return getHibernateTemplate().find(preparedQuery, values);
		} catch (RuntimeException re) {
			log.error("find by category, organ and dept failed", re);
			throw re;
		}
	}

	/**
	 * written by sillywolf@9#
	 * 
	 * @param catCode
	 * @param organCode
	 * @param deptCode
	 * @param posCode
	 * @return
	 */
	public List findByCatOrganDeptPosCode(String catCode, String organCode,
			String deptCode, String posCode) {
		log.debug("finding Posts instance with category: " + catCode
				+ ", organ " + organCode + " depts " + deptCode + "posCode "
				+ posCode);
		try {
			String preparedQuery = "FROM Posts AS model WHERE model.categoryCode = ?"
					+ " AND model.organCode = ? AND model.deptCode = ? "
					+ " AND model.code = ?";
			String[] values = { catCode, organCode, deptCode, posCode };
			return getHibernateTemplate().find(preparedQuery, values);
		} catch (RuntimeException re) {
			log.error("find by category, organ, dept ,pos failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding Posts instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Posts as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByFullcode(Object fullcode) {
		return findByProperty(FULLCODE, fullcode);
	}

	public List findByFullname(Object fullname) {
		return findByProperty(FULLNAME, fullname);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public List findByWeight(Object weight) {
		return findByProperty(WEIGHT, weight);
	}

	public List findByCoefficient(Object coefficient) {
		return findByProperty(COEFFICIENT, coefficient);
	}

	public List findByCategoryCode(Object categoryCode) {
		return findByProperty(CATEGORY_CODE, categoryCode);
	}

	public List findByOrganCode(Object organCode) {
		return findByProperty(ORGAN_CODE, organCode);
	}

	public List findByDeptCode(Object deptCode) {
		return findByProperty(DEPT_CODE, deptCode);
	}

	public List findByPosReq(Object posReq) {
		return findByProperty(POS_REQ, posReq);
	}

	public List findByPosAbility1(Object posAbility1) {
		return findByProperty(POS_ABILITY1, posAbility1);
	}

	public List findByPosAbility2(Object posAbility2) {
		return findByProperty(POS_ABILITY2, posAbility2);
	}

	public List findByPosAbility3(Object posAbility3) {
		return findByProperty(POS_ABILITY3, posAbility3);
	}

	public List findByPosAbility4(Object posAbility4) {
		return findByProperty(POS_ABILITY4, posAbility4);
	}

	public List findByPosAbility5(Object posAbility5) {
		return findByProperty(POS_ABILITY5, posAbility5);
	}

	public List findByPosAbility6(Object posAbility6) {
		return findByProperty(POS_ABILITY6, posAbility6);
	}

	public List findByPosCost(Object posCost) {
		return findByProperty(POS_COST, posCost);
	}

	public List findByRelatedPos(Object relatedPos) {
		return findByProperty(RELATED_POS, relatedPos);
	}

	public Posts merge(Posts detachedInstance) {
		log.debug("merging Posts instance");
		try {
			Posts result = (Posts) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Posts instance) {
		log.debug("attaching dirty Posts instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Posts instance) {
		log.debug("attaching clean Posts instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static PostsDAO getFromApplicationContext(ApplicationContext ctx) {
		return (PostsDAO) ctx.getBean("PostsDAO");
	}

	/**
	 * Get the all positions of one dept specified by String dept<br>
	 * written by sillywolf@9#
	 * 
	 * @author zhangduo
	 * @param dept
	 *            the specified department(such as Computer Science Department)
	 * @return the postitions list of the department
	 */
	@SuppressWarnings("unchecked")
	public List<Posts> getSuperByDept(String dept) {
		String depts = "4" + dept;
		List<Posts> list = findByCategoryCode(depts);
		return list;
	}

	/**
	 * Get all positions supervise the position fullcode in the department<br>
	 * written by sillywolf@9#
	 * 
	 * @author zhangduo
	 * @param dept:the
	 *            department(e.g computer science department) which to be
	 *            concerned
	 * @param fullcode:the
	 *            fullcode of the position
	 * @return:the supervised positions of the "fullcode" position in the
	 *             department
	 */
	@SuppressWarnings("unchecked")
	public List<Posts> getSuperByDeptPost(String dept, String fullcode) {
		int templen = fullcode.length();
		String tempCode = null;
		// Get the first section which is not "101" from the low order
		while ((templen > 0)
				&& (fullcode.substring(templen - 3, templen).equals("101") == true))
			templen = templen - 3;

		// if the fullcode stands by the top position(101,101,101,101), return
		// null
		if (templen == 0)
			return null;
		else {
			tempCode = fullcode.substring(0, templen);
		}
		String deptCode = "4" + dept;
		// Get the all positions of the department(e.g Computer
		// ScienceDepartment)
		List<Posts> allPost = findByCategoryCode(deptCode);
		Posts posts = null;
		if (allPost.isEmpty() == false) {
			Iterator iter = allPost.iterator();
			while (iter.hasNext()) {
				posts = (Posts) iter.next();
				if ((posts.getFullcode().substring(0, templen))
						.equals(tempCode) == true)
					iter.remove();
			}
			if ((posts.getFullcode().substring(0, templen)).equals(tempCode) == true)
				allPost.remove(allPost.size() - 1);
		}
		return allPost;
	}

	/**
	 * Write by sillywolf@9#<br>
	 * 
	 * @author zhangduo
	 * @param fullcode:The
	 *            position's full code
	 * @return:
	 */
	@SuppressWarnings("unchecked")
	public List<Posts> getSubOneByDeptPost(String fullcode) {
		int templen = fullcode.length();
		while ((templen > 0)
				&& (fullcode.substring(templen - 3, templen).equals("101") == true))
			templen = templen - 3;
		if (templen == 0) {
			return new ArrayList();
		}
		String deptCode = fullcode.substring(0, 3);
		List<Posts> allPost = findByCategoryCode(deptCode);
		ArrayList<Posts> result = new ArrayList<Posts>();

		if (allPost.isEmpty() == false) {
			getVicePosition(allPost, result, fullcode);
			getExactSubOne(allPost, result, fullcode, templen);
		}
		return result;
	}

	/**
	 * This method returns all the position supervised by the "fullcode"
	 * position<br>
	 * written by sillywolf@9# May,15th,2007<br>
	 * 
	 * @author zhangduo
	 * @param fullcode:the
	 *            position's fullcode
	 * @return:the all position list supervised by "fullcode" position
	 */
	@SuppressWarnings("unchecked")
	public List<Posts> getAllSubPos(String fullcode) {
		int templen = 12;
		while ((templen > 0)
				&& (fullcode.substring(templen - 3, templen).equals("101") == true))
			templen = templen - 3;
		String prefix = fullcode.substring(0, templen);
		if (prefix.length() == 3)
			return findByCategoryCode(prefix);
		else if (prefix.length() == 6)
			return findByCatOrganCode(prefix.substring(0, 3), prefix.substring(
					3, 6));
		else if (prefix.length() == 9)
			return findByCatOrganDeptCode(prefix.substring(0, 3), prefix
					.substring(3, 6), prefix.substring(6, 9));
		else
			return new ArrayList<Posts>();
	}

	/**
	 * This method returns the the positions which the "fullcode" position can
	 * appoint(任命)<br>
	 * written by sillywolf@9#,May 15th ,2007
	 * 
	 * @author zhangduo
	 * @param fullcode:
	 *            the position's full code
	 * @return:all the positions which the "fullcode" position can appoint
	 */
	public List<Posts> getCanAppointPos(String fullcode) {
		int templen = 12;
		while ((templen > 0)
				&& (fullcode.substring(templen - 3, templen).equals("101") == true))
			templen = templen - 3;
		if (templen >= 9) {
			return new ArrayList<Posts>();
		} else if (templen == 6)
			return getAllSubPos(fullcode);
		else
			return getSubOneByDeptPost(fullcode);
	}
	
	//write by echo 2007.09.12
	
	//if boss can rev fired 
	//	return ture
	//else
	//	return false
	
	public static Boolean getCanRevPos(String bossCode, String firedCode){
		if(PermissionValidate.isCategoryBoss(bossCode))
		{
			if(bossCode.substring(0, 3).equals(firedCode.substring(0, 3))&&(!bossCode.equals(firedCode)))
				return true;
			else
				return false;
		}
		if(PermissionValidate.isOrganizationBoss(bossCode))
		{
			if(bossCode.substring(0, 6).equals(firedCode.substring(0, 6))&&(!bossCode.equals(firedCode)))
				return true;
			else
				return false;
		}
		return false;
	}

	/**
	 * Written by sillywolf@9#,May 23,2007
	 * 
	 * @author zhangduo
	 * @param posCode
	 * @return
	 */
	public Posts getPosbyCode(String posCode) {
		List list = findByFullcode(posCode);
		if (list.size() == 0)
			return null;
		else
			return (Posts) (list.get(0));
	}

	private void getVicePosition(List<Posts> allPost, List<Posts> result,
			String fullcode) {
		int point = 0;
		String prefix = fullcode.substring(0, 9);
		while (point < allPost.size()) {
			String tempFullCode = allPost.get(point).getFullcode();
			if ((tempFullCode.equals(fullcode) == false)
					&& (tempFullCode.substring(0, 9).equals(prefix) == true))
				result.add(allPost.get(point));
			point++;
		}
	}

	private void getExactSubOne(List<Posts> allPost, List<Posts> result,
			String fullcode, int prelen) {
		int point = 0;
		int suflen = 9 - prelen;
		String suffix = "";
		for (int i = 0; i < suflen / 3; i++)
			suffix = suffix + "101";
		while (point < allPost.size()) {
			String tempFullcode = allPost.get(point).getFullcode();
			if ((tempFullcode.equals(fullcode) == false)
					&& (tempFullcode.substring(12 - suflen, 12).equals(suffix) == true)
					&& (tempFullcode.substring(0, prelen).equals(
							fullcode.substring(0, prelen)) == true)) {
				if (!result.contains(allPost.get(point)))
					result.add(allPost.get(point));
			}
			point++;
		}
	}
}