package org.net9.redbud.web.util;

import org.json.simple.JSONObject;
import org.net9.redbud.storage.hibernate.applytraining.Applytrainging;
import org.net9.redbud.storage.hibernate.category.Category;
import org.net9.redbud.storage.hibernate.depts.Depts;
import org.net9.redbud.storage.hibernate.organs.Organs;
import org.net9.redbud.storage.hibernate.posts.Posts;
import org.net9.redbud.storage.hibernate.training.Training;
import org.net9.redbud.storage.hibernate.userinfo.Userinfo;
import org.net9.redbud.util.DateUtils;
import org.net9.redbud.util.DepartmentUtils;
import org.net9.redbud.util.RegionUtils;

public final class JsonUtils {

	// by Apache
	@SuppressWarnings("unchecked")
	public static JSONObject getJsonObjectForUserinfo(Userinfo info) {
		JSONObject json = new JSONObject();
		json.put("name", info.getName());
		json.put("grade", info.getGrade());
		json.put("gender", info.getGender().equals("0") ? "女" : "男");
		String nation = info.getNation();
		String[] ss = nation.split("_");
		json.put("region", RegionUtils.getInstance().getFullRegion(ss[0]));
		if (ss.length == 2) {
			json.put("nation", ss[1]);
		}
		json.put("department", DepartmentUtils.getInstance().getDepartment(
				info.getDepartment()));
		json.put("classnum", info.getClassnum());
		json.put("political", info.getPolitical());
		json.put("tel", info.getTel());
		json.put("mobile", info.getMobile());
		json.put("email", info.getEmail());
		json.put("address", info.getAddress());
		json.put("ability1", info.getAbility1());
		json.put("ability2", info.getAbility2());
		json.put("ability3", info.getAbility3());
		json.put("ability4", info.getAbility4());
		json.put("ability5", info.getAbility5());
		json.put("ability6", info.getAbility6());
		json.put("postRec", info.getPostRec());
		json.put("trainingRec", info.getTrainingRec());
		json.put("activityRec", info.getActivityRec());
		json.put("specialty", info.getSpecialty());
		return json;
	}

	@SuppressWarnings("unchecked")
	public static JSONObject getJsonObjectForPositionDetail(boolean ifApplyed,
			Posts post, Depts dept, Organs organ, Category cate) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("ifapplyed", ifApplyed);
		jsonObject.put("poscode", post.getFullcode());
		jsonObject.put("category", cate.getName());// the categoty the position
		// belongs
		jsonObject.put("organs", organ.getName());// the organs
		jsonObject.put("depts", dept.getName());// the depts
		jsonObject.put("posts", post.getName());// the position name
		jsonObject.put("pos_Descrip", post.getDescription());// the position
		// description
		jsonObject.put("fullname", post.getFullname());// the full name of the
		// position
		jsonObject.put("pos_require", post.getPosReq());// the position
		// requirment
		/*
		 * the ability1 to ability6 which the position requires
		 */
		jsonObject.put("pos_ability1", post.getPosAbility1());
		jsonObject.put("pos_ability2", post.getPosAbility2());
		jsonObject.put("pos_ability3", post.getPosAbility3());
		jsonObject.put("pos_ability4", post.getPosAbility4());
		jsonObject.put("pos_ability5", post.getPosAbility5());
		jsonObject.put("pos_ability6", post.getPosAbility6());
		jsonObject.put("pos_cost", post.getPosCost());// the postion burden
		jsonObject.put("pos_relate", post.getRelatedPos());// the positions
		// related to this
		// position
		return jsonObject;
	}

	// by echo
	@SuppressWarnings("unchecked")
	public static JSONObject getJsonObjectForPosts(Posts post) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("posname", post.getFullname());
		jsonObject.put("descrip", post.getDescription());
		jsonObject.put("posrequire", post.getPosReq());
		jsonObject.put("pos_ability1", post.getPosAbility1());
		jsonObject.put("pos_ability2", post.getPosAbility2());
		jsonObject.put("pos_ability3", post.getPosAbility3());
		jsonObject.put("pos_ability4", post.getPosAbility4());
		jsonObject.put("pos_ability5", post.getPosAbility5());
		jsonObject.put("pos_ability6", post.getPosAbility6());
		jsonObject.put("pos_cost", post.getPosCost());
		jsonObject.put("relatepos", post.getRelatedPos());
		return jsonObject;
	}
	
	// urong@9#,2007.07.29
	@SuppressWarnings("unchecked")
	public static JSONObject getJsonObjectForApplyTrainingDetail(
			Userinfo userInfo, Training training, Applytrainging applyTraining) {

		JSONObject jsonObject = new JSONObject();

		jsonObject
				.put("userInfo", JsonUtils.getJsonObjectForUserinfo(userInfo));
		jsonObject.put("trainingName", training.getName());
		jsonObject.put("trainingDescription", training.getDescription());
		jsonObject.put("trainingPostcode", training.getPostcode());
		jsonObject.put("trainingStarttime", DateUtils.getFormatTime(Long.parseLong(training.getStarttime())));
		jsonObject.put("trainingEndtime", DateUtils.getFormatTime(Long.parseLong(training.getEndtime())));
		jsonObject.put("trainingIspublic", training.getIspublic());
		jsonObject.put("trainingDeadline", DateUtils.getFormatTime(Long.parseLong(training.getDeadline())));

		jsonObject.put("applyReason", applyTraining.getApplyreason());

		jsonObject.put("applyTime", DateUtils.getFormatTime(Long.parseLong(applyTraining.getApplytime())));

		jsonObject.put("applyStatus", applyTraining.getApplystatus());

		return jsonObject;
	}

	// urong@9#, 2007.07.29
	@SuppressWarnings("unchecked")
	public static JSONObject getJsonObjectForTrainingDetail(Training training) {

		JSONObject jsonObject = new JSONObject();

		jsonObject.put("name", training.getName());
		jsonObject.put("description", training.getDescription());
		jsonObject.put("postcode", training.getPostcode());
		jsonObject.put("starttime", DateUtils.getFormatTime(Long.parseLong(training.getStarttime())));
		jsonObject.put("endtime", DateUtils.getFormatTime(Long.parseLong(training.getEndtime())));
		jsonObject.put("ispublic", training.getIspublic());
		jsonObject.put("deadline", DateUtils.getFormatTime(Long.parseLong(training.getDeadline())));
		jsonObject.put("posttime", DateUtils.getFormatTime(Long.parseLong(training.getPosttime())));

		return jsonObject;
	}
}
