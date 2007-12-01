var Inbox ={
	initURL : "pubinfo.do?method=basicInfoStatus",
	getPageURL : "pubinfo.do?method=getPage",
	showDetailURL : "pubinfo.do?method=showDetail",
	replyURL : "pubinfo.do?method=setReply",
	deleteItemURL : "pubinfo.do?method=delete",
	//used as const var
	itemCountPerPage : 17,
	//the number of pages in three panel
	currentPages : {
		info : 0,
		task : 0,
		bulletin : 0
	},
	//how many items are not read.
	//first set in init's callback
	notReadCount : {
		inboxItemCount : 0,
		inboxNewItemCount : 0,
		taskItemCount : 0,
		taskNewItemCount : 0,
		taskNotRepliedItemCount : 0,
		bulletinCount : 0,
		bulletinNewCount : 0
	},
	taskReadStatus : [],
	replystatus : []
};

//init function of the inbox panel
Inbox.init = function (){
	//首先发出异步请求
	//dojo.debug("in inbox's init program");
	dojo.io.bind({
		url : Inbox.initURL,
		preventCache : true,
		handler : function(type, data, evt){
			if(basicHandler(type, data, evt))
			{
				//dojo.debug(data);
				var basicdata = eval("(" + data + ")");
				//设置"未读"标记
				Inbox.notReadCount = basicdata;
				Inbox.updateNotifier();
				//请求第一页的列表
				if(basicdata.taskItemCount >=Inbox.itemCountPerPage)
				{
					Inbox.getPage(1,0,Inbox.itemCountPerPage-1);
				}
				else
				{
					Inbox.getPage(1,0,basicdata.taskItemCount);
				}
				if(basicdata.inboxItemCount >=Inbox.itemCountPerPage)
				{
					Inbox.getPage(0,0,Inbox.itemCountPerPage-1);
				}
				else
				{
					Inbox.getPage(0,0,basicdata.inboxItemCount);
				}
				if(basicdata.bulletinCount >=Inbox.itemCountPerPage)
				{
					Inbox.getPage(2,0,Inbox.itemCountPerPage-1);
				}
				else
				{
					Inbox.getPage(2,0,basicdata.bulletinCount);
				}
			}
			loading(false);
		}
	});
};

//一个简化了的init函数,只获取index.jsp里头看到的收件箱里头的是否还有新的信,不区分具体是什么类型的.
Inbox.simpleInit = function (){
	//dojo.debug("in inbox's simple init program");
	dojo.io.bind({
		url : Inbox.initURL,
		preventCache : true,
		handler : function(type, data, evt){
			if(basicHandler(type, data, evt))
			{
				//dojo.debug(data);
				var basicdata = eval("(" + data + ")");
				Inbox.notReadCount = basicdata;
				var notifier1 = "收件箱";
				if(Inbox.notReadCount.taskNewItemCount==0&&Inbox.notReadCount.inboxNewItemCount==0&&Inbox.notReadCount.bulletinNewCount==0)
				{
					notifier1 += "(<span style='color:grey'>无新邮件</span>)";
				}
				else
				{
					notifier1 += "(<span style='color:red'>有新邮件</span>)";
				}
				
				dojo.byId("inbox").innerHTML = notifier1;
	
				//dojo.byId("inbox").innerHTML = "收件箱("+basicdata.taskNewItemCount+"&"+basicdata.inboxNewItemCount+")";
				
			}
		}
	});
};

//发出请求某类别的某一页的请求
Inbox.getPage = function (type, low, high){

	dojo.io.bind({
	url : Inbox.getPageURL,
	preventCache : true,
	handler : function(loadtype, data, evt){
		//dojo.debug(data);
		if(basicHandler(loadtype, data, evt))
		{
			//发出请求后,把返回的数据送给处理函数
			if(type == 0)
			{
				//for info
				var infoContainer = dojo.byId("infos");
				infoContainer.innerHTML = "";
				var InfoData = eval("(" + data + ")");
				Inbox.currentPages.info = low/Inbox.itemCountPerPage;
				Inbox.generateTable(type, infoContainer, InfoData);
			}
			if(type == 1)
			{
				//for tasks
				var taskContainer = dojo.byId("tasks");
				taskContainer.innerHTML = "";
				var taskData = eval("(" + data + ")");
				Inbox.currentPages.task = low/Inbox.itemCountPerPage;
				Inbox.generateTable(type, taskContainer, taskData);
			}
			if(type == 2)
			{
				//for bulletin
				var bulletinContainer = dojo.byId("bulletins");
				bulletinContainer.innerHTML = "";
				var bulletinData = eval("(" + data + ")");
				Inbox.currentPages.bulletin = low/Inbox.itemCountPerPage;
				Inbox.generateTable(type, bulletinContainer, bulletinData);
			}
		}
	},
	content : {
		"type" : type,
		"low" : low,
		"high" : high
	}
	});
};

//画出基本的信件列表,并顺便调用翻页代码
Inbox.generateTable = function(type, container, data){
	//dojo.debug("in Inbox.generateTable "+type+data.length);
	if(type == 0)
	{
		//for info
		var table = document.createElement("table");
		table.style.width="100%";
		for(var i = 0;i<data.length;i++)
		{
			var item = data[i];
			var tr = table.insertRow(-1);
			tr.style.cursor = "pointer";
			//dojo.debug(item.title+"'s read status is "+item.readstatus);
			//set row's style
			Inbox.setRowReadStatus(tr,item.readstatus);
			tr.setAttribute("id","info"+item.id);
			//tr.setAttribute("read",item.readstatus);
			Inbox.taskReadStatus[item.id] = item.readstatus;
			var td = tr.insertCell(-1);
			td.width = "20px";
			td.innerHTML = "<input type='checkbox' id='checkbox0_"+item.id+"' onchange='Inbox.setOneRowSelected(\"info"+item.id+"\",this.checked)' />";
			tr.style.background = "#E8EEF7";
			var td = tr.insertCell(-1);
			td.innerHTML = "<div onclick='Inbox.showDetail("+item.id+",0)'>"+item.title+"</div>";
			
			var td = tr.insertCell(-1);
			td.width = "150px";
			td.innerHTML = "<div onclick='Inbox.showDetail("+item.id+",0)'>"+item.time+"</div>";
		}
		container.appendChild(table);
	}
	if(type == 1)
	{
		//for tasks
		var table = document.createElement("table");
		table.style.width="100%";
		for(var i = 0;i<data.length;i++)
		{
			var item = data[i];
			var tr = table.insertRow(-1);
			tr.style.cursor = "pointer";
			Inbox.replystatus[item.id] = item.replystatus;
			Inbox.setRowReadStatus(tr,item.readstatus);
			tr.setAttribute("id","task"+item.id);
			Inbox.taskReadStatus[item.id] = item.readstatus;
			//tr.setAttribute("read",item.readstatus);
			var td = tr.insertCell(-1);
			td.width = "20px";
			td.innerHTML = "<input type=\"checkbox\" id=\"checkbox1_"+item.id+"\" onchange=\"Inbox.setOneRowSelected('task"+item.id+"',this.checked)\" />";
			tr.style.background = "#E8EEF7";
			var td = tr.insertCell(-1);
			td.innerHTML = "<div onclick='Inbox.showDetail("+item.id+",1,"+item.replystatus+")'>"+item.title+"</div>";
			
			var td = tr.insertCell(-1);
			td.width = "80px";
			
			td.innerHTML = (Inbox.replystatus[item.id]=="true")?"<div id=\"rs"+item.id+"\">已经回复</div>":"<div id=\"rs"+item.id+"\">尚未回复</div>";
		
			var td = tr.insertCell(-1);
			td.width = "150px";
			td.innerHTML = "<div onclick='Inbox.showDetail("+item.id+",1,"+item.replystatus+")'>"+item.time+"</div>";
		}
		container.appendChild(table);
	}
	if(type == 2)
	{
		//for bulletins
		var table = document.createElement("table");
		table.style.width="100%";
		for(var i = 0;i<data.length;i++)
		{
			var item = data[i];
			var tr = table.insertRow(-1);
			tr.style.cursor = "pointer";
			Inbox.setRowReadStatus(tr,item.readstatus);
			tr.setAttribute("id","bulletin"+item.id);
			//tr.setAttribute("read",item.readstatus);
			Inbox.taskReadStatus[item.id] = item.readstatus;
			var td = tr.insertCell(-1);
			td.width = "20px";
			td.innerHTML = "<input type=\"checkbox\" id=\"checkbox2_"+item.id+"\" onchange=\"Inbox.setOneRowSelected('bulletin"+item.id+"',this.checked)\" />";
			tr.style.background = "#E8EEF7";
			var td = tr.insertCell(-1);
			td.innerHTML = "<div onclick='Inbox.showDetail("+item.id+",2)'>"+item.title+"</div>";
			
				
			var td = tr.insertCell(-1);
			td.width = "150px";
			td.innerHTML = "<div onclick='Inbox.showDetail("+item.id+",2)'>"+item.time+"</div>";
		}
		container.appendChild(table);
	}
	//创建翻页的那一条
	container.appendChild(Inbox.generatePagingTool(type));
	//创建删除函数
	container.appendChild(Inbox.generateDelButton(type));
	dojo.html.setOpacity("del_"+type,0.3);
};

//创建翻页的函数
Inbox.generatePagingTool = function(type)
{
	//dojo.debug("in generatePagingTool"+type);
	var div = document.createElement("div");
	dojo.html.addClass(div,"paging");
	
	if(type==0)
	{
		if(Inbox.itemCountPerPage > Inbox.notReadCount.inboxItemCount)
		{
			return div;
		}
		//for info
		var pageCount = 0;
		while(pageCount*Inbox.itemCountPerPage < Inbox.notReadCount.inboxItemCount)
		{
			pageCount++;
			var item = null;
			if(Inbox.currentPages.info == pageCount-1)
			{
				//for currentPage
				item = "&nbsp;"+(pageCount)+"&nbsp;";
				//dojo.debug("currentPage "+item);
				
			}
			else
			{
				var low = (pageCount-1)*Inbox.itemCountPerPage;
				var high = (pageCount*Inbox.itemCountPerPage-1) < Inbox.notReadCount.inboxItemCount ? (pageCount*Inbox.itemCountPerPage-1) : Inbox.notReadCount.inboxItemCount;
				item = "&nbsp;<a href='javascript:Inbox.getPage(0,"+low+","+high+")'>"+pageCount+"</a>&nbsp;";
			}
			div.innerHTML+=item;
		}
	}
	
	if(type==1)
	{
		//for task
		if(Inbox.itemCountPerPage > Inbox.notReadCount.taskItemCount)
		{
			return div;
		}
		var pageCount = 0;
		while(pageCount*Inbox.itemCountPerPage < Inbox.notReadCount.taskItemCount)
		{
			pageCount++;
			var item = null;
			if(Inbox.currentPages.task == pageCount-1)
			{
				//for currentPage
				item = "&nbsp;"+(pageCount)+"&nbsp;";
				//dojo.debug("currentPage "+item);
				
			}
			else
			{
				var low = (pageCount-1)*Inbox.itemCountPerPage;
				var high = (pageCount*Inbox.itemCountPerPage-1) < Inbox.notReadCount.taskItemCount ? (pageCount*Inbox.itemCountPerPage-1) : Inbox.notReadCount.taskItemCount;
				item = "&nbsp;<a href='javascript:Inbox.getPage(1,"+low+","+high+")'>"+pageCount+"</a>&nbsp;";
			}
			div.innerHTML+=item;
		}
	}
	
	if(type==2)
	{
		//for task
		if(Inbox.itemCountPerPage > Inbox.notReadCount.bulletinCount)
		{
			return div;
		}
		var pageCount = 0;
		while(pageCount*Inbox.itemCountPerPage < Inbox.notReadCount.bulletinCount)
		{
			pageCount++;
			var item = null;
			if(Inbox.currentPages.bulletin == pageCount-1)
			{
				//for currentPage
				item = "&nbsp;"+(pageCount)+"&nbsp;";
				//dojo.debug("currentPage "+item);
				
			}
			else
			{
				var low = (pageCount-1)*Inbox.itemCountPerPage;
				var high = (pageCount*Inbox.itemCountPerPage-1) < Inbox.notReadCount.bulletinCount ? (pageCount*Inbox.itemCountPerPage-1) : Inbox.notReadCount.bulletinCount;
				item = "&nbsp;<a href='javascript:Inbox.getPage(2,"+low+","+high+")'>"+pageCount+"</a>&nbsp;";
			}
			div.innerHTML+=item;
		}
	}
	return div;
}
//创建删除按钮
Inbox.generateDelButton = function(type)
{
	var div = document.createElement("div");
	divid = "del_"+type;
	div.innerHTML = "<div id='"+divid+"' onmouseover='Inbox.onDelButtonOver(\""+divid+"\")' onmouseout='Inbox.onDelButtonOut(\""+divid+"\")'><input type='button' value='删除' onclick='Inbox.deleteItem("+type+")' /></div>"
	dojo.html.addClass(div,"delButton");
	//dojo.event.connect(div, 'onmouseover', 'Inbox.onDelButtonOver('+div.id+')');
//	div.setAttribut("onmouseover","Inbox.onDelButtonOver(div.id)");
//	div.onmouseout = Inbox.onDelButtonOut(div.id);
	return div;
}
//鼠标移至删除按钮上的响应函数
Inbox.onDelButtonOver = function(nodeName)
{
//	//dojo.debug("mouse over "+nodeName);
	var anim = dojo.lfx.propertyAnimation(
			[nodeName],
			[
				{ property: "opacity", end: 1}		
			],
			300,
			dojo.lfx.easeInOut
		).play();
	if(!!(window.attachEvent && !window.opera))
	{
		//browser is IE
		dojo.html.setOpacity(nodeName,1);
	}
}
//鼠标移出删除按钮上的响应函数
Inbox.onDelButtonOut = function(nodeName)
{
//	//dojo.debug("mouse out "+nodeName);
	var anim = dojo.lfx.propertyAnimation(
			[nodeName],
			[
				{ property: "opacity", end: 0.3}				
			],
			300,
			dojo.lfx.easeInOut
		).play();
	if(!!(window.attachEvent && !window.opera))
	{
		//browser is IE
		dojo.html.setOpacity(nodeName,0.3);
	}
		
}
//设置某一行的样式(已读未读)
Inbox.setRowReadStatus = function(tr, readstatus)
{
	//dojo.debug("set "+tr.id+"to "+readstatus);
	if(readstatus==="true")
	{
		dojo.html.addClass(tr,"readRow");
		
	}
	if(readstatus==="false")
	{
		dojo.html.addClass(tr,"notReadRow");
	//	tr.style["font-weight"] = "bold";
	}
}
//当某一行被选中后的样式变化
Inbox.setOneRowSelected = function(rowId, status){
	if(status)
	{
		dojo.byId(rowId).style.background = "#FFFFCC";
	}
	else
	{
		dojo.byId(rowId).style.background = "#E8EEF7";
	}
}

//当点击某一行时的响应函数
Inbox.showDetail = function (id, type, needReply){
	//dojo.debug("show detail"+id+ " "+type+"typeof type is "+typeof(type));
	var trid = null;
	switch (type)
	{
		case 0:
			trid = "info";
			break;
		case 1:
			trid = "task";
			break;
		case 2:
			trid = "bulletin";
			break;
	}
	//设定一些样式
	var tr = dojo.byId(trid+id);
	//dojo.debug(tr);
	dojo.html.removeClass(tr,"notReadRow");
	dojo.html.addClass(tr,"readRow");
	//dojo.debug(tr.getAttribute("read"));
	if(Inbox.taskReadStatus[id] == "false")
	{
		Inbox.taskReadStatus[id] = "true";
		//dojo.debug("lahblahlhlbhlab"+Inbox.notReadCount.taskNewItemCount);
		switch (type)
		{
			case 0:
				Inbox.notReadCount.inboxNewItemCount --;
				break;
			case 1:
				Inbox.notReadCount.taskNewItemCount --;
				break;
			case 2:
				Inbox.notReadCount.bulletinNewCount --;
				break;
		}
		//dojo.debug("lahblahlhlbhlab"+Inbox.notReadCount.taskNewItemCount);
	}
	Inbox.updateNotifier();
	//发出请求
	dojo.io.bind({
		url : Inbox.showDetailURL,
		preventCache : false,
		handler : function(loadtype, data, evt){
			if(basicHandler(loadtype, data, evt))
			{
			if(type == 0)
			{
				//for info
				var infos = dojo.byId("infos");
				var infoDetail = dojo.byId("infoDetail");
				infoDetail.innerHTML = "";
				
				
				dojo.html.hide(dojo.byId("infoDetail"));
				var wipeOut = dojo.lfx.wipeOut("infos", 300, null, function(n) {
					var div = document.createElement("div");
					div.innerHTML = "<a href=\"javascript:Inbox.goBacktoInfo()\" style=\"color:black;\"><strong>返回信息箱</strong></a>";
					infoDetail.appendChild(div);
					
					var item = eval("(" + data + ")");
					var table = document.createElement("table");
					dojo.html.addClass(table,"inboxTable");
					var tr =table.insertRow(-1);
					var td = tr.insertCell(-1);
					td.innerHTML = "名称";
					td.style.width="50px";
					
					var td = tr.insertCell(-1);
					td.innerHTML = item.title;
					tr =table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "时间";
					var td = tr.insertCell(-1);
					td.innerHTML = item.time;
					tr =table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "来源";
					var td = tr.insertCell(-1);
					td.innerHTML = item.author;
					tr =table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "内容";
					var td = tr.insertCell(-1);
					td.innerHTML = item.content;
					//div = document.createTextNode("得到数据: "+data);
					infoDetail.appendChild(table);
					dojo.lfx.wipeIn("infoDetail",300).play();
				});
				wipeOut.play();
				
							
			}
			if(type == 1)
			{
				var tasks = dojo.byId("tasks");
				var taskDetail = dojo.byId("taskDetail");
				taskDetail.innerHTML = "";
				
				dojo.html.hide(dojo.byId("taskDetail"));
				var wipeOut = dojo.lfx.wipeOut("tasks", 300, null, function(n) {
					var div = document.createElement("span");
					div.innerHTML = "<a href=\"javascript:Inbox.goBacktoTask()\" style='color:black;'><strong>返回任务箱</strong></a>　　　　　";
					taskDetail.appendChild(div);
					//dojo.debug("id :" + id+"type of is"+typeof(id));
					//dojo.debug("Inbox.replystatus[id] : "+Inbox.replystatus[id]+"and type is"+typeof(Inbox.replystatus[id]));
					if(Inbox.replystatus[id] == "false")
					{
						div = document.createElement("span");
						div.id = "replyTask";
						div.innerHTML = "<a href=\"javascript:Inbox.reply("+id+")\" style='color:red;text-decoration:none' >回复本任务</a>";
						taskDetail.appendChild(div);
						Inbox.replystatus[id] = true;
					}
					var item = eval("(" + data + ")");
					var table = document.createElement("table");
					dojo.html.addClass(table,"inboxTable");
					var tr =table.insertRow(-1);
					var td = tr.insertCell(-1);
					td.innerHTML = "名称";
					td.style.width="50px";
					var td = tr.insertCell(-1);
					td.innerHTML = item.title;
					tr =table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "时间";
					var td = tr.insertCell(-1);
					td.innerHTML = item.time;
					tr =table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "来源";
					var td = tr.insertCell(-1);
					td.innerHTML = item.author;
					tr =table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "内容";
					var td = tr.insertCell(-1);
					td.innerHTML = item.content;
					//div = document.createTextNode("得到数据: "+data);
					taskDetail.appendChild(table);
					dojo.lfx.wipeIn("taskDetail",300).play();
				});
				wipeOut.play();
			}
			if(type == 2)
			{
				var bulletins = dojo.byId("bulletins");
				var bulletinDetail = dojo.byId("bulletinDetail");
				bulletinDetail.innerHTML = "";
				
				dojo.html.hide(dojo.byId("bulletinDetail"));
				var wipeOut = dojo.lfx.wipeOut("bulletins", 300, null, function(n) {
					var div = document.createElement("div");
					div.innerHTML = "<a href=\"javascript:Inbox.goBacktoBulletin()\" style='color:black;'><strong>返回公告箱</strong></a>";
					bulletinDetail.appendChild(div);
					var item = eval("(" + data + ")");
					var table = document.createElement("table");
					dojo.html.addClass(table,"inboxTable");
					var tr =table.insertRow(-1);
					var td = tr.insertCell(-1);
					td.innerHTML = "名称";
					td.style.width="50px";
					var td = tr.insertCell(-1);
					td.innerHTML = item.title;
					tr =table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "时间";
					var td = tr.insertCell(-1);
					td.innerHTML = item.time;
					tr =table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "来源";
					var td = tr.insertCell(-1);
					td.innerHTML = item.author;
					tr =table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "内容";
					var td = tr.insertCell(-1);
					td.innerHTML = item.content;
					//div = document.createTextNode("得到数据: "+data);
					bulletinDetail.appendChild(table);
					dojo.lfx.wipeIn("bulletinDetail",300).play();
				});
				wipeOut.play();
			}
		}
		},
		content : {
			type : type,
			id : id
		}
	});
	
};
//更新未读标记
Inbox.updateNotifier = function() {
	var s = "";
	for( var prop in Inbox.notReadCount)
	{
		s+="Inbox.notReadCount["+prop+"] = "+Inbox.notReadCount[prop]+"\n";
	}
	//dojo.debug(s);
	var notifier1 = "收件箱";
	if(Inbox.notReadCount.taskNewItemCount==0&&Inbox.notReadCount.inboxNewItemCount==0&&Inbox.notReadCount.bulletinNewCount==0)
	{
		notifier1 += "(<span style='color:grey'>无新邮件</span>)";
	}
	else
	{
		notifier1 += "(<span style='color:red'>有新邮件</span>)";
	}
	var notifier2 = "任务箱";
	if(Inbox.notReadCount.taskNewItemCount!=0||Inbox.notReadCount.taskNotRepliedItemCount!=0)
	{
		notifier2 += "("+Inbox.notReadCount.taskNewItemCount+"/"+Inbox.notReadCount.taskNotRepliedItemCount+")";
	}
	var notifier3 = "信息箱";
	if(Inbox.notReadCount.inboxNewItemCount!=0)
	{
		notifier3 += "("+Inbox.notReadCount.inboxNewItemCount+")";
	}
	var notifier4 = "公告箱";
	if(Inbox.notReadCount.bulletinNewCount!=0)
	{
		notifier4 += "("+Inbox.notReadCount.bulletinNewCount+")";
	}
	dojo.byId("inbox").innerHTML = notifier1;
	dojo.widget.byId("taskTab").controlButton.titleNode.innerHTML = notifier2;
	dojo.widget.byId("infoTab").controlButton.titleNode.innerHTML = notifier3;
	dojo.widget.byId("bulletinTab").controlButton.titleNode.innerHTML = notifier4;
};

//后退按钮
Inbox.goBacktoInfo = function()
{
	dojo.lfx.chain(
		dojo.lfx.wipeOut("infoDetail", 300),
		dojo.lfx.wipeIn("infos", 300)
	).play();
}
//后退按钮
Inbox.goBacktoTask = function()
{
	dojo.lfx.chain(
		dojo.lfx.wipeOut("taskDetail", 300),
		dojo.lfx.wipeIn("tasks", 300)
	).play();
}
//后退按钮
Inbox.goBacktoBulletin = function()
{
	dojo.lfx.chain(
		dojo.lfx.wipeOut("bulletinDetail", 300),
		dojo.lfx.wipeIn("bulletins", 300)
	).play();
}
//回复某个任务
Inbox.reply = function (id){
	dojo.io.bind({
		url : Inbox.replyURL,
		preventCache : true,
		content : {
			id : id
		},
		handler : function(type, data, evt)
		{
			if(basicHandler(type, data, evt))
			{
				if(data=="true")
				{
					postMessage("回复成功!");
					dojo.byId("rs"+id).innerHTML = "已经回复";
					//dojo.lfx.wipeOut("task"+id, 1);
					dojo.lfx.fadeOut("replyTask",300,null, function(){
						dojo.html.hide("replyTask");
					}).play();
					Inbox.notReadCount.taskNotRepliedItemCount --;
					Inbox.updateNotifier();
				}
			}
		}
	});
};
//删除函数
Inbox.deleteItem = function (type){
	//dojo.debug("del! type="+type);
	var checkboxes = document.getElementsByTagName("input");
	var idlists = "";
	for(var i in checkboxes)
	{
		var checkbox = checkboxes[i];
		if(checkbox.checked&&(checkbox.id.indexOf("checkbox"+type+"_")!=-1))
		{
			//dojo.debug(checkbox.id);
			idlists += checkbox.id.replace("checkbox"+type+"_","")+",";
		}
	}
	if(confirm("are u sure to delete?"))
	{
		dojo.io.bind({
			url: Inbox.deleteItemURL,
			preventCache: true,
			content : {
				type : type,
				ids : idlists
			},
			handler : function(type, data, evt){
				if(basicHandler(type, data, evt))
				{
				if(data=="true")
				{
					postMessage("回复成功!");
					Inbox.init();
				}
			}
			}
		});
	}
};
