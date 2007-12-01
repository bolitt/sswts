package org.net9.redbud.storage.hibernate.category;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.LockMode;
import org.net9.redbud.storage.hibernate.depts.Depts;
import org.net9.redbud.storage.hibernate.organs.Organs;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Data access object (DAO) for domain model class Category.
 * 
 * @see org.net9.redbud.storage.hibernate.category.Category
 * @author MyEclipse - Hibernate Tools
 */
public class CategoryDAO extends HibernateDaoSupport {

	private static final Log log = LogFactory.getLog(CategoryDAO.class);

	// property constants
	public static final String CODE = "code";

	public static final String NAME = "name";

	public static final String DESCRIPTION = "description";

	protected void initDao() {
		// do nothing
	}

	public void save(Category transientInstance) {
		log.debug("saving Category instance");
		try {
			getHibernateTemplate().save(transientInstance);
			log.debug("save successful");
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Category persistentInstance) {
		log.debug("deleting Category instance");
		try {
			getHibernateTemplate().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Category findById(java.lang.Integer id) {
		log.debug("getting Category instance with id: " + id);
		try {
			Category instance = (Category) getHibernateTemplate().get(
					"org.net9.redbud.storage.hibernate.category.Category", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Category instance) {
		log.debug("finding Category instance by example");
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
		log.debug("finding Category instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Category as model where model."
					+ propertyName + "= ?";
			return getHibernateTemplate().find(queryString, value);
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	// by DennisZz
	public List findAllCat() {
		log.debug("finding all Category instance");
		try {
			String queryString = "from Category as model";
			return getHibernateTemplate().find(queryString);
		} catch (RuntimeException re) {
			log.error("return all category failed", re);
			throw re;
		}
	}

	public List findByCode(Object code) {
		return findByProperty(CODE, code);
	}

	public List findByName(Object name) {
		return findByProperty(NAME, name);
	}

	public List findByDescription(Object description) {
		return findByProperty(DESCRIPTION, description);
	}

	public Category merge(Category detachedInstance) {
		log.debug("merging Category instance");
		try {
			Category result = (Category) getHibernateTemplate().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Category instance) {
		log.debug("attaching dirty Category instance");
		try {
			getHibernateTemplate().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Category instance) {
		log.debug("attaching clean Category instance");
		try {
			getHibernateTemplate().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public static CategoryDAO getFromApplicationContext(ApplicationContext ctx) {
		return (CategoryDAO) ctx.getBean("CategoryDAO");
	}

	/**
	 * 根据一级节点的三位编码，返回编码对应的一级节点对象<br>
	 * written by sillywolf@9#,May,27,2007
	 * 
	 * @author zhangduo
	 * @param three_code:一级节点的三位编码
	 * @return：对应的一级节点对象
	 */
	public Category getCategoryByCatCode(String three_code) {
		List li = findByCode(three_code);
		if (li.size() == 0)
			return new Category();
		else
			return (Category) li.get(0);
	}

	/**
	 * 根据一、二级节点的六位编码，返回编码对应的二级节点对象 written by DennisZz@9#,May 24th ,2007
	 * 
	 * @author
	 * @param six_code:
	 *            一、二级节点的六位编码
	 * @return:对应的二级节点对象
	 */
	public Organs getOrganByCatOrganCode(String six_code) {
		Category tempCat = new Category();
		//Organs tempOrgan = new Organs();\
		Organs tempOrgan=null;
		String tempCatCode = six_code.substring(0, 3);
		String tempOrganCode = six_code.substring(3, 6);
		Iterator catIt = findByCode(tempCatCode).iterator();
		while (catIt.hasNext()) {
			tempCat = (Category) catIt.next();
			Iterator organIt = tempCat.getOrganses().iterator();
			while (organIt.hasNext()) {
				Organs to = (Organs) organIt.next();
				if (to.getCode().equals(tempOrganCode)) {
					tempOrgan = to;
					break;
				}
			}
		}
		return tempOrgan;
	}
	
	/**
	 * 根据一级节点的三位编码和二级节点的name，返回编码对应的二级节点对象
	 * by urong@9#, 2007.10.20
	 */
	public Organs getOrganByCatCodeOrganName(String categoryCode, String organName) {
		Category tempCat = new Category();
		Organs tempOrgan=null;
		Iterator catIt = findByCode(categoryCode).iterator();
		while (catIt.hasNext()) {
			tempCat = (Category) catIt.next();
			Iterator organIt = tempCat.getOrganses().iterator();
			while (organIt.hasNext()) {
				Organs to = (Organs) organIt.next();
				if (to.getName().equals(organName)) {
					tempOrgan = to;
					break;
				}
			}
		}
		return tempOrgan;
	}

	/**
	 * 根据一、二、三级节点的九位编码，返回编码对应的三级节点对象 written by DennisZz@9#,August 7th ,2007
	 * 
	 * @author DennisZz
	 * @param nine_code:
	 *            一、二、三级节点的九位编码
	 * @return:对应的三级节点对象
	 */
	public Depts getOrganByCatOrganDeptCode(String nine_code) {
		Category tempCat = new Category();
		Organs tempOrgan = new Organs();
		Depts tempDept = null;
		String tempCatCode = nine_code.substring(0, 3);
		String tempDeptCode = nine_code.substring(6, 9);
		Iterator catIt = findByCode(tempCatCode).iterator();
		while (catIt.hasNext()) {
			tempCat = (Category) catIt.next();
			Iterator organIt = tempCat.getOrganses().iterator();
			while (organIt.hasNext()) {
				tempOrgan = (Organs) organIt.next();
				Iterator deptIt = tempOrgan.getDeptses().iterator();
				while (deptIt.hasNext()) {
					Depts td = (Depts) deptIt.next();
					if (td.getCode().equals(tempDeptCode)) {
						tempDept = td;
						break;
					}
				}
			}
		}
		return tempDept;
	}

	/**
	 * 根据一、二、三级节点的九位编码，返回编码对应的三级节点对象 written by DennisZz@9#,May 24th ,2007
	 * 
	 * @author zhangduo
	 * @param nine_code:
	 *            一、二、三级节点的九位编码
	 * @return:对应的三级节点对象
	 */
	public Depts getDeptByCatOrganDeptCode(String nine_code) {
		Category tempCat = new Category();
		Organs tempOrgan = new Organs();
		Depts tempDept = null;
		String tempCatCode = nine_code.substring(0, 3);
		String tempOrganCode = nine_code.substring(3, 6);
		String tempDeptCode = nine_code.substring(6, 9);

		Iterator catIt = findByCode(tempCatCode).iterator();
		while (catIt.hasNext()) {
			tempCat = (Category) catIt.next();
			Iterator organIt = tempCat.getOrganses().iterator();
			while (organIt.hasNext()) {
				tempOrgan = (Organs) organIt.next();
				if (tempOrgan.getCode().equals(tempOrganCode)) {
					Iterator deptIt = tempOrgan.getDeptses().iterator();
					while (deptIt.hasNext()) {
						Depts td = (Depts) deptIt.next();
						if (td.getCode().equals(tempDeptCode)) {
							tempDept = td;
							break;
						}
					}
					break;
				}
			}
		}
		return tempDept;
	}
	
	/**
	 * 根据一、二级节点的六位编码和三级节点的name，返回编码对应的三级节点对象
	 * by urong@9#, 2007.10.20
	 */
	public Depts getDeptByCatOrganCodeDeptName(String catOrganCode, String deptName) {
		Category tempCat = new Category();
		Organs tempOrgan = new Organs();
		Depts tempDept = null;
		String tempCatCode = catOrganCode.substring(0, 3);
		String tempOrganCode = catOrganCode.substring(3, 6);

		Iterator catIt = findByCode(tempCatCode).iterator();
		while (catIt.hasNext()) {
			tempCat = (Category) catIt.next();
			Iterator organIt = tempCat.getOrganses().iterator();
			while (organIt.hasNext()) {
				tempOrgan = (Organs) organIt.next();
				if (tempOrgan.getCode().equals(tempOrganCode)) {
					Iterator deptIt = tempOrgan.getDeptses().iterator();
					while (deptIt.hasNext()) {
						Depts td = (Depts) deptIt.next();
						if (td.getName().equals(deptName)) {
							tempDept = td;
							break;
						}
					}
					break;
				}
			}
		}
		return tempDept;
	}
}