var AppointPosition = {
	getSimpleUserinfoURL : "position.do?method=getSimpleUserinfo",
	appPositionURL : "position.do?method=getAppPosition",
	getrevPositionURL : "position.do?method=getRevPosition",
	appointPostionURL : "position.do?method=appointPosition",
	revPositionURL : "position.do?method=revPosition",
	curPostNo : null
};

function getSimpleUserinfo() {
	var ret = null;
	dojo.io.bind({
		url : AppointPosition.getSimpleUserinfoURL,
		handler : function(type, data, evt) {
			//dojo.debug(data);
			if (type == "error") {
				ret = "用户不存在";
				return;
			}
			if (data == "false") {
				ret = "您权限不足";
				return;
			}
			var userinfo = eval("(" + data + ")");
			if (userinfo["error"]) {
				ret = "用户不存在";
				return;
			}
			var div = dojo.byId("appointuserinfo");
			div.innerHTML = "";
			var table = document.createElement("table");
			div.appendChild(table);
			var tr = table.insertRow(-1);
			var td = tr.insertCell(-1);
			td.innerHTML = "学号";
			td = tr.insertCell(-1);
			td.innerHTML = userinfo["stunum"];
			
			tr = table.insertRow(-1);
			td = tr.insertCell(-1);
			td.innerHTML = "姓名";
			td = tr.insertCell(-1);
			td.innerHTML = userinfo["name"];
			
			tr = table.insertRow(-1);
			td = tr.insertCell(-1);
			td.innerHTML = "性别";
			td = tr.insertCell(-1);
			td.innerHTML = userinfo["gender"] === 0 ? "女" : "男";
			
			tr = table.insertRow(-1);
			td = tr.insertCell(-1);
			td.innerHTML = "系别";
			td = tr.insertCell(-1);
			td.innerHTML = userinfo["dept"];
			
			tr = table.insertRow(-1);
			td = tr.insertCell(-1);
			td.innerHTML = "班号";
			td = tr.insertCell(-1);
			td.innerHTML = userinfo["classnum"];
			dojo.byId("appointuserinfocb").checked = false;
		},
		sync : true,
		preventCache : true,
		content : {
			"stnum" : dojo.byId("appointstnum").value
		}
	});
	if (ret !== null) {
		return ret;
	}
}

function checkUserinfo() {
	if (!dojo.byId("appointuserinfocb").checked) {
		return "请确认信息是否正确";
	}
	if (dojo.byId("appointappposrd").checked) {
		dojo.byId("appointposstep4").innerHTML = "Step4: 请选择要任命的职务";
		dojo.io.bind({
			url : AppointPosition.appPositionURL,
			handler : function(type, data, error) {
				//dojo.debug(data);
				var div = dojo.byId("appointposition");
				if (type == "error") {
					div.innerHTML = "系统出现异常，请联系管理员";
					return;
				}
				dojo.byId("appointposition").innerHTML="";
				var posArr = eval("(" + data + ")");
				if (posArr.length === 0) {
					div.innerHTML = "没有可以任命的职位";
					return;
				}
				div.innerHTML="";
				//for (var i = 0; i < posArr.length; i=i+1) {
					generateTree(div, posArr);
				//}
			},
			preventCache : true,
			content : {
				"studentnum" : dojo.byId("appointstnum").value
			}
		});
	}
	else {
		dojo.byId("appointposstep4").innerHTML = "Step4: 请选择要免除的职务";
		dojo.io.bind({
			url : AppointPosition.getrevPositionURL,
			handler : function(type, data, error) {
				//dojo.debug(data);
				var div = dojo.byId("appointposition");
				if (type == "error") {
					div.innerHTML = "没有可以免除的职位";
					return;
				}
				var pos = eval("(" + data + ")");
				if (pos.length === 0) {
					div.innerHTML = "没有可以免除的职位";
					return;
				}
				var innerHTML = "";
				for (var i = 0; i < pos.length; i=i+1) {
					innerHTML += "<input type='checkbox' name='revposcb' value='" + pos[i]["fullCode"]	+ "' />" + pos[i]["fullName"] + "<br />";
				}
				div.innerHTML = innerHTML;
			},
			preventCache : true,
			content : {
				"studentnum" : dojo.byId("appointstnum").value
			}
		});
	}
}

function submitAppointPostion() {
	//dojo.debug("in submitAppointPostion");
	if (dojo.byId("appointappposrd").checked) {
		if(AppointPosition.curPostNo)
		{
			//已经点选了节点
			dojo.io.bind({
				url : AppointPosition.appointPostionURL,
				handler : function(type,data,evt){
					if(type==="load")
					{
						if(data==="true")
						{
							//everthing's fine
							postMessage("任命成功!","MESSAGE");
						}
						else
						{
							postMessage("任命失败，请联系管理员!","ERROR");
						}
						myNavigator.goAppointPosition();
					}
				},
				content : {
					"studentnum" : dojo.byId("appointstnum").value,
					"posNo" : AppointPosition.curPostNo
				},
				preventCache : true
			});
		}
		else
		{
			window.alert("请选择您要任命的职位");
		}
	}
	else {
		var revPosArray = document.getElementsByName("revposcb");
		//dojo.debug(dojo.json.serialize(revPosArray));
		//dojo.debug(revPosArray.length);
		if(revPosArray.length>0)
		{
			//已经点选了节点
			var revPosString = "";
			for(var i=0;i<revPosArray.length;i=i+1)
			{
				//dojo.debug(revPosArray[i].checked+" "+revPosArray[i].value);
				if(revPosArray[i].checked)
				{
						revPosString+=revPosArray[i].value+",";
				}
			}
			revPosString = revPosString.substring(0,revPosString.length-1);
			//dojo.debug(revPosString);
			dojo.io.bind({
				url : AppointPosition.revPositionURL ,
				handler : function(type,data,evt){
					if(type==="load"&&data==="true")
					{
						//everthing's fine
						postMessage("免除成功!","MESSAGE");
					}
					else
					{
						postMessage("免除失败，请联系管理员!","ERROR");
					}
					myNavigator.goAppointPosition();
				},
				content : {
					"studentnum" : dojo.byId("appointstnum").value,
					"posNos" : revPosString
				},
				preventCache : true
			});
		}
		else
		{
			window.alert("请选择您要免除的职位");
		}
	}
}

function generateTree(node, data) {
	//dojo.debug("in generateTree , there're " + data.length + " orgs");
	var controller = dojo.widget.createWidget("TreeBasicControllerV3");
	var doccontroller = dojo.widget.createWidget("TreeDocIconExtension");
	var TreeSelectorV3 = dojo.widget.createWidget("TreeSelectorV3");
	var TreeEmphasizeOnSelect = dojo.widget.createWidget("TreeEmphasizeOnSelect", {selector:TreeSelectorV3});
	var tree = dojo.widget.createWidget("TreeV3", {toggle:"wipe",listeners:[controller.widgetId, doccontroller.widgetId, TreeSelectorV3.widgetId]});
	tree.setChildren(data);
	//node.innerHTML="";
	node.appendChild(tree.domNode);
	
	var reporter = function(reporter) 
	{
		this.name = "select";
		this.go = function(message) {
			if(message.node.postNo) {
				AppointPosition.curPostNo = message.node.postNo;
			}
			else {
				AppointPosition.curPostNo = "";
			}
		};
	};		
	
	dojo.event.topic.subscribe(TreeSelectorV3.eventNames.select, new reporter("TreeSelectorV3"), "go");
}

var content = dojo.widget.byId("main");
content.onLoad = function() {
	loading(false);
};
