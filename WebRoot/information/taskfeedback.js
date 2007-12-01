//查看任务回复
var TaskFeedback = {
getTasksFeedbackListURL:"pubinfo.do?method=getTasksFeedbackList", 
showFeedbackDetailURL:"pubinfo.do?method=showTaskFeedbackDetail", 
moveNewTaskToOldURL : "pubinfo.do?method=moveNewTaskToOld" 
};
//init函数
TaskFeedback.init = function () {
	dojo.io.bind({url:TaskFeedback.getTasksFeedbackListURL, preventCache:true, handler:function (type, data, evt) {
		if (basicHandler(type, data, evt)) {
			//dojo.debug(data);
			var basicdata = eval("(" + data + ")");
			//for current tasks
			var currentTasksContainer = dojo.byId("currentTasks");
			currentTasksContainer.innerHTML = "";
			var currentTasksData = basicdata[0];
			//画出第一个表格
			TaskFeedback.generateTable(0, currentTasksContainer, currentTasksData);
			//for old tasks
			var oldTaskContainer = dojo.byId("oldTasks");
			oldTaskContainer.innerHTML = "";
			var oldTaskData = basicdata[1];
			//第二个表格
			TaskFeedback.generateTable(1, oldTaskContainer, oldTaskData);
			loading(false);
		}
	}});
};
//画表格
TaskFeedback.generateTable = function (type, container, data) {
	//dojo.debug("in TaskFeedback.generateTable " + type + " " + data.length);
	//for current tasks
	var table = document.createElement("table");
	table.style.width = "100%";
	for (var i = 0; i < data.length; i++) {
		var item = data[i];
		var tr = table.insertRow(-1);
		tr.style.cursor = "pointer";
		tr.id = type+ "_" +item.id;
		
		var td = tr.insertCell(-1);
		td.width = "20px";
		td.innerHTML = "<input type='checkbox' id='checkbox" + item.id + "' />";
		tr.style.background = "#E8EEF7";
		var td = tr.insertCell(-1);
		td.innerHTML = "<div onclick='TaskFeedback.showDetail(" + item.id + ","+type+")'>" + item.title + "</div>";
		var td = tr.insertCell(-1);
		td.width = "150px";
		td.innerHTML = "<div onclick='TaskFeedback.showDetail(" + item.id + ","+type+")'>" + item.time + "</div>";
	}
	container.appendChild(table);

	/*
	if (type == 1) {
		//for old tasks
		var table = document.createElement("table");
		table.style.width = "100%";
		for (var i = 0; i < data.length; i++) {
			var item = data[i];
			var tr = table.insertRow(-1);
			tr.style.cursor = "pointer";
			Inbox.replystatus[item.id] = item.replystatus;
			Inbox.setRowReadStatus(tr, item.readstatus);
			tr.setAttribute("id", "task" + item.id);
			Inbox.taskReadStatus[item.id] = item.readstatus;
			//tr.setAttribute("read",item.readstatus);
			var td = tr.insertCell(-1);
			td.width = "20px";
			td.innerHTML = "<input type=\"checkbox\" id=\"checkbox" + item.id + "\" onchange=\"Inbox.setOneRowSelected('task" + item.id + "',this.checked)\" />";
			tr.style.background = "#E8EEF7";
			var td = tr.insertCell(-1);
			td.innerHTML = "<div onclick='Inbox.showDetail(" + item.id + ",1," + item.replystatus + ")'>" + item.title + "</div>";
			var td = tr.insertCell(-1);
			td.width = "80px";
			td.innerHTML = (Inbox.replystatus[item.id] == "true") ? "<div id=\"rs" + item.id + "\">\u5df2\u7ecf\u56de\u590d</div>" : "<div id=\"rs" + item.id + "\">\u5c1a\u672a\u56de\u590d</div>";
			var td = tr.insertCell(-1);
			td.width = "140px";
			td.innerHTML = "<div onclick='Inbox.showDetail(" + item.id + ",1" + item.replystatus + ")'>" + item.time + "</div>";
		}
		container.appendChild(table);
	}
	if (type == 2) {
		//for bulletins
		var table = document.createElement("table");
		table.style.width = "100%";
		for (var i = 0; i < data.length; i++) {
			var item = data[i];
			var tr = table.insertRow(-1);
			tr.style.cursor = "pointer";
			Inbox.setRowReadStatus(tr, item.readstatus);
			tr.setAttribute("id", "bulletin" + item.id);
			//tr.setAttribute("read",item.readstatus);
			Inbox.taskReadStatus[item.id] = item.readstatus;
			var td = tr.insertCell(-1);
			td.width = "20px";
			td.innerHTML = "<input type=\"checkbox\" id=\"checkbox" + item.id + "\" onchange=\"Inbox.setOneRowSelected('bulletin" + item.id + "',this.checked)\" />";
			tr.style.background = "#E8EEF7";
			var td = tr.insertCell(-1);
			td.innerHTML = "<div onclick='Inbox.showDetail(" + item.id + ",2)'>" + item.title + "</div>";
			var td = tr.insertCell(-1);
			td.width = "140px";
			td.innerHTML = "<div onclick='Inbox.showDetail(" + item.id + ",2)'>" + item.time + "</div>";
		}
		container.appendChild(table);
	}*/
};
//查看详情
TaskFeedback.showDetail = function (id, type) {
	//dojo.debug("show detail" + id + " " + type + "typeof type is " + typeof (type));
	
	var tr = dojo.byId(type+ "_" + id);
	//dojo.debug(tr);
	
	dojo.io.bind({url:TaskFeedback.showFeedbackDetailURL, preventCache:false, handler:function (loadtype, data, evt) {
			//if(loadbasicHandler(type, data, evt))
		//dojo.debug(data);
		if (type == 0) {
			//for current task
			var currentTasks = dojo.byId("currentTasks");
			var currentTasksDetail = dojo.byId("currentTasksDetail");
			currentTasksDetail.innerHTML = "";
			dojo.html.hide(dojo.byId("currentTasksDetail"));
			//藏起来一个,show出来第二个
			var wipeOut = dojo.lfx.wipeOut("currentTasks", 300, null, function (n) {
				var div = document.createElement("span");
				div.innerHTML = "<a style='color:black' href=\"javascript:TaskFeedback.goBack('currentTasks','currentTasksDetail')\">\u8fd4\u56de\u4fe1\u606f\u7bb1</a>　　　";
				currentTasksDetail.appendChild(div);
				div = document.createElement("span");
				div.innerHTML = "<a id='mto"+id+"' style='color:red;text-decoration:none' href=\"javascript:TaskFeedback.moveNewTaskToOld("+id+")\">移至过往任务箱</a><br />&nbsp;";
				currentTasksDetail.appendChild(div);
				
				//div = document.createTextNode("\u5f97\u5230\u6570\u636e: " + data);
				div = document.createElement("div");
				currentTasksDetail.appendChild(div);
				
				TaskFeedback.generateDetailTable(div,eval("("+data+")"));
				
				dojo.lfx.wipeIn("currentTasksDetail",300).play();
			});
			wipeOut.play();
		}
		if (type == 1) {
			//for old task
			var oldTasks = dojo.byId("oldTasks");
			var oldTasksDetail = dojo.byId("oldTasksDetail");
			oldTasksDetail.innerHTML = "";
			dojo.html.hide(dojo.byId("oldTasksDetail"));
			var wipeOut = dojo.lfx.wipeOut("oldTasks", 300, null, function (n) {
				var div = document.createElement("div");
				div.innerHTML = "<a style='color:black;' href=\"javascript:TaskFeedback.goBack('oldTasks','oldTasksDetail')\">\u8fd4\u56de\u4fe1\u606f\u7bb1</a><br />&nbsp;";
				oldTasksDetail.appendChild(div);
				
				div = document.createElement("div");
				oldTasksDetail.appendChild(div);
				TaskFeedback.generateDetailTable(div,eval("("+data+")"));
				
				//oldTasksDetail.appendChild(div);
				dojo.lfx.wipeIn("oldTasksDetail",300).play();
			});
			wipeOut.play();
		}
	}, content:{id:id}});
};
var testColumns = null;
var testData = null;
//画出详细内容表格,并令其可以排序
TaskFeedback.generateDetailTable = function(node, data)
{
	var titleDiv = document.createElement("div");
	titleDiv.innerHTML = data.title + "@" + data.time;
	node.innerHTML = "";
	node.appendChild(titleDiv);	
	//dojo.require("dojo.widget.FilteringTable");
	
	var table = document.createElement("table");
	table.style.width= "320px";
	//div.innerHTML = "<table id=\"mytable\" style=\"width: 400px; height: 200px;\"></table>"
	node.appendChild(table);	
	table.border = "1";
	dojo.html.addClass(table,"mytable");
	
	
	 testColumns = [
			{ field: "姓名" },
			{ field: "学号"},
			{ field: "是否回复" }
	];
	
	 testData = [];	
	for(var i = 0; i<data.children.length;i = i+1)
	{
		var item = data.children[i];
		var newItem = {};
		newItem.myId = i;
		newItem["姓名"] = item.name;
		newItem["学号"] = item.stunum;
		newItem["是否回复"] = (item.replied == "false")?"尚未回复":"已经回复";
		testData.push(newItem);		
	}
	
	filteringTable = dojo.widget.createWidget("dojo:FilteringTable",{valueField: "myId"},table);
	
	for (var x = 0; x<testColumns.length; x++) {
				filteringTable.columns.push(filteringTable.createMetaData(testColumns[x]));
			}
	
	filteringTable.store.setData(testData);
	
	
}

TaskFeedback.goBack = function(a , b){
//("+currentTasks+","+currentTasksDetail+") = function () {
	dojo.lfx.chain(dojo.lfx.wipeOut(b, 300), dojo.lfx.wipeIn(a, 300)).play();
};

//将某一个task移至旧的
TaskFeedback.moveNewTaskToOld = function (id) {
	dojo.io.bind({url:TaskFeedback.moveNewTaskToOldURL, preventCache:true, content:{id:id}, handler:function (type, data, evt) {
		if (basicHandler(type, data, evt)&&data == "true") {
			postMessage("移动成功!");
			dojo.lfx.fadeOut("mto"+id,300).play();
			TaskFeedback.init();
		}
	}});
};

var content = dojo.widget.byId("main");
content.onLoad = TaskFeedback.init;

