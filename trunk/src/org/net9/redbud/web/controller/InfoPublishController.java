package org.net9.redbud.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.net9.redbud.storage.hibernate.category.Category;
import org.net9.redbud.storage.hibernate.category.CategoryDAO;
import org.net9.redbud.storage.hibernate.depts.Depts;
import org.net9.redbud.storage.hibernate.internalinfo.Internalinfo;
import org.net9.redbud.storage.hibernate.internalinfo.InternalinfoDAO;
import org.net9.redbud.storage.hibernate.internalinfologin.Internalinfologin;
import org.net9.redbud.storage.hibernate.internalinfologin.InternalinfologinDAO;
import org.net9.redbud.storage.hibernate.internaltask.Internaltask;
import org.net9.redbud.storage.hibernate.internaltask.InternaltaskDAO;
import org.net9.redbud.storage.hibernate.internaltasklogin.Internaltasklogin;
import org.net9.redbud.storage.hibernate.internaltasklogin.InternaltaskloginDAO;
import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;
import org.net9.redbud.storage.hibernate.organs.Organs;
import org.net9.redbud.storage.hibernate.posts.Posts;
import org.net9.redbud.storage.hibernate.posts.PostsDAO;
import org.net9.redbud.storage.hibernate.publicinfo.Publicinfo;
import org.net9.redbud.storage.hibernate.publicinfo.PublicinfoDAO;
import org.net9.redbud.storage.hibernate.publicinfologin.Publicinfologin;
import org.net9.redbud.storage.hibernate.publicinfologin.PublicinfologinDAO;
import org.net9.redbud.storage.hibernate.userinfo.Userinfo;
import org.net9.redbud.util.DataCenter;
import org.net9.redbud.util.DateUtils;
import org.net9.redbud.util.PermissionValidate;
import org.net9.redbud.util.ComparatorUtils.CatComparator;
import org.net9.redbud.web.util.JsonUtils;
import org.springframework.web.servlet.ModelAndView;

public class InfoPublishController extends RedbudBaseController {

	private PublicinfoDAO publicinfoDAO;

	private PublicinfologinDAO publicinfologinDAO;

	private PostsDAO postsDAO;

	private LoginDAO loginDAO;

	private CategoryDAO categoryDAO;

	private InternalinfoDAO internalinfoDAO;

	private InternalinfologinDAO internalinfologinDAO;

	private InternaltaskDAO internaltaskDAO;

	private InternaltaskloginDAO internaltaskloginDAO;

	private DataCenter dataCenter;

	public DataCenter getDataCenter() {
		return dataCenter;
	}

	public void setDataCenter(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}

	public void setInternaltaskDAO(InternaltaskDAO internaltaskDAO) {
		this.internaltaskDAO = internaltaskDAO;
	}

	public void setInternaltaskloginDAO(
			InternaltaskloginDAO internaltaskloginDAO) {
		this.internaltaskloginDAO = internaltaskloginDAO;
	}

	public void setInternalinfoDAO(InternalinfoDAO internalinfoDAO) {
		this.internalinfoDAO = internalinfoDAO;
	}

	public void setInternalinfologinDAO(
			InternalinfologinDAO internalinfologinDAO) {
		this.internalinfologinDAO = internalinfologinDAO;
	}

	public CategoryDAO getCategoryDAO() {
		return categoryDAO;
	}

	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	public PostsDAO getPostsDAO() {
		return postsDAO;
	}

	public void setPostsDAO(PostsDAO postsDAO) {
		this.postsDAO = postsDAO;
	}

	public PublicinfoDAO getPublicinfoDAO() {
		return publicinfoDAO;
	}

	public void setPublicinfoDAO(PublicinfoDAO publicinfoDAO) {
		this.publicinfoDAO = publicinfoDAO;
	}

	public PublicinfologinDAO getPublicinfologinDAO() {
		return publicinfologinDAO;
	}

	public void setPublicinfologinDAO(PublicinfologinDAO publicinfologinDAO) {
		this.publicinfologinDAO = publicinfologinDAO;
	}

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}

	/**
	 * @author DennisZz@9# August 6th, 2007. 返回在岗人员树的Json结构
	 */
	// TODO: 这里需要修改
	// 目前没有接受任何参数,返回了一个多个高权限可以控制的多个岗位的树
	// 应当改成,给如一个postcode,在判断了这个postcode是不是当前用户所属且是否有发布权限之后,返回一个
	// 当前postcode对应的树
	public ModelAndView showStaffTree(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JSONArray staffTrees = new JSONArray();
		ArrayList<String> v = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		Iterator it = v.iterator();
		while (it.hasNext()) {
			String temp = (String) it.next();
			if (PermissionValidate.isRoot(temp)) {
				List list = categoryDAO.findAllCat();
				Object[] tempCatArray = list.toArray();
				Arrays.sort(tempCatArray, new CatComparator());
				int flag = 0;
				for (int i = 0; i < tempCatArray.length; i++) {
					try {
						Category cat = (Category) tempCatArray[i];
						String catCode = cat.getCode().toString();
						if(!catCode.equals("000")){
							JSONObject catNode = new JSONObject();
							dataCenter.getCatJsonTree(catNode, catCode, 3);
							staffTrees.add(catNode);
						}
						flag = 1;

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				String noCatInitString = "校级岗位结构尚未进行初始化";
				if (flag == 0) {
					JSONObject noCat = new JSONObject();
					noCat.put("title", noCatInitString);
					staffTrees.add(noCat);
				}
			} else if (PermissionValidate.isCategoryBoss(temp)) {
				String tempCatCode = temp.substring(0, 3);
				JSONObject catNode = new JSONObject();
				dataCenter.getCatJsonTree(catNode, tempCatCode, 3);
				staffTrees.add(catNode);

			} else if (PermissionValidate.isOrganizationBoss(temp)) {
				String tempCatOrganCode = temp.substring(0, 6);
				Organs tempOrgan = new Organs();
				tempOrgan = categoryDAO
						.getOrganByCatOrganCode(tempCatOrganCode);
				JSONObject organNode = new JSONObject();
				dataCenter.getOrganJsonTree(organNode, tempOrgan, 3);
				staffTrees.add(organNode);
			} else if (PermissionValidate.isDepartmentBoss(temp)) {
				String tempCatOrganDeptCode = temp.substring(0, 9);
				Depts tempDept = new Depts();
				tempDept = categoryDAO
						.getOrganByCatOrganDeptCode(tempCatOrganDeptCode);
				JSONObject deptNode = new JSONObject();
				dataCenter.getDeptJsonTree(deptNode, tempDept, 3);
				staffTrees.add(deptNode);
			}
		}

		PrintWriter out = response.getWriter();
		out.write(staffTrees.toString());
		out.flush();
		out.close();
		return null;
	}

	/**
	 * @author urong@9#, 2007.08.04 校级信息发布的具体动作，只有Root和CategoryBoss有权力发布
	 */
	public ModelAndView postPublicInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		try {
			// get infoTitle & infoContent & posistion
			String infoTitle = request.getParameter("title");
			String infoContent = request.getParameter("content");
			String posCode = request.getParameter("postcode_selected");

			// permission validate
			if (!PermissionValidate.isCategoryBoss(posCode)
					&& !PermissionValidate.isRoot(posCode)) {
				return null;
			}

			// save information to Publicinfo Table
			Posts pos = (Posts) (postsDAO.findByFullcode(posCode).get(0));
			Login login = pos.getLogin();
			int loginId = login.getId();

			String categoryCode = pos.getCategoryCode();
			Category category = (Category) (categoryDAO
					.findByCode(categoryCode).get(0));
			String categoryName = category.getName();

			long publishTime = System.currentTimeMillis();
			long approveTime = 0;// not approved
			int approveStatus = 0;// not approved

			Publicinfo publicinfo = new Publicinfo();
			publicinfo.setApprovestatus(approveStatus);
			publicinfo.setApprovetime(approveTime);
			publicinfo.setInfocontent(infoContent);
			publicinfo.setInfotitle(infoTitle);
			publicinfo.setOwnercatename(categoryName);
			publicinfo.setOwnerid(loginId);
			publicinfo.setPublishtime(publishTime);
			publicinfoDAO.save(publicinfo);

			// out print running status
			out.print("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.print("false");
		}

		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author urong@9#, 2007.08.04 获得已经申请的校级信息，目的是用于审批。只有Root有这个权力
	 */
	public ModelAndView getAppliedPublicInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		// permission validate, root only
		ArrayList<String> posList = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.hasRootPermission(posList)) {
			out.print("NULL");
			out.flush();
			out.close();
			return null;
		}

		// find in publicinfo table via approveStatus = 0(not approved)
		int approveStatus = 0;// not approved
		List publicinfoList = publicinfoDAO.findByApprovestatus(approveStatus);

		// out print list get from publicinfo table in form of JSONObject
		Iterator publicinfoListIter = publicinfoList.iterator();
		JSONArray jsonArray = new JSONArray();
		while (publicinfoListIter.hasNext()) {
			Publicinfo publicinfo = (Publicinfo) publicinfoListIter.next();

			JSONObject jsonObject = new JSONObject();
			jsonObject.put("id", publicinfo.getId());
			jsonObject.put("infotitle", publicinfo.getInfotitle());
			jsonObject.put("ownercatname", publicinfo.getOwnercatename());
			jsonObject.put("publishtime", DateUtils.getFormatTime(publicinfo
					.getPublishtime()));

			jsonArray.add(jsonObject);
		}

		out.print(jsonArray);
		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author urong@9#, 2007.08.04 获得申请的校级信息的详细信息。只有Root有这个权力
	 */
	public ModelAndView showAppliedPublicInfoDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		// permission validate, root only
		ArrayList<String> posList = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.hasRootPermission(posList)) {
			out.print("NULL");
			out.flush();
			out.close();
			return null;
		}

		// get publicinfo id
		String publicinfoId = request.getParameter("id");

		// get user information
		Publicinfo publicinfo = publicinfoDAO.findById(Integer
				.parseInt(publicinfoId));
		int ownerId = publicinfo.getOwnerid();
		Login login = loginDAO.findById(ownerId);
		Userinfo userinfo = login.getUserinfo();

		// format information
		JSONObject jsonObject = new JSONObject();
		jsonObject
				.put("userInfo", JsonUtils.getJsonObjectForUserinfo(userinfo));
		jsonObject.put("infotitle", publicinfo.getInfotitle());
		jsonObject.put("infocontent", publicinfo.getInfocontent());
		jsonObject.put("ownercatname", publicinfo.getOwnercatename());
		jsonObject.put("publishtime", DateUtils.getFormatTime(publicinfo
				.getPublishtime()));

		out.print(jsonObject);
		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author urong@9#, 2007.08.04 审批校级信息的具体动作。只有Root有这个权力
	 */
	public ModelAndView approvePublicInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		// permission validate, root only
		ArrayList<String> posList = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.hasRootPermission(posList)) {
			out.print("false");
			out.flush();
			out.close();
			return null;
		}

		// get approve status & publicinfo id
		String approveStatus = request.getParameter("status");
		String publicinfoId = request.getParameter("id");
		// String approveStatus = "false";
		// String publicinfoId = "3";

		// if not approved & agreed then send information to all
		try {
			Publicinfo publicinfo = publicinfoDAO.findById(Integer
					.parseInt(publicinfoId));
			int approveStatusInTable = publicinfo.getApprovestatus();
			if (approveStatusInTable == 0 && approveStatus.equals("true")) {
				// set publicinfo table
				publicinfo.setApprovestatus(1); // agreed
				publicinfo.setApprovetime(System.currentTimeMillis());
				publicinfoDAO.update(publicinfo);

				// set publicinfologin table, send information to all
				List loginList = loginDAO.findAllLogin();
				Iterator loginListIter = loginList.iterator();
				while (loginListIter.hasNext()) {
					Login login = (Login) loginListIter.next();
					Publicinfologin publicinfologin = new Publicinfologin();
					publicinfologin.setLogin(login);
					publicinfologin.setPublicinfo(publicinfo);
					publicinfologin.setStatus("false");
					publicinfologinDAO.save(publicinfologin);
				}
			}
			// else if not approved & not agreed set publicinfo table only, do
			// not sent information
			else if (approveStatusInTable == 0 && approveStatus.equals("false")) {
				publicinfo.setApprovestatus(2);// not agreed
				publicinfo.setApprovetime(System.currentTimeMillis());
				publicinfoDAO.update(publicinfo);
			} else {
				// do nothing
			}
			
			//放行的反馈信息
			int ownerID = publicinfo.getOwnerid();
			String infoTitle = "信息放行：" + publicinfo.getInfotitle();
			
			String infoContent = "";
			if(approveStatus.equals("fasle")){
				infoContent = "拒绝发送该消息给全校同学";
			}
			else if(approveStatus.equals("true")){
				infoContent = "同意发送该消息给全校同学";
			}
			else{
				infoContent = "未知审批结果";
			}
			
			this.sendMsgToPos("000000000000", ownerID, infoTitle, infoContent);
			
			out.print("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.print("false");
		}

		out.flush();
		out.close();
		return null;
	}

	/**
	 * @author urong@9#, 2007.08.04 判断一个岗位号列表的中岗位是否有Root权限
	 * @param posList
	 *            岗位号的列表
	 * @return 如果posList这个列表中有一个岗位有Root权限则返回true，否则返回false
	 */
	private boolean hasRootPermission(ArrayList<String> posList) {
		Iterator posListIter = posList.iterator();
		while (posListIter.hasNext()) {
			String posCode = (String) posListIter.next();
			if (PermissionValidate.isRoot(posCode)) {
				return true;
			}
		}

		return false;
	}

	// echo@9#
	// post internal infomation
	public ModelAndView postInternalInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");
		String posArray = request.getParameter("lists");
		String postcode_selected = request.getParameter("postcode_selected");
		String infoTitle = request.getParameter("title");

		String infoContent = request.getParameter("content");
		String[] poses = posArray.split(",");

		Internalinfo p = new Internalinfo();
		p.setInfocontent(infoContent);
		p.setInfotitle(infoTitle);

		Posts posts = (Posts) postsDAO.findByFullcode(postcode_selected).get(0);

		if(PermissionValidate.isAtLeastDepartmentBoss(posts.getFullcode())==false)
		{
			response.getWriter().print("false");
			return null;
		}
		else
		{
			p.setOwnerpos(posts.getFullname());
			Login temp2 = posts.getLogin();
			Login login = loginDAO.findById(temp2.getId());

			if (login.getStudentnum().equals(studentnum)) {
				p.setOwnerid(login.getId());
				p.setOwnername(loginDAO.findById(login.getId()).getUserinfo()
						.getName());
				p.setPublishtime(System.currentTimeMillis());
				internalinfoDAO.save(p);
				for (int i = 0; i < poses.length; i++) {
					Internalinfologin pd = new Internalinfologin();
					Posts temp = (Posts) postsDAO.findByFullcode(poses[i]).get(0);
					Login l = temp.getLogin();
					pd.setLogin(l);
					pd.setInternalinfo(p);
					pd.setStatus("false");
					internalinfologinDAO.save(pd);
				}
				response.getWriter().print("true");
			} else
				response.getWriter().print("false");
			return null;
		}
	}

	// echo@9#
	// post interanl tasks
	// tested Nov.25 2007 
	public ModelAndView postInternalTask(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String posArray = request.getParameter("lists");
		String postcode_selected = request.getParameter("postcode_selected");
		String taskTitle = request.getParameter("title");
		String taskContent = request.getParameter("content");
		String[] poses = posArray.split(",");
		Internaltask internaltask = new Internaltask();
		internaltask.setTaskcontent(taskContent);
		internaltask.setTasktitle(taskTitle);
		Posts posts = (Posts) postsDAO.findByFullcode(postcode_selected).get(0);
		if(PermissionValidate.isAtLeastDepartmentBoss(posts.getFullcode())==false)
		{
			response.getWriter().print("false");
			return null;
		}
		else{
			String temp11 = posts.getFullname();
			internaltask.setOwnerposname(temp11);
			Login login = posts.getLogin();
			internaltask.setPublishtime(System.currentTimeMillis());
			internaltask.setOwnerid(login.getId());
			internaltask.setOwenername(loginDAO.findById(login.getId())
					.getUserinfo().getName());
			internaltask.setStatus("new");
			internaltaskDAO.save(internaltask);
			for (int i = 0; i < poses.length; i++) {
				Internaltasklogin internaltasklogin = new Internaltasklogin();
				Posts temp = (Posts) postsDAO.findByFullcode(poses[i]).get(0);
				Login l = temp.getLogin();
				internaltasklogin.setLogin(l);
				internaltasklogin.setInternaltask(internaltask);
				internaltasklogin.setReadstatus("false");
				internaltasklogin.setEchostatus("false");
				internaltaskloginDAO.save(internaltasklogin);
			}
			response.getWriter().print("true");
			return null;
		}
	}

	// echo@9#
	// get feed back one's all tasks
	@SuppressWarnings("unchecked")
	public ModelAndView getTasksFeedbackList(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JSONArray newarray = new JSONArray();
		JSONArray oldarray = new JSONArray();
		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");
		Login login = (Login) loginDAO.findByStudentnum(studentnum).get(0);

		Iterator It2 = (internaltaskDAO.findByOwnerid(login.getId()))
				.iterator();
		while (It2.hasNext()) {
			Internaltask internaltask = (Internaltask) It2.next();
			JSONObject one = new JSONObject();
			one.put("id", internaltask.getId());
			one.put("title", internaltask.getTasktitle());
			one.put("time", DateUtils.getFormatTime(internaltask
					.getPublishtime()));
			if (internaltask.getStatus().equals("new")) {
				newarray.add(one);
			}
			if (internaltask.getStatus().equals("old")) {
				oldarray.add(one);
			}
		}

		response.getWriter().print(
				"[" + newarray.toString() + "," + oldarray.toString() + "]");
		response.getWriter().flush();
		response.getWriter().close();
		return null;
	}

	// echo@9#
	// TODO: deleteTaskReplies
//	public ModelAndView deleteTaskReplies(HttpServletRequest request,
//			HttpServletResponse response) throws ServletException, IOException {
//		String idlist = request.getParameter("type");
//		//String[] IDs = idlist.split(",");
//
//		return null;
//	}

	// echo@9#
	// move the finished task to Old type;
	public ModelAndView moveNewTaskToOld(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		Internaltask internaltask = internaltaskDAO.findById(id);
		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");
		if (loginDAO.findById(internaltask.getOwnerid()).getStudentnum()
				.equals(studentnum)) {
			internaltask.setStatus("old");
			internaltaskDAO.update(internaltask);
		} else
			return null;

		response.getWriter().print("true");
		response.getWriter().flush();
		response.getWriter().close();

		return null;
	}

	// echo@9#
	// showTaskFeedbackDetail : who have feed back ~and who not
	public ModelAndView showTaskFeedbackDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));

		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");

		Internaltask internaltask = internaltaskDAO.findById(id);

		if (loginDAO.findById(internaltask.getOwnerid()).getStudentnum()
				.equals(studentnum)) {
			JSONObject detail = new JSONObject();
			detail.put("id", internaltask.getId());
			detail.put("title", internaltask.getTasktitle());
			detail.put("time", DateUtils.getFormatTime(internaltask
					.getPublishtime()));

			JSONArray child = new JSONArray();

			Set<Internaltasklogin> internaltasklogins = internaltask
					.getInternaltasklogins();
			Iterator It = internaltasklogins.iterator();
			while (It.hasNext()) {
				Internaltasklogin temp = (Internaltasklogin) It.next();
				JSONObject one = new JSONObject();
				one.put("replied", temp.getEchostatus());
				Login login = loginDAO.findById(temp.getLogin().getId());
				one.put("stunum", login.getStudentnum());
				Userinfo userinfo = login.getUserinfo();
				one.put("name", userinfo.getName());
				child.add(one);
			}
			detail.put("children", child);
			response.getWriter().print(detail.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		return null;
	}

	// echo@9#
	// delete all kinds of infomation, depends on the type
	public ModelAndView delete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int type = Integer.parseInt(request.getParameter("type"));
		String idlist = request.getParameter("ids");
		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");
		String[] IDs = idlist.split(",");
		if (IDs.length <= 0)
			return null;
		for (int i = 0; i < IDs.length; i++) {
			if (type == 0) {
				Internalinfologin temp = internalinfologinDAO.findById(Integer
						.parseInt(IDs[i]));
				// authorization control
				if (loginDAO.findById(temp.getLogin().getId()).getStudentnum()
						.equals(studentnum)) {
					internalinfologinDAO.delete(temp);
				} else
					return null;

			}
			if (type == 1) {
				Internaltasklogin temp = internaltaskloginDAO.findById(Integer
						.parseInt(IDs[i]));
				// authorization control
				if (loginDAO.findById(temp.getLogin().getId()).getStudentnum()
						.equals(studentnum)) {
					internaltaskloginDAO.delete(temp);
				} else
					return null;
			}
			if (type == 2) {
				Publicinfologin temp = publicinfologinDAO.findById(Integer
						.parseInt(IDs[i]));
				// authorization control
				if (loginDAO.findById(temp.getLogin().getId()).getStudentnum()
						.equals(studentnum)) {
					publicinfologinDAO.delete(temp);
				} else
					return null;
			}
		}
		PrintWriter out = response.getWriter();
		out.write("true");
		out.close();
		return null;
	}

	// echo @9#
	// return the basic info of infomations
	// for the first page of logins.
	@SuppressWarnings("unchecked")
	public ModelAndView basicInfoStatus(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");
		Login login = (Login) loginDAO.findByStudentnum(studentnum).get(0);
		Set<Internalinfologin> internalinfologins = login
				.getInternalinfologins();
		Set<Internaltasklogin> internaltasklogins = login
				.getInternaltasklogins();

		Set<Publicinfologin> publicinfologins = login.getPublicinfologins();

		JSONObject json = new JSONObject();

		int inboxItemCount = internalinfologins.size();
		int inboxNewItemCount = 0;
		Iterator It = internalinfologins.iterator();
		while (It.hasNext()) {
			Internalinfologin temp = (Internalinfologin) It.next();
			if (temp.getStatus().equals("false"))
				inboxNewItemCount++;
		}

		int taskItemCount = internaltasklogins.size();
		int taskNewItemCount = 0;
		int taskNotRepliedItemCount = 0;

		Iterator It2 = internaltasklogins.iterator();
		while (It2.hasNext()) {
			Internaltasklogin temp2 = (Internaltasklogin) It2.next();
			if (temp2.getReadstatus().equals("false"))
				taskNewItemCount++;
			if (temp2.getEchostatus().equals("false"))
				taskNotRepliedItemCount++;
		}

		int bulletinCount = publicinfologins.size();
		int bulletinNewCount = 0;

		Iterator It3 = publicinfologins.iterator();
		while (It3.hasNext()) {
			Publicinfologin temp2 = (Publicinfologin) It3.next();
			if (temp2.getStatus().equals("false"))
				bulletinNewCount++;
		}

		json.put("inboxItemCount", inboxItemCount);
		json.put("inboxNewItemCount", inboxNewItemCount);
		json.put("taskItemCount", taskItemCount);
		json.put("taskNewItemCount", taskNewItemCount);
		json.put("taskNotRepliedItemCount", taskNotRepliedItemCount);
		json.put("bulletinCount", bulletinCount);
		json.put("bulletinNewCount", bulletinNewCount);

		response.getWriter().print(json.toString());
		response.getWriter().flush();
		response.getWriter().close();
		return null;
	}

	// echo @ 9#
	// set the task to replied status(also called feedback)
	public ModelAndView setReply(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id"));
		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");
		Internaltasklogin temp = internaltaskloginDAO.findById(id);

		// authorization
		if (loginDAO.findById(temp.getLogin().getId()).getStudentnum().equals(
				studentnum)) {
			temp.setEchostatus("true");
			internaltaskloginDAO.update(temp);
		} else
			return null;
		response.getWriter().print("true");
		response.getWriter().flush();
		response.getWriter().close();
		return null;
	}

	// echo @9#
	// show the single page of infomation

	// 传入type\low\high
	// type : 0表示inbox 1表示task
	// low,high为个数的下标，非id

	@SuppressWarnings("unchecked")
	public ModelAndView getPage(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		JSONArray jsonarray = new JSONArray();

		int type = Integer.parseInt(request.getParameter("type"));
		int low = Integer.parseInt(request.getParameter("low"));
		int high = Integer.parseInt(request.getParameter("high"));

		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");
		Login login = (Login) loginDAO.findByStudentnum(studentnum).get(0);

		int count = 0;
		if (type == 0) {
			Set<Internalinfologin> internalinfologins = login.getInternalinfologins();
			//internalinfologins.
			Iterator It = internalinfologins.iterator();
			while (It.hasNext()) {
				Internalinfologin temp = (Internalinfologin) It.next();
				if (count >= low && count < high) {
					Internalinfo ooo = temp.getInternalinfo();
					Internalinfo oneInternal = internalinfoDAO.findById(ooo
							.getId());
					JSONObject one = new JSONObject();
					//System.out.println(temp.getId());
					one.put("id", temp.getId());
					one.put("author", oneInternal.getOwnername());
					one.put("title", oneInternal.getInfotitle());
					one.put("time", DateUtils.getFormatTime(oneInternal
							.getPublishtime()));
					one.put("readstatus", temp.getStatus());
					jsonarray.add(one);
				}
				if (count > high)
					break;
				count++;
			}
		}

		if (type == 1) {
			Set<Internaltasklogin> internaltasklogins = login.getInternaltasklogins();
			Iterator It = internaltasklogins.iterator();
			while (It.hasNext()) {
				Internaltasklogin temp = (Internaltasklogin) It.next();
				if (count >= low && count < high) {
					Internaltask ooo = temp.getInternaltask();
					Internaltask oneInternal = internaltaskDAO.findById(ooo
							.getId());
					JSONObject one = new JSONObject();
					one.put("id", temp.getId());
					one.put("author", oneInternal.getOwenername());
					one.put("title", oneInternal.getTasktitle());
					one.put("time", DateUtils.getFormatTime(oneInternal
							.getPublishtime()));
					one.put("readstatus", temp.getReadstatus());
					one.put("replystatus", temp.getEchostatus());
					jsonarray.add(one);
				}
				if (count > high)
					break;
				count++;
			}
		}
		if (type == 2) {
			Set<Publicinfologin> publicinfologins = login.getPublicinfologins();
			Iterator It = publicinfologins.iterator();
			while (It.hasNext()) {
				Publicinfologin temp = (Publicinfologin) It.next();
				if (count >= low && count < high) {
					Publicinfo ooo = temp.getPublicinfo();
					Publicinfo oneInternal = publicinfoDAO
							.findById(ooo.getId());
					JSONObject one = new JSONObject();
					one.put("id", temp.getId());
					one.put("author", oneInternal.getOwnercatename());
					one.put("title", oneInternal.getInfotitle());
					one.put("time", DateUtils.getFormatTime(oneInternal
							.getPublishtime()));
					one.put("readstatus", temp.getStatus());
					jsonarray.add(one);
				}
				if (count > high)
					break;
				count++;
			}
		}

		response.getWriter().print(jsonarray.toString());
		response.getWriter().flush();
		response.getWriter().close();
		return null;

	}

	// echo @ 9#
	// show detail of on infomation;

	@SuppressWarnings("unchecked")
	public ModelAndView showDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		int type = Integer.parseInt(request.getParameter("type"));
		int id = Integer.parseInt(request.getParameter("id"));
		JSONObject json = new JSONObject();
		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");
		if (type == 0) {
			Internalinfologin interinfo = internalinfologinDAO.findById(id);
			if (loginDAO.findById(interinfo.getLogin().getId()).getStudentnum()
					.equals(studentnum)) {
				Internalinfo temp = interinfo.getInternalinfo();
				Internalinfo internalInfo = internalinfoDAO.findById(temp
						.getId());
				json.put("id", id);
				json.put("author", internalInfo.getOwnername()+"("+internalInfo.getOwnerpos()+")");
				json.put("title", internalInfo.getInfotitle());
				json.put("content", internalInfo.getInfocontent());
				json.put("time", DateUtils.getFormatTime(internalInfo
						.getPublishtime()));
				json.put("readstatus", true);
				interinfo.setStatus("true");
				internalinfologinDAO.update(interinfo);
			} else
				return null;
		}
		if (type == 1) {
			Internaltasklogin internalt = internaltaskloginDAO.findById(id);
			if (loginDAO.findById(internalt.getLogin().getId()).getStudentnum()
					.equals(studentnum)) {
				Internaltask temp = internalt.getInternaltask();
				Internaltask internalTask = internaltaskDAO.findById(temp
						.getId());
				json.put("author", internalTask.getOwenername()+"("+internalTask.getOwnerposname()+")");
				json.put("title", internalTask.getTasktitle());
				json.put("content", internalTask.getTaskcontent());
				json.put("time", DateUtils.getFormatTime(internalTask
						.getPublishtime()));
				json.put("id", id);
				json.put("readstatus", true);
				json.put("replystatus ", internaltaskloginDAO.findById(id)
						.getEchostatus());
				internalt.setReadstatus("true");
				internaltaskloginDAO.update(internalt);
			} else
				return null;
		}

		if (type == 2) {
			Publicinfologin internalt = publicinfologinDAO.findById(id);
			if (loginDAO.findById(internalt.getLogin().getId()).getStudentnum()
					.equals(studentnum)) {

				Publicinfo temp = internalt.getPublicinfo();
				Publicinfo publicinfo = publicinfoDAO.findById(temp.getId());
				json.put("author", publicinfo.getOwnercatename());
				json.put("title", publicinfo.getInfotitle());
				json.put("content", publicinfo.getInfocontent());
				json.put("time", DateUtils.getFormatTime(publicinfo
						.getPublishtime()));
				json.put("id", id);
				json.put("readstatus", true);
				json.put("replystatus ", publicinfologinDAO.findById(id)
						.getStatus());
				internalt.setStatus("true");
				publicinfologinDAO.update(internalt);
			} else
				return null;
		}
		response.getWriter().print(json.toString());
		response.getWriter().flush();
		response.getWriter().close();
		return null;
	}
	
	/**
	 * 给某个pos发送一条msg
	 * urong@9#, 2007.10.20
	 */
	public void sendMsgToPos(String sendPosCode, int receiverLoginID, String infoTitle, String infoContent){
		//设置internalInfo表
		Internalinfo interInfo = new Internalinfo();
		
		interInfo.setInfotitle(infoTitle);
		interInfo.setInfocontent(infoContent);
		
		Posts posSend = (Posts)postsDAO.findByFullcode(sendPosCode).get(0);
		interInfo.setOwnerpos(posSend.getFullname());
		
		Login loginTmp = posSend.getLogin();
		Login loginSend = loginDAO.findById(loginTmp.getId());
		interInfo.setOwnerid(loginSend.getId());
		interInfo.setOwnername(loginSend.getUserinfo().getName());
		
		interInfo.setPublishtime(System.currentTimeMillis());
		internalinfoDAO.save(interInfo);
		
		//设置internalinfologin表
		Internalinfologin interInfoLogin = new Internalinfologin();
		
		Login loginRece = loginDAO.findById(receiverLoginID);
		interInfoLogin.setLogin(loginRece);
		interInfoLogin.setInternalinfo(interInfo);
		interInfoLogin.setStatus("false");
		internalinfologinDAO.save(interInfoLogin);
	}
}
