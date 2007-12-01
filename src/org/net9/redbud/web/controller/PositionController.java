package org.net9.redbud.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.net9.redbud.storage.hibernate.posrecord.Posrecord;
import org.net9.redbud.storage.hibernate.posrecord.PosrecordDAO;
import org.net9.redbud.storage.hibernate.posts.Posts;
import org.net9.redbud.storage.hibernate.posts.PostsDAO;
import org.net9.redbud.storage.hibernate.userinfo.Userinfo;
import org.net9.redbud.util.DataCenter;
import org.net9.redbud.util.DateUtils;
import org.net9.redbud.util.DepartmentUtils;
import org.net9.redbud.util.PermissionValidate;
import org.net9.redbud.util.ComparatorUtils.CatComparator;
import org.net9.redbud.web.util.JsonUtils;
import org.springframework.web.servlet.ModelAndView;

public class PositionController extends RedbudBaseController {

	private PosrecordDAO posrecordDAO;

	private CategoryDAO categoryDAO;

	private ApplyDAO applyDAO;

	private PostsDAO postsDAO;

	private LoginDAO loginDAO;

	private DeptsDAO deptsDAO;

	private OrgansDAO organsDAO;

	private DataCenter dataCenter;

	public void setDataCenter(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}

	public void setApplyDAO(ApplyDAO applyDAO) {
		this.applyDAO = applyDAO;
	}

	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public void setDeptsDAO(DeptsDAO deptsDAO) {
		this.deptsDAO = deptsDAO;
	}

	public void setOrgansDAO(OrgansDAO organsDAO) {
		this.organsDAO = organsDAO;
	}

	public void setPostsDAO(PostsDAO postsDAO) {
		this.postsDAO = postsDAO;
	}

	public void setPosrecordDAO(PosrecordDAO posrecordDAO) {
		this.posrecordDAO = posrecordDAO;
	}

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}

	// by echo 07.28
	// 岗位申请 "提交"
	public ModelAndView applyPosition(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter pw = response.getWriter();
		String studentNum = (String) request.getSession().getAttribute(
				"studentnum");
		Userinfo ui = (Userinfo) request.getSession().getAttribute("userinfo");
		String posCode = request.getParameter("poscode");
		String applyReason = request.getParameter("reason");

		// 恶意攻击处理 by DennisZz, August 13th,2007.
		// posCode所代表的岗位存在且应当是空闲岗位
		List tempPosts = postsDAO.findByFullcode(posCode);
		if (tempPosts.size() == 0) {
			pw.print("invalid");
			pw.close();
			return null;
		}
		Posts tempPost = (Posts) tempPosts.get(0);
		if (tempPost.getLogin() != null) {
			pw.print("invalid");
			pw.close();
			return null;
		}

		// 恶意攻击处理 by DennisZz, August 13th,2007.
		// posCode如果代表的是系级某岗位，那么它应当与本人所在系一致
		if (posCode.substring(0, 1).equals("4")) {
			String deptcode = new String();
			int deptNo = ui.getDepartment();
			if (deptNo < 10)
				deptcode = "0" + deptNo;
			deptcode = "4" + deptcode;
			if (!posCode.substring(0, 3).equals(deptcode)) {
				pw.print("invalid");
				pw.close();
				return null;
			}
		}

		Apply apply = new Apply();
		apply.setPoscode(posCode);
		apply.setStudentnum(studentNum);
		apply.setApplyreason(applyReason);
		apply.setPoscategory(posCode.substring(0, 3));
		String dateFormat = DateUtils.getCurrentDay();
		apply.setApplytime(dateFormat);
		apply.setApplystatus("2");
		applyDAO.save(apply);
		pw.print("true");
		pw.close();
		return null;
	}

	// by echo 07.28
	public ModelAndView approvePos(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter pw = response.getWriter();
		String applyid = request.getParameter("applyid");
		String ok = request.getParameter("ok");

		Apply apply = applyDAO.findById(Integer.parseInt(applyid));
		String poscode = apply.getPoscode();
		Posts posts = (Posts) (postsDAO.findByFullcode(poscode).get(0));

		List temp = posrecordDAO.findByPoscode(posts.getFullcode());

		if (temp.size() == 0) {
			pw.print("false");
			pw.close();
			return null;
		}

		String studentnum = apply.getStudentnum();

		Login l = (Login) loginDAO.findByStudentnum(studentnum).get(0);

		
		
		
		List<Posrecord> posrecordList = posrecordDAO.findByStunum(studentnum);
		int point = 0;
		while (point < posrecordList.size()) {
			Posrecord tempRecord = posrecordList.get(point);
			// check if there is some position which is still on//
			if((tempRecord.getPoscode().equals(poscode))&&(tempRecord.getEndtime()==null))
			{
				pw.print("false");
				pw.close();
				return null;
			}
			point++;
		}
		//else
		
		if (ok.equals("true")) {
			posts.setLogin(l);
			apply.setApplystatus("0");

		}
		if (ok.equals("false")) {
			apply.setApplystatus("1");
		}
		applyDAO.update(apply);
		postsDAO.update(posts);
		
		
		Posrecord postrecord = new Posrecord();
		postrecord.setLogin(l);
		postrecord.setStunum(l.getStudentnum());

		String dateFormat = DateUtils.getCurrentDay();

		postrecord.setStarttime(dateFormat);
		postrecord.setPoscode(posts.getFullcode());
		postrecord.setPosname(posts.getFullname());

		// update
		
		posrecordDAO.save(postrecord);
		pw.print("true");
		pw.close();
		return null;
	}

	// by echo 07.28, modified by DennisZz.
	// 岗位审批

	public ModelAndView getApprovable(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		// PostsDAO pd = new PostsDAO();
		// LoginDAO ld = new LoginDAO();

		ArrayList<String> allpost = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		int point2 = 0;
		JSONArray thearray = new JSONArray();
		while (point2 < allpost.size()) {
			String mypostcode = (String) allpost.get(point2);

			// by DennisZz, August 13th, 2007.
			// 只有OrganBoss或者root返回，其他的由于不具备审批权限不返回。
			if (!PermissionValidate.isAtLeastOrganBoss(mypostcode)) {
				point2++;
				continue;
			}

			JSONObject cate = new JSONObject();
			JSONArray childrenarray = new JSONArray();
			Posts myposts = (Posts) (postsDAO.findByFullcode(mypostcode).get(0));
			cate.put("name", myposts.getFullname());

			List<Apply> result = applyDAO.getAllMyApprovableApply(mypostcode);
			Apply apply;
			int point = 0;
			while (point < result.size()) {
				Posts nodepos;
				Userinfo userinfo;
				Login login;

				apply = (Apply) result.get(point);
				nodepos = (Posts) postsDAO.findByFullcode(apply.getPoscode())
						.get(0);
				login = (Login) loginDAO
						.findByStudentnum(apply.getStudentnum()).get(0);
				userinfo = login.getUserinfo();

				JSONObject node = new JSONObject();
				node.put("posname", nodepos.getFullname());
				node.put("stuname", userinfo.getName());
				node.put("studentnum", login.getStudentnum());
				node.put("id", apply.getId());
				childrenarray.add(node);
				point++;
			}
			cate.put("children", childrenarray);
			thearray.add(cate);
			point2++;
		}

		String s1 = thearray.toString();

		pw.print(s1);
		pw.flush();
		pw.close();
		return null;
	}

	// by echo 07.28;
	// 看到下属的申请状况
	public ModelAndView showApplyDetail(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter pw = response.getWriter();

		// 恶意攻击处理 by DennisZz, Auguest 13th,2007.
		// 1 本人需有两个101以上权限
		// 2 判断applyId对应的申请职位是否为其管辖范围内
		List myPosCodeList = (List) request.getSession().getAttribute(
				"postList");
		if (myPosCodeList.size() == 0) {
			pw.write("invalid");
			pw.close();
			return null;
		}
		String applyId = request.getParameter("applyid");
		int id = Integer.parseInt(applyId);
		int flag = 0;
		Apply apply = applyDAO.findById(id);
		String applyPosCode = apply.getPoscode();
		for (int i = 0; i < myPosCodeList.size(); i++) {
			String tempPosCode = (String) myPosCodeList.get(i);
			if ((PermissionValidate.isAtLeastOrganBoss(tempPosCode))
					&& (PermissionValidate.hasPosManagementPermission(
							tempPosCode, applyPosCode))) {
				flag = 1;
			}
		}
		if (flag == 0)
		// 没有管辖权限
		{
			pw.write("invalid");
			pw.close();
			return null;
		}
		// 有管辖权限
		JSONObject jsonObject = new JSONObject();
		String studentNum = apply.getStudentnum();
		List list = loginDAO.findByStudentnum(studentNum);
		Posts post = (Posts) postsDAO.findByFullcode(applyPosCode).get(0);
		if (list.isEmpty() == false) {
			Userinfo user = ((Login) list.get(0)).getUserinfo();
			jsonObject
					.put("userinfo", JsonUtils.getJsonObjectForUserinfo(user));
			jsonObject.put("pos", JsonUtils.getJsonObjectForPosts(post));
			jsonObject.put("reason", apply.getApplyreason());
			jsonObject.put("applytime", apply.getApplytime());
			jsonObject.put("applystatus", apply.getApplystatus());
		}
		pw.print(jsonObject);
		pw.flush();
		pw.close();
		return null;
	}

	/**
	 * @author DennisZz@9# 返回岗位浏览树的Json结构
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView getAllPosition(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// all the positions
		JSONArray allPosTree = new JSONArray();
		// positions of school-level
		JSONArray schPosTree = new JSONArray();
		// positions of dept-level
		JSONArray deptPosTree = new JSONArray();
		// positions of assosiations
		JSONArray assnPosTree = new JSONArray();

		Userinfo user = new Userinfo();
		PrintWriter out = response.getWriter();

		user = (Userinfo) request.getSession().getAttribute("userinfo");
		List postList = (List) request.getSession().getAttribute("postList");
		String deptcode = new String();
		int deptNo = user.getDepartment();
		if (deptNo < 10)
			deptcode = "0" + deptNo;
		deptcode = "4" + deptcode;
		int[] flag = { 0, 0, 0 };

		List list = categoryDAO.findAllCat();
		Object[] tempCatArray = list.toArray();
		Arrays.sort(tempCatArray, new CatComparator());
		// no category data at all!
		for (int i = 0; i < tempCatArray.length; i++) {
			try {
				Category cat = (Category) tempCatArray[i];
				String catCode = cat.getCode().toString();
				if (catCode.equals("000")) {
					// 不显示root Category
				}
				// 协会
				else if (catCode.equals("601")) {
					JSONObject catNode = new JSONObject();
					dataCenter.getCatJsonTree(catNode, catCode, 1);
					assnPosTree = (JSONArray) catNode.get("children");
					flag[2] = 1;
				}
				// 系级
				else if (catCode.substring(0, 1).equals("4")) {
					int rootFlag = 0;
					for (int postIndex = 0; postIndex < postList.size(); postIndex++) {
						String tempPostStr = (String) postList.get(postIndex);
						if (PermissionValidate.isRoot(tempPostStr)) {
							JSONObject catNode = new JSONObject();
							dataCenter.getCatJsonTree(catNode, catCode, 1);
							deptPosTree.add(catNode);
							flag[1] = 1;
							rootFlag = 1;
							break;
						}
					}
					if ((rootFlag == 0) && (catCode.equals(deptcode))) {
						JSONObject catNode = new JSONObject();
						dataCenter.getCatJsonTree(catNode, catCode, 1);
						deptPosTree = (JSONArray) catNode.get("children");
						flag[1] = 1;
					}
				}
				// 校级(未来602以上的6字头组织归入此列)
				else {
					JSONObject catNode = new JSONObject();
					dataCenter.getCatJsonTree(catNode, catCode, 1);
					schPosTree.add(catNode);
					flag[0] = 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String str1 = "校级岗位结构尚未进行初始化";
		String str2 = "贵系的岗位结构尚未进行初始化";
		String str3 = "协会的岗位结构尚未进行初始化";
		if (flag[0] == 0) {
			JSONObject noSchCat = new JSONObject();
			noSchCat.put("title", str1);
			schPosTree.add(noSchCat);
		}
		if (flag[1] == 0) {
			JSONObject noDeptCat = new JSONObject();
			noDeptCat.put("title", str2);
			deptPosTree.add(noDeptCat);
		}
		if (flag[2] == 0) {
			JSONObject noAssnCat = new JSONObject();
			noAssnCat.put("title", str3);
			assnPosTree.add(noAssnCat);
		}
		allPosTree.add(schPosTree);
		allPosTree.add(deptPosTree);
		allPosTree.add(assnPosTree);

		out.write(allPosTree.toString());
		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author DennisZz@9# 返回岗位申请树的Json结构
	 */
	public ModelAndView getAvailablePosition(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// all the vacant positions
		JSONArray allVacantPosTree = new JSONArray();
		// vacant positions of school-level
		JSONArray schVacantPosTree = new JSONArray();
		// vacant positions of dept-level
		JSONArray deptVacantPosTree = new JSONArray();
		// vacant positions of assosiations
		JSONArray assnVacantPosTree = new JSONArray();

		Userinfo user = new Userinfo();
		PrintWriter out = response.getWriter();

		user = (Userinfo) request.getSession().getAttribute("userinfo");
		List postList = (List) request.getSession().getAttribute("postList");
		String deptcode = user.getDepartment().toString();
		if (Short.parseShort(deptcode) < (short) 10)
			deptcode = "0" + deptcode;
		deptcode = "4" + deptcode;
		int[] flag = { 0, 0, 0 };
		List catList = categoryDAO.findAllCat();
		Object tempCatArray[] = catList.toArray();
		Arrays.sort(tempCatArray, new CatComparator());
		// no category data at all!
		for (int catIndex = 0; catIndex < tempCatArray.length; catIndex++) {
			try {
				Category cat = (Category) tempCatArray[catIndex];
				String catCode = cat.getCode().toString();
				if (catCode.equals("000")) {
					// 不显示root的Category
				}
				// 协会
				else if (catCode.equals("601")) {
					JSONObject catNode = new JSONObject();
					dataCenter.getCatJsonTree(catNode, catCode, 2);
					assnVacantPosTree = (JSONArray) catNode.get("children");
					flag[2] = 1;
				}
				// 系级
				else if (catCode.substring(0, 1).equals("4")) {
					int rootFlag = 0;
					for (int postIndex = 0; postIndex < postList.size(); postIndex++) {
						String tempPostStr = (String) postList.get(postIndex);
						if (PermissionValidate.isRoot(tempPostStr)) {
							JSONObject catNode = new JSONObject();
							dataCenter.getCatJsonTree(catNode, catCode, 2);
							deptVacantPosTree.add(catNode);
							flag[1] = 1;
							rootFlag = 1;
							break;
						}
					}
					if ((rootFlag == 0) && (catCode.equals(deptcode))) {
						JSONObject catNode = new JSONObject();
						dataCenter.getCatJsonTree(catNode, catCode, 2);
						deptVacantPosTree = (JSONArray) catNode.get("children");
						flag[1] = 1;
					}
				}
				// 校级(未来602以上的6字头组织归入此列)
				else {
					JSONObject catNode = new JSONObject();
					dataCenter.getCatJsonTree(catNode, catCode, 2);
					schVacantPosTree.add(catNode);
					flag[0] = 1;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String str1 = "校级岗位结构尚未进行初始化";
		String str2 = "贵系的岗位结构尚未进行初始化";
		String str3 = "协会的岗位结构尚未进行初始化";
		if (flag[0] == 0) {
			JSONObject noSchCat = new JSONObject();
			noSchCat.put("title", str1);
			schVacantPosTree.add(noSchCat);
		}
		if (flag[1] == 0) {
			JSONObject noDeptCat = new JSONObject();
			noDeptCat.put("title", str2);
			deptVacantPosTree.add(noDeptCat);
		}
		if (flag[2] == 0) {
			JSONObject noAssnCat = new JSONObject();
			noAssnCat.put("title", str3);
			assnVacantPosTree.add(noAssnCat);
		}

		allVacantPosTree.add(schVacantPosTree);
		allVacantPosTree.add(deptVacantPosTree);
		allVacantPosTree.add(assnVacantPosTree);

		out.write(allVacantPosTree.toString());
		out.flush();
		out.close();
		return null;
	}

	@SuppressWarnings("unchecked")
	// 任命点Done的一下
	public ModelAndView appointPosition(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();

		String applyNum = request.getParameter("studentnum");
		String posNo = request.getParameter("posNo");

		Posts posts = postsDAO.getPosbyCode(posNo);
		Login login = loginDAO.getLoginByStudentNum(applyNum);
		Userinfo appointedUserinfo = login.getUserinfo();

		if ((login != null) && (posts != null)) {
			// 恶意攻击处理 by DennisZz, Auguest 27th,2007.
			// 1 如果是系级的老大,判断studentnum是否是该系的（校级的不管）
			// 2 判断posNo在不在其管辖范围
			ArrayList<String> allpost = (ArrayList<String>) request
					.getSession().getAttribute("postList");
			int flag = 0;
			for (int index = 0; index < allpost.size(); index++) {
				String mypostcode = (String) allpost.get(index);
				// 有任免权限且对有posNo有管理权限
				if ((PermissionValidate.isXiJiAtLeastOrganBoss(mypostcode))
						&& (PermissionValidate.hasPosManagementPermission(
								mypostcode, posNo))) {
					int deptNo = Integer.parseInt(mypostcode.substring(1, 3));
					if (appointedUserinfo.getDepartment() == deptNo) {
						flag = 1;
					}
				} else if ((PermissionValidate.isAtLeastOrganBoss(mypostcode))
						&& (PermissionValidate.hasPosManagementPermission(
								mypostcode, posNo))) {
					flag = 1;
				}
			}
			if (flag == 0) {
				out.print("invalid");
				out.flush();
				return null;
			}
			//
			posts.setLogin(login);
			postsDAO.update(posts);
			Posrecord postrecord = new Posrecord();
			postrecord.setLogin(login);
			postrecord.setStunum(login.getStudentnum());

			String dateFormat = DateUtils.getCurrentDay();

			postrecord.setStarttime(dateFormat);
			postrecord.setPoscode(posts.getFullcode());
			postrecord.setPosname(posts.getFullname());
			posrecordDAO.save(postrecord);

			out.print("true");
			out.flush();
		}
		return null;
	}

	/**
	 * @author DennisZz@9# 返回岗位任命树的Json结构
	 */
	@SuppressWarnings("unchecked")
	public ModelAndView getAppPosition(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JSONArray appTrees = new JSONArray();
		ArrayList<String> v = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		Iterator it = v.iterator();
		while (it.hasNext()) {
			String temp = (String) it.next();
			if (PermissionValidate.isRoot(temp)) {
				// vacant positions of school-level
				JSONArray schVacantPosTree = new JSONArray();
				// vacant positions of dept-level
				JSONArray deptVacantPosTree = new JSONArray();
				// vacant positions of assosiations
				JSONArray assnVacantPosTree = new JSONArray();

				Userinfo user = new Userinfo();
				user = (Userinfo) request.getSession().getAttribute("userinfo");
				String deptcode = user.getDepartment().toString();
				if (Short.parseShort(deptcode) < (short) 10)
					deptcode = "0" + deptcode;
				deptcode = "4" + deptcode;
				int[] flag = { 0, 0, 0 };
				List catList = categoryDAO.findAllCat();
				Object tempCatArray[] = catList.toArray();
				Arrays.sort(tempCatArray, new CatComparator());
				// no category data at all!
				for (int catIndex = 0; catIndex < tempCatArray.length; catIndex++) {
					try {
						Category cat = (Category) tempCatArray[catIndex];
						String catCode = cat.getCode().toString();
						if (catCode.equals("000")) {
							// 不显示root的Category
						}
						// 协会
						else if (catCode.equals("601")) {
							JSONObject catNode = new JSONObject();
							dataCenter.getCatJsonTree(catNode, catCode, 2);
							assnVacantPosTree = (JSONArray) catNode
									.get("children");
							flag[2] = 1;
						}
						// 系级
						else if (catCode.substring(0, 1).equals("4")) {
							JSONObject catNode = new JSONObject();
							dataCenter.getCatJsonTree(catNode, catCode, 2);
							deptVacantPosTree.add(catNode);
							flag[1] = 1;
						}
						// 校级(未来602以上的6字头组织归入此列)
						else {
							JSONObject catNode = new JSONObject();
							dataCenter.getCatJsonTree(catNode, catCode, 2);
							schVacantPosTree.add(catNode);
							flag[0] = 1;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				String str1 = "校级岗位结构尚未进行初始化";
				String str2 = "贵系的岗位结构尚未进行初始化";
				String str3 = "协会的岗位结构尚未进行初始化";
				if (flag[0] == 0) {
					JSONObject noSchCat = new JSONObject();
					noSchCat.put("title", str1);
					schVacantPosTree.add(noSchCat);
				}
				if (flag[1] == 0) {
					JSONObject noDeptCat = new JSONObject();
					noDeptCat.put("title", str2);
					deptVacantPosTree.add(noDeptCat);
				}
				if (flag[2] == 0) {
					JSONObject noAssnCat = new JSONObject();
					noAssnCat.put("title", str3);
					assnVacantPosTree.add(noAssnCat);
				}
				JSONObject schVacantPosNode = new JSONObject();
				JSONObject deptVacantPosNode = new JSONObject();
				JSONObject assnVacantPosNode = new JSONObject();
				schVacantPosNode.put("title", "校级岗位");
				schVacantPosNode.put("children", schVacantPosTree);
				deptVacantPosNode.put("title", "系级岗位");
				deptVacantPosNode.put("children", deptVacantPosTree);
				assnVacantPosNode.put("title", "协会岗位");
				assnVacantPosNode.put("children", assnVacantPosTree);
				appTrees.add(schVacantPosNode);
				appTrees.add(deptVacantPosNode);
				appTrees.add(assnVacantPosNode);
			} else if (PermissionValidate.isCategoryBoss(temp)) {
				String tempCatCode = temp.substring(0, 3);
				JSONObject catNode = new JSONObject();
				dataCenter.getCatJsonTree(catNode, tempCatCode, 2);
				appTrees.add(catNode);

				// else if保证了cat老大不会落入此列
			} else if (PermissionValidate.isOrganizationBoss(temp)) {
				String tempCatOrganCode = temp.substring(0, 6);
				Organs tempOrgan = new Organs();
				tempOrgan = categoryDAO
						.getOrganByCatOrganCode(tempCatOrganCode);
				JSONObject organNode = new JSONObject();
				dataCenter.getOrganJsonTree(organNode, tempOrgan, 2);
				appTrees.add(organNode);
			}
		}

		PrintWriter out = response.getWriter();
		out.write(appTrees.toString());
		out.flush();
		out.close();
		return null;
	}

	// 岗位任免中输入学号之后，出可免职务的页面

	// rewrite by echo 2007.09.12

	public ModelAndView getRevPosition(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();
		JSONArray canFirePoses = new JSONArray();
		ArrayList<String> myPostList = (ArrayList<String>) request.getSession()
				.getAttribute("postList");

		String myStudentNum = (String) request.getSession().getAttribute(
				"studentnum");

		String firedStudentNum = request.getParameter("studentnum");

		Login firedManslogin = loginDAO.getLoginByStudentNum(firedStudentNum);

		// is Root
		if (myStudentNum.equals("root")) {
			Iterator firedManPostIt = firedManslogin.getPostses().iterator();
			while (firedManPostIt.hasNext()) {
				Posts tempPost = (Posts) firedManPostIt.next();
				JSONObject canFirePos = new JSONObject();
				if (!(tempPost.getFullcode()).equals("000000000000")) {
					canFirePos.put("fullCode", tempPost.getFullcode());
					canFirePos.put("fullName", tempPost.getFullname());
					canFirePoses.add(canFirePos);
				}
			}
		}
		// is not Root
		else {
			Iterator postIt = myPostList.iterator();
			List<String> posFlags=new ArrayList<String>();
			int flag=0;
			while (postIt.hasNext()) {
				Iterator firedManPostIt = firedManslogin.getPostses().iterator();
				String fireManFullcode = (String) postIt.next();
				while (firedManPostIt.hasNext()) {
					Posts tempPost = (Posts) firedManPostIt.next();
					for(int flagIndex=0;flagIndex<posFlags.size();flagIndex++){
						if(posFlags.get(flagIndex).equals(tempPost.getFullcode())){
							flag=1;
						}
					}
					if (PostsDAO.getCanRevPos(fireManFullcode, tempPost
							.getFullcode())&&(flag==0)) {
						posFlags.add(tempPost.getFullcode());
						JSONObject canFirePos = new JSONObject();
						canFirePos.put("fullCode", tempPost.getFullcode());
						canFirePos.put("fullName", tempPost.getFullname());
						canFirePoses.add(canFirePos);
					}
				}
			}
		}
		out.write(canFirePoses.toString());
		out.close();
		return null;
	}

	// by echo
	// 输入学号，得到相关信息
	// 代码很难看,urong
	public ModelAndView getSimpleUserinfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 权限判断
		boolean hasPermission = false;
		ArrayList<String> allpos = (ArrayList<String>) (request.getSession()
				.getAttribute("postList"));
		Iterator allposIter = allpos.iterator();
		while (allposIter.hasNext()) {
			String pos = (String) allposIter.next();

			// 系级的2个101以上只能看到本系的
			if (PermissionValidate.isXiJiAtLeastOrganBoss(pos)) {
				String stnum = request.getParameter("stnum");
				Iterator iter = loginDAO.findByStudentnum(stnum).iterator();
				if (iter.hasNext()) {
					Userinfo ui = ((Login) iter.next()).getUserinfo();
					int depNum = ui.getDepartment();
					int fullDepNum = 400 + depNum;
					String fullDepNumStr = String.valueOf(fullDepNum);
					if (pos.startsWith(fullDepNumStr)) {
						hasPermission = true;
						break;
					}
				}
			}
			// 校级的所有2个101以上都能看到全校的
			else if (PermissionValidate.isAtLeastOrganBoss(pos)) {
				hasPermission = true;
				break;
			}
		}

		if (!hasPermission) {
			response.getWriter().print("false");
			return null;
		}

		String stnum = request.getParameter("stnum");
		List list = loginDAO.findByStudentnum(stnum);
		Iterator it = list.iterator();
		JSONObject json;
		if (!it.hasNext()) {
			json = new JSONObject();
			json.put("error", "1");
		} else {
			Login l = (Login) it.next();
			Userinfo userinfo;
			userinfo = l.getUserinfo();
			JSONObject basicInfo = new JSONObject();
			basicInfo.put("pic", "SOME URL");
			basicInfo.put("stunum", l.getStudentnum());
			basicInfo.put("name", userinfo.getName());
			basicInfo.put("gender", userinfo.getGender());
			basicInfo.put("dept", DepartmentUtils.getInstance().getDepartment(
					userinfo.getDepartment()));
			basicInfo.put("classnum", userinfo.getClassnum());
			json = basicInfo;
		}
		PrintWriter pw = response.getWriter();
		pw.print(json);
		pw.close();
		return null;
	}

	// 免除点done的一下
	public ModelAndView revPosition(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String studentnum = request.getParameter("studentnum");
		String fullCode = request.getParameter("posNos");
		ArrayList<String> v = (ArrayList<String>) request.getSession()
				.getAttribute("postList");

		Posts post;
		Posrecord posr;
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		String monthStr, dayStr;
		try {
			if (month < 10) {
				monthStr = "0" + Integer.toString(month);
			} else {
				monthStr = Integer.toString(month);
			}
			if (day < 10) {
				dayStr = "0" + Integer.toString(day);
			} else {
				dayStr = Integer.toString(day);
			}
			String dateFormat = Integer.toString(year) + monthStr + dayStr;
			List pList;
			List prList;
			Iterator iter;
			if (fullCode.length() > 12) {
				String[] codeArray = fullCode.split(",");
				for (int i = 0; i < codeArray.length; i++) {
					// 恶意攻击处理 by DennisZz, Auguest 30th,2007.
					// 1 传入的fullCode是不是在当前用户的任免能力范围内
					int flag = 0;
					String postcodeToBeFired = codeArray[i];
					Iterator postIt = v.iterator();
					while (postIt.hasNext()) {
						String postcodeToFire = (String) postIt.next();
						if (PermissionValidate.hasPosManagementPermission(
								postcodeToFire, postcodeToBeFired)) {
							flag = 1;
						}
					}
					if (flag == 0) {
						out.print("invalid");
						out.flush();
						return null;
					}
					pList = postsDAO.findByFullcode(postcodeToBeFired);
					if (pList.size() != 0) {
						post = (Posts) pList.get(0);
						// 恶意攻击处理 by DennisZz, Auguest 27th,2007.
						// 2 那个岗位的人是不是传入的studentnum
						if (post.getLogin() != null) {
							Login login = loginDAO.findById(post.getLogin()
									.getId());
							String temp = login.getStudentnum();
							if (!temp.equals(studentnum)) {
								out.print("invalid");
								out.flush();
								return null;
							}
						}
						post.setLogin(null);
						postsDAO.update(post);

						// 设置apply表中的applystatus值为3，表示已经申请过然后被免除
						List applyList = applyDAO.findByStudentnum(studentnum);
						Iterator applyListIter = applyList.iterator();
						while (applyListIter.hasNext()) {
							Apply apply = (Apply) applyListIter.next();
							if (apply.getPoscode().equals(fullCode)) {
								apply.setApplystatus("3");
								applyDAO.update(apply);
							}
						}
					}
					prList = posrecordDAO.findByFullCodeStudentNum(
							codeArray[i], studentnum);
					if (!prList.isEmpty()) {
						iter = prList.iterator();
						while (iter.hasNext()) {
							posr = (Posrecord) iter.next();
							if (posr.getEndtime() == null) {
								posr.setEndtime(dateFormat);
								posrecordDAO.update(posr);
							}
						}
					}
				}
			} else {
				// 恶意攻击处理 by DennisZz, Auguest 30th,2007.
				// 1 传入的fullCode是不是在当前用户的任免能力范围内
				int flag = 0;
				Iterator postIt = v.iterator();
				while (postIt.hasNext()) {
					String postcodeToFire = (String) postIt.next();
					if (PermissionValidate.hasPosManagementPermission(
							postcodeToFire, fullCode)) {
						flag = 1;
					}
				}
				if (flag == 0) {
					out.print("invalid");
					out.flush();
					return null;
				}
				pList = postsDAO.findByFullcode(fullCode);
				if (pList.size() != 0) {
					post = (Posts) pList.get(0);
					// 恶意攻击处理 by DennisZz, Auguest 27th,2007.
					// 2 那个岗位的人是不是传入的studentnum
					if (post.getLogin() != null) {
						Login login = loginDAO
								.findById(post.getLogin().getId());
						String temp = login.getStudentnum();
						if (!temp.equals(studentnum)) {
							out.print("invalid");
							out.flush();
							return null;
						}
					}
					post.setLogin(null);
					postsDAO.update(post);

					// 设置apply表中的applystatus值为3，表示已经申请过然后被免除
					List applyList = applyDAO.findByStudentnum(studentnum);
					Iterator applyListIter = applyList.iterator();
					while (applyListIter.hasNext()) {
						Apply apply = (Apply) applyListIter.next();
						if (apply.getPoscode().equals(fullCode)) {
							apply.setApplystatus("3");
							applyDAO.update(apply);
						}
					}
				}
				prList = posrecordDAO.findByFullCodeStudentNum(fullCode,
						studentnum);
				if (!prList.isEmpty()) {
					iter = prList.iterator();
					while (iter.hasNext()) {
						posr = (Posrecord) iter.next();
						if (posr.getEndtime() == null) {
							posr.setEndtime(dateFormat);
							posrecordDAO.update(posr);
						}
					}
				}
			}
			out.print("true");
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// originally written by sillywolf; migrated by DennisZz, July 28th, 2007.
	// fixed by echo , Nov. 25,2007

	public ModelAndView showPositionDetail(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();
		String studentNum = (String) request.getSession().getAttribute(
				"studentnum");
		String posCode = (String) request.getParameter("posnum");

		// 恶意攻击处理 by DennisZz, Auguest 27th,2007.
		// 判断request中的posnum,如果是系别的岗位号，且不属于本人所在系，进行处理
		if ((!studentNum.equals("root")) && posCode.startsWith("4")) {
			List loginList = loginDAO.findByStudentnum(studentNum);
			if (loginList.size() != 0) {
				Login login = (Login) loginList.get(0);
				Userinfo ui = login.getUserinfo();
				if (Integer.parseInt(posCode.substring(1, 3)) != ui
						.getDepartment()) {
					out.write("invalid");
					out.flush();
					return null;
				}
			}
		}
		if (posCode.length() == 12) {
			List list = applyDAO.findByPoscode(posCode);
			Apply apply = null;
			boolean ifApplyed = false;
			if (list != null) {
				Iterator listIter = list.iterator();
				while (listIter.hasNext()) {
					apply = (Apply) listIter.next();
					if (apply.getStudentnum().equals(studentNum)) {
						// 如果applystuts == 3，表示以前担任过，但已经被免除了，可以继续申请
						// 如果applystuts == 2，表示以前申请过，但被拒绝了，可以继续申请
						if (!apply.getApplystatus().equals("3") && !apply.getApplystatus().equals("2")) {
							ifApplyed = true;
							break;
						}
					}
				}
			}
			List postList = postsDAO.findByFullcode(posCode);
			Posts post = (Posts) postList.get(0);
			Depts tempDept = post.getDepts();
			Depts dept = deptsDAO.findById(tempDept.getId());
			Organs tempOrgan = dept.getOrgans();
			Organs organ = organsDAO.findById(tempOrgan.getId());
			Category tempCate = organ.getCategory();
			Category cate = categoryDAO.findById(tempCate.getId());
			JSONObject jsonObject = JsonUtils.getJsonObjectForPositionDetail(
					ifApplyed, post, dept, organ, cate);
			out.print(jsonObject.toString());
			out.close();
			return null;
		}else if(posCode.length()==9)
		{
			/*
			 * fixed by echo  Nov. 25,2007
			 * */
			Depts dept = categoryDAO.getDeptByCatOrganDeptCode(posCode);
			Organs tempOrgan = dept.getOrgans();
			Organs organ = organsDAO.findById(tempOrgan.getId());
			Category tempCate = organ.getCategory();
			Category cate = categoryDAO.findById(tempCate.getId());
			
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("ifapplyed", false);
			jsonObject.put("poscode", posCode);
			jsonObject.put("category", cate.getName());// the categoty the position
			// belongs
			jsonObject.put("organs", organ.getName());// the organs
			jsonObject.put("depts", dept.getName());// the depts
			jsonObject.put("posts", "-");// the position name
			jsonObject.put("pos_Descrip", "-");// the position
			// description
			jsonObject.put("fullname", cate.getName()+"-"+organ.getName()+"-"+dept.getName());// the full name of the
			// position
			jsonObject.put("pos_require", "-");// the position
			// requirment
			/*
			 * the ability1 to ability6 which the position requires
			 */
			jsonObject.put("pos_ability1", "-");
			jsonObject.put("pos_ability2", "-");
			jsonObject.put("pos_ability3", "-");
			jsonObject.put("pos_ability4", "-");
			jsonObject.put("pos_ability5", "-");
			jsonObject.put("pos_ability6", "-");
			jsonObject.put("pos_cost", "-");// the postion burden
			jsonObject.put("pos_relate", "-");// the positions

			out.print(jsonObject.toString());
			out.close();
			return null;
		}else if(posCode.length()==6)
		{
			/*
			 * fixed by echo Nov. 25,2007
			 * */
			Organs organ = categoryDAO.getOrganByCatOrganCode(posCode);
			//List organlist = organsDAO.findByCode(posCode);
			//Organs organ = (Organs) organlist.get(0);
			Category tempCate = organ.getCategory();
			Category cate = categoryDAO.findById(tempCate.getId());	
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("ifapplyed", false);
			jsonObject.put("poscode", posCode);
			jsonObject.put("category", cate.getName());// the categoty the position
			// belongs
			jsonObject.put("organs", organ.getName());// the organs
			jsonObject.put("depts", "-");// the depts
			jsonObject.put("posts", "-");// the position name
			jsonObject.put("pos_Descrip", "-");// the position
			// description
			jsonObject.put("fullname", cate.getName()+"-"+organ.getName());// the full name of the
			// position
			jsonObject.put("pos_require", "-");// the position
			// requirment
			/*
			 * the ability1 to ability6 which the position requires
			 */
			jsonObject.put("pos_ability1", "-");
			jsonObject.put("pos_ability2", "-");
			jsonObject.put("pos_ability3", "-");
			jsonObject.put("pos_ability4", "-");
			jsonObject.put("pos_ability5", "-");
			jsonObject.put("pos_ability6", "-");
			jsonObject.put("pos_cost", "-");// the postion burden
			jsonObject.put("pos_relate", "-");// the positions

			out.print(jsonObject.toString());
			out.close();
			return null;
			
		}else if(posCode.length()==3)
		{
			List catelist = categoryDAO.findByCode(posCode);
			Category cate = (Category) catelist.get(0);
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("ifapplyed", false);
			jsonObject.put("poscode", posCode);
			jsonObject.put("category", cate.getName());// the categoty the position
			// belongs
			jsonObject.put("organs", "-");// the organs
			jsonObject.put("depts", "-");// the depts
			jsonObject.put("posts", "-");// the position name
			jsonObject.put("pos_Descrip", "-");// the position
			// description
			jsonObject.put("fullname", cate.getName());// the full name of the
			// position
			jsonObject.put("pos_require", "-");// the position
			// requirment
			/*
			 * the ability1 to ability6 which the position requires
			 */
			jsonObject.put("pos_ability1", "-");
			jsonObject.put("pos_ability2", "-");
			jsonObject.put("pos_ability3", "-");
			jsonObject.put("pos_ability4", "-");
			jsonObject.put("pos_ability5", "-");
			jsonObject.put("pos_ability6", "-");
			jsonObject.put("pos_cost", "-");// the postion burden
			jsonObject.put("pos_relate", "-");// the positions

			out.print(jsonObject.toString());
			out.close();
			return null;
		}
		out.write("invalid");
		out.flush();
		return null;
	}

	/**
	 * @author urong, 2007.08.30 获得担任的岗位,包括现在担任的和曾经担任的
	 */
	public ModelAndView getOccupyPosition(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// 在session取得自己的学号
		String studentNum = (String) request.getSession().getAttribute(
				"studentnum");

		// 使用学号在postrecord表中查找自己担任的所有岗位
		List posRecList = posrecordDAO.findByStunum(studentNum);

		// 对于现在担任的和过去担任的分别封装
		JSONArray occupyingPos = new JSONArray();
		JSONArray occupiedPos = new JSONArray();
		Iterator posRecListIter = posRecList.iterator();
		while (posRecListIter.hasNext()) {
			Posrecord posRec = (Posrecord) posRecListIter.next();

			// 记录岗位信息
			JSONObject posInfo = new JSONObject();
			posInfo.put("posname", posRec.getPosname());
			posInfo.put("starttime", posRec.getStarttime());
			posInfo.put("endtime", posRec.getEndtime());
			posInfo.put("poscode", posRec.getPoscode());

			// 通过结束时间来判断是现在担任的岗位还是以前担任的岗位
			String endTime = posRec.getEndtime();
			// 如果endTime是空,则说明是现在担任的岗位
			if (endTime == null) {
				occupyingPos.add(posInfo);
			}
			// 否则为以前担任的岗位
			else {
				occupiedPos.add(posInfo);
			}
		}

		// out print相应的担任的岗位信息
		JSONObject occupyPos = new JSONObject();
		occupyPos.put("occupyingPos", occupyingPos);// 现在担任的岗位
		occupyPos.put("occupiedPos", occupiedPos);// 过去担任的岗位
		PrintWriter out = response.getWriter();
		out.print(occupyPos);
		out.flush();
		out.close();
		return null;
	}

	/**
	 *@author sillywolf,Oct,20,2007
	 * 
	 */
	public ModelAndView getApplyHistory(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		//在session取得自己的学号
		String studentnum=(String)request.getSession().getAttribute("studentnum");
		
		//查找自己的申请记录
		List applyList=applyDAO.findByStudentnum(studentnum);
		Iterator applyIter=applyList.iterator();
		
		JSONArray applyedPos = new JSONArray();
		
		while (applyIter.hasNext())
		{
			Apply apply=(Apply)applyIter.next();
			JSONObject applyInfo = new JSONObject();
			Posts pos=(Posts)postsDAO.findByFullcode(apply.getPoscode()).get(0);
			applyInfo.put("applyname", pos.getFullname());
			applyInfo.put("applyreason", apply.getApplyreason());
			applyInfo.put("applytime", apply.getApplytime());
			int status=Integer.parseInt(apply.getApplystatus());
			switch (status)
			{
			case 0:
				applyInfo.put("applystatus", "待审批");
				break;
			case 1:
				applyInfo.put("applystatus", "已通过");
				break;
			case 2:
				applyInfo.put("applystatus", "已否决");
				break;
				default :
					throw new IOException(" status error");
					
			}
			applyedPos.add(applyInfo);
		}
		PrintWriter out = response.getWriter();
		out.print(applyedPos);
		out.flush();
		out.close();
		return null;
	}
	// by echo@net9
	// 2007/09/10
	public ModelAndView getPosExp(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");
		List posExpList = posrecordDAO.findByStunum(studentnum);
		Iterator posExpListIter = posExpList.iterator();
		JSONObject bigdata = new JSONObject();

		JSONArray jsonArrayCurrent = new JSONArray();
		JSONArray jsonArrayOld = new JSONArray();
		while (posExpListIter.hasNext()) {
			Posrecord posrecord = (Posrecord) posExpListIter.next();
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("posname", posrecord.getPosname());
			jsonObject.put("a1", ((posrecord.getAbility1() == null) ? "-"
					: posrecord.getAbility1()));
			jsonObject.put("a2", ((posrecord.getAbility2() == null) ? "-"
					: posrecord.getAbility2()));
			jsonObject.put("a3", ((posrecord.getAbility3() == null) ? "-"
					: posrecord.getAbility3()));
			jsonObject.put("a4", ((posrecord.getAbility4() == null) ? "-"
					: posrecord.getAbility4()));
			jsonObject.put("a5", ((posrecord.getAbility5() == null) ? "-"
					: posrecord.getAbility5()));
			jsonObject.put("a6", ((posrecord.getAbility6() == null) ? "-"
					: posrecord.getAbility6()));
			jsonObject.put("remark", ((posrecord.getRemark() == null) ? "-"
					: posrecord.getRemark()));
			jsonObject.put("starttime",
					((posrecord.getStarttime() == null) ? "-" : posrecord
							.getStarttime()));
			String temp = posrecord.getEndtime();
			jsonObject.put("endtime", ((posrecord.getEndtime() == null) ? "-"
					: posrecord.getEndtime()));
			if (temp == null)
				jsonArrayCurrent.add(jsonObject);
			else
				jsonArrayOld.add(jsonObject);
		}

		PrintWriter out = response.getWriter();
		bigdata.put("Current", jsonArrayCurrent);
		bigdata.put("Old", jsonArrayOld);
		out.print(bigdata);
		out.flush();
		out.close();

		return null;
	}
}
