//左侧的按钮控制函数
var myNavigator = {
	lastPage : ""
};

// these functions are responsers of the dispatcher, it mainly sets the url of the main container 
myNavigator.goInbox = function()
{
	//dojo.debug("in go inbox");
	var content = dojo.widget.byId("main");
	content.onLoad=Inbox.init;
	content.setUrl(pathRelative + "boxes/inbox.html");
};

myNavigator.goApplyPosition = function() {
	//dojo.debug("in go apply position");
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "position/applyPosition.html");
//	content.onLoad=onApplyPostion;
};
myNavigator.goApprovePosition = function() {
	//dojo.debug("in go approve position");
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "position/approvePosition.html");
//	content.onLoad=onApplyPostion;
};
myNavigator.goBrowsePosition = function() {
	//dojo.debug("in go goBrowsePosition");
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "position/browsePosition.html");
};
myNavigator.goWorkingExp = function() {
	//dojo.debug("in go goWorkingExp");
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "position/showexp.html");
};
myNavigator.goInitData = function() {
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "System/init.html");
	content.onLoad = function () {
	};
};
myNavigator.goAppointPosition = function() {
	//dojo.debug("in go AppointPosition");
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "position/appointPosition.html");
	//content.onLoad=onAppAndRevPosition;
};

myNavigator.goShowInformation = function() {
    //dojo.debug("in go ShowInformation");
    var content = dojo.widget.byId("main");
    content.onLoad=function(){loading(false);};
	content.setUrl(pathRelative + "index1.html");
	//content.onLoad = Misc.getInformation;
	//myNavigator.setButtonStyles("");
};
myNavigator.goEditInformation = function() {
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "userinfo/userinfo.html");
};
myNavigator.goEditPassword = function() {
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "userinfo/password.html");
};

//trainings
myNavigator.goAuthTraining = function() {
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "training/AuthTraining.html");
};
myNavigator.goApplyStatus = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	loading(false);
	};
	content.setUrl(pathRelative + "training/applystatus.html");
};
myNavigator.goApproveTraining = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	};
	content.setUrl(pathRelative + "training/approvetraining.html");
};
myNavigator.goManageTraining = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	};
	content.setUrl(pathRelative + "training/admin.html");
};
myNavigator.goPostTraining = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	};
	content.setUrl(pathRelative + "training/posttraining.html");
};
myNavigator.goViewnApplyTraining = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	};
	content.setUrl(pathRelative + "training/ViewnApplyTraining.html");
};


//informations
myNavigator.goPublicInformation = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	};
	content.setUrl(pathRelative + "information/pubinformation.html");
};
myNavigator.goReleaseInformation = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	};
	content.setUrl(pathRelative + "information/releaseinformation.html");
};

myNavigator.goPostInternalTask = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	};
	content.setUrl(pathRelative + "information/postinternaltask.html");
};
myNavigator.goPostInternalInfo = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	};
	content.setUrl(pathRelative + "information/postinternalinfo.html");
};
myNavigator.goTaskFeedback = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	};
	content.setUrl(pathRelative + "information/taskfeedback.html");
};
myNavigator.goInit2ndLevelNode = function() {
	var content = dojo.widget.byId("main");
	content.onLoad = function () {
	};
	content.setUrl(pathRelative + "System/init2ndlevelnode.jsp");
};
myNavigator.goModifyPositionArchitecture = function() {
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "System/modPosArch.html");
};
//为了能够后退而增加的函数
myNavigator.saveHistory = function(func, statusName)
{
	//dojo.debug("try to save history " + statusName);
	if(myNavigator.lastPage!=statusName)
	{
		//dojo.debug("and success");
		myNavigator.lastPage = statusName;
		var appState = new ApplicationState(func, statusName);
		appState.showStateData();
		dojo.undo.browser.addToHistory(appState);
	}
};

dojo.require("dojo.json");

// this function is a dispatcher,
// it receives the name of the button clicked on the left accordion panel
// and fires a proper function

//var firstTimeInit = false;

function fireNavigatorButtonEvent(buttonName,panelName) {

	
	var content = dojo.widget.byId("main");
	
	/*if(firstTimeInit == false && content.isLoaded == false)
	{
		//尚未执行完成，则100ms后重新尝试本函数
		firstTimeInit = true;
		dojo.debug("尚未执行完成，则100ms后重新尝试本函数");
		window.setTimeout("fireNavigatorButtonEvent('"+buttonName+"')",100 );
		return;
	}*/
	if(buttonName != "ShowInformation" && buttonName )
	{
		dojo.debug("show!");
		//loading(true);
	}
	dojo.debug("go "+buttonName);
	//dojo.debug("content.onLoad"+content.onLoad);

	
	content.onLoad = function () {
		loading(false);
	};
	myNavigator.setButtonStyles(buttonName, panelName);
	SetCookie("lastModule", buttonName);
	SetCookie("lastPanel", panelName);
	
	switch (buttonName) {
	  case "inbox":
		myNavigator.goInbox();
		myNavigator.saveHistory(myNavigator.goInbox, "收件箱");	
		break;
	  case "ShowInformation":
		myNavigator.goShowInformation();
		myNavigator.saveHistory(myNavigator.goShowInformation, "个人主页");
		break;
	  case "EditInformation":
		myNavigator.saveHistory(myNavigator.goEditInformation, "更改信息");
		myNavigator.goEditInformation();		
		break;
	  case "EditPassword":
		myNavigator.goEditPassword();
		myNavigator.saveHistory(myNavigator.goEditPassword, "修改密码");
		break;
	  case "ApplyPosition":
		myNavigator.goApplyPosition();
		myNavigator.saveHistory(myNavigator.goApplyPosition, "岗位申请");
		break;
	  case "AppointPosition":
		myNavigator.goAppointPosition();
		myNavigator.saveHistory(myNavigator.goAppointPosition, "岗位任免");
		break;
	  case "ApprovePosition":
		myNavigator.goApprovePosition();
		myNavigator.saveHistory(myNavigator.goApprovePosition, "岗位审批");
		break;
	  case "BrowsePosition":
		myNavigator.goBrowsePosition();
		myNavigator.saveHistory(myNavigator.goBrowsePosition, "岗位浏览");
		break;
	  case "WorkingExp":
		myNavigator.goWorkingExp();
		myNavigator.saveHistory(myNavigator.goBrowsePosition, "社工经历");
		break;
	  case "AuthTraining":
		myNavigator.goAuthTraining();
		myNavigator.saveHistory(myNavigator.goAuthTraining, "培训申请");
		break;
	  case "ApplyStatus":
		myNavigator.goApplyStatus();
		myNavigator.saveHistory(myNavigator.goApplyStatus, "申请状态");
		break;
	  case "ApproveTraining":
		myNavigator.goApproveTraining();
		myNavigator.saveHistory(myNavigator.goApproveTraining, "培训审批");
		break;
	  case "ManageTraining":
		myNavigator.goManageTraining();
		myNavigator.saveHistory(myNavigator.goManageTraining, "培训管理");
		break;
	  case "PostTraining":
	  	myNavigator.goPostTraining();
		myNavigator.saveHistory(myNavigator.goPostTraining, "发布培训信息");
		break;
	  case "ViewnApplyTraining":
		myNavigator.goViewnApplyTraining();
		myNavigator.saveHistory(myNavigator.goViewnApplyTraining, "查看培训活动");
		break;
	  case "PublicInformation":
		myNavigator.goPublicInformation();
		myNavigator.saveHistory(myNavigator.goPublicInformation, "校级信息发布");
		break;
	  case "ReleaseInformation":
		myNavigator.goReleaseInformation();
		myNavigator.saveHistory(myNavigator.goReleaseInformation, "放行校级信息");
		break;
	  case "PostInternalTask":
		myNavigator.goPostInternalTask();
		myNavigator.saveHistory(myNavigator.goPostInternalTask, "工作任务部署");
		break;
	  case "PostInternalInfo":
		myNavigator.goPostInternalInfo();
		myNavigator.saveHistory(myNavigator.goPostInternalInfo, "内部通知发布");
		break;
	  case "TaskFeedback":
		myNavigator.goTaskFeedback();
		myNavigator.saveHistory(myNavigator.goTaskFeedback, "任务反馈查看");
		break;
	  case "Init":
		myNavigator.goInitData();
		myNavigator.saveHistory(myNavigator.goInitData, "初始化数据库");
		break;
	  case "ModifyPositionArchitecture":
		myNavigator.goModifyPositionArchitecture();
		myNavigator.saveHistory(myNavigator.goModifyPositionArchitecture, "修改岗位结构");
		break;
	  default:
		//dojo.debug("strange, how did u make it happen?");
	}
}
//让当前页面的函数变成红色的
myNavigator.setButtonStyles = function(buttonName,panelName)
{
	var buttons = document.getElementsByTagName("li");
	
	for (var i = 0;i < buttons.length; i=i+1)
	{
		var button = buttons[i];
		//alert(dojo.html.getClasses(button));
		dojo.html.removeClass(button, "item_selected");
		if(!dojo.html.hasClass(button,"darkitem"))
		{
			if(button.id === buttonName)
			{
				dojo.html.addClass(button,"item_selected");				
			}
		}
		else
		{
			dojo.html.addClass(button,"dark_item");
			button.style["color"]="#808080";
		}
		
	}
	dojo.debug(panelName);
	if(parseInt(panelName))
	{			
		dojo.widget.widgets[0].selectChild(dojo.widget.widgets[6+parseInt(panelName)]);
	}
	else
	{
		dojo.widget.widgets[0].selectChild(dojo.widget.widgets[6]);
	}
};
