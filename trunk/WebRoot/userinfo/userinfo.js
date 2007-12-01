var userInfo = {
	viewUserinfoURL : "userinfo.do?method=viewUserinfo",
	editUserinfoURL : "userinfo.do?method=editUserinfo"
};


userInfo.onViewUserinfo = function() {
	dojo.io.bind({
		url : userInfo.viewUserinfoURL,
		load : userInfo.initViewUserinfoCallBack,
		error : function (type, error) {
			window.alert(error.message);
		},
		preventCache:true
	});
};

userInfo.initViewUserinfoCallBack= function (type, data, evt) {
	//dojo.debug("get data = " + data);
	if(basicHandler(type, data, evt))
	{
	var userinfo = eval("(" + data + ")");
	dojo.byId("name").innerHTML=userinfo.name;
	dojo.byId("grade").innerHTML=userinfo.grade;
	dojo.byId("gender").innerHTML=userinfo.gender;
	dojo.byId("region").innerHTML=userinfo.region;
	if(userinfo.nation)dojo.byId("nation").innerHTML=userinfo.nation;
	dojo.byId("department").innerHTML=userinfo.department;
	dojo.byId("classnum").innerHTML=userinfo.classnum;
	dojo.byId("political").innerHTML=userinfo.political;
	dojo.widget.byId("userinfo-tel").setValue(userinfo.tel);
	dojo.widget.byId("userinfo-mobile").setValue(userinfo.mobile);
	dojo.widget.byId("userinfo-email").setValue(userinfo.email);
	dojo.widget.byId("userinfo-address").setValue(userinfo.address);
	
	}
				loading(false);
};

userInfo.submitUserinfoChange = function() {
	dojo.byId("changeUserinfoResult").innerHTML = "";
	var valid = true;
	valid = valid && dojo.widget.byId("userinfo-tel").isValid();
	valid = valid && dojo.widget.byId("userinfo-mobile").isValid();
	valid = valid && dojo.widget.byId("userinfo-email").isValid();
	valid = valid && dojo.widget.byId("userinfo-address").isValid();
	
	if(!valid)
	{
		alert("您的信息不符合规则");
		return;
	}
	
	dojo.io.bind({
		url : userInfo.editUserinfoURL,
		method: "post",
		handler : function (type, data, evt) {
			if(basicHandler(type, data, evt))
			{
				postMessage("操作成功","MESSAGE");
			}
		},
		content : {
			"tel" : dojo.byId("userinfo-tel").value,
			"mobile" : dojo.byId("userinfo-mobile").value,
			"email" : dojo.byId("userinfo-email").value,
			"address" : dojo.byId("userinfo-address").value
		},
		preventCache:true
	});
};

var content = dojo.widget.byId("main");
content.onLoad = userInfo.onViewUserinfo;

