package org.net9.redbud.web.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.net9.redbud.storage.hibernate.posrecord.Posrecord;
import org.net9.redbud.storage.hibernate.posrecord.PosrecordDAO;
import org.net9.redbud.storage.hibernate.posts.Posts;
import org.net9.redbud.storage.hibernate.posts.PostsDAO;
import org.net9.redbud.storage.hibernate.userinfo.Userinfo;
import org.net9.redbud.util.DepartmentUtils;
import org.net9.redbud.util.PermissionValidate;
import org.springframework.web.servlet.ModelAndView;

public class MiscController extends RedbudBaseController {

	private PostsDAO postsDAO;

	private PosrecordDAO posrecordDAO;

	public void setPosrecordDAO(PosrecordDAO posrecordDAO) {
		this.posrecordDAO = posrecordDAO;
	}

	public void setPostsDAO(PostsDAO postsDAO) {
		this.postsDAO = postsDAO;
	}

	public ModelAndView getPostsCode(HttpServletRequest request,
			HttpServletResponse response) throws IOException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		String methodName = request.getParameter("permissionMethodName");
		Method[] methods = PermissionValidate.class.getMethods();
		int methodIndex = 0;
		for (methodIndex = 0; methodIndex < methods.length; methodIndex++) {
			if (methodName.equals(methods[methodIndex].getName())) {
				break;
			}
		}
		if (methodIndex >= methods.length) {
			return null;
		}

		ArrayList<String> posts = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		JSONArray JSONposts = new JSONArray();
		for (int i = 0; i < posts.size(); i++) {
			JSONArray onePost = new JSONArray();
			Posts pos = (Posts) (postsDAO.findByFullcode(posts.get(i)).get(0));

			Object hasPermission = methods[methodIndex].invoke(null, pos
					.getFullcode());
			if (hasPermission.toString().equals("false")) {// has no permission
				continue;
			}

			onePost.add(pos.getFullname());
			onePost.add(posts.get(i));

			JSONposts.add(onePost);
		}

		PrintWriter out = response.getWriter();
		out.write(JSONposts.toString());
		out.flush();
		out.close();
		return null;
	}

	public ModelAndView getMiscInfo(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String stunum = (String) request.getSession()
				.getAttribute("studentnum");
		ArrayList<String> posts = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		Userinfo userinfo = (Userinfo) request.getSession().getAttribute(
				"userinfo");

		JSONObject basicInfo = new JSONObject();
		basicInfo.put("pic", "SOME URL");
		basicInfo.put("stunum", stunum);
		basicInfo.put("name", userinfo.getName());
		basicInfo.put("gender", userinfo.getGender());
		basicInfo.put("dept", DepartmentUtils.getInstance().getDepartment(
				userinfo.getDepartment()));

		String classnum = userinfo.getClassnum();
		if (classnum.equals("NA")) {
			basicInfo.put("classnum", " ");
		} else {
			basicInfo.put("classnum", userinfo.getClassnum());
		}
		// end basicinfo

		// begin abilities
		JSONObject abilities = new JSONObject();
		abilities.put("ability1", donwTo100(userinfo.getAbility1()));
		abilities.put("ability2", donwTo100(userinfo.getAbility2()));
		abilities.put("ability3", donwTo100(userinfo.getAbility3()));
		abilities.put("ability4", donwTo100(userinfo.getAbility4()));
		abilities.put("ability5", donwTo100(userinfo.getAbility5()));
		abilities.put("ability6", donwTo100(userinfo.getAbility6()));
		// end abilities

		// begin current pos
		JSONObject poses = new JSONObject();
		JSONArray posarray = new JSONArray();
		Posts onepost;
		int point = 0;
		while (point < posts.size()) {
			JSONObject node = new JSONObject();
			onepost = (Posts) postsDAO
					.findByFullcode((String) posts.get(point)).get(0);
			node.put("posname", onepost.getFullname());
			node.put("posid", onepost.getId());
			posarray.add(node);
			point++;
		}
		poses.put("currentpos", posarray);
		// end current pos

		// begin pos record
		JSONObject posrecord = new JSONObject();
		JSONArray posrecordarray = new JSONArray();

		List prds = posrecordDAO.findByStunum(stunum);
		Posrecord onerecord;
		point = 0;
		while (point < prds.size()) {
			onerecord = (Posrecord) prds.get(point);
			if (onerecord.getEndtime() != null) {
				JSONObject node = new JSONObject();
				node.put("posname", onerecord.getPosname());
				node.put("recid", onerecord.getId());
				node.put("starttime", onerecord.getStarttime());
				node.put("endtime", onerecord.getEndtime());
				posrecordarray.add(node);
			}
			point++;
		}
		posrecord.put("posrecord", posrecordarray);
		// end posrecord

		JSONArray thearray = new JSONArray();

		thearray.add(basicInfo);
		thearray.add(abilities);
		thearray.add(poses);
		thearray.add(posrecord);
		PrintWriter pw = response.getWriter();
		pw.print(thearray.toString());
		pw.close();
		return null;
	}

	// by echo@net9
	// 2007/09/10
	private int donwTo100(int n) {
		int up = n / 100;
		up++;
		return n / up;
	}
}
