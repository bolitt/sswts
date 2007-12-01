package org.net9.redbud.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.net9.redbud.storage.hibernate.apply.Apply;
import org.net9.redbud.storage.hibernate.apply.ApplyDAO;
import org.net9.redbud.storage.hibernate.category.Category;
import org.net9.redbud.storage.hibernate.category.CategoryDAO;
import org.net9.redbud.storage.hibernate.depts.Depts;
import org.net9.redbud.storage.hibernate.depts.DeptsDAO;
import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;
import org.net9.redbud.storage.hibernate.organs.Organs;
import org.net9.redbud.storage.hibernate.organs.OrgansDAO;
import org.net9.redbud.storage.hibernate.posts.Posts;
import org.net9.redbud.storage.hibernate.posts.PostsDAO;
import org.net9.redbud.storage.hibernate.userinfo.Userinfo;
import org.net9.redbud.util.ComparatorUtils.DeptComparator;
import org.net9.redbud.util.ComparatorUtils.OrganComparator;
import org.net9.redbud.util.ComparatorUtils.PostComparator;

public class DataCenter {

	private PostsDAO postsDAO;

	private ApplyDAO applyDAO;

	private CategoryDAO categoryDAO;

	private OrgansDAO organsDAO;

	private DeptsDAO deptsDAO;

	private LoginDAO loginDAO;

	public LoginDAO getLoginDAO() {
		return loginDAO;
	}

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}

	public void setDeptsDAO(DeptsDAO deptsDAO) {
		this.deptsDAO = deptsDAO;
	}

	public void setApplyDAO(ApplyDAO applyDAO) {
		this.applyDAO = applyDAO;
	}

	public void setPostsDAO(PostsDAO postsDAO) {
		this.postsDAO = postsDAO;
	}

	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public void setOrgansDAO(OrgansDAO organsDAO) {
		this.organsDAO = organsDAO;
	}

	/*
	 * by DennisZz @ August 3th, 2007.
	 * 通过给定当前Cat节点的编码(nodeCode)、希望生成的树的类型(method)来返回特定的树,放入catNode
	 * methodType: 1为所有岗位树 2为空闲岗位树 3为在岗的岗位树
	 */
	public void getCatJsonTree(JSONObject catNode, String catCode,
			int methodType) {
		String noOrganString = new String("尚未进行组织初始化");
		JSONArray organTree = new JSONArray();
		List tempCatList = categoryDAO.findByCode(catCode);
		if (tempCatList == null) {
			return;
		} else {
			Category tempCat = (Category) tempCatList.get(0);
			Set organSet = tempCat.getOrganses();
			if (organSet.size() == 0) {
				JSONObject noOrgan = new JSONObject();
				noOrgan.put("title", noOrganString);
				organTree.add(noOrgan);
			} else {
				Object tempOrganArray[] = organSet.toArray();
				Arrays.sort(tempOrganArray, new OrganComparator());
				for (int organIndex = 0; organIndex < tempOrganArray.length; organIndex++) {
					JSONObject organNode = new JSONObject();
					Organs tempOrgan = (Organs) tempOrganArray[organIndex];
					if(!tempOrgan.getCode().equals("000")){
						getOrganJsonTree(organNode,tempOrgan,methodType);
						organTree.add(organNode);	
					}
				}
			}
			catNode.put("title", tempCat.getName());
			catNode.put("type", "1");
			catNode.put("children", organTree);
			catNode.put("postNo", tempCat.getCode());
		}
		return;
	}

	/*
	 * by DennisZz @ August 4th, 2007.
	 * 通过给定当前Organ节点的对象(tempOrgan)、希望生成的树的类型(method)来返回特定的树,放入organNode
	 * methodType: 1为所有岗位树 2为空闲岗位树 3在岗的岗位树
	 */
	public void getOrganJsonTree(JSONObject organNode, Organs tempOrgan, int methodType){
		String noDeptString = new String("尚未进行部门初始化");
		JSONArray deptTree = new JSONArray();
		Set deptSet = tempOrgan.getDeptses();
		if (deptSet.size() == 0) {
			JSONObject noDept = new JSONObject();
			noDept.put("title", noDeptString);
			deptTree.add(noDept);
		} else {
			Object tempDeptArray[] = deptSet.toArray();
			Arrays.sort(tempDeptArray, new DeptComparator());
			for (int deptIndex = 0; deptIndex < tempDeptArray.length; deptIndex++) {
				JSONObject deptNode = new JSONObject();
				Depts tempDept = (Depts) tempDeptArray[deptIndex];
				getDeptJsonTree(deptNode,tempDept,methodType);
				deptTree.add(deptNode);
			}
		}
		Category tempCat=tempOrgan.getCategory();
		organNode.put("title", tempOrgan.getName());
		organNode.put("type", "2");
		organNode.put("children", deptTree);
		organNode.put("postNo", tempCat.getCode()+ tempOrgan.getCode());
	}
	
	/*
	 * by DennisZz @ August 4th, 2007.
	 * 通过给定当前dept节点的对象(tempDept)、希望生成的树的类型(method)来返回特定的树,放入deptNode
	 * methodType: 1为所有岗位树 2为空闲岗位树 3在岗的岗位树
	 */
	public void getDeptJsonTree(JSONObject deptNode, Depts tempDept,
			int methodType) {
		
		String noPostString = new String("尚未进行岗位初始化");
		String noVacantPosString = new String("该部门目前没有空闲岗位");
		String noHoldingPosString = new String("该部门目前没有人任职");		
		JSONArray postTree = new JSONArray();
		
		Set postSet = tempDept.getPostses();
		int i = 0;
		if (postSet.size() == 0) {
			JSONObject noPost = new JSONObject();
			noPost.put("title", noPostString);
			postTree.add(noPost);
		} else {
			Object tempPostArray[] = postSet.toArray();
			Arrays
					.sort(tempPostArray,
							new PostComparator());
			for (int postIndex = 0; postIndex < tempPostArray.length; postIndex++) {
				Posts tempPost = (Posts) tempPostArray[postIndex];
				JSONObject postNode = new JSONObject();
				if (methodType == 1) {
					postNode.put("title", tempPost
							.getFullname());
					postNode.put("type", "4");
					postNode.put("postNo", tempPost
							.getFullcode());
					postTree.add(postNode);
				} else if ((methodType == 2)
						&& (tempPost.getLogin() == null)) {
					i = 1;
					postNode.put("title", tempPost
							.getFullname());
					postNode.put("type", "4");
					postNode.put("postNo", tempPost
							.getFullcode());
					postTree.add(postNode);
				} else if ((methodType == 3)
						&& (tempPost.getLogin() != null)) {
					i = 2;
					postNode.put("title", tempPost
							.getFullname());
					postNode.put("type", "4");
					postNode.put("postNo", tempPost
							.getFullcode());
					postTree.add(postNode);
				}
			}
		}
		if ((i == 0) && (methodType == 2)) {
			JSONObject noVacantPos = new JSONObject();
			noVacantPos.put("title", noVacantPosString);
			postTree.add(noVacantPos);
		}
		if ((i == 0) && (methodType == 3)) {
			JSONObject noHoldingPos = new JSONObject();
			noHoldingPos.put("title", noHoldingPosString);
			postTree.add(noHoldingPos);
		}
		Organs tempOrgan=(Organs)tempDept.getOrgans();
		Category tempCat=(Category)tempOrgan.getCategory();
		deptNode.put("title", tempDept.getName());
		deptNode.put("type", "3");
		deptNode.put("children", postTree);
		deptNode.put("postNo", tempCat.getCode()
				+ tempOrgan.getCode() + tempDept.getCode());
		return;
	}

	public Vector<String> getPostnumByLogin(Login login) {
		Vector<String> vector = new Vector<String>();
		Iterator it = login.getPostses().iterator();
		Posts posts = new Posts();

		while (it.hasNext()) {
			posts = (Posts) it.next();
			String postNum = posts.getFullcode();
			vector.add(postNum);
		}
		return vector;
	}

	public static void getExactSubOneApply(List<Apply> allApply,
			List<Apply> result, String fullcode, int prelen) {
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
		// System.out.println("getExactSubOne:"+result.size());
	}

	private void getVicePositionApply(List<Apply> allApply, List<Apply> result,
			String fullcode) {
		int point = 0;
		String prefix = fullcode.substring(0, 9);
		while (point < allApply.size()) {
			String tempFullCode = allApply.get(point).getPoscode();
			if ((tempFullCode.equals(fullcode) == false)
					&& (tempFullCode.substring(0, 9).equals(prefix) == true)) {
				Apply a = allApply.get(point);
				int temp = Integer.parseInt(a.getApplystatus());
				if (temp == 2)
					result.add(allApply.get(point));
			}
			point++;
		}
		System.out.println("getVicePositionApply :" + result.size());
	}

	/**
	 * Created by ryan 返回所有的可以审批的岗位
	 * 
	 * @param fullcode
	 * @return
	 */
	public List getAllMyAppointApply(String fullcode) {
		int templen = fullcode.length();
		while ((templen > 0)
				&& (fullcode.substring(templen - 3, templen).equals("101") == true))
			templen = templen - 3;
		if (templen == 0)
			return null;

		String cateCode = fullcode.substring(0, 3);

		List<Apply> allCategoryApply = applyDAO.findByPoscategory(cateCode);
		ArrayList<Apply> result = new ArrayList();

		if (allCategoryApply.isEmpty() == false) {
			getVicePositionApply(allCategoryApply, result, fullcode);
			getExactSubOneApply(allCategoryApply, result, fullcode, templen);
		}
		return result;

	}
	
	/**
	 * 
	 * @param code
	 * @param name
	 * @param cate_id
	 * @param organs_id
	 * @param depts_id
	 * @param descrip
	 * @param weight
	 * @param coefficient
	 * @param pos_req
	 * @param pos_ability1
	 * @param pos_ability2
	 * @param pos_ability3
	 * @param pos_ability4
	 * @param pos_ability5
	 * @param pos_ability6
	 * @param pos_cost
	 * @param related_pos
	 * @return
	 * 
	 */
	public int setNewPosts(String code, String name, int cate_id,
			int organs_id, int depts_id, String descrip, short weight,
			float coefficient, String pos_req, short pos_ability1,
			short pos_ability2, short pos_ability3, short pos_ability4,
			short pos_ability5, short pos_ability6, String pos_cost,
			String related_pos) {

		Category cate = categoryDAO.findById(cate_id);
		Organs organs = organsDAO.findById(organs_id);
		Depts depts = deptsDAO.findById(depts_id);

		Posts posts = new Posts();
		posts.setCode(code);
		posts.setName(name);
		String fullcode = cate.getCode() + organs.getCode() + depts.getCode()
				+ code;
		String fullname = cate.getName() + organs.getName() + depts.getName()
				+ name;
		posts.setFullcode(fullcode);
		posts.setFullname(fullname);
		posts.setDescription(descrip);
		posts.setWeight(weight);
		posts.setCoefficient(coefficient);
		posts.setDepts(depts);
		posts.setCategoryCode(cate.getCode());
		posts.setOrganCode(organs.getCode());
		posts.setDeptCode(depts.getCode());
		posts.setPosReq(pos_req);
		posts.setPosAbility1(pos_ability1);
		posts.setPosAbility2(pos_ability2);
		posts.setPosAbility3(pos_ability3);
		posts.setPosAbility4(pos_ability4);
		posts.setPosAbility5(pos_ability5);
		posts.setPosAbility6(pos_ability6);
		posts.setPosCost(pos_cost);
		posts.setRelatedPos(related_pos);
		//return posts;
		postsDAO.save(posts);
		return posts.getId();
	}
	
	/**
	 * 
	 * @param code
	 * @param name
	 * @param catePoint
	 * @param organPoint
	 * @param depPoint
	 * @param descrip
	 * @param weight
	 * @param coefficient
	 * @param pos_req
	 * @param pos_ability1
	 * @param pos_ability2
	 * @param pos_ability3
	 * @param pos_ability4
	 * @param pos_ability5
	 * @param pos_ability6
	 * @param pos_cost
	 * @param related_pos
	 */
	public Posts setNewPost(String code,String name,CategoryPoint catePoint,
			OrgansPoint organPoint,DeptsPoint depPoint,String descrip,
			short weight,float coefficient, String pos_req, short pos_ability1,
			short pos_ability2, short pos_ability3, short pos_ability4,
			short pos_ability5, short pos_ability6, String pos_cost,
			String related_pos){
		
		Category cate=catePoint.getCategory();
		Organs organ=organPoint.getOrgans();
		Depts dept=depPoint.getDepts();
		Posts posts = new Posts();
		posts.setCode(code);
		posts.setName(name);
		String fullcode=cate.getCode()+organ.getCode()+dept.getCode()+code;
		String fullname=cate.getName()+organ.getName()+dept.getName()+name;
		posts.setFullcode(fullcode);
		posts.setFullname(fullname);
		posts.setDescription(descrip);
		posts.setWeight(weight);
		posts.setCoefficient(coefficient);
		posts.setDepts(dept);
		posts.setCategoryCode(cate.getCode());
		posts.setOrganCode(organ.getCode());
		posts.setDeptCode(dept.getCode());
		posts.setPosReq(pos_req);
		posts.setPosAbility1(pos_ability1);
		posts.setPosAbility2(pos_ability2);
		posts.setPosAbility3(pos_ability3);
		posts.setPosAbility4(pos_ability4);
		posts.setPosAbility5(pos_ability5);
		posts.setPosAbility6(pos_ability6);
		posts.setPosCost(pos_cost);
		posts.setRelatedPos(related_pos);
		return posts;
	}

	public Login setNewUser(String studentNum, String password, String name,
			short grade, String gender, String nation, short depart,
			String classnum, String political, String specialty, String tel,
			String mobile, String email, String address, short ability1,
			short ability2, short ability3, short ability4, short ability5,
			short ability6) {
		Userinfo userinfo = new Userinfo();
		Login login = new Login();
		login.setStudentnum(studentNum);
		login.setPassword(password);
		userinfo.setName(name);
		userinfo.setGrade(grade);
		userinfo.setGender(gender);
		userinfo.setNation(nation);
		userinfo.setDepartment(depart);
		userinfo.setPolitical(political);
		userinfo.setClassnum(classnum);
		userinfo.setSpecialty(specialty);
		userinfo.setEmail(email);
		userinfo.setAddress(address);
		userinfo.setMobile(mobile);
		userinfo.setTel(tel);
		userinfo.setLogin(login);
		userinfo.setAbility1(ability1);
		userinfo.setAbility2(ability2);
		userinfo.setAbility3(ability3);
		userinfo.setAbility4(ability4);
		userinfo.setAbility5(ability5);
		userinfo.setAbility6(ability6);

		login.setUserinfo(userinfo);

		//loginDAO.save(login);

		// userid=userinfo.getId();
		return login;
	}
}
