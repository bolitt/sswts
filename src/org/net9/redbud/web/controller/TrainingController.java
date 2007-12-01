package org.net9.redbud.web.controller;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.net9.redbud.storage.hibernate.applytraining.Applytrainging;
import org.net9.redbud.storage.hibernate.applytraining.ApplytraingingDAO;
import org.net9.redbud.storage.hibernate.login.Login;
import org.net9.redbud.storage.hibernate.login.LoginDAO;
import org.net9.redbud.storage.hibernate.posts.Posts;
import org.net9.redbud.storage.hibernate.posts.PostsDAO;
import org.net9.redbud.storage.hibernate.tokencontainer.Tokencontainer;
import org.net9.redbud.storage.hibernate.tokencontainer.TokencontainerDAO;
import org.net9.redbud.storage.hibernate.training.Training;
import org.net9.redbud.storage.hibernate.training.TrainingDAO;
import org.net9.redbud.storage.hibernate.trainrecord.Trainrecord;
import org.net9.redbud.storage.hibernate.trainrecord.TrainrecordDAO;
import org.net9.redbud.storage.hibernate.userinfo.Userinfo;
import org.net9.redbud.storage.hibernate.userinfo.UserinfoDAO;
import org.net9.redbud.util.DateUtils;
import org.net9.redbud.util.PermissionValidate;
import org.net9.redbud.web.util.JsonUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

public class TrainingController extends RedbudBaseController {

	private String uploadPath = "upload/";

	private TrainingDAO trainingDAO;

	private ApplytraingingDAO applytrainingDAO;

	private PostsDAO postsDAO;

	private LoginDAO loginDAO;

	private UserinfoDAO userinfoDAO;

	private TrainrecordDAO trainrecordDAO;

	private TokencontainerDAO tokencontainerDAO;

	private final static long s_oneYear = 31536000000l; // unit : ms

	private final static long s_oneMonth = 2592000000l; // unit : ms

	public TokencontainerDAO getTokencontainerDAO() {
		return tokencontainerDAO;
	}

	public void setTokencontainerDAO(TokencontainerDAO tokencontainerDAO) {
		this.tokencontainerDAO = tokencontainerDAO;
	}

	public TrainrecordDAO getTrainrecordDAO() {
		return trainrecordDAO;
	}

	public void setTrainrecordDAO(TrainrecordDAO trainrecordDAO) {
		this.trainrecordDAO = trainrecordDAO;
	}

	public UserinfoDAO getUserinfoDAO() {
		return userinfoDAO;
	}

	public void setUserinfoDAO(UserinfoDAO userinfoDAO) {
		this.userinfoDAO = userinfoDAO;
	}

	public ApplytraingingDAO getApplytrainingDAO() {
		return applytrainingDAO;
	}

	public void setApplytrainingDAO(ApplytraingingDAO applytrainingDAO) {
		this.applytrainingDAO = applytrainingDAO;
	}

	public LoginDAO getLoginDAO() {
		return loginDAO;
	}

	public void setLoginDAO(LoginDAO loginDAO) {
		this.loginDAO = loginDAO;
	}

	public PostsDAO getPostsDAO() {
		return postsDAO;
	}

	public void setPostsDAO(PostsDAO postsDAO) {
		this.postsDAO = postsDAO;
	}

	public TrainingDAO getTrainingDAO() {
		return trainingDAO;
	}

	public void setTrainingDAO(TrainingDAO trainingDAO) {
		this.trainingDAO = trainingDAO;
	}

	/**
	 * @author urong@9#, 2007.07.28 获得某个培训的详情
	 */
	public ModelAndView showTrainingDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			ParseException {
		PrintWriter out = response.getWriter();

		int trainingId = Integer.parseInt(request.getParameter("id"));
		try {
			Training trainingDetail = trainingDAO.findById(trainingId);

			JSONObject jsonObject = JsonUtils
					.getJsonObjectForTrainingDetail(trainingDetail);

			// 加入发布培训的岗位的信息
			List posList = postsDAO
					.findByFullcode(trainingDetail.getPostcode());
			jsonObject.put("posName", ((Posts) posList.get(0)).getFullname());

			// 加入时间信息
			long currentTime = System.currentTimeMillis();
			String starttime = trainingDetail.getStarttime();

			boolean canDelete = true;
			if (currentTime >= Long.parseLong(starttime))
				canDelete = false;
			else
				canDelete = true;
			jsonObject.put("canDelete", canDelete);

			// 加入培训相关文件的信息：filelist
			String fileList = trainingDetail.getFilelist();
			JSONArray jsonArray = getFileNameFromFileList(fileList);
			jsonObject.put("filelist", jsonArray);
			
			// 加入是否产生过密码条的信息
			jsonObject.put("isTokened", trainingDetail.getIstokened());

			out.print(jsonObject);
		} catch (NullPointerException e) {
			e.printStackTrace();
			out.write("NULL");
		}
		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author urong@9#, 2007.07.28 获得可以审批的培训
	 */
	public ModelAndView getApprovableTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// get all own positions
		ArrayList<String> allpos = (ArrayList<String>) (request.getSession()
				.getAttribute("postList"));

		// get the approvable training of each position, applystatus = "0"
		JSONArray approvableTraining = this.getAllApprovedTraining(allpos, 1);

		// out print information
		PrintWriter out = response.getWriter();
		out.print(approvableTraining);
		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author urong@9#,2007.07.28 得到申请的培训的详情
	 */
	public ModelAndView showApplyTrainingDetail(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// get Applytraining
		String applyTraingingIdStr = request.getParameter("applyTraingingId");
		int applyTrainingId = Integer.parseInt(applyTraingingIdStr);
		Applytrainging applyTraining = applytrainingDAO
				.findById(applyTrainingId);
		if (applyTraining == null) {// should not happen in real system
			response.getWriter().print("NULL");
			return null;
		}

		// get student informantion
		String studentNum = applyTraining.getStudentnum();

		List studentList = loginDAO.findByStudentnum(studentNum);
		if (studentList.isEmpty()) {// no student applied this training,
			// shouldn't happen in real system
			response.getWriter().print("NULL");
			return null;
		}

		// get training information
		int trainingId = applyTraining.getTrainingid();
		Training training = trainingDAO.findById(trainingId);

		// set JSONObject: student information, training information, apply
		// reason, apply time, apply status
		JSONObject jsonObject = JsonUtils.getJsonObjectForApplyTrainingDetail(
				((Login) studentList.get(0)).getUserinfo(), training,
				applyTraining);

		// 加入培训相关文件的信息：filelist
		String fileList = training.getFilelist();
		JSONArray jsonArray = getFileNameFromFileList(fileList);
		jsonObject.put("filelist", jsonArray);

		// out print JSONObject
		PrintWriter out = response.getWriter();
		out.print(jsonObject);
		out.flush();
		out.close();

		return null;
	}

	/**
	 * *************************************new design at
	 * 2007.08.06********************************************
	 */

	/**
	 * @author written by sillywolf@9#, modify: urong@9#, 2007.08.06
	 *         获得目前可以参加的所有培训，包括可申请的和公开的
	 */
	public ModelAndView getAllAvailableTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// get student number from session
		String studentNumber = (String) request.getSession().getAttribute(
				"studentnum");

		// get all training
		List trainingList = trainingDAO.findAll();
		Iterator trainingListIter = trainingList.iterator();

		// get currentDay
		long currentDay = System.currentTimeMillis();

		// get all available training
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayPublic = new JSONArray();
		JSONArray jsonArrayPrivate = new JSONArray();
		while (trainingListIter.hasNext()) {
			Training training = (Training) trainingListIter.next();

			long deadline = Long.parseLong(training.getDeadline());
			long starttime = Long.parseLong(training.getStarttime());
			if (starttime > currentDay) {// 培训还没有开始，对于公开培训来说应该以开始时间作为界限
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("id", training.getId());
				jsonObject.put("name", training.getName());
				jsonObject.put("deadline", DateUtils.getSimpleFormatTime(Long
						.parseLong(training.getDeadline())));
				int ispublic = training.getIspublic();
				if (ispublic == 0) {// public
					jsonArrayPublic.add(jsonObject);
				} else if (ispublic == 1 && deadline > currentDay) {// need
					// apply，对于需申请的培训来说应该以申请结束时间作为界限
					// 如果已经申请过，则加入已申请标记
					List appTrainList = applytrainingDAO
							.findByTrainingid(training.getId());
					Iterator appTrainListIter = appTrainList.iterator();
					String hasApplied = "false";
					while (appTrainListIter.hasNext()) {
						Applytrainging appTrain = (Applytrainging) appTrainListIter
								.next();
						if (appTrain.getStudentnum().equals(studentNumber)
								&& (!appTrain.getApplystatus().equals("2"))) {
							hasApplied = "true";
						}
					}
					jsonObject.put("hasApplied", hasApplied);
					jsonArrayPrivate.add(jsonObject);
				} else {
					// do nothing
				}
			}
		}
		jsonArray.add(jsonArrayPublic);
		jsonArray.add(jsonArrayPrivate);

		// out print
		PrintWriter out = response.getWriter();
		out.print(jsonArray);
		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author written by sillywolf@9#, modify: urong@9#, 2007.08.09 申请培训的具体动作
	 */
	public ModelAndView applyTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		try {
			String studentnum = (String) request.getSession().getAttribute(
					"studentnum");
			String applyReason = request.getParameter("applyReason");
			String trainid = request.getParameter("trainid");

			Applytrainging apptr = new Applytrainging();
			apptr.setStudentnum(studentnum);
			apptr.setApplyreason(applyReason);
			apptr.setTrainingid(Integer.parseInt(trainid));
			apptr.setApplytime("" + System.currentTimeMillis());
			apptr.setApplystatus("0");

			applytrainingDAO.save(apptr);

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
	 * @author written by sillywolf@9#, modify: urong@9#, 2007.08.06 获得已经申请的培训
	 */
	public ModelAndView getAppliedTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// get apply training
		String studentnum = (String) request.getSession().getAttribute(
				"studentnum");
		List applyTrainingList = applytrainingDAO.findByStudentnum(studentnum);
		Iterator applyTrainingListIter = applyTrainingList.iterator();

		// get current time
		long currentTime = System.currentTimeMillis();

		JSONArray jsonArray = new JSONArray();
		while (applyTrainingListIter.hasNext()) {
			Applytrainging applytraining = (Applytrainging) applyTrainingListIter
					.next();

			long applytime = Long.parseLong(applytraining.getApplytime());

			// 获得一年的所有申请
			if (currentTime - applytime <= TrainingController.s_oneYear) {
				JSONObject jsonObject = new JSONObject();
				Training training = trainingDAO.findById(applytraining
						.getTrainingid());
				jsonObject.put("applystatus", applytraining.getApplystatus());
				int status = Integer.parseInt(applytraining.getApplystatus());
				if (status == 3) {
					jsonObject.put("name", applytraining.getTrainingname());
					jsonObject.put("starttime", "00000000");
					jsonObject.put("endtime", "00000000");
				} else {
					jsonObject.put("name", training.getName());
					jsonObject.put("starttime", DateUtils.getFormatTime(Long
							.parseLong(training.getStarttime())));
					jsonObject.put("endtime", DateUtils.getFormatTime(Long
							.parseLong(training.getEndtime())));
				}
				jsonArray.add(jsonObject);
			}
		}

		PrintWriter out = response.getWriter();
		out.print(jsonArray);
		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author DennisZz@9#, 2007.08.09 认证培训的具体动作
	 */
	public ModelAndView authenticateTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// get token
		try {
			String token = request.getParameter("token");
			String trainingId = request.getParameter("trainingid");
			Userinfo ui = (Userinfo) request.getSession().getAttribute(
					"userinfo");
			String loginId = ui.getId() + "";
			int trainId = Integer.parseInt(trainingId);
			PrintWriter out = response.getWriter();

			// 检查申请是否被拒绝
			String studentNum = (String) request.getSession().getAttribute(
					"studentnum");
			List applyTrainingList = applytrainingDAO
					.findByTrainingIdAndStudentNum(trainId, studentNum);
			if (applyTrainingList.size() == 0) {
				out.write("false");
				return null;
			} else {
				boolean canAuthenticate = false;
				Iterator atlIter = applyTrainingList.iterator();
				while (atlIter.hasNext()) {
					Applytrainging applyTraining = (Applytrainging) atlIter
							.next();
					if (!applyTraining.getApplystatus().equals("2")) {// 不是拒绝
						canAuthenticate = true;
						break;
					}
				}
				if (canAuthenticate == false) {
					out.write("false");
					return null;
				}
			}

			List<Tokencontainer> tokenList = tokencontainerDAO
					.findByObjectid(trainId);
			if (tokenList.size() == 0) {
				out.write("false");

			} else {
				int flag = 0;
				for (int i = 0; i < tokenList.size(); i++) {
					Tokencontainer tempTokencontainer = tokenList.get(i);
					if (tempTokencontainer.getPassword().equals(token)) {
						tokencontainerDAO.delete(tempTokencontainer);
						Trainrecord trainrecord = new Trainrecord();
						trainrecord.setTrainid(trainingId);
						trainrecord.setLoginid(loginId);
						trainrecordDAO.save(trainrecord);
						out.write("true");
						flag = 1;
						break;
					}
				}
				if (flag == 0) {
					out.write("false");
				}
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author written by sillywolf@9#, modify: urong@9#, 2007.08.07 发布培训的具体动作
	 */
	public ModelAndView postTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		// 权限控制
		ArrayList<String> allPos = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.hasPermission(allPos)) {
			out.print("false");
			out.flush();
			out.close();
			return null;
		}

		try {
			Training train = new Training();
			String name = request.getParameter("name");
			String postCode = request.getParameter("postcode_selected");
			String ispublic = request.getParameter("ispublic");
			String starttime = request.getParameter("starttime");
			String endtime = request.getParameter("endtime");
			String deadline = request.getParameter("deadline");

			String description = request.getParameter("description");
			String fileList = request.getParameter("filelist");
			String currentDay = "" + System.currentTimeMillis();

			train.setName(name);
			train.setPostcode(postCode);
			train.setIspublic(Integer.parseInt(ispublic));
			train.setStarttime("" + DateUtils.getParseTime(starttime));
			train.setEndtime("" + DateUtils.getParseTime(endtime));
			train.setDeadline("" + DateUtils.getParseTime(deadline));
			train
					.setValidtime(""
							+ (DateUtils.getParseTime(endtime) + TrainingController.s_oneMonth));
			train.setDescription(description);
			train.setPosttime(currentDay);
			train.setIstokened(0);// 默认为没有产生过token
			train.setFilelist(fileList);
			trainingDAO.save(train);

			out.write("true");
		} catch (Exception e) {
			e.printStackTrace();
			out.write("false");
		}

		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author urong@9#, 2007.08.07 审批培训的具体动作
	 */
	public ModelAndView approveTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		// permission validate
		ArrayList<String> allPos = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.hasPermission(allPos)) {
			out.print("false");
			out.flush();
			out.close();
			return null;
		}

		try {
			// get applyTrainingId and approveStatus
			String applyTrainingId = request.getParameter("applyTrainingId");
			String approveStatus = request.getParameter("approveStatus");

			// check out whether the user is the training publisher
			Applytrainging applytraining = applytrainingDAO.findById(Integer
					.parseInt(applyTrainingId));
			int trainingId = applytraining.getTrainingid();
			if (!this.checkTrainingOwnership(trainingId, allPos)) {
				out.print("invalid");
				out.flush();
				out.close();
				return null;
			}

			// if approved
			if (approveStatus.equals("true")) {
				// set applyStatus "true": 1
				applytraining.setApplystatus("1");
			}
			// else, not approve
			else {
				// set applyStatus "false": 2
				applytraining.setApplystatus("2");
			}
			applytrainingDAO.update(applytraining);

			// return running status
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
	 * @author written by sillywolf@9#, modify: urong@9#, 2007.08.07 更新一个培训的信息
	 */
	public ModelAndView updateTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		// 权限控制
		ArrayList<String> allPos = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.hasPermission(allPos)) {
			out.print("false");
			out.flush();
			out.close();
			return null;
		}

		try {
			Training train = new Training();
			String id = request.getParameter("id");
			String name = request.getParameter("name");
			String postCode = request.getParameter("postcode_selected");
			String ispublic = request.getParameter("ispublic");
			String starttime = request.getParameter("starttime");
			String endtime = request.getParameter("endtime");
			String deadline = request.getParameter("deadline");
			String description = request.getParameter("description");
			String fileList = request.getParameter("filelist");

			// check out if the user is the training publisher && if the
			// postCode is the user's
			if (!this.checkTrainingOwnership(Integer.parseInt(id), allPos)
					|| !allPos.contains(postCode)) {
				out.print("invalid");
				out.flush();
				out.close();
				return null;
			}

			train.setId(Integer.parseInt(id));
			train.setName(name);
			train.setPostcode(postCode);
			train.setIspublic(Integer.parseInt(ispublic));
			train.setStarttime("" + DateUtils.getParseTime(starttime));
			train.setEndtime("" + DateUtils.getParseTime(endtime));
			train.setDeadline("" + DateUtils.getParseTime(deadline));
			train
					.setValidtime(""
							+ (DateUtils.getParseTime(endtime) + TrainingController.s_oneMonth));
			train.setDescription(description);
			train.setPosttime("" + System.currentTimeMillis());
			// trainingDAO.merge(train);, urong@9#, 2007.08.13
			Training training = trainingDAO.findById(Integer.parseInt(id));
			train.setIstokened(training.getIstokened());

			train.setFilelist(fileList);

			trainingDAO.update(train);

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
	 * @author written by sillywolf@9#, modify: urong@9#, 2007.08.07 删除一个培训
	 */
	public ModelAndView deleteTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		ArrayList<String> allPos = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.hasPermission(allPos)) {
			out.print("false");
			out.flush();
			out.close();
			return null;
		}

		try {
			String id = request.getParameter("id");
			Training training = trainingDAO.findById(Integer.parseInt(id));

			// check out if the user is the training publisher
			if (!this.checkTrainingOwnership(Integer.parseInt(id), allPos)) {
				out.print("invalid");
				out.flush();
				out.close();
				return null;
			}

			List<Applytrainging> list = applytrainingDAO
					.findByTrainingid(Integer.parseInt(id));
			for (int i = 0; i < list.size(); i++) {
				Applytrainging apply = list.get(i);
				// "3" : deleted
				apply.setApplystatus("3");
				apply.setTrainingname(training.getName());
				applytrainingDAO.update(apply);
			}

			trainingDAO.delete(training);

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
	 * @author written by sillywolf@9#, modify: urong@9#, 2007.08.12
	 *         获得自己发布过的所有的培训，分为已经完成的培训和未完成的培训
	 */
	public ModelAndView getAllPostedTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		JSONObject totleTraining = new JSONObject();
		JSONArray pastTraining = new JSONArray();
		JSONArray currentTraining = new JSONArray();

		ArrayList<String> post = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		Iterator postIter = post.iterator();
		while (postIter.hasNext()) {
			String postcode = (String) postIter.next();
			if (!PermissionValidate.hasPostTrainingPermission(postcode)) {
				continue;
			}

			Posts posts = (Posts) postsDAO.findByFullcode(postcode).get(0);
			String name = posts.getFullname();

			JSONObject jsonObjectPast = new JSONObject();
			JSONObject jsonObjectCurrent = new JSONObject();
			jsonObjectPast.put("name", name);
			jsonObjectCurrent.put("name", name);

			JSONArray jsonArrayPast = new JSONArray();
			JSONArray jsonArrayCurrent = new JSONArray();

			long currentTime = System.currentTimeMillis();
			List<Training> list = trainingDAO.findByPostcode(postcode);
			Iterator listIter = list.iterator();
			while (listIter.hasNext()) {
				Training train = (Training) listIter.next();

				JSONObject js = new JSONObject();
				js.put("id", train.getId());
				js.put("name", train.getName());
				js.put("posttime", DateUtils.getSimpleFormatTime(Long
						.parseLong(train.getPosttime())));

				long endTime = Long.parseLong(train.getEndtime());
				if (currentTime >= endTime) {// past training
					jsonArrayPast.add(js);
				} else {// current training
					jsonArrayCurrent.add(js);
				}
			}

			jsonObjectPast.put("children", jsonArrayPast);
			jsonObjectCurrent.put("children", jsonArrayCurrent);

			pastTraining.add(jsonObjectPast);
			currentTraining.add(jsonObjectCurrent);
		}

		totleTraining.put("currentTraining", currentTraining);
		totleTraining.put("pastTraining", pastTraining);

		out.print(totleTraining);
		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author urong@9#, 2007.08.07 获得一个培训的申请者的信息
	 */
	public ModelAndView getApprovedTrainingInfo(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		String trainingId = request.getParameter("id");
		Training training = (Training) trainingDAO.findById(Integer
				.parseInt(trainingId));

		// check out if the user is the training publisher
		ArrayList<String> allPos = (ArrayList<String>) request.getSession()
				.getAttribute("postList");
		if (!this.checkTrainingOwnership(Integer.parseInt(trainingId), allPos)) {
			out.print("invalid");
			out.flush();
			out.close();
			return null;
		}

		JSONObject jsonObject = JsonUtils
				.getJsonObjectForTrainingDetail(training);
		JSONArray jsonArray = new JSONArray();
		List appTrainList = applytrainingDAO.findByTrainingid(trainingId);
		Iterator appTrainListIter = appTrainList.iterator();
		while (appTrainListIter.hasNext()) {
			Applytrainging applytraining = (Applytrainging) appTrainListIter
					.next();
			String studentNum = applytraining.getStudentnum();
			Login login = (Login) (loginDAO.findByStudentnum(studentNum).get(0));
			Userinfo userinfo = login.getUserinfo();

			String studentName = userinfo.getName();

			String applyStatus = applytraining.getApplystatus();
			
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("studentNum", studentNum);
			jsonObj.put("studentName", studentName);
			jsonObj.put("applyStatus", applyStatus);

			jsonArray.add(jsonObj);
		}

		jsonObject.put("applicant", jsonArray);
		out.print(jsonObject);
		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author written by DennisZz, 2007.08.12, modify: urong@9#, 2007.08.14
	 *         获得目前所有可以使用密码条验证的培训，也就是已经产生过密码条并且还没有过期的培训
	 */
	public ModelAndView getTrainingsForAuthentication(
			HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();
		try {
			JSONArray trainJSONArray = new JSONArray();
			long currentTime = System.currentTimeMillis();

			String currentTimeString = currentTime + "";
			List trainingList = trainingDAO
					.findTheTrainingByCurrentTime(currentTimeString);
			if (trainingList.size() == 0) {
				out.write("[]");
			}
			for (int i = 0; i < trainingList.size(); i++) {
				JSONObject trainJSONObject = new JSONObject();
				Training tempTraining = (Training) trainingList.get(i);
				String postCode = tempTraining.getPostcode();
				List tempPostList = postsDAO.findByFullcode(postCode);
				if (tempPostList.size() == 0) {
					continue;
				}

				// 如果该training没有产生过token在不能认证
				int istokened = tempTraining.getIstokened();
				if (istokened == 0) {
					continue;
				}

				// 如果已经认证则不能再次认证
				List trainingRecordList = trainrecordDAO
						.findByTrainid(tempTraining);
				if (trainingRecordList.size() != 0) {
					continue;
				}

				Posts tempPost = (Posts) tempPostList.get(0);

				trainJSONObject.put("trainingId", tempTraining.getId());
				trainJSONObject.put("trainingName", tempTraining.getName());
				trainJSONObject.put("publishPostName", tempPost.getFullname());
				trainJSONObject.put("startTime", DateUtils.getFormatTime(Long
						.parseLong(tempTraining.getStarttime())));
				trainJSONObject.put("endTime", DateUtils.getFormatTime(Long
						.parseLong(tempTraining.getEndtime())));
				trainJSONArray.add(trainJSONObject);
			}
			out.write(trainJSONArray.toString());
		} catch (Exception e) {
			e.printStackTrace();
			out.print("[]");
		}
		out.flush();
		out.close();
		return null;
	}
	
	/**
	 * @author urong@9#, 2007.08.13 获得自己已经认证过的培训
	 */
	public ModelAndView getAuthenticatedTraining(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		try {
			// get student number from session
			String studentNumber = (String) request.getSession().getAttribute(
					"studentnum");

			// get authenticated training from trainrecord table via student
			// number
			// get loginId
			Login login = (Login) loginDAO.findByStudentnum(studentNumber).get(
					0);
			int loginId = login.getId();

			JSONArray jsonArray = new JSONArray();
			List trainRecList = trainrecordDAO.findByLoginid(loginId);
			Iterator trainRecListIter = trainRecList.iterator();
			while (trainRecListIter.hasNext()) {
				// get trainingId
				Trainrecord trainRec = (Trainrecord) trainRecListIter.next();
				int trainingId = Integer.parseInt(trainRec.getTrainid());
				Training training = trainingDAO.findById(trainingId);

				String postCode = training.getPostcode();
				List posList = postsDAO.findByFullcode(postCode);
				if (posList.size() == 0) {
					continue;
				}
				Posts pos = (Posts) posList.get(0);

				JSONObject jsonObject = new JSONObject();
				jsonObject.put("trainingName", training.getName());
				jsonObject.put("publishPostName", pos.getFullname());
				jsonObject.put("startTime", DateUtils.getFormatTime(Long
						.parseLong(training.getStarttime())));
				jsonObject.put("endTime", DateUtils.getFormatTime(Long
						.parseLong(training.getEndtime())));
				jsonObject.put("trainingId", trainingId);

				jsonArray.add(jsonObject);
			}

			// out print
			out.print(jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("[]");
		}

		out.flush();
		out.close();

		return null;
	}

	/**
	 * @author urong and DennisZz@9#, 2007.08.06 培训发布者获得培训的密码条
	 */
	public ModelAndView getTrainingToken(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		try {
			// get trainingId & tokenCount & type
			// type : 判断是产生新的token还是拿以前的产生的
			// type = "0" : 产生的新的token，JSONArray形式传回表现层
			// type = "1" : 从数据库获得以前的token，JSONArray形式传回表现层
			// type = "2" : 从数据库获得以前的token，以供下载，每条后面使用\r\n，使用String传回表现层
			String trainingId = request.getParameter("id");
			String tokenCount = request.getParameter("count");
			String type = request.getParameter("type");

			// get currentTime & deadline & starttime
			// deadline: 申请结束时间 starttime:培训开始时间
			long currentTime = System.currentTimeMillis();
			Training training = trainingDAO.findById(Integer
					.parseInt(trainingId));

			// check out if the user is the training publisher
			ArrayList<String> allPos = (ArrayList<String>) request.getSession()
					.getAttribute("postList");
			if (!this.checkTrainingOwnership(Integer.parseInt(trainingId),
					allPos)) {
				out.print("invalid");
				out.flush();
				out.close();
				return null;
			}

			// 设置token的文件名
			String fileName;
			Browser browser = getBrowserInfo(request);
			if (browser == Browser.IE || browser == Browser.IE6
					|| browser == Browser.IE7) {
				fileName = URLEncoder.encode(training.getName(), "UTF-8")
						.replace("+", "%20")
						+ ".pwd.txt";
			} else {
				fileName = new String(training.getName().getBytes("UTF-8"),
						"ISO-8859-1")
						+ ".pwd.txt";
			}
			response.addHeader("Content-Disposition", "attachment; filename=\""
					+ fileName + "\"");
			response.setContentType("application/octet-stream");

			long deadline = Long.parseLong(training.getDeadline());
			long starttime = Long.parseLong(training.getStarttime());

			// if deadline < currentTime < starttime, then allow to create token
			if ((currentTime > deadline) && (currentTime < starttime)) {

				JSONArray jsonArray = new JSONArray();
				String allToken = "";

				// modify : urong@9#, 2007.08.13
				// type = "0" : 产生的新的token，JSONArray形式传回表现层
				if (type.equals("0")) {
					int trainId = Integer.parseInt(trainingId);
					List toBeDeleted = tokencontainerDAO
							.findByObjectid(trainId);
					if (toBeDeleted != null) {
						for (int i = 0; i < toBeDeleted.size(); i++) {
							tokencontainerDAO
									.delete((Tokencontainer) toBeDeleted.get(i));
						}
					}

					// modify : DennisZz@9#, 2007.09.10
					// 恶意公鸡！！
					if (tokenCount.equals("")) {
						out.print("invalid");
						out.flush();
						out.close();
						return null;
					}
					int count = Integer.parseInt(tokenCount);
					if ((count < 1) && (count > 1000)) {
						out.print("invalid");
						out.flush();
						out.close();
						return null;
					}

					HashSet<String> tokenSet = generateTokenSet(count, trainId);
					Iterator tokenIt = tokenSet.iterator();
					if (!tokenIt.hasNext()) {
						out.write("[]");
						out.close();
					}
					while (tokenIt.hasNext()) {
						String tempString = (String) tokenIt.next();
						Tokencontainer tokencontainer = new Tokencontainer();
						tokencontainer.setObjectid(trainId);
						tokencontainer.setPassword(tempString);
						tokencontainerDAO.save(tokencontainer);
						jsonArray.add(tempString);
					}

					// 设置training表的istokened字段为1，表示已经产生token
					training.setIstokened(1);
					trainingDAO.update(training);
				}
				// type = "1" : 从数据库获得以前的token，JSONArray形式传回表现层
				// type = "2" : 从数据库获得以前的token，以供下载，每条后面使用\r\n，使用String传回表现层
				else if (type.equals("1") || type.equals("2")) {
					List tokenContainerList = tokencontainerDAO
							.findByObjectid(Integer.parseInt(trainingId));
					Iterator tokenContainerListIter = tokenContainerList
							.iterator();

					while (tokenContainerListIter.hasNext()) {
						Tokencontainer tokenContainer = (Tokencontainer) tokenContainerListIter
								.next();
						String token = tokenContainer.getPassword();
						if (type.equals("1")) {
							jsonArray.add(token);
						} else {
							token += System.getProperty("line.separator");
							allToken += token;
						}
					}
				} else {
					return null;
				}

				if (type.equals("2")) {
					out.print(allToken);
				} else {
					out.write(jsonArray.toString());
				}

				out.close();
			} else {
				out.write("1");
				out.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public ModelAndView upload(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest
				.getFile("training-upload-file");
		String fileName = System.currentTimeMillis() + "-"
				+ file.getOriginalFilename();
		FileOutputStream fout = new FileOutputStream(System
				.getProperty("redbud.servlet.home")
				+ "/../" + uploadPath + fileName, false);
		byte[] buf = new byte[1024 * 1024];
		InputStream is = file.getInputStream();
		int sz;
		while ((sz = is.read(buf)) != -1) {
			fout.write(buf, 0, sz);
		}
		is.close();
		fout.close();
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		JSONObject json = new JSONObject();
		json.put("name", fileName);
		json.put("path", uploadPath + fileName);
		out
				.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\""
						+ " \"http://www.w3.org/TR/html4/loose.dtd\">"
						+ "<html>	<head>		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">"
						+ "<title>Insert title here</title>	</head>	<body>");
		out.print(json);
		out.println("</body></html>");
		out.flush();
		return null;
	}

	/**
	 * Check if has permission to Post/Update/Delete/Approve trainings.
	 * 
	 * @author urong@9#, 2007.07.29 判断是否有发布/更新/删除/审批 培训的权力
	 * @param allPos
	 *            岗位号的列表
	 * @return 如果有权力则返回true，否则返回false
	 */
	private boolean hasPermission(final ArrayList<String> allPos) {
		final Iterator posIter = allPos.iterator();
		boolean hasPermission = false;
		while (posIter.hasNext()) {
			final String posCode = (String) posIter.next();
			if (PermissionValidate.hasPostTrainingPermission(posCode) == true) {
				hasPermission = true;
				break;
			}
		}

		return hasPermission;
	}

	/**
	 * @author DennisZz, 2007.08.07 产生密码条
	 * @param count
	 *            需要产生的密码条的数量
	 * @param trainingId
	 *            培训的ID
	 * @return 一个装有密码条HashSet
	 */
	// by DennisZz, August 7th,2007.
	private HashSet<String> generateTokenSet(int count, int trainingId) {
		final int tokenLength = 10;
		HashSet<String> tokenSet = new HashSet<String>();
		String charFactory = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
		int tempLength = charFactory.length();
		for (int i = 0; i < count; i++) {
			String tempToken = new String();
			do {
				tempToken = "";
				for (int j = 0; j < tokenLength; j++) {
					double tempDouble = Math.random() * tempLength;
					int tempInt = (int) Math.floor(tempDouble);
					tempToken += charFactory.charAt(tempInt);
				}
				tempToken = trainingId + tempToken;
			} while (!tokenSet.add(tempToken));
		}
		return tokenSet;
	}

	/**
	 * @author urong@9#, 2007.08.09 获得所有的已经审批过的培训，包括所有的审批状态 "0" -
	 *         未审批，"1"-同意，"2"-不同意，"3"-已经删除
	 * @param allpos
	 *            岗位号的列表
	 * @param mark
	 *            如果是1，则只返回没有审批的培训；如果是2，则返回所有培训
	 */
	private JSONArray getAllApprovedTraining(ArrayList allpos, int mark) {
		// get the approvable training of each position
		JSONArray approvedTraining = new JSONArray();
		for (int posIndex = 0; posIndex < allpos.size(); posIndex++) {
			String posCode = (String) allpos.get(posIndex);
			if (!PermissionValidate.hasPostTrainingPermission(posCode)) {
				continue;
			}
			List trainingList = trainingDAO.findByPostcode(posCode);

			// get approvable trainings from applytraining table via trainingIds
			Iterator trainListIter = trainingList.iterator();
			// get approvable trainings via each trainingId
			JSONArray appTrainOfEachId = new JSONArray();
			while (trainListIter.hasNext()) {
				// get trainingId via training
				Training training = (Training) trainListIter.next();
				int trainingId = training.getId();

				// get approvable training via trainingId
				List approvableTrainingList = applytrainingDAO
						.findByTrainingid(trainingId);

				// put information into JSONArray
				Iterator appTrainListIter = approvableTrainingList.iterator();
				while (appTrainListIter.hasNext()) {
					Applytrainging applytraining = (Applytrainging) appTrainListIter
							.next();

					// if the training is not approved then put information inot
					// JSONObject
					String approveStatus = applytraining.getApplystatus();

					if ((mark == 1) && !approveStatus.equals("0")) {
						continue;
					} else if (mark == 2) {
						// do nothing
					}

					JSONObject infoOfAAppTrain = new JSONObject();
					infoOfAAppTrain.put("id", applytraining.getId());
					infoOfAAppTrain.put("studentnum", applytraining
							.getStudentnum());
					infoOfAAppTrain.put("applyreason", applytraining
							.getApplyreason());
					infoOfAAppTrain.put("applytime", DateUtils
							.getSimpleFormatTime(Long.parseLong(applytraining
									.getApplytime())));
					infoOfAAppTrain.put("trainingname", training.getName());

					// add to JSONArray
					appTrainOfEachId.add(infoOfAAppTrain);
				}
			}

			// put approvable training information of a position into JSONObject
			JSONObject appTrainInfoOfAPos = new JSONObject();
			appTrainInfoOfAPos.put("children", appTrainOfEachId);
			Posts pos = (Posts) (postsDAO.findByFullcode(posCode).get(0));
			appTrainInfoOfAPos.put("name", pos.getFullname());
			approvedTraining.add(appTrainInfoOfAPos);
		}

		return approvedTraining;
	}

	// 判断培训的所有权，如果有的则返回true
	private boolean checkTrainingOwnership(int trainingId,
			ArrayList<String> allPos) {
		Training training = trainingDAO.findById(trainingId);
		String posCodeInTraining = training.getPostcode();

		return allPos.contains(posCodeInTraining);
	}

	public void setUploadPath(String uploadPath) {
		this.uploadPath = uploadPath;
	}

	// 把从数据库中得到的fileList（一个String）用JSONArray封装，JSONArray中的没有元素是String（一个文件名）
	private JSONArray getFileNameFromFileList(String fileList) {
		JSONArray jsonArray = new JSONArray();

		if (fileList == null || fileList.length() == 0) {
			return jsonArray;
		}

		String split = ":";
		String[] fileNames = fileList.split(split);
		for (int i = 0; i < fileNames.length; i++) {
			jsonArray.add(fileNames[i]);
		}

		return jsonArray;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(MimeUtility.encodeText("程昶"));
	}
}