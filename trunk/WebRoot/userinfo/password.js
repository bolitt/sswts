var changePwd = {
	changePwdURL : "userinfo.do?method=changePassword",
	styles : [
		{"background": "#dddddd",border:"1px solid #e5e5e5",width:"0px",height:"5px"},
		{"background": "#aa0033",border:"1px solid #e5e5e5",width:"30px",height:"5px"},
		{"background": "#ffcc33",border:"1px solid #e5e5e5",width:"60px",height:"5px"},
		{"background": "#6699cc",border:"1px solid #e5e5e5",width:"90px",height:"5px"},
		{"background": "#008000",border:"1px solid #e5e5e5",width:"120px",height:"5px"}
		/* 
		  ratingMsgs[0] = "太短";
		  ratingMsgs[1] = "弱";
		  ratingMsgs[2] = "一般";
		  ratingMsgs[3] = "很好";
		  ratingMsgs[4] = "极佳";
		  ratingMsgs[5] = "未评级"; //If the password server is down
		
		  ratingMsgColors[0] = "#676767";
		  ratingMsgColors[1] = "#aa0033";
		  ratingMsgColors[2] = "#f5ac00";
		  ratingMsgColors[3] = "#6699cc";
		  ratingMsgColors[4] = "#008000";
		  ratingMsgColors[5] = "#676767";
		*/
	],
	style_char : ["太短","弱","一般","很好","极佳"]
};

changePwd.checkNewPassword = function() {
	var newPwd = dojo.byId("changepwdnewpwd").value;
		this.setStyles(this.updateStrength(newPwd));
	
	if (newPwd === "") {
		dojo.byId("submitchangepwdbn").disabled = true;
		dojo.byId("same").innerHTML = "";
		return;
	}
	if (newPwd != dojo.byId("changepwdnewpwd1").value) {
		dojo.byId("submitchangepwdbn").disabled = true;
		if(dojo.byId("changepwdnewpwd1").value)
		{
			dojo.byId("same").innerHTML = "两次输入密码不符";
		}
		return;
	}
	dojo.byId("same").innerHTML = "";		
	dojo.byId("submitchangepwdbn").disabled = false;
};

changePwd.submitNewPassword = function(){
	dojo.io.bind({
		url : changePwd.changePwdURL,
		handler : function(type, data, evt) {
			if (basicHandler(type, data, evt))
			{
				if (data != "1") {
					postMessage(data,"FATAL");
					return;
				}
				postMessage("修改成功");
			}
		},
		preventCache : true,
		content : {
			"oldPwd" : dojo.crypto.MD5.compute(dojo.byId("changepwdoldpwd").value, dojo.crypto.outputTypes.Hex), 
			"newPwd" : dojo.crypto.MD5.compute(dojo.byId("changepwdnewpwd").value, dojo.crypto.outputTypes.Hex)
		}
	});
};

changePwd.updateStrength = function(s){
	var ls = 0;
	if (s.length < 6){
		return 0;
	}
	
	if (s.match(/[a-z]/g)){
		//lower case test
		ls++;
	}
	if (s.match(/[A-Z]/g)){
		//upper case test
		ls++;
	}
	if (s.match(/[0-9]/ig)){
		//number test
		ls++;
	}
	if (s.match(/(.[^a-zA-z0-9])/ig)){
		//special char
		ls++;
	}
	if (s.length > 12 && ls >2)
	{
		ls++;
	}
	if(ls>4)
	{
		ls = 4;
	}
	return ls;
}

changePwd.setStyles = function(s)
{
	var newstyle = this.styles[s];
	var passwdStr = dojo.byId("passwdStr");
	var div = document.createElement("div");
	passwdStr.innerHTML = "";
	passwdStr.appendChild(div);
	for(var prop in newstyle)
	{
		div.style[prop]=newstyle[prop];
		////dojo.debug("set "+prop+" to "+newstyle[prop]);
	}
	div =  document.createElement("div");
	div.innerHTML = "密码强度：　　　"+this.style_char[s];
	passwdStr.appendChild(div);
}

var content = dojo.widget.byId("main");
content.onLoad = function() {			loading(false);};