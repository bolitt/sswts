var ApplyPosition = {
	getPosURL : "position.do?method=getAvailablePosition",
	showPosDetailURL : "position.do?method=showPositionDetail",
	applyPosURL : "position.do?method=applyPosition",
	currentPos : null,
	appliedPos : []
};

//申请的init函数
ApplyPosition.init = function() {

	dojo.html.hide("applyDiv");
	dojo.html.setOpacity("posDetail",0);
	//show loading dialog
	loading(true);
	dojo.io.bind({url:ApplyPosition.getPosURL, handler:function (type, data, evt) {
	loading(false);
	if(basicHandler(type, data, evt)) {
		//dojo.debug("get data: " + data);
		var bigdata = eval(data);
		//dojo.debug(bigdata);
		//parse school positions
		var schooldata = bigdata[0];
		var node = dojo.byId("SchoolPos");
		ApplyPosition.generateTree(node, schooldata);
		var deptdata = bigdata[1];
		node = dojo.byId("DeptPos");
		ApplyPosition.generateTree(node, deptdata);
		var orgsdata = bigdata[2];
		node = dojo.byId("OrgsPos");
		ApplyPosition.generateTree(node, orgsdata);
	}
	loading(false);
}, preventCache: true});
};

//画树
ApplyPosition.generateTree = function(node, data) {
	//dojo.debug("in generateTree , there're " + data.length + " orgs");
	var controller = dojo.widget.createWidget("TreeBasicControllerV3");
	var doccontroller = dojo.widget.createWidget("TreeDocIconExtension");
	var TreeSelectorV3 = dojo.widget.createWidget("TreeSelectorV3");
	var TreeEmphasizeOnSelect = dojo.widget.createWidget("TreeEmphasizeOnSelect", {selector:TreeSelectorV3});
	var tree = dojo.widget.createWidget("TreeV3", {toggle:"wipe",listeners:[controller.widgetId, doccontroller.widgetId, TreeSelectorV3.widgetId]});
	tree.setChildren(data);
	node.innerHTML="";
	node.appendChild(tree.domNode);
	
	var reporter = function(reporter) 
	{
		this.name = "select";
		this.go = function(message) 
		{
			if(message.node.postNo)
			{
				ApplyPosition.showPosDetail(message.node.postNo);
			}
		};
	};		
	
	
	dojo.event.topic.subscribe(TreeSelectorV3.eventNames.select, new reporter("TreeSelectorV3"), "go");
};
//查看详情
ApplyPosition.showPosDetail = function(posNo) {
	ApplyPosition.currentPos = posNo;
	dojo.io.bind({url:ApplyPosition.showPosDetailURL, handler:function(type, data, evt) {
	if (basicHandler(type, data, evt)) {
		var fadeInOut = dojo.lfx.fadeOut("posDetail",100,null,function()
		{
		var details = eval("(" + data + ")");
		//dojo.byId("poscode").innerHTML=details.poscode;
		dojo.byId("fullname").innerHTML=details.fullname;
		dojo.byId("pos_Descrip").innerHTML=details.pos_Descrip;
		//dojo.byId("category").innerHTML=details.category;
		//dojo.byId("organs").innerHTML=details.organs;
		//dojo.byId("depts").innerHTML=details.depts;
		//dojo.byId("posts").innerHTML=details.posts;
		dojo.byId("pos_require").innerHTML=details.pos_require;
		dojo.byId("pos_ability1").innerHTML=details.pos_ability1;
		dojo.byId("pos_ability2").innerHTML=details.pos_ability2;
		dojo.byId("pos_ability3").innerHTML=details.pos_ability3;
		dojo.byId("pos_ability4").innerHTML=details.pos_ability4;
		dojo.byId("pos_ability5").innerHTML=details.pos_ability5;
		dojo.byId("pos_ability6").innerHTML=details.pos_ability6;
		
		dojo.byId("pos_cost").innerHTML=details.pos_cost;
		dojo.byId("pos_relate").innerHTML=details.pos_relate;

		//hide apply button first
		dojo.html.hide("applyDiv");
		var applyDiv = dojo.byId("applyDiv");
		//create the apply button 
		applyDiv.innerHTML = '<input type="button" onclick=\'ApplyPosition.applyPos()\' id="applyButton" value="申请该岗位！" />';
		//add apply reason part
		var reasondiv = document.createElement("div");
		reasondiv.id = "reasondiv";
		//add it to applydiv
		applyDiv.appendChild(reasondiv);
		//hide reason div
		dojo.html.hide("reasondiv");
		reasondiv.appendChild(document.createTextNode("请输入您申请的理由，并按提交："));
		reasondiv.innerHTML += "<textarea id=\"reason\"></textarea><br />" +  "<a href=\"#\" onclick=\"ApplyPosition.submitApply()\">提交</a>";
		
		ApplyPosition.appliedPos[ApplyPosition.currentPos] = details.ifapplyed;
		//如果已经藏起来就不要再藏,如果没有,就藏起来
		if (ApplyPosition.appliedPos[ApplyPosition.currentPos] === false) {
			dojo.lfx.wipeIn("applyDiv",250).play();
		} else {
			dojo.lfx.wipeOut("applyDiv",250).play();
		}
		dojo.lfx.fadeIn("posDetail",100).play()
		});
		fadeInOut.play();
	}
}, preventCache: true,content:{posnum:posNo}});
};


ApplyPosition.applyPos = function() {
	ApplyPosition.appliedPos[ApplyPosition.currentPos] = true;
	dojo.lfx.wipeIn("reasondiv",300).play();
	dojo.lfx.wipeOut("applyButton",100).play();
}

ApplyPosition.submitApply = function()
{
	var myreason=dojo.byId("reason").value;
	dojo.io.bind({url:ApplyPosition.applyPosURL, 
	preventCache: true,
	handler:function (type, data, evt) {
		if (basicHandler(type, data, evt)) {
			if (data == "true") {
				postMessage("申请提交成功!","MESSAGE");
				dojo.lfx.wipeOut("reasondiv",300).play();
			}
		}
	}, 
	method: "post",
	content:{poscode:ApplyPosition.currentPos,reason:myreason}});
}

var content = dojo.widget.byId("main");
content.onLoad = ApplyPosition.init;

