package org.net9.redbud.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.net9.redbud.misc.CodeErrorException;
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
import org.net9.redbud.util.CategoryPoint;
import org.net9.redbud.util.DataCenter;
import org.net9.redbud.util.DateUtils;
import org.net9.redbud.util.DepartmentUtils;
import org.net9.redbud.util.DeptsPoint;
import org.net9.redbud.util.OrgansPoint;
import org.net9.redbud.util.PermissionValidate;
import org.net9.redbud.util.PostsPoint;
import org.net9.redbud.util.ComparatorUtils.CatComparator;
import org.net9.redbud.util.ComparatorUtils.DeptComparator;
import org.net9.redbud.util.ComparatorUtils.OrganComparator;
import org.net9.redbud.util.ComparatorUtils.PostComparator;
import org.net9.redbud.web.util.MD5Util;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

public class SettingsController extends RedbudBaseController {

	private CategoryDAO categoryDAO;

	private OrgansDAO organsDAO;

	private PostsDAO postsDAO;

	private DeptsDAO deptsDAO;

	private LoginDAO loginDAO;

	private DataCenter dataCenter;

	private PosrecordDAO posrecordDAO;

	public void setPosrecordDAO(PosrecordDAO posrecordDAO) {
		this.posrecordDAO = posrecordDAO;
	}

	public void setDataCenter(DataCenter dataCenter) {
		this.dataCenter = dataCenter;
	}

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
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

	public void setCategoryDAO(CategoryDAO categoryDAO) {
		this.categoryDAO = categoryDAO;
	}

	/**
	 * @author DennisZz@9# 返回当前校级岗位的Json结构
	 */
	public ModelAndView returnCurrentPosition(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		JSONArray appTrees = new JSONArray();
		ArrayList<String> postList = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		Iterator it = postList.iterator();
		while (it.hasNext()) {
			try {
				String temp = (String) it.next();
				// root级别
				if (PermissionValidate.isRoot(temp)) {
					// vacant positions of school-level
					JSONArray schVacantPosTree = new JSONArray();
					// vacant positions of dept-level
					JSONArray deptVacantPosTree = new JSONArray();
					// vacant positions of assosiations
					JSONArray assnVacantPosTree = new JSONArray();

					Userinfo user = new Userinfo();
					user = (Userinfo) request.getSession().getAttribute(
							"userinfo");
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
								dataCenter.getCatJsonTree(catNode, catCode, 1);
								assnVacantPosTree = (JSONArray) catNode
										.get("children");
								flag[2] = 1;
							}
							// 系级
							else if (catCode.substring(0, 1).equals("4")) {
								JSONObject catNode = new JSONObject();
								dataCenter.getCatJsonTree(catNode, catCode, 1);
								deptVacantPosTree.add(catNode);
								flag[1] = 1;
							}
							// 校级(未来602以上的6字头组织归入此列)
							else {
								JSONObject catNode = new JSONObject();
								dataCenter.getCatJsonTree(catNode, catCode, 1);
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
					schVacantPosNode.put("type", "0");
					schVacantPosNode.put("children", schVacantPosTree);
					deptVacantPosNode.put("title", "系级岗位");
					deptVacantPosNode.put("type", "0");
					deptVacantPosNode.put("children", deptVacantPosTree);
					assnVacantPosNode.put("title", "协会岗位");
					assnVacantPosNode.put("type", "5");
					assnVacantPosNode.put("children", assnVacantPosTree);
					appTrees.add(schVacantPosNode);
					appTrees.add(deptVacantPosNode);
					appTrees.add(assnVacantPosNode);

				}
				// Category级别的boss
				else if (PermissionValidate.isCategoryBoss(temp)) {
					JSONObject appTreeNode = new JSONObject();
					JSONArray appTree = new JSONArray();
					String tempCatCode = temp.substring(0, 3);
					Iterator catIt = categoryDAO.findByCode(tempCatCode)
							.iterator();
					Category tempCat = new Category();
					tempCat = (Category) catIt.next();
					Set organSet = tempCat.getOrganses();
					Object tempOrganArray[] = organSet.toArray();
					Arrays.sort(tempOrganArray, new OrganComparator());
					if (tempOrganArray.length == 0) {
						JSONObject noOrgan = new JSONObject();
						String str1 = tempCat.getName() + "尚未初始化";
						noOrgan.put("type", "-1");
						noOrgan.put("title", str1);
						appTree.add(noOrgan);
					}
					for (int organIndex = 0; organIndex < tempOrganArray.length; organIndex++) {
						JSONArray deptTree = new JSONArray();
						Organs tempOrgan = (Organs) tempOrganArray[organIndex];
						// 管理帐号不能被看见
						if (tempOrgan.getCode().equals("000")) {
							continue;
						}
						Set deptSet = tempOrgan.getDeptses();
						Object tempDeptArray[] = deptSet.toArray();
						Arrays.sort(tempDeptArray, new DeptComparator());
						if (tempDeptArray.length == 0) {
							JSONObject noDept = new JSONObject();
							String str2 = tempOrgan.getName() + "尚未初始化";
							noDept.put("type", "-1");
							noDept.put("title", str2);
							deptTree.add(noDept);
						}
						for (int deptIndex = 0; deptIndex < tempDeptArray.length; deptIndex++) {
							JSONArray postTree = new JSONArray();
							Depts tempDept = (Depts) tempDeptArray[deptIndex];
							Set postSet = tempDept.getPostses();
							Object tempPostArray[] = postSet.toArray();
							Arrays.sort(tempPostArray, new PostComparator());
							if (tempPostArray.length == 0) {
								JSONObject noPost = new JSONObject();
								String str3 = tempDept.getName() + "尚未初始化";
								noPost.put("type", "-1");
								noPost.put("title", str3);
								postTree.add(noPost);
							}
							for (int postIndex = 0; postIndex < tempPostArray.length; postIndex++) {
								Posts tempPost = (Posts) tempPostArray[postIndex];
								JSONObject postNode = new JSONObject();
								postNode.put("type", "4");
								postNode.put("title", tempPost.getName());
								postNode.put("postNo", tempPost.getFullcode());
								postTree.add(postNode);
							}
							JSONObject deptNode = new JSONObject();
							deptNode.put("type", "3");
							deptNode.put("title", tempDept.getName());
							deptNode.put("children", postTree);
							deptNode.put("postNo", tempCat.getCode()
									+ tempOrgan.getCode() + tempDept.getCode());
							deptTree.add(deptNode);
						}
						JSONObject organNode = new JSONObject();
						organNode.put("type", "2");
						organNode.put("title", tempOrgan.getName());
						organNode.put("children", deptTree);
						organNode.put("postNo", tempCat.getCode()
								+ tempOrgan.getCode());
						appTree.add(organNode);
					}
					appTreeNode.put("type", "1");
					appTreeNode.put("title", tempCat.getName());
					appTreeNode.put("children", appTree);
					appTreeNode.put("postNo", tempCat.getCode());
					appTrees.add(appTreeNode);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		PrintWriter out = response.getWriter();
		out.write(appTrees.toString());
		out.flush();
		out.close();
		return null;
	}

	// originally written by golden and sillywolf; migrated by DennisZz
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		// DennisZz, August 12th, 2007.
		List postList = (List) request.getSession().getAttribute("postList");
		if ((postList == null)
				|| (!PermissionValidate.isRoot(postList.get(0) + ""))) {
			out.println("请不要试图篡权");
			out.close();
			return null;
		}

		int state = Integer.parseInt(request.getParameter("state_selected"));
		// Check that we have a file upload request
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
				.getFile("csvfile");
		FileItem fi = file.getFileItem();
		BufferedReader br = new BufferedReader(new InputStreamReader(fi
				.getInputStream(), "UTF-8"));
		String line;

		CategoryPoint categoryP = new CategoryPoint();
		int order = 0;

		ArrayList<String> malformedLines = new ArrayList<String>();
		ArrayList<String> wellformedLines = new ArrayList<String>();
		// boolean hasmalformedLines = false;
		out
				.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""
						+ " \"http://www.w3.org/TR/html4/loose.dtd\">"
						+ "<html>	<head>		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
						+ "<title>Insert title here</title>	</head>	<body>");
		while ((line = br.readLine()) != null) {

			try {

				categoryP = parseUpload(line, categoryP, state);

				// out.println("Init: Parse string:" + line + "<br />");
				wellformedLines.add(line);
				// out.flush();
				order++;

			} catch (ArrayIndexOutOfBoundsException e) {
				// possible reason: uploaded a wrong format file(.xls\.doc....)
				e.printStackTrace();
				// out.println(e.toString());
				out.println("请提交正确格式的文件!");
				// out.println("<input type=\"button\"
				// onclick=\"window.parent.fireNavigatorButtonEvent('Init')\"
				// value='返回' />");
				out.flush();
				return null;
			} catch (NullPointerException e) {
				e.printStackTrace();
				out.println("数据格式有误，可能的原因是您的数据类别(系级、校级)未选择正确");
				out.flush();
				out.close();
				return null;
			} catch (CodeErrorException e) {
				// some lines contain some malformed information
				e.printStackTrace();
				malformedLines.add("line " + order + " : " + line);
				// hasmalformedLines = true;
			} catch (Exception e) {
				e.printStackTrace();
				out.println(e.toString());
				out.println("Error occur in line" + order + "!");
				out.flush();
				return null;
			}
		}

		Iterator it = malformedLines.iterator();
		if (malformedLines.size() > 0) {
			out.println("<h1>如下几行数据有问题。请检查数据</h1>");
			out.print("<div>");
			while (it.hasNext()) {
				out.println(it.next().toString() + "<br />");
			}
			out.print("</div>");

		}

		out.println("解析完毕，开始存入数据库<br />");
		out.flush();
		boolean result = saveToSQL(categoryP);
		out.println("执行结束<br />");
		if (result) {
			it = wellformedLines.iterator();
			out.println("<h1>以下是正确导入的数据</h1>");
			out.print("<div>");
			while (it.hasNext()) {
				out.println(it.next().toString() + "<br />");
			}
			out.print("</div>");
			System.out.println("parse almost ok");
			out.println("finished!");
		} else {
			out.println("存入数据库失败，可能原因是您未选择文件或者已经导入过该节点数据。");
		}
		out.println("</body></html>");

		out.close();
		return null;
	}

	/**
	 * written by sillywolf@9#
	 * 
	 * @param cateP
	 * @return
	 */
	private boolean saveToSQL(CategoryPoint cateP) {
		try {
			String code = cateP.getCategory().getCode();
			List list = categoryDAO.findByCode(code);
			if (list.isEmpty() == false) {
				return false;
			} else {
				Category category = cateP.getCategory();
				categoryDAO.save(category);
				/*
				 * 
				 */
				Organs or = new Organs();
				or.setCategory(category);
				or.setCode("000");
				or.setName("");
				or.setDescription("");
				organsDAO.save(or);
				Depts de = new Depts();
				de.setOrgans(or);
				de.setCode("000");
				de.setDescription("");
				de.setName("");
				deptsDAO.save(de);
				// Posts pos=new Posts();
				// pos.setName("");
				// pos.setCode("000");
				// p

				int pos_id = dataCenter.setNewPosts("000", "管理帐号", (category
						.getId()).intValue(), (or.getId()).intValue(), (de
						.getId()).intValue(), "管理帐号", (short) 100, (float) 1.0,
						"管理帐号", (short) 5, (short) 5, (short) 5, (short) 5,
						(short) 5, (short) 5, "NA", "NA");
				// dataCenter.setNewUser(category.getCode(), category.getCode(),
				// category.getName()+"管理帐号", (short)0, , nation, depart,
				// classnum,
				// political, specialty, tel, mobile, email, address, ability1,
				// ability2, ability3, ability4, ability5, ability6)
				Login loginf = dataCenter.setNewUser(category.getCode()
						.toString(),
						MD5Util.MD5(category.getCode().toString()), category
								.getName()
								+ "管理帐号", (short) 0, "1", "CN", Short
								.parseShort((category.getCode().substring(1))),
						"NA", "NA", "NA", "NA", "NA", "NA", "NA", (short) 99,
						(short) 99, (short) 99, (short) 99, (short) 99,
						(short) 99);
				loginDAO.save(loginf);
				Posts postsf = postsDAO.findById(pos_id);
				postsf.setLogin(loginf);
				// postsDAO.merge(postsf);
				postsDAO.update(postsf);
				// postsDAO.update(postsf);
				List<OrgansPoint> organPList = cateP.getOrgansList();
				for (int i = 0; i < organPList.size(); i++) {
					OrgansPoint organP = organPList.get(i);
					Organs organs = organP.getOrgans();
					organs.setCategory(category);
					organsDAO.save(organs);
					List<DeptsPoint> deptsPList = organP.getDeptsList();
					for (int j = 0; j < deptsPList.size(); j++) {
						DeptsPoint deptP = deptsPList.get(j);
						Depts depts = deptP.getDepts();
						depts.setOrgans(organs);
						deptsDAO.save(depts);
						List<PostsPoint> postPList = deptP.getPostsList();
						for (int k = 0; k < postPList.size(); k++) {
							PostsPoint postP = postPList.get(k);
							Posts posts = postP.getPost();
							posts.setDepts(depts);
							Login login = postP.getLogin();
							if (login != null) {
								List loginList = loginDAO
										.findByStudentnum(login.getStudentnum());
								if (loginList.isEmpty()) {
									loginDAO.save(login);
								} else
									login = (Login) loginList.get(0);
								posts.setLogin(login);
								setPosRecord(login, posts);
							}
							postsDAO.save(posts);

						}
					}
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean errorCheck(String code) {
		if ((code.length() != 3) && (code.length() != 0))
			return false;
		else
			return true;
	}

	// originally written by golden and sillywolf; migrated by DennisZz
	/**
	 * Parse the Category section of the input file<br>
	 * written by Golden@9#
	 * 
	 * @param cateString:
	 *            The Category Section
	 * @return The category_id of the category table
	 */
	public Category parseCate(String cateString) throws Exception {
		// Put your code here
		Category category = new Category();
		String sub1, sub2;// sub1 is the code of the category and sub2 is the
		// name of the category
		/**
		 * Get the category code and category name of the section
		 */
		String[] cateArray = cateString.split(",", -1);
		sub1 = cateArray[0] + cateArray[1];
		sub2 = cateArray[2];
		
		if (sub2.equals("")) sub2="请定义";
		/** *********************************************************** */
		/**
		 * Save the category object to database
		 */
		if (errorCheck(sub1) == false) {
			throw new CodeErrorException("category code error");
		}

		category.setCode(sub1);
		category.setName(sub2);

		category.setDescription("");
		return category;
	}

	/**
	 * Parse the organ section of the input file<br>
	 * written by Golden@9#
	 * 
	 * @param organString:the
	 *            organ section which to be parsed
	 * @param cate_id
	 *            :the category id which the organ belongs
	 * @return:the id of the organ record in organs table
	 */
	public OrgansPoint parseOrgan(String organString, CategoryPoint cate_id)
			throws Exception {
		String sub1, sub2;
		String[] organArray = organString.split(",", -1);
		if ((organArray[0].equals("")) && (organArray[1].equals("") == false)) {
			organArray[0] = cate_id.getTempOrgans().getOrgans().getCode()
					.substring(0, 1);
		}
		sub1 = organArray[0] + organArray[1];
		sub2 = organArray[2];

		if (sub2.equals("")) sub2="请定义";
		if (errorCheck(sub1) == false) {
			throw new CodeErrorException("Organ code error!");
		}
		if (sub1.equals("") == true)
			return cate_id.getTempOrgans();
		OrgansPoint organsP = cate_id.findByOrgansCode(sub1);
		if (organsP == null) {
			OrgansPoint organsPo = new OrgansPoint();
			Organs organs = new Organs();
			organs.setCode(sub1);
			organs.setName(sub2);
			organs.setDescription("");
			organsPo.setOrgans(organs);
			cate_id.addOrgansP(organsPo);
			return organsPo;

		} else
			return organsP;

	}

	/**
	 * Returns the order in the depts of the <br>
	 * written by sillywolf@9#
	 * 
	 * @param depts:
	 *            the list of the Depts
	 * @param organ_id:
	 *            the organ_id which to be inspected
	 * @return: true if the organ_id exists in the 'depts' list and false else
	 */
	private int ifExistDept(List depts, int organ_id) {
		if (depts == null)
			return -1;// if the list is a null list,return -1
		int point = 0;
		Depts dept = null;
		while (point < depts.size()) {
			dept = (Depts) (depts.get(point));
			if (dept.getOrgans().getId() == organ_id)
				return dept.getId();
			point++;
		}
		return -1;

	}

	/**
	 * Parse the Dept section of the input file<br>
	 * written by sillywolf@9#
	 * 
	 * @param deptString:the
	 *            Dept section which to be parsed
	 * @param cate_id:the
	 *            id of the category which the dept belongs
	 * @param organ_id:
	 *            the id of the organs which the dept belongs
	 * @return:the id of the dept record in dept table
	 */
	// public int parseDept(String deptString, int cate_id, int organ_id) {
	public DeptsPoint parseDept(String deptString, OrgansPoint organsP,
			CategoryPoint cate_id) throws Exception {
		String sub1, sub2;
		String[] deptArray = deptString.split(",", -1);
		if ((deptArray[0].equals("")) && (deptArray[1].equals("") == false)) {
			deptArray[0] = organsP.getTempDepts().getDepts().getCode()
					.substring(0, 1);
		}
		sub1 = deptArray[0] + deptArray[1];

		sub2 = deptArray[2];

		if (errorCheck(sub1) == false) {
			throw new CodeErrorException("code error");
		}
		if (sub1.equals(""))
			return organsP.getTempDepts();
		
		if (sub2.equals("")) sub2="请定义";
		
		DeptsPoint deptPo = organsP.findByDeptsCode(sub1);
		if (deptPo == null) {
			Depts depts = new Depts();
			depts.setCode(sub1);
			depts.setName(sub2);
			depts.setDescription(cate_id.getCategory().getName()
					+ organsP.getOrgans().getName() + depts.getName());
			DeptsPoint deptP = new DeptsPoint();
			deptP.setDepts(depts);
			organsP.addDeptsPoint(deptP);
			return deptP;
		} else {
			return deptPo;
		}
	}

	private Login setUserinfo(String userString, int state,
			CategoryPoint cateP, OrgansPoint organP, DeptsPoint deptP)
			throws Exception {
		String[] item = userString.split(",", -1);
		// 前期判断
		if (item[0].equals("")) {
			for (int i = 1; i < 6; i++) {
				if (item[i].equals("") == false) {
					throw new CodeErrorException("userinfo error");
					// return null;
				}
			}
			return null;
		} else {
			for (int i = 1; i < 6; i++) {
				if (item[i].equals("") == true) {
					throw new CodeErrorException("userinfo error");
				}
			}
		}
		String studentNum = item[0];
		{
			String name = item[1];
			String grad = item[2];
			short grade = Short.parseShort(grad);
			String gender = item[3];
			String nation = "CN_" + item[4];
			String political = item[5];
			String classnum;
			String specialty;
			String email;
			String address;
			String mobile;
			String tel;
			String password;
			short depart;

			// modified by sillywolf,September,10th,2007

			if (state == 2) {
				classnum = item[6] + item[7];
				specialty = item[8];
				email = item[9];
				address = item[10];
				mobile = item[11];
				tel = item[12];
				password = MD5Util.MD5(studentNum);
				depart = Short.parseShort(cateP.getCategory().getCode()
						.substring(1));
			} else {
				depart = DepartmentUtils.getInstance().getDeptNum(item[6]);
				classnum = item[7] + item[8];
				specialty = item[9];
				email = item[10];
				address = item[11];
				mobile = item[12];
				tel = item[13];
				password = MD5Util.MD5(studentNum);
			}
			// int user_id=1;
			short ability_init = 0;
			Login login = dataCenter.setNewUser(studentNum, password, name,
					grade, gender, nation, depart, classnum, political,
					specialty, tel, mobile, email, address, ability_init,
					ability_init, ability_init, ability_init, ability_init,
					ability_init);
			return login;

		}
	}

	/**
	 * Parse the position section of the input file<br>
	 * written by sillywolf@9#
	 * 
	 * @param posString:
	 *            the position section which to be parsed
	 * @param cate_id:
	 *            the category id the position section belongs
	 * @param organ_id:the
	 *            organs id the position section belongs
	 * @param dept_id:the
	 *            dept id the position section belongs
	 * @return: the id of the position record in posts table
	 */
	void parsePos(String posString, String userString, CategoryPoint catePoint,
			OrgansPoint organP, DeptsPoint deptsP, int state) throws Exception {
		String[] str = posString.split(",", -1);
		String sub1, sub2;
		if ((str[0].equals("")) && (str[1].equals("") == false)) {
			str[0] = deptsP.getTempPostsP().getPost().getCode().substring(0, 1);
		}
		sub1 = str[0] + str[1];
		sub2 = str[2];
		//modified by sillywolf Oct,20
		if (sub2.equals("")) sub2="请定义";
		//modified finished
		if (errorCheck(sub1) == false) {
			throw new CodeErrorException("position code error");
		}
		short weight = Short.parseShort(str[3]);
		// List list = null;
		PostsPoint po = deptsP.findByPostsCode(sub1);
		if (po == null) {
			Posts posts = dataCenter.setNewPost(sub1, sub2, catePoint, organP,
					deptsP, "null", weight, (float) 1, "null", (short) 1,
					(short) 1, (short) 1, (short) 1, (short) 1, (short) 1,
					"null", "null");
			po = new PostsPoint();
			po.setPost(posts);
			Login login = this.setUserinfo(userString, state, catePoint,
					organP, deptsP);
			if (login != null) {
				po.setLogin(login);

			}
			deptsP.addPostsPoint(po);

		}
	}

	/**
	 * Parse the userinfo section of the input file<br>
	 * written by sillywolf@9#
	 * 
	 * @param userString:传入初始化用户的信息的信息
	 * @param posId:传入岗位的id
	 * @return
	 */
	public int parseUser(String userString, int posId, int state) {
		Posts posts = postsDAO.findById(posId);
		Depts depts = deptsDAO.findById(posts.getDepts().getId());
		Organs organs = organsDAO.findById(depts.getOrgans().getId());
		Category cate = categoryDAO.findById(organs.getCategory().getId());
		String[] item = userString.split(",");
		String studentNum = item[0];
		List list = loginDAO.findByStudentnum(studentNum);
		if (list.isEmpty() == false) {
			posts.setLogin(((Login) list.get(0)));
		} else {
			String name = item[1];
			String grad = item[2];
			short grade = Short.parseShort(grad);
			String gender = item[3];
			String nation = "CN_" + item[4];
			String political = item[5];
			String classnum;
			String specialty;
			String email;
			String address;
			String mobile;
			String tel;
			String password;
			short depart;
			if (state == 2) {
				classnum = item[6] + item[7];
				specialty = item[8];
				email = item[9];
				address = item[10];
				mobile = item[11];
				tel = item[12];
				password = MD5Util.MD5(studentNum);
				depart = Short.parseShort(cate.getCode().substring(1));
			} else {
				depart = DepartmentUtils.getInstance().getDeptNum(item[6]);
				classnum = item[7] + item[8];
				specialty = item[9];
				email = item[10];
				address = item[11];
				mobile = item[12];
				tel = item[13];
				password = MD5Util.MD5(studentNum);
			}
			short ability_init = 0;
			Login login = dataCenter.setNewUser(studentNum, password, name,
					grade, gender, nation, depart, classnum, political,
					specialty, tel, mobile, email, address, ability_init,
					ability_init, ability_init, ability_init, ability_init,
					ability_init);
			posts.setLogin(login);
		}
		postsDAO.update(posts);
		// return user_id;
		return 1;
	}

	/**
	 * 把读入的字符串分成category段，Organ段，Depts段和Post段存在数组String[]中 written by golden@9# &
	 * sillywolf@9#
	 * 
	 * @param line：读入的字符串
	 * @param num：每段的字段数
	 * @return：返回的String[]数组
	 */
	public String[] parseSubString(String line, int num) {
		String tempStr[] = line.split(",", -1);

		String[] str = new String[5];
		int i, j;
		for (i = 0; i < 5; i++)
			str[i] = "";

		for (i = 0; i < 4; i++) {
			for (j = 0; j < 2; j++)
				str[i] = str[i] + tempStr[i * 3 + j] + ",";
			str[i] = str[i] + tempStr[i * 3 + j];
		}
		str[3] = str[3] + "," + tempStr[12];
		for (i = 13; i < tempStr.length - 1; i++)
			str[4] = str[4] + tempStr[i] + ",";
		str[4] = str[4] + tempStr[tempStr.length - 1];
		return str;
	}

	public CategoryPoint parseUpload(String upload, CategoryPoint cateP,
			int state) throws Exception {
		// Integer start = new Integer(-1);
		String[] str1 = parseSubString(upload, 3);
		String cateString = str1[0];
		String organString = str1[1];
		String deptString = str1[2];
		String posString = str1[3];
		String userString = str1[4];
		OrgansPoint organsP = null;
		DeptsPoint dept_id = null;
		if (cateP.isEmpty() == true)
			cateP.newCategory(parseCate(cateString));
		organsP = parseOrgan(organString, cateP);
		dept_id = parseDept(deptString, organsP, cateP);
		parsePos(posString, userString, cateP, organsP, dept_id, state);
		return cateP;
	}

	/**
	 * 根据pos_id的
	 * 
	 * @param pos_id
	 */
	private void setPosRecord(Login login, Posts posts) {

		String fullname = posts.getFullname();

		String starttime = "uncertain";
		String stunum = login.getStudentnum();
		String posCode = posts.getFullcode();
		Posrecord posre = new Posrecord();
		posre.setLogin(login);
		posre.setPosname(fullname);
		posre.setAbility1(null);
		posre.setAbility2(null);
		posre.setAbility3(null);
		posre.setAbility4(null);
		posre.setAbility5(null);
		posre.setAbility6(null);
		posre.setRemark(null);
		posre.setStarttime(starttime);
		posre.setEndtime(null);
		posre.setPoscode(posCode);
		posre.setStunum(stunum);
		posrecordDAO.save(posre);

	}

	/*
	 * 在修改岗位结构中增加一个岗位树节点 written by golden@9# && sillywolf@9#
	 */
	public ModelAndView addTreeNode(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		// urong@9#, 2007.08.13
		// permission validate, only root and xxx101101101
		ArrayList<String> allPos = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.hasRootOrCategoryPermission(allPos)) {
			out.print("false");
			out.close();
			return null;
		}

		String code = request.getParameter("code");
		if (!isValidCode(code)) {
			out.print("false");
			out.close();
			return null;
		}
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		int len = code.length();
		if (len == 3) {
			//判断修改的名字是否已经存在
			List categoryList = categoryDAO.findByName(name);
			if(!categoryList.isEmpty()){
				out.print("false");
				return null;
			}
			
			if (!this.hasRootPermission(allPos)) {
				out.print("false");
				out.close();
				return null;
			}
			List list = categoryDAO.findByCode(code);
			if (list.isEmpty() == false) {
				out.print("false");
				out.flush();
				return null;
			}
			
			Category c = new Category();
			c.setCode(code);
			c.setName(name);
			c.setDescription(description);
			categoryDAO.save(c);

		} else if (len == 6) {
			//判断修改的名字是否已经存在
			Organs organ = categoryDAO.getOrganByCatCodeOrganName(code.substring(0, 3), name);
			if(organ != null){
				out.print("false");
				return null;
			}
			
			organ = categoryDAO.getOrganByCatOrganCode(code);
			if (organ != null) {
				out.println("false");
				out.flush();
				return null;
			}
			Organs o = new Organs();
			o.setCode(code.substring(3, 6));
			o.setName(name);
			o.setDescription(description);
			Category c = new Category();
			c = categoryDAO.getCategoryByCatCode(code.substring(0, 3));
			o.setCategory(c);
			organsDAO.save(o);

		} else if (len == 9) {
			//判断修改的名字是否已经存在
			Depts dept = categoryDAO.getDeptByCatOrganCodeDeptName(code.substring(0, 6), name);
			if(dept != null){
				out.print("false");
				return null;
			}
			
			dept = categoryDAO.getDeptByCatOrganDeptCode(code);
			if (dept != null) {
				out.println("false");
				out.flush();
				return null;
			}

			Depts d = new Depts();
			d.setCode(code.substring(6, 9));
			d.setName(name);
			d.setDescription(description);
			Organs o = categoryDAO.getOrganByCatOrganCode(code.substring(0, 6));
			d.setOrgans(o);
			deptsDAO.save(d);
		} else if (len == 12) {
			//判断修改的名字是否已经存在
			List postList = postsDAO.findByName(name);
			if(!postList.isEmpty()){
				out.print("false");
				return null;
			}
			
			postList = postsDAO.findByFullcode(code);
			if (postList.isEmpty() == false) {
				out.println("false");
				out.flush();
				return null;
			}
			Posts p = new Posts();
			p.setName(name);
			p.setDescription(description);
			Category category = categoryDAO.getCategoryByCatCode(code
					.substring(0, 3));
			Organs organ = categoryDAO.getOrganByCatOrganCode(code.substring(0,
					6));
			Depts dept = categoryDAO.getDeptByCatOrganDeptCode(code.substring(
					0, 9));
			p.setCategoryCode(category.getCode());

			p.setCode(code.substring(9, 12));

			p.setDepts(dept);
			p.setDeptCode(dept.getCode());

			p.setFullcode(code);

			String fullname = category.getName() + organ.getName()
					+ dept.getName() + name;
			p.setFullname(fullname);

			/* Modified by sillywolf,July,12,2007 */
			// p.setOrgan_code(organ.getName());
			p.setOrganCode(organ.getCode());

			// *****************************************
			Short ability1 = (request.getParameter("ability1").equals("")) ? 0
					: Short.parseShort(request.getParameter("ability1"));
			Short ability2 = (request.getParameter("ability2").equals("")) ? 0
					: Short.parseShort(request.getParameter("ability2"));
			Short ability3 = (request.getParameter("ability3").equals("")) ? 0
					: Short.parseShort(request.getParameter("ability3"));
			Short ability4 = (request.getParameter("ability4").equals("")) ? 0
					: Short.parseShort(request.getParameter("ability4"));
			Short ability5 = (request.getParameter("ability5").equals("")) ? 0
					: Short.parseShort(request.getParameter("ability5"));
			Short ability6 = (request.getParameter("ability6").equals("")) ? 0
					: Short.parseShort(request.getParameter("ability6"));
			p.setPosAbility1(ability1);
			p.setPosAbility2(ability2);
			p.setPosAbility3(ability3);
			p.setPosAbility4(ability4);
			p.setPosAbility5(ability5);
			p.setPosAbility6(ability6);

			Float coefficient = (request.getParameter("coefficient").equals("")) ? 0
					: Float.parseFloat(request.getParameter("coefficient"));

			p.setCoefficient(coefficient);

			p.setPosCost(request.getParameter("pos_cost"));

			// Modified by sillywolf@9#,July,15,2007
			// p.setPos_req(relatedpos );

			p.setPosReq(request.getParameter("pos_req"));
			p.setRelatedPos(request.getParameter("relatedpos"));
			// ********************************************
			Short weight = (request.getParameter("weight").equals("")) ? 0
					: Short.parseShort(request.getParameter("weight"));
			p.setWeight(weight);
			postsDAO.save(p);
		}
		out.print("true");
		out.close();
		return null;

	}

	/*
	 * 修改一个岗位节点的信息 written by golden@9# && sillywolf@9#
	 */
	public ModelAndView changePosArchInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		// urong@9#, 2007.08.13
		// permission validate, only root and xxx101101101
		ArrayList<String> allPos = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.hasRootOrCategoryPermission(allPos)) {
			out.print("false");
			out.close();
			return null;
		}

		Category cate;
		Organs organ;
		Depts dept;
		Posts post;
		Posrecord posrecord;
		String posArchCode = request.getParameter("posArchCode");
		if (!isValidCode(posArchCode)) {
			out.print("false");
			out.close();
			return null;
		}
		List cList;
		// List oList;
		// List dList;
		List pList;
		List prList;
		Iterator iter;
		Iterator prIter;
		String fullCode;
		String newFullCode;
		String fullName;
		String cateCode;
		String cateName;
		String organCode;
		String organName;
		String deptCode;
		String deptName;
		// String postCode; delete: urong@9#, 2007.08.13
		String postName;
		// Set organSet;
		String code;
		String name;
		String description;
		String weight;
		String coefficient;
		// String req; delete: urong@9#, 2007.08.13
		String a1;
		String a2;
		String a3;
		String a4;
		String a5;
		String a6;
		String cost;
		String relate;

		int codeIsChanged = 0; // if code is changed : 1, else: 0
		int nameIsChanged = 0; // if name is changed : 1, else: 0
		// System.out.print(posArchCode.length());
		switch (posArchCode.length()) {
		// change the info of cate
		case 3:
			code = request.getParameter("code");
			name = request.getParameter("name");
			cateName = name;
			description = request.getParameter("description");
			cList = categoryDAO.findByCode(posArchCode);
			if (!cList.isEmpty()) {
				cate = (Category) cList.get(0);
				if (!posArchCode.equalsIgnoreCase(code)) {
					cList = categoryDAO.findByCode(code);
					if (!cList.isEmpty()) {// cate_code is exist
						out.print("false");
						return null;
					}
					cate.setCode(code);
					codeIsChanged = 1;
				}
				if (!cate.getName().equalsIgnoreCase(name)) {
					cList = categoryDAO.findByName(name);
					if (!cList.isEmpty()) {// cate_name is exist
						out.print("false");
						return null;
					}
					cateName = cate.getName();
					cate.setName(name);
					nameIsChanged = 1;
				}
				cate.setDescription(description);
				categoryDAO.merge(cate);
			}
			if ((codeIsChanged == 1) || (nameIsChanged == 1)) {// cate_code or
				// cate_name is
				// changed
				pList = postsDAO.findByCategoryCode(posArchCode);
				if (!pList.isEmpty()) {
					iter = pList.iterator();
					while (iter.hasNext()) {
						post = (Posts) iter.next();
						fullCode = post.getFullcode();
						newFullCode = code + fullCode.substring(3);
						post.setFullcode(newFullCode);
						post.setCategoryCode(code);
						fullName = post.getFullname();
						fullName.replaceAll(cateName, name);
						post.setFullname(fullName);
						postsDAO.merge(post);

						prList = posrecordDAO.findByPoscode(fullCode);
						if (!prList.isEmpty()) {
							prIter = prList.iterator();
							while (prIter.hasNext()) {
								posrecord = (Posrecord) prIter.next();
								if (posrecord.getEndtime() != null) {
									posrecord.setPoscode(newFullCode);
									posrecord.setPosname(fullName);
									posrecordDAO.merge(posrecord);

								}
							}
						}
					}
				}
			}
			break;
		// change the info of organ
		case 6:
			code = request.getParameter("code");
			name = request.getParameter("name");
			organName = name;
			description = request.getParameter("description");
			cateCode = posArchCode.substring(0, 3);
			organCode = posArchCode.substring(3);
			organ = categoryDAO.getOrganByCatOrganCode(posArchCode);
			if (organ == null) {
				out.print("false");
				return null;
			}
			if (!organ.getCode().equalsIgnoreCase(code)) {
				if (categoryDAO.getOrganByCatOrganCode(cateCode + code) != null) {
					// organ_code is exist
					out.print("false");
					return null;
				}
				organ.setCode(code);
				codeIsChanged = 1;
			}
			if (!organ.getName().equalsIgnoreCase(name)) {
				//判断修改的名字是否已经存在
				Organs tmpOrgan = categoryDAO.getOrganByCatCodeOrganName(cateCode, name);
				if(tmpOrgan != null){
					out.print("false");
					return null;
				}
				
				organName = organ.getName();
				organ.setName(name);
				nameIsChanged = 1;
			}
			organ.setDescription(description);
			organsDAO.merge(organ);
			if ((codeIsChanged == 1) || (nameIsChanged == 1)) {// organ_code or
				// organ_name is
				// changed
				pList = postsDAO.findByCatOrganCode(cateCode, organCode);
				if (!pList.isEmpty()) {
					iter = pList.iterator();
					while (iter.hasNext()) {
						post = (Posts) iter.next();
						fullCode = post.getFullcode();
						newFullCode = fullCode.substring(0, 3) + code
								+ fullCode.substring(6);
						post.setFullcode(newFullCode);
						post.setOrganCode(code);
						fullName = post.getFullname();
						fullName.replaceAll(organName, name);
						post.setFullname(fullName);
						postsDAO.merge(post);
						prList = posrecordDAO.findByPoscode(fullCode);
						if (!prList.isEmpty()) {
							prIter = prList.iterator();
							while (prIter.hasNext()) {
								posrecord = (Posrecord) prIter.next();
								if (posrecord.getEndtime() != null) {
									posrecord.setPoscode(newFullCode);
									posrecord.setPosname(fullName);
									posrecordDAO.merge(posrecord);
								}
							}
						}
					}
				}
			}
			break;
		// change the info of dept
		case 9:
			code = request.getParameter("code");
			name = request.getParameter("name");
			deptName = name;
			description = request.getParameter("description");
			cateCode = posArchCode.substring(0, 3);
			organCode = posArchCode.substring(3, 6);
			deptCode = posArchCode.substring(6);
			dept = categoryDAO.getDeptByCatOrganDeptCode(posArchCode);
			if (dept == null) {
				out.print("false");
				return null;
			}
			if (!dept.getCode().equalsIgnoreCase(code)) {
				if (categoryDAO.getDeptByCatOrganDeptCode(cateCode + organCode
						+ code) != null) {
					out.print("false");
					return null;
				}
				dept.setCode(code);
				codeIsChanged = 1;
			}
			if (!dept.getName().equalsIgnoreCase(name)) {
				//判断修改的名字是否已经存在
				Depts tmpDept = categoryDAO.getDeptByCatOrganCodeDeptName(code.substring(0, 6), name);
				if(tmpDept != null){
					out.print("false");
					return null;
				}
				
				deptName = dept.getName();
				dept.setName(name);
				nameIsChanged = 1;
			}
			dept.setDescription(description);
			deptsDAO.merge(dept);
			if ((codeIsChanged == 1) || (nameIsChanged == 1)) {// dept_code or
				// dept_name is
				// changed
				pList = postsDAO.findByCatOrganDeptCode(cateCode, organCode,
						deptCode);
				if (!pList.isEmpty()) {
					iter = pList.iterator();
					while (iter.hasNext()) {
						post = (Posts) iter.next();
						fullCode = post.getFullcode();
						newFullCode = fullCode.substring(0, 6) + code
								+ fullCode.substring(9);
						post.setFullcode(newFullCode);
						post.setDeptCode(code);
						fullName = post.getFullname();
						fullName.replaceAll(deptName, name);
						post.setFullname(fullName);
						postsDAO.merge(post);
						prList = posrecordDAO.findByPoscode(fullCode);
						if (!prList.isEmpty()) {
							prIter = prList.iterator();
							while (prIter.hasNext()) {
								posrecord = (Posrecord) prIter.next();
								if (posrecord.getEndtime() != null) {
									posrecord.setPoscode(newFullCode);
									posrecord.setPosname(fullName);
									posrecordDAO.merge(posrecord);
								}
							}
						}
					}
				}
			}
			break;
		// change the info of post
		case 12:
			code = request.getParameter("code");
			name = request.getParameter("name");
			postName = name;
			description = request.getParameter("description");
			weight = request.getParameter("weight");
			coefficient = request.getParameter("coefficient");
			// req = request.getParameter("pos_req"); delete: urong@9#,
			// 2007.08.13
			a1 = request.getParameter("ability1");
			a2 = request.getParameter("ability2");
			a3 = request.getParameter("ability3");
			a4 = request.getParameter("ability4");
			a5 = request.getParameter("ability5");
			a6 = request.getParameter("ability6");
			cost = request.getParameter("pos_cost");
			relate = request.getParameter("relatedpos");
			cateCode = posArchCode.substring(0, 3);
			organCode = posArchCode.substring(3, 6);
			deptCode = posArchCode.substring(6, 9);
			// postCode = posArchCode.substring(9); delete: urong@9#, 2007.08.13
			post = (Posts) postsDAO.findByFullcode(posArchCode).get(0);
			if (post == null) {
				out.print("false");
				return null;
			}
			if (!post.getCode().equalsIgnoreCase(code)) {
				if (!postsDAO
						.findByFullcode(posArchCode.substring(0, 9) + code)
						.isEmpty()) {
					out.print("false");
					return null;
				}
				post.setCode(code);
				codeIsChanged = 1;
			}
			if (!post.getName().equalsIgnoreCase(name)) {
				postName = post.getName();
				post.setName(name);
				nameIsChanged = 1;
			}
			post.setDescription(description);
			post.setWeight(Short.parseShort(weight));
			post.setCoefficient(Float.parseFloat(coefficient));
			post.setPosAbility1(Short.parseShort(a1));
			post.setPosAbility2(Short.parseShort(a2));
			post.setPosAbility3(Short.parseShort(a3));
			post.setPosAbility4(Short.parseShort(a4));
			post.setPosAbility5(Short.parseShort(a5));
			post.setPosAbility6(Short.parseShort(a6));
			post.setPosCost(cost);
			post.setRelatedPos(relate);
			if ((codeIsChanged == 1) || (nameIsChanged == 1)) {
				fullCode = post.getFullcode();
				newFullCode = fullCode.substring(0, 9) + code;
				post.setFullcode(newFullCode);
				fullName = post.getFullname();
				fullName.replaceAll(postName, name);
				post.setFullname(fullName);
				prList = posrecordDAO.findByPoscode(fullCode);
				if (!prList.isEmpty()) {
					prIter = prList.iterator();
					while (prIter.hasNext()) {
						posrecord = (Posrecord) prIter.next();
						if (posrecord.getEndtime() != null) {
							posrecord.setPoscode(newFullCode);
							posrecord.setPosname(fullName);
							posrecordDAO.merge(posrecord);
						}
					}
				}
			}
			postsDAO.merge(post);
			break;
		default:
			break;
		}
		out.print("true");
		out.flush();
		out.close();
		return null;
	}

	private void setPosRecord(Posts post) {
		String poscode = post.getFullcode();
		String stunum = null;
		Login loginTmp = post.getLogin();
		Login login = null;

		if (login != null) {
			login = loginDAO.findById(loginTmp.getId());
			stunum = login.getStudentnum();
			return;
		}

		List li = posrecordDAO.findByFullCodeStudentNum(poscode, stunum);

		Iterator iter = li.iterator();
		while (iter.hasNext()) {
			Posrecord posre = (Posrecord) iter.next();
			if (posre.getEndtime() == null) {
				String day = DateUtils.getCurrentDay();
				posre.setEndtime(day);
				posrecordDAO.merge(posre);
			}
		}
	}

	/**
	 * This method delete a position object specified by 12 bit full code<br>
	 * written by sillywolf@9#,May,27,2007<br>
	 * 
	 * @param fullcode:the
	 *            fullcode of the position code
	 */
	private void deletePos(String fullcode) {
		/**
		 * should verify whether the length of parameter "fullcode" is 12
		 * because of thelimit of time,this section is left to finish on phase 2
		 * of the project
		 */
		Posts posts = postsDAO.getPosbyCode(fullcode);
		setPosRecord(posts);
		postsDAO.delete(posts);

	}

	/**
	 * The method delete a dept object specified by 9 bit code<br>
	 * written by sillywolf@9#,May 27,2007
	 * 
	 * @param deptcode:the
	 *            perfix of the fullcode which has length 9
	 */
	private void deleteDept(String nine_code) {

		Depts depts = categoryDAO.getDeptByCatOrganDeptCode(nine_code);
		Set posSet = depts.getPostses();
		Iterator iter = posSet.iterator();
		Posts posts = null;
		while (iter.hasNext()) {
			posts = (Posts) iter.next();
			setPosRecord(posts);
			postsDAO.delete(posts);
		}
		deptsDAO.delete(depts);

	}

	/**
	 * The method delete the organ specified by a 6 bit code<br>
	 * written by sillywolf@9#,May,27,2007 modify: urong@9#, 2007.08.12
	 * 
	 * @param six_code
	 */
	private void deleteOrgan(String six_code) {
		Organs organs = categoryDAO.getOrganByCatOrganCode(six_code);
		Set deptSet = organs.getDeptses();
		Iterator deptIter = deptSet.iterator();
		Depts depts = null;

		while (deptIter.hasNext()) {
			depts = (Depts) deptIter.next();
			deleteDept(six_code + depts.getCode());
		}
		organsDAO.delete(organs);
	}

	/**
	 * The method delete a category object specified by a 3 bit code<br>
	 * written by sillywolf@9# modify: urong@9#, 2007.08.12
	 * 
	 * @param three_code
	 */
	private void deleteCate(String three_code) {

		Category cate = categoryDAO.getCategoryByCatCode(three_code);
		Set organSet = cate.getOrganses();
		Iterator organIter = organSet.iterator();
		Organs organ = null;

		while (organIter.hasNext()) {
			organ = (Organs) organIter.next();
			deleteOrgan(three_code + organ.getCode());
		}
		categoryDAO.delete(cate);
	}

	public ModelAndView delNodeServ(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		// urong@9#, 2007.08.13
		// permission validate, only root and xxx101101101
		ArrayList<String> allPos = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.hasRootOrCategoryPermission(allPos)) {
			out.print("false");
			out.close();
			return null;
		}

		int type = Integer.parseInt(request.getParameter("type"));
		String code = request.getParameter("num");
		switch (type) {
		case 1:
			deleteCate(code);
			break;
		case 2:
			deleteOrgan(code);
			break;
		case 3:
			deleteDept(code);
			break;
		case 4:
			deletePos(code);
			break;
		default:
			break;
		}

		out.print("true");
		out.flush();
		out.close();
		return null;
	}

	// for showTreeDetail
	// echo
	@SuppressWarnings("unchecked")
	private JSONObject Postdetail(String posCode) {
		JSONObject jsonObject = new JSONObject();

		List postList = postsDAO.findByFullcode(posCode);
		Posts posts = (Posts) postList.get(0);
		@SuppressWarnings("unused")
		String descrip = posts.getDescription();

		jsonObject.put("code", posts.getCode());
		jsonObject.put("name", posts.getName());
		jsonObject.put("description", posts.getDescription());
		jsonObject.put("weight", posts.getWeight());
		jsonObject.put("coefficient", posts.getCoefficient());
		jsonObject.put("pos_req", posts.getPosReq());

		/*
		 * the ability1 to ability6 which the position requires
		 */
		jsonObject.put("ability1", posts.getPosAbility1());
		jsonObject.put("ability2", posts.getPosAbility2());
		jsonObject.put("ability3", posts.getPosAbility3());
		jsonObject.put("ability4", posts.getPosAbility4());
		jsonObject.put("ability5", posts.getPosAbility5());
		jsonObject.put("ability6", posts.getPosAbility6());

		jsonObject.put("pos_cost", posts.getPosCost());// the postion burden
		jsonObject.put("relatedpos", posts.getRelatedPos());// the positions
		// related to
		// this position
		return jsonObject;
	}

	// for showTreeDetail
	// echo
	@SuppressWarnings("unchecked")
	private JSONObject nodePostdetail(String code) {
		JSONObject jsonObject = new JSONObject();
		int len = code.length();
		if (len == 3) {
			Category cate = (Category) categoryDAO.findByCode(code).get(0);
			jsonObject.put("name", cate.getName());
			jsonObject.put("code", cate.getCode());
			jsonObject.put("description", cate.getDescription());
			return jsonObject;
		} else if (len == 6) {
			Organs o = categoryDAO.getOrganByCatOrganCode(code);
			jsonObject.put("name", o.getName());
			jsonObject.put("code", o.getCode());
			jsonObject.put("description", o.getDescription());
			return jsonObject;
		} else if (len == 9) {
			Depts d = categoryDAO.getDeptByCatOrganDeptCode(code);
			jsonObject.put("name", d.getName());
			jsonObject.put("code", d.getCode());
			jsonObject.put("description", d.getDescription());
			return jsonObject;
		}
		return null;
	}

	/*
	 * 显示一个岗位节点的细节 written by golden & sillywolf@9#
	 */
	public ModelAndView showTreeDetail(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String code = request.getParameter("code");
		JSONObject result;
		if (code.length() == 12) {
			result = Postdetail(code);
		} else {
			result = nodePostdetail(code);
		}
		if (result == null)
			out.print("false");
		else
			out.print(result);
		out.close();

		return null;
	}

	/**
	 * urong@9#,2007.08.13 判断是否是root或者CategoryBoss
	 */
	private boolean hasRootOrCategoryPermission(ArrayList allPos) {
		boolean hasPermission = false;

		Iterator allPosIter = allPos.iterator();
		while (allPosIter.hasNext()) {
			String posCode = (String) allPosIter.next();
			if (PermissionValidate.isRoot(posCode)
					|| PermissionValidate.isCategoryBoss(posCode)) {
				hasPermission = true;
			}
		}

		return hasPermission;
	}

	private boolean hasRootPermission(ArrayList allPos) {
		boolean hasPermission = false;

		Iterator allPosIter = allPos.iterator();
		while (allPosIter.hasNext()) {
			String posCode = (String) allPosIter.next();
			if (PermissionValidate.isRoot(posCode)) {
				hasPermission = true;
			}
		}

		return hasPermission;
	}

	private boolean isValidCode(String s) {
		int len = s.length();
		if (len != 3 && len != 6 && len != 9) {
			return false;
		}
		for (char c : s.toCharArray()) {
			if ((int) c < (int) '0' || (int) c > (int) '9') {
				return false;
			}
		}
		return true;
	}
}
