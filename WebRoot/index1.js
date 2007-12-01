var Misc = {
	getInformationURL : "misc.do?method=getMiscInfo"
}
//获得首页的信息，比如用户信息、能力值、任职历史之类的。。
Misc.getInformation = function() {
	dojo.debug("in on appointposition");
	if(!!(window.attachEvent && !window.opera))
	{
		//browser is IE
		dojo.html.addClass(dojo.byId("seperator"), "seperator1");
	}
	else
	{
		dojo.html.addClass(dojo.byId("seperator"), "seperator");
	}
	
	SetCookie("lastModule","ShowInformation");
	dojo.io.bind({url:Misc.getInformationURL, handler:Misc.getInformationCallBack, content:{}, preventCache:true});
	dojo.require("dojo.lfx.*");
	dojo.require("dojo.lfx.extras");
	dojo.require("dojo.event.*");
	dojo.lfx.fadeIn("abilities", 800).play(300);
	dojo.lfx.fadeOut("userinfo", 10).play();
	dojo.lfx.wipeOut("currentPos", 10).play();
	dojo.lfx.wipeOut("historicalPos", 10).play();
}

//根据返回的信息设定页面上的样式
Misc.getInformationCallBack = function(type, data, evt) {
	if (basicHandler(type, data, evt))
	{
		//dojo.debug("get data: " + data);
		var bigdata = eval("(" + data + ")");
		Misc.setBasicInformation(bigdata[0]);
		Misc.setAbilities(bigdata[1]);
		Misc.setCurrentPos(bigdata[2]);
		Misc.setOldPos(bigdata[3]);
		dojo.lfx.fadeIn("userinfo", 500).play();
		dojo.lfx.wipeIn("currentPos", 500).play();
		dojo.lfx.wipeIn("historicalPos", 500).play();
	
	}
}
//设定姓名、图片、学号、班号。
Misc.setBasicInformation = function(data) {
	dojo.byId("photo").src = data.pic;
	dojo.byId("name").innerHTML = data.name;
	dojo.byId("stunum").innerHTML = data.stunum;
	dojo.byId("classnum").innerHTML = data.dept + data.classnum;
}
//设定6项能力值，并动画的拉长他们
Misc.setAbilities = function(data) {
	dojo.require("dojo.lfx.*");
	dojo.require("dojo.lfx.extras");
	var a1 = data.ability1*239;
	dojo.lfx.scale("a1", a1, true, false, 2500).connect("onPlay", function(){}).play(1000);
	var a2 = data.ability2*239;
	dojo.lfx.scale("a2", a2, true, false, 2500).connect("onPlay", function(){}).play(1000);
	var a3 = data.ability3*239;
	dojo.lfx.scale("a3", a3, true, false, 2500).connect("onPlay", function(){}).play(1000);
	var a4 = data.ability4*239;
	dojo.lfx.scale("a4", a4, true, false, 2500).connect("onPlay", function(){}).play(1000);
	var a5 = data.ability5*239;
	dojo.lfx.scale("a5", a5, true, false, 2500).connect("onPlay", function(){}).play(1000);
	var a6 = data.ability6*239;
	dojo.lfx.scale("a6", a6, true, false, 2500).connect("onPlay", function(){}).play(1000);
};
//设定当前职位
Misc.setCurrentPos = function(data) {
	var container = dojo.byId("currentPos");
	for (var i = 0; i < data.currentpos.length; i = i + 1) {
		var li = document.createElement("li");
		li.innerHTML = data.currentpos[i].posname;
		container.appendChild(li);
	}
};
//设定以前的职位
Misc.setOldPos = function(data) {
	var container = dojo.byId("historicalPos");
	for (var i = 0; i < data.posrecord.length; i = i + 1) {
		var li = document.createElement("li");
		li.innerHTML = data.posrecord[i].posname;
		container.appendChild(li);
	}
};
//当点击首页的logo时的响应函数
Misc.goIndex = function() {
	dojo.debug("in go index!");
	SetCookie("lastModule","ShowInformation");
	SetCookie("lastPanel","");
	var content = dojo.widget.byId("main");
	content.setUrl(pathRelative + "index1.html");
	content.onLoad = Misc.getInformation;
	myNavigator.setButtonStyles("");
};

var content = dojo.widget.byId("main");
content.onLoad = Misc.getInformation;
//content.onLoad = Misc.goIndex;

/*content.onLoad = function(){
	Inbox.simpleInit();
	window.setInterval(Inbox.simpleInit, 1000*60*5);
	
	var lastModule = GetCookie("lastModule");
	if((!lastModule)||lastModule=="getInformation")
	{
		Misc.getInformation();
		var appState = new ApplicationState(Misc.goIndex,"go index!");
		appState.showStateData();
		dojo.undo.browser.setInitialState(appState);
		//dojo.debug("Set init state");
	}
	else
	{
		fireNavigatorButtonEvent(lastModule);
		myNavigator.lastPage = lastModule;
	}
};
*/