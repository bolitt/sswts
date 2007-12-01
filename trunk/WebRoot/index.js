
var basicHandler = function (type, data, evt)
{
//	dojo.debug("in basic");
	if(type == "error")
	{
		postMessage("系统故障，请稍候再试","FATAL");
		dojo.debug(type+" "+data+" "+evt);
		return false;
	}
	if(type == "load")
	{
	//	dojo.debug("data="+data);
		//for bad users:
		if(data == "invalid")
		{
			document.location = pathRelative+"error.html";
			return;
		}
		if(data == "false")
		{
			postMessage("执行失败，请稍候再试","FATAL");
			return false;
		}
		
		return true;
		//alert(typeof(this));
		//this.AdvancedHandler(type, data, evt);
	}
};
//显示\隐藏全屏loading对话框
function loading(start, delay)
{
	var mydelay = 50;
	if(delay)
	{
		mydelay =delay;
	}
	if(start)
	{
		dojo.widget.byId("loadingDlg").show();		
	}
	else
	{
		window.setTimeout("dojo.widget.byId(\"loadingDlg\").hide()", mydelay);
	}
}
//发布一条msg
function postMessage(str, lvl)
{
	var level;
	if(lvl)
	{
		level = lvl;
	}
	else
	{
		level = "MESSAGE";
	}
	dojo.event.topic.publish("testMessageTopic", {message:str, type:level, delay:1500});
}

dojo.addOnLoad(function(){
	dojo.debug("in index.js 's addOnload");
	Inbox.simpleInit();
	window.setInterval(Inbox.simpleInit, 1000*60*5);
	var lastModule = GetCookie("lastModule");
	if(!(lastModule&&lastModule.length>0))
	{
		lastModule = "ShowInformation";
		SetCookie("lastModule","ShowInformation");
	}
	
	var lastPanel = GetCookie("lastPanel");
	if(!(lastPanel&&lastPanel.length>0))
	{
		lastPanel = "0";
		SetCookie("lastPanel","0");
	}
	dojo.debug("lastModule ="+lastModule );
	dojo.debug("lastPanel ="+lastPanel );
	
	
	fireNavigatorButtonEvent(lastModule,lastPanel);
	myNavigator.lastPage = lastModule;
});

