//由root来放行全校性的信息
var ReleaseInformation =  {
	getAppliedPublicInfoURL : "pubinfo.do?method=getAppliedPublicInfo",
	showAppliedPublicInfoDetailURL : "pubinfo.do?method=showAppliedPublicInfoDetail",
	approvePublicInfoURL : "pubinfo.do?method=approvePublicInfo"
};
//init函数,查找有哪些需要审批的
ReleaseInformation.init = function(){
	dojo.io.bind({
		url: ReleaseInformation.getAppliedPublicInfoURL,
		preventCache:true,
		handler : function(type, data, evt){
			//dojo.debug(data);
			if(basicHandler(type, data, evt))
			{
				var lists = eval("(" + data + ")");
				ReleaseInformation.generateTable(lists);
			}
			loading(false);
		}
		
	});
};
//生成需要审批的列表
ReleaseInformation.generateTable = function(lists){
	var container = dojo.byId("applyingPubInfos");
	
	if(lists.length === 0)
	{
		container.innerHTML = "no apply";
		return;
	}
	
	var table = document.createElement("table");
	table.border = "1";
	table.style.width="100%";
	dojo.html.addClass(table,"mytable");
	var tr = table.insertRow(-1);
	var td = tr.insertCell(-1);
	td.style.width = "10%";
	td.innerHTML = "申请发起部门";
	td = tr.insertCell(-1);
	td.innerHTML = "标题";
	td = tr.insertCell(-1);
	td.style.width = "20%";
	td.innerHTML = "申请发起时间";
	td = tr.insertCell(-1);
	td.style.width = "7%";
	td.innerHTML = "查看详情";
	td = tr.insertCell(-1);
	td.style.width = "4%";
	td.innerHTML = "同意";
	td = tr.insertCell(-1);
	td.style.width = "4%";
	td.innerHTML = "拒绝";
	
	for (var i=0;i<lists.length;i=i+1)
	{
		tr = table.insertRow(-1);
		tr.setAttribute("id", lists[i].id);
		td = tr.insertCell(-1);
		td.innerHTML = lists[i].ownercatname;
		td = tr.insertCell(-1);
		td.innerHTML = lists[i].infotitle;
		td = tr.insertCell(-1);
		td.innerHTML = lists[i].publishtime;
		td = tr.insertCell(-1);
		td.innerHTML = "<a href=\"javascript:ReleaseInformation.showAppliedPublicInfoDetail('" + lists[i].id + "')\">详情</a>";
		td=tr.insertCell(-1);
		td.innerHTML="<a href=\"javascript:ReleaseInformation.approvePublicInfo('"+lists[i].id+"',true)\">同意</a>";
		td=tr.insertCell(-1);
		td.innerHTML="<a href=\"javascript:ReleaseInformation.approvePublicInfo('"+lists[i].id+"',false)\">拒绝</a>";
		
	}
	
	container.innerHTML = "";
	container.appendChild(table);
};
//查看某个申请的详情
ReleaseInformation.showAppliedPublicInfoDetail = function(id)
{
	dojo.io.bind({
		url: ReleaseInformation.showAppliedPublicInfoDetailURL,
		preventCache: true,
		handler : function(type, data ,evt){
			if(basicHandler(type, data, evt))
			{
				//dojo.debug(data);
				var detail = eval("("+data+")");
				//var container = dojo.byId("details");
				var container = document.createElement("div");
				
				
					var table = document.createElement("table");
					var tr = table.insertRow(-1);
					var td = tr.insertCell(-1);
					td.style.width = "54px";
					td.innerHTML = "信息名称";
					td = tr.insertCell(-1);
					td.innerHTML = detail.infotitle;
					
					tr = table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "信息来源";
					td = tr.insertCell(-1);
					td.innerHTML = detail.userInfo.name;
					
					tr = table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "发布时间";
					td = tr.insertCell(-1);
					td.innerHTML = detail.publishtime;
					
					tr = table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "信息内容";
					td = tr.insertCell(-1);
					td.innerHTML = detail.infocontent;
					
					container.appendChild(table);
					//dojo.lfx.wipeIn(container,500).play();
				//}).play();
				//计算位置
				if(!ReleaseInformation.lastWindowPos)
				{
					//create the first window, set init positions
					ReleaseInformation.lastWindowPos = {};
					ReleaseInformation.lastWindowPos.top = 350;
					ReleaseInformation.lastWindowPos.left = 400;
					ReleaseInformation.lastWindowPos.offsetX = 70;
					ReleaseInformation.lastWindowPos.offsetY = 20;
				}
				else
				{
					//add offsets;
					ReleaseInformation.lastWindowPos.top += ReleaseInformation.lastWindowPos.offsetY;
					ReleaseInformation.lastWindowPos.left += ReleaseInformation.lastWindowPos.offsetX;
					container.style.position = "absolute";
				}
				
				var view = dojo.html.getViewport();
				container.style.width = view.width/3 + "px";
				container.style.height = view.height/3 + "px";
				container.style.top = ReleaseInformation.lastWindowPos.top + "px";
				container.style.left = ReleaseInformation.lastWindowPos.left + "px";
				container.style.position = "absolute";
				//生成float panel
				document.body.appendChild(container);
				var params = {
					title : detail.infotitle,
					displayCloseAction : true,
					toggle : "fade",
					hasShadow : true
				};
				var widget = dojo.widget.createWidget("FloatingPane", params,container);
			}
		},
		content : {
			"id" : id
		}
	});
};

//当点击同意或者否决的时候的函数
ReleaseInformation.approvePublicInfo = function(id, status)
{
	//因为可能比较慢,所以用dialog锁住屏幕
	loading(true);
	dojo.io.bind({url: ReleaseInformation.approvePublicInfoURL, 
		handler : function(type,data,evt){
			if(basicHandler(type, data, evt))
			{
				if (data == "true") {
					dojo.lfx.wipeOut(id+"",250).play();
					postMessage("审批第"+id+"号申请成功!","MESSAGE");
				} else {
					//dojo.lfx.wipeOut(id+"",250).play();
					postMessage("审批第"+id+"号申请失败,请联系管理员!","FATAL");
				}	
			}
			loading(false,1);	
		},
		preventCache : true,
		content:{
			"id" : id,
			"status" : status}
	});
};


var content = dojo.widget.byId("main");
content.onLoad = ReleaseInformation.init;

