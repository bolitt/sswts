var loading;
var basicHandler;
var postMessage;

//本文件的代码可以再次进行精炼的
var ModifyPositionArch = {
	showAllPosUrl : "settings.do?method=returnCurrentPosition",
	showPosDetailUrl : "settings.do?method=showTreeDetail",
	AddNodeUrl : "settings.do?method=addTreeNode",
	EditNodeUrl : "settings.do?method=changePosArchInfo",
	DelNodeUrl : "settings.do?method=delNodeServ",
	currentCode : null,
	currentType : null
};

ModifyPositionArch.init = function() {
	loading(true);
	dojo.byId("PosTrees").innerHTML="加载中...";
	dojo.io.bind({url:ModifyPositionArch.showAllPosUrl, handler:function(type, data, evt) {
		loading(false);
		if (basicHandler(type, data, evt)) {
			//dojo.debug(data);
			var bigdata = eval("(" + data + ")");
			ModifyPositionArch.generateTree(dojo.byId("PosTrees"), bigdata);
			
		}
	}, preventCache:true});
};

ModifyPositionArch.generateTree = function(node, data) {
	//dojo.debug("in generateTree , there're " + data.length + " orgs");
	var controller = dojo.widget.createWidget("TreeBasicControllerV3");
	var doccontroller = dojo.widget.createWidget("TreeDocIconExtension");
	var TreeSelectorV3 = dojo.widget.createWidget("TreeSelectorV3");
	var TreeEmphasizeOnSelect = dojo.widget.createWidget("TreeEmphasizeOnSelect", {selector:TreeSelectorV3});
	var tree = dojo.widget.createWidget("TreeV3", {listeners:[controller.widgetId, doccontroller.widgetId, TreeSelectorV3.widgetId]});
	tree.setChildren(data);
//	controller.expandToLevel(tree,4);
	node.innerHTML = "";
	node.appendChild(tree.domNode);
	var reporter = function (reporter) {
		this.name = "select";
		this.go = function (message) {
			ModifyPositionArch.currentCode=message.node.postNo;
			//alert(message.node.postNo);
			ModifyPositionArch.currentType=message.node.type;
			ModifyPositionArch.setPanels();
			ModifyPositionArch.showPosDetail(message.node.postNo, message.node.type);
		};
	};
	dojo.event.topic.subscribe(TreeSelectorV3.eventNames.select, new reporter("TreeSelectorV3"), "go");
};

ModifyPositionArch.setPanels = function()
{
	dojo.byId("Addcate").style.display = "none";
	dojo.byId("Addorg").style.display = "none";
	dojo.byId("Adddept").style.display = "none";
	dojo.byId("Addpost").style.display = "none";
	dojo.byId("Editcate").style.display = "none";
	dojo.byId("Editorg").style.display = "none";
	dojo.byId("Editdept").style.display = "none";
	dojo.byId("Editpost").style.display = "none";
	dojo.byId("Delcate").style.display = "none";
	dojo.byId("Delorg").style.display = "none";
	dojo.byId("Deldept").style.display = "none";
	dojo.byId("Delpost").style.display = "none";
	switch (ModifyPositionArch.currentType)
	{
		case "0":
			//dojo.debug("clicked on Tsinghua");
			dojo.byId("Addcate").style.display = "";
			break;
		case "1":
			dojo.byId("Addorg").style.display = "";
			dojo.byId("Editcate").style.display = "";
			dojo.byId("Delcate").style.display = "";
			break;
		case "2":
			dojo.byId("Adddept").style.display = "";
			dojo.byId("Editorg").style.display = "";
			dojo.byId("Delorg").style.display = "";
			break;
		case "3":
			dojo.byId("Addpost").style.display = "";
			dojo.byId("Editdept").style.display = "";
			dojo.byId("Deldept").style.display = "";
			break;
		case "4":
			dojo.byId("Addpost").style.display = "";
			dojo.byId("Editpost").style.display = "";
			dojo.byId("Delpost").style.display = "";
			break;
		case "5":
			dojo.byId("Addorg").style.display = "";
			break;
	}
};

ModifyPositionArch.showPosDetail = function(code, nodeType) {
	//dojo.debug("in showPosDetail : " + code + " " + nodeType);
	if(nodeType == "0")
	{
		//dojo.debug("level 0, hide all");
		return;
	}
	if(nodeType == "5")
	{
		//dojo.debug("level 1, hide all");	
		return;
	}
	
	
	dojo.io.bind({url:ModifyPositionArch.showPosDetailUrl, handler:function (type, data, evt) {
		if (basicHandler(type, data, evt)) {
			//dojo.debug(data);
			var mydata = eval("("+data+")");
			switch (nodeType) {
			  case "0":
					//dojo.byId("Addcate").style.display="";
				//dojo.debug("nothing to show");
				break;
			  case "1":
				dojo.widget.byId("Editcatecode").setValue(mydata.code);
				dojo.byId("Editcatename").value = mydata.name;
				dojo.byId("Editcatedescription").value = mydata.description;
				break;
			  case "2":
				dojo.widget.byId("Editorgcode").setValue(mydata.code);
				dojo.byId("Editorgname").value = mydata.name;
				dojo.byId("Editorgdescription").value = mydata.description;
				break;
			  case "3":
				dojo.widget.byId("Editdeptcode").setValue(mydata.code);
				dojo.byId("Editdeptname").value = mydata.name;
				dojo.byId("Editdeptdescription").value = mydata.description;
				break;
			  case "4":
			  	dojo.widget.byId("Editpostcode").setValue(mydata.code);
				dojo.byId("Editpostname").value=mydata.name;
				dojo.byId("Editpostdescription").value=mydata.description;
				dojo.byId("Editpostweight").value=mydata.weight;
				dojo.byId("Editpostcoefficient").value=mydata.coefficient;
				dojo.byId("Editpostpos_req").value=mydata.pos_req;
				dojo.byId("Editpostability1").value=mydata.ability1;
				dojo.byId("Editpostability2").value=mydata.ability1;
				dojo.byId("Editpostability3").value=mydata.ability1;
				dojo.byId("Editpostability4").value=mydata.ability1;
				dojo.byId("Editpostability5").value=mydata.ability1;
				dojo.byId("Editpostability6").value=mydata.ability1;
				dojo.byId("Editpostpos_cost").value=mydata.pos_cost;
				dojo.byId("Editpostrelatedpos").value=mydata.relatedpos;
				break;
			}
		}
		if(type==="error")
		{
			postMessage("系统故障,请联系管理员!","FATAL");
		}
	}, content:{"code":code}, preventCache:true});
};

ModifyPositionArch.addCate = function()
{
	//dojo.debug("in addCate");
	dojo.io.bind({
		url : ModifyPositionArch.AddNodeUrl,
		preventCache : true,
		method : "post",
		encoding : "UTF-8",
		handler : function(type,data,evt)
		{
			if(basicHandler(type, data, evt))
			{
				if(data == "true")
				{
					//dojo.debug("del "+ModifyPositionArch.currentCode+" ok");
					postMessage("添加成功!","MESSAGE");
					ModifyPositionArch.init();
				}
				else
				{
					postMessage("添加失败!","FATAL");
				}
			}
		},
		content : {
			"code" : dojo.byId("Addcatecode").value,
			"name" : dojo.byId("Addcatename").value,
			"description" : dojo.byId("Addcatedescription").value
		}
	});
};

ModifyPositionArch.addOrg =function()
{
	if(ModifyPositionArch.currentCode.length==12)
	{
		ModifyPositionArch.currentCode=ModifyPositionArch.currentCode.substring(0,9);
	}
	//dojo.debug("in addOrg");
	dojo.io.bind({
		url : ModifyPositionArch.AddNodeUrl,
		preventCache : true,
		method : "post",
		encoding : "UTF-8",
		handler : function(type,data,evt)
		{
			if(basicHandler(type, data, evt))
			{
				if(data=="true")
				{
					//dojo.debug("del "+ModifyPositionArch.currentCode+" ok");
					postMessage("添加成功!","MESSAGE");
					ModifyPositionArch.init();
				}
				else
				{
					postMessage("添加失败!","FATAL");
				}
			}
		},
		content : {
			"code" : ModifyPositionArch.currentCode+dojo.byId("Addorgcode").value,
			"name" : dojo.byId("Addorgname").value,
			"description" : dojo.byId("Addorgdescription").value
		}
	});
};

ModifyPositionArch.addDept = function()
{
	if(ModifyPositionArch.currentCode.length==12)
	{
		ModifyPositionArch.currentCode=ModifyPositionArch.currentCode.substring(0,9);
	}
	//dojo.debug("in addDept");
	dojo.io.bind({
		url : ModifyPositionArch.AddNodeUrl,
		preventCache : true,
		method : "post",
		encoding : "UTF-8",
		handler : function(type,data,evt)
		{
			if(basicHandler(type, data, evt))
			{
				if(data=="true")
				{
					//dojo.debug("del "+ModifyPositionArch.currentCode+" ok");
					postMessage("添加成功!","MESSAGE");
					ModifyPositionArch.init();
	
				}
				else
				{
					postMessage("添加失败!","FATAL");
				}
			}
		},
		content : {
			"code" : ModifyPositionArch.currentCode+dojo.byId("Adddeptcode").value,
			"name" : dojo.byId("Adddeptname").value,
			"description" : dojo.byId("Adddeptdescription").value
		}
	});
};

ModifyPositionArch.addPost = function()
{
	if(ModifyPositionArch.currentCode.length==12)
	{
		ModifyPositionArch.currentCode=ModifyPositionArch.currentCode.substring(0,9);
	}
	//dojo.debug("in addPost");
		dojo.io.bind({
		url : ModifyPositionArch.AddNodeUrl,
		preventCache : true,
		method : "post",
		encoding : "UTF-8",
		handler : function(type,data,evt)
		{
			if(basicHandler(type, data, evt))
			{
				if(data=="true")
				{
					//dojo.debug("del "+ModifyPositionArch.currentCode+" ok");
					postMessage("添加成功!","MESSAGE");
					ModifyPositionArch.init();
	
				}
				else
				{
					postMessage("添加失败!","FATAL");
				}
			}
		},
		content : {
			"code" : ModifyPositionArch.currentCode+dojo.byId("Addpostcode").value,
			"name" : dojo.byId("Addpostname").value,
			"description" : dojo.byId("Addpostdescription").value,
			"weight" : dojo.byId("Addpostweight").value,
			"coefficient" : dojo.byId("Addpostcoefficient").value,
			"pos_req" : dojo.byId("Addpostpos_req").value,
			"ability1" : dojo.byId("Addpostability1").value,
			"ability2" : dojo.byId("Addpostability2").value,
			"ability3" : dojo.byId("Addpostability3").value,
			"ability4" : dojo.byId("Addpostability4").value,
			"ability5" : dojo.byId("Addpostability5").value,
			"ability6" : dojo.byId("Addpostability6").value,
			"pos_cost" : dojo.byId("Addpostpos_cost").value,
			"relatedpos" : dojo.byId("Addpostrelatedpos").value
		}
	});
};

ModifyPositionArch.editCate = function()
{
	//dojo.debug("in editCate");
	loading(true);
	dojo.io.bind({
		url : ModifyPositionArch.EditNodeUrl,
		preventCache : true,
		method : "post",
		encoding : "UTF-8",
		handler : function(type,data,evt)
		{
			loading(false);
			if(basicHandler(type, data, evt)&&data==="true")
			{
				//dojo.debug("del "+ModifyPositionArch.currentCode+" ok");
				postMessage("修改成功!","MESSAGE");			
				ModifyPositionArch.init();
			}
		},
		content : {
			"posArchCode" : ModifyPositionArch.currentCode,
			"code" : dojo.byId("Editcatecode").value,
			"name" : dojo.byId("Editcatename").value,
			"description" : dojo.byId("Editcatedescription").value
		}
	});
};

ModifyPositionArch.editOrg = function()
{
	//dojo.debug("in editOrg");
	loading(true);
	dojo.io.bind({
		url : ModifyPositionArch.EditNodeUrl,
		preventCache : true,
		method : "post",
		encoding : "UTF-8",
		handler : function(type,data,evt)
		{
			loading(false);
			if(basicHandler(type, data, evt)&&data==="true")
			{
				//dojo.debug("del "+ModifyPositionArch.currentCode+" ok");
				postMessage("修改成功!","MESSAGE");
				ModifyPositionArch.init();
			}
		},
		content : {
			"posArchCode" : ModifyPositionArch.currentCode,
			"code" : dojo.byId("Editorgcode").value,
			"name" : dojo.byId("Editorgname").value,
			"description" : dojo.byId("Editorgdescription").value
		}
	});
};

ModifyPositionArch.editDept = function()
{
	//dojo.debug("in editDept");
	loading(true);
	dojo.io.bind({
		url : ModifyPositionArch.EditNodeUrl,
		preventCache : true,
		method : "post",
		encoding : "UTF-8",
		handler : function(type,data,evt)
		{
			loading(false);
			if(basicHandler(type, data, evt)&&data==="true")
			{
				//dojo.debug("del "+ModifyPositionArch.currentCode+" ok");
				postMessage("修改成功!","MESSAGE");
				ModifyPositionArch.init();
			}
		},
		content : {
			"posArchCode" : ModifyPositionArch.currentCode,
			"code" : dojo.byId("Editdeptcode").value,
			"name" : dojo.byId("Editdeptname").value,
			"description" : dojo.byId("Editdeptdescription").value
		}
	});
};

ModifyPositionArch.editPost = function()
{
	//dojo.debug("in editPost");
	loading(true);
	dojo.io.bind({
		url : ModifyPositionArch.EditNodeUrl,
		preventCache : true,
		method : "post",
		encoding : "UTF-8",
		handler : function(type,data,evt)
		{
			loading(false);
			if(basicHandler(type, data, evt)&&data==="true")
			{
				//dojo.debug("del "+ModifyPositionArch.currentCode+" ok");
				postMessage("修改成功!","MESSAGE");
				ModifyPositionArch.init();
			}
		},
		content : {
			"posArchCode" : ModifyPositionArch.currentCode,
			"code" : dojo.byId("Editpostcode").value,
			"name" : dojo.byId("Editpostname").value,
			"description" : dojo.byId("Editpostdescription").value,
			"weight" : dojo.byId("Editpostweight").value,
			"coefficient" : dojo.byId("Editpostcoefficient").value,
			"pos_req" : dojo.byId("Editpostpos_req").value,
			"ability1" : dojo.byId("Editpostability1").value,
			"ability2" : dojo.byId("Editpostability2").value,
			"ability3" : dojo.byId("Editpostability3").value,
			"ability4" : dojo.byId("Editpostability4").value,
			"ability5" : dojo.byId("Editpostability5").value,
			"ability6" : dojo.byId("Editpostability6").value,
			"pos_cost" : dojo.byId("Editpostpos_cost").value,
			"relatedpos" : dojo.byId("Editpostrelatedpos").value
		}
	});
};

ModifyPositionArch.delNode = function()
{
	//dojo.debug("in del!");
	loading(true);
	dojo.io.bind({
		url : ModifyPositionArch.DelNodeUrl,
		content : {"type":ModifyPositionArch.currentType, "num" : ModifyPositionArch.currentCode},
		preventCache : true,
		handler : function(type,data,evt)
		{
			if(basicHandler(type, data, evt)&&data==="true")
			{
				//dojo.debug("del "+ModifyPositionArch.currentCode+" ok");
				postMessage("删除成功!","MESSAGE");
				loading(false);
				ModifyPositionArch.init();
			}
		}
	});
};


var content = dojo.widget.byId("main");
content.onLoad = ModifyPositionArch.init;

