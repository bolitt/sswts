var TrainingAdmin = {
// urls for retrieving data
ViewTrainingsURL : "training.do?method=getAllPostedTraining",
showTrainingDetailURL : "training.do?method=showTrainingDetail",
getTrainingTokenURL : "training.do?method=getTrainingToken",
downloadTokensURL : "training.do?method=downloadTrainingToken",
PostTrainingURL : "training.do?method=postTraining",
getApprovedTrainingInfoURL:"training.do?method=getApprovedTrainingInfo",
updateATrainingURL : "training.do?method=updateTraining",
delTrainingURL : "training.do?method=deleteTraining",
UploadURL : "training.do?method=upload",
// one flag, indicates whether the current training can be simply deleted (as some people may have already applied it)
currentTraingingCanDelete : true,
viewTrainingPage : null,
editdelTrainingPage : null,
tokenTrainingPage : null,
deadLineShowed : true,
filelist : []
};

TrainingAdmin.setPubStatus = function(isPub){
	//alert("a "+isPub.value);
	if(isPub == 1)
	{
		if(TrainingAdmin.deadLineShowed === false)
		{
			dojo.lfx.wipeIn("deadline1",200).play();
			dojo.lfx.wipeIn("deadline2",200,null,function(){
				TrainingAdmin.deadLineShowed = true;
			}).play();
		}
	}
	else
	{
		if(TrainingAdmin.deadLineShowed == true)
		{
			dojo.lfx.wipeOut("deadline1",200).play();
			dojo.lfx.wipeOut("deadline2",200,null,function(){
				TrainingAdmin.deadLineShowed = false;
			}).play();
		}
	}
};

// load trainings which can be administrated by current user
TrainingAdmin.init = function() {
	//dojo.debug("in loadTrainings");
	var rightTabs = dojo.widget.byId("actions");
	TrainingAdmin.viewTrainingPage = rightTabs.children[0];
	TrainingAdmin.editdelTrainingPage = rightTabs.children[1];
	TrainingAdmin.tokenTrainingPage = rightTabs.children[2];
	
	rightTabs.removeChild(TrainingAdmin.editdelTrainingPage);
	rightTabs.removeChild(TrainingAdmin.tokenTrainingPage);
	dojo.widget.byId("postcode").domNode.childNodes[2].disabled=true;
	dojo.widget.byId("postcode").setAllValues("","");
	
	//fires one xmlhttprequest and retrieves the data
	dojo.io.bind({url:TrainingAdmin.ViewTrainingsURL, preventCache:true, handler:function (type, data, evt) {
		if (basicHandler(type, data, evt)) {
				//do something with data;
			//dojo.debug(data);
			var bigdata = eval("(" + data + ")");
			var currentTrainings = bigdata.currentTraining;
			var pastTrainings = bigdata.pastTraining;
			
			var container = dojo.byId("currentTraining");
			//clean the original table
			container.innerHTML="";
			//if there's no trainings...
			if (currentTrainings.length == 0) {
				var div = document.createElement("div");
				div.innerHTML = "\u5c1a\u6ca1\u6709\u4efb\u4f55\u57f9\u8bad";
				container.appendChild(div);
				return;
			}
			
			//if there're some trainings
			for (var i = 0; i < currentTrainings.length; i = i + 1) {
				uniondata = currentTrainings[i];
				//dojo.debug("for union" + uniondata.name + ", there're " + uniondata.children.length + " trainings");
				var div = document.createElement("div");
				container.appendChild(div);
				var params = {label:uniondata.name, labelNodeClass:"label", containerNodeClass:"content"};
				var titlepane = dojo.widget.createWidget("TitlePane", params, div);
				titlepane.setContent(TrainingAdmin.generateTrainingPosTable(uniondata.children, 0));
				if (uniondata.children.length === 0) {
					titlepane.onLabelClick(100);
				}
			}
			
			var container = dojo.byId("oldTraining");
			//clean the original table
			container.innerHTML="";
			//if there's no trainings...
			if (pastTrainings.length == 0) {
				var div = document.createElement("div");
				div.innerHTML = "\u5c1a\u6ca1\u6709\u4efb\u4f55\u57f9\u8bad";
				container.appendChild(div);
				return;
			}
			
			//if there're some trainings
			for (var i = 0; i < pastTrainings.length; i = i + 1) {
				uniondata = pastTrainings[i];
				//dojo.debug("for union" + uniondata.name + ", there're " + uniondata.children.length + " trainings");
				var div = document.createElement("div");
				container.appendChild(div);
				var params = {label:uniondata.name, labelNodeClass:"label", containerNodeClass:"content"};
				var titlepane = dojo.widget.createWidget("TitlePane", params, div);
				titlepane.setContent(TrainingAdmin.generateTrainingPosTable(uniondata.children, 1));
				if (uniondata.children.length === 0) {
					titlepane.onLabelClick(100);
				}
			}
			
			
		}
		loading(false);
	}});
}
TrainingAdmin.generateTrainingPosTable = function(children, type) {

	if (children.length === 0) {
		return "\u6682\u65f6\u65e0\u4eba\u7533\u8bf7";
	}
	var table = document.createElement("table");
	table.border = "1";
	table.style.width="100%";
	dojo.html.addClass(table,"mytable");
	var tr = table.insertRow(-1);
	var td = tr.insertCell(-1);
	td.innerHTML = "\u57f9\u8bad\u540d\u79f0";
	td = tr.insertCell(-1);
	td.innerHTML = "\u53d1\u5e03";
	td = tr.insertCell(-1);
	td.innerHTML = "\u8be6\u60c5";
	//td = tr.insertCell(-1);
	//td.innerHTML = "密码条";
	
	for (var i = 0; i < children.length; i = i + 1) {
		tr = table.insertRow(-1);
		tr.setAttribute("id", children[i].id);
		td = tr.insertCell(-1);
		td.innerHTML = children[i].name;
		td = tr.insertCell(-1);
		td.innerHTML = children[i].posttime;
		//alert(children[i].posttime);
		td = tr.insertCell(-1);
		td.innerHTML = "<a href=\"javascript:TrainingAdmin.showTrainingDetail('" + children[i].id + "',"+type+")\">\u67e5\u770b</a>";
		//td = tr.insertCell(-1);
		//td.innerHTML = "<a href=\"javascript:TrainingAdmin.getTokens('" + children[i].id + "')\">获取</a>";
	}
	return table;
	//"<table border=\"1\">" + table.innerHTML + "</table>";
}
TrainingAdmin.showTrainingDetail = function(id, trainingType) {
//	dojo.widget.byId("actions").removeChild(TrainingAdmin.editdelTrainingPage);
//	dojo.widget.byId("actions").removeChild(TrainingAdmin.tokenTrainingPage);
	var rightTabs = dojo.widget.byId("actions");
		
	//get more info about training
	TrainingAdmin.ApplicantInfo(id);

	if(trainingType == 0)
	{
		if(rightTabs.children.length == 1)
		{
			dojo.widget.byId("actions").addChild(TrainingAdmin.editdelTrainingPage);
			dojo.widget.byId("actions").addChild(TrainingAdmin.tokenTrainingPage);
		}
	}
	else
	{
		if(rightTabs.children.length == 3)
		{
			dojo.widget.byId("actions").removeChild(TrainingAdmin.editdelTrainingPage);
			dojo.widget.byId("actions").removeChild(TrainingAdmin.tokenTrainingPage);
		}
	}

	if(trainingType == 0)
	{
			
	dojo.io.bind({url:TrainingAdmin.showTrainingDetailURL, handler:function (type, data, evt) {
		if (basicHandler(type, data, evt)) {
			//alert("trainingType: "+trainingType+" rightTabs.children.length : "+rightTabs.children.length);
			
			
			//dojo.debug(data);
			var detail = eval("(" + data + ")");
			
			TrainingAdmin.isTokened = detail.isTokened;
			
			document.getElementsByName("name")[0].value = detail.name;
			
			dojo.widget.byId("postcode").setAllValues(detail.posName,detail.postcode);
			document.getElementsByName("ispublic")[0].value = detail.ispublic;
			TrainingAdmin.setPubStatus(detail.ispublic);
			
			var rawstartdate = detail.starttime.split(" ")[0];
			dojo.widget.byId("startdate").setDate(rawstartdate.replace(/[年月日]/g,""));
			dojo.widget.byId("starttime").setTime(detail.starttime.split(" ")[1]);
			
			var rawenddate = detail.endtime.split(" ")[0];
			dojo.widget.byId("enddate").setDate(rawenddate.replace(/[年月日]/g,""));
			dojo.widget.byId("endtime").setTime(detail.endtime.split(" ")[1]);
			
			if(TrainingAdmin.deadLineShowed)
			{
				try{
				var rawdeadlinedate = detail.deadline.split(" ")[0];
				dojo.widget.byId("deadlinedate").setDate(rawdeadlinedate.replace(/[年月日]/g,""));
				dojo.widget.byId("deadlinetime").setTime(detail.deadline.split(" ")[1]);
				}catch(e){};
			}
			dojo.byId("description").value = detail.description;
			dojo.byId("id").value = id;		
			TrainingAdmin.currentTraingingCanDelete = detail.canDelete;	
			
			//set tokens id also
			dojo.byId("tokenId").value = id;
			
			if(detail.isTokened == 1)
			{
				TrainingAdmin.getTokens(id, 1);
			}
			dojo.byId("training-filelink").innerHTML = "";
			TrainingAdmin.filelist = [];
			if (detail.filelist && detail.filelist.length > 0) {
				var filelistHtml = "";
				
				for (var i = 0; i < detail.filelist.length; i++) {
					TrainingAdmin.filelist[i] = escape(detail.filelist[i]);
					filelistHtml += "<div id='file"+i+"'><a href=\"" + escape(detail.filelist[i]) + "\">" + detail.filelist[i].split("/")[1] + "</a>&nbsp;&nbsp;<a href='#' onclick=\"TrainingAdmin.removeFile('file"+i+"','"+escape(detail.filelist[i])+"')\">删除</a><br />";
				}
				dojo.byId("training-filelink").innerHTML = filelistHtml;
			}
			
		}
	}, preventCache:true, content:{"id":id}});
	}
}
TrainingAdmin.ApplicantInfo = function(id)
{
	dojo.io.bind({
		url:TrainingAdmin.getApprovedTrainingInfoURL,
		preventCache:true,
		handler: function(type, data, evt){
			if(basicHandler(type, data, evt))
			{
				var bigdata = eval("("+data+")");
				/*{"endtime":"2007年09月08日 22:45:00","starttime":"2007年09月06日 22:15:00","ispublic":1,"posttime":"2007年08月27日 22:02:45","description":"blahblah","name":"a","applicant":[{"studentName": "ROOT","studentNum":"root"}],"postcode":"000000000000","deadline":"2007年 09月04日 22:00:00"}*/
				dojo.byId("trainingName").innerHTML = bigdata.name;
				dojo.byId("trainingStarttime").innerHTML = bigdata.starttime;
				dojo.byId("trainingEndtime").innerHTML = bigdata.endtime;
				dojo.byId("trainingDescription").innerHTML = bigdata.description;

				dojo.byId("applicants").innerHTML = "";
				var table = document.createElement("table");
				var tr = table.insertRow(-1);
				var td = tr.insertCell(-1);
				td.innerHTML = "申请者姓名";
				td = tr.insertCell(-1);
				td.innerHTML = "申请者学号";
				td = tr.insertCell(-1);
				td.innerHTML = "申请者状态";
				
				
				for(var i =0; i<bigdata.applicant.length;i=i+1)
				{
					tr = table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = bigdata.applicant[i].studentName;
					td = tr.insertCell(-1);
					td.innerHTML = bigdata.applicant[i].studentNum;
					td = tr.insertCell(-1);
					switch(bigdata.applicant[i].applyStatus)
					{
						case "0":
							td.innerHTML = "未审批";
							break;
						case "1":
							td.innerHTML = "通过";
							break;
						case "2":
							td.innerHTML = "拒绝";
							break;
					}
				}
				dojo.byId("applicants").appendChild(table);
				
			}
		},
		content : {id:id}
	});
}



TrainingAdmin.generateTokens = function(){

	if(TrainingAdmin.isTokened == "0")
	{
		TrainingAdmin.getTokens(dojo.byId("tokenId").value,0,dojo.byId("tokenCount").value);
	}
	if(TrainingAdmin.isTokened == "1"&&confirm("密码条已经生成，如产生新的密码条则会覆盖原有密码条，您是否继续?"))
	{
		TrainingAdmin.getTokens(dojo.byId("tokenId").value,0,dojo.byId("tokenCount").value);
	}
}

TrainingAdmin.getTokens = function(id, Gtype, count) {

	try{
	if(dojo.widget.byId("tokenCount").isValid() == false)
	{
		alert("请输入合适的数字(1-999)");
		return;
	}
	}catch(e)
	{
	//alert("error"+e);
		//return;
	}
	dojo.io.bind({url:TrainingAdmin.getTrainingTokenURL, handler:function (type, data, evt) {
		
		if (basicHandler(type, data, evt))
		{
			if(Gtype<=1)
			{
			
				if(data == "1")
				{
					postMessage("尚未到产生密码条的时间，合适的时间为申请结束后、培训开始之前","FATAL");
				}
				TrainingAdmin.isTokened = "1";
				
				//dojo.debug(data);
				var array = eval("("+data+")");
				var container = dojo.byId("token");
				container.innerHTML ="";
				for(var i=0;i<array.length;i++)
				{
					container.innerHTML +=array[i]+"<br />";
				}
				//some epecial hack for ie6.0, fuck ie6
				if(dojo.render.html.ie60)
				{
					container.innerHTML += "<a href='"+TrainingAdmin.getTrainingTokenURL+"&id="+id+"&count="+count+"&type=2"+"' target='about_blank'>保存到本地</a>";
				}
				else
				{
					container.innerHTML += "<input type='button' onclick='TrainingAdmin.saveTokens("+id+","+count+")' value='保存到本地' />"
				//container.innerHTML +="<a href='"+TrainingAdmin.getTrainingTokenURL+"&id="+id+"&count="+count+"' target='blank'>保存到本地</a>"; 			
				}
			}
		}
	}, preventCache:true, content:{"id":id, count: count, type:Gtype}});
}

TrainingAdmin.saveTokens = function(id, count)
{
	//	var container = dojo.byId("token");
	//document.body.innerHTML+=
	dojo.byId("downloadToken").src=TrainingAdmin.getTrainingTokenURL+"&id="+id+"&count="+count+"&type=2";
}
/*
function addNewTraining() {
	if (validateDate()) {
		dojo.io.bind({url:PostTrainingURL, preventCache:true, encoding:"utf-8", handler:function (type, data, evt) {
			//dojo.debug(data);
			if (data == "true") {
				postMessage("添加新培训成功!");
			} else {
				postMessage("添加新培训失败，请联系管理员!","FATAL");
			}
			loadTrainings();
		}, formNode:document.getElementById("Training"),
		method: "post"});
	} else {
		alert("\u65e5\u671f\u51b2\u7a81");
	}
}
*/

TrainingAdmin.editTraining = function() {
	if (TrainingAdmin.validateDate()) {
	
	//	alert(document.getElementById("trainingform"));
	//	alert(document.getElementsByName("trainingform")[0]);
		dojo.require("dojo.date.format");
		dojo.byId("posttime").value = dojo.date.format(new Date(), {datePattern:"yyyyMMdd", selector:"dateOnly"});
		var filelist = dojo.byId("training-filelist");
		var listString = "";
		if (TrainingAdmin.filelist.length != 0) {
			listString = TrainingAdmin.filelist[0];
			for(var i = 1;i<TrainingAdmin.filelist.length;i++)
			{
				listString += ":" + TrainingAdmin.filelist[i];
			}
		}
		filelist.value=listString;
		dojo.io.bind({url:TrainingAdmin.updateATrainingURL, preventCache:true, encoding:"utf-8", handler:function (type, data, evt) {
			if(basicHandler(type, data, evt))
			{//dojo.debug(data);
			if (data == "true") {
				postMessage("修改培训成功!");
			} else {
				postMessage("修改培训失败，请联系管理员!","FATAL");
			}
			TrainingAdmin.init();
		}
		},method: "post", formNode:document.getElementById("trainingform")
		});
	} else {
		alert("\u65e5\u671f\u51b2\u7a81");
	}
}

TrainingAdmin.delTraining = function(){
	if(TrainingAdmin.currentTraingingCanDelete)
	{
		dojo.io.bind({url:TrainingAdmin.delTrainingURL, preventCache:true, encoding:"utf-8", handler:function (type, data, evt) {
			if(basicHandler(type, data, evt))
			{
			//dojo.debug(data);
			if (data == "true") {
				postMessage("删除培训成功!");
			} else {
				postMessage("删除培训失败，请联系管理员!","FATAL");
			}
			TrainingAdmin.init();
		}
		}, content : {
			id : dojo.byId("id").value
		}
		});
	}
	else
	{
		postMessage("本条目不可删除!","FATAL");
	}
}
//注意时间格式...
TrainingAdmin.validateDate = function() {
	//"20070808"
	try
	{
	var startdateValue = parseInt(dojo.widget.byId("startdate").getValue());
	var enddateValue = parseInt(dojo.widget.byId("enddate").getValue());
	if(TrainingAdmin.deadLineShowed)
	var deadlinedateValue = parseInt(dojo.widget.byId("deadlinedate").getValue());
	
	//"2007-08-07T21:45:00+08:00"
	var starttimeValue = dojo.widget.byId("starttime").getTime();
	var endtimeValue = dojo.widget.byId("endtime").getTime();
	if(TrainingAdmin.deadLineShowed)
	var deadlinetimeValue = dojo.widget.byId("deadlinetime").getTime();
	
	var starttime = startdateValue + starttimeValue.split("T")[1].split("+")[0];
	var endtime = enddateValue + endtimeValue.split("T")[1].split("+")[0];
	if(TrainingAdmin.deadLineShowed)
	var deadline = deadlinedateValue + deadlinetimeValue.split("T")[1].split("+")[0];

	document.getElementsByName("starttime")[0].value = starttime;
	document.getElementsByName("endtime")[0].value = endtime;
	if(TrainingAdmin.deadLineShowed)
	document.getElementsByName("deadline")[0].value = deadline;
	dojo.byId("finalstarttime").value = starttime;
	dojo.byId("finalendtime").value = endtime;
	if(TrainingAdmin.deadLineShowed)
	dojo.byId("finaldeadline").value = deadline;
	
	}
	catch(e)
	{
		//dojo.debug(e);
		return false;
	}
	//dojo.debug("deadline: " + deadline + " start: " + starttime + " end: " + endtime);
	
	var final_start = parseInt(starttime.replace(":",""));
	var final_end = parseInt(endtime.replace(":",""));
	if(TrainingAdmin.deadLineShowed)
	var final_deadline = parseInt(deadline.replace(":",""));
	
	if(TrainingAdmin.deadLineShowed)
	{
	if (final_deadline <	 final_start && final_start < final_end) {
		//if(true){//valid
		//dojo.debug("true!");
		return true;
	} else {
		//dojo.debug("false");
		return false;
	}
	}
	else
	{
		if (final_start < final_end) {
			//if(true){//valid
			//dojo.debug("true!");
			return true;
		} else {
			//dojo.debug("false");
			return false;
		}
	}
};

TrainingAdmin.startUploadFile = function() {
	dojo.byId("startUploadFile").innerHTML = "上传另一个文件";
	dojo.byId("training-upload-form").style.display="";
};

TrainingAdmin.cancelUpload = function() {
	dojo.byId("training-upload-form").style.display="none";
};

TrainingAdmin.upload = function() {
	//dojo.debug("in upload");
	dojo.require("dojo.io.IframeIO");
	dojo.require("dojo.json");
	//dojo.debug("start upload");
	dojo.io.bind({ 
		url: TrainingAdmin.UploadURL, 
		mimetype: "text/html",
		method: "post",
		handler: function(type, data, evt) {
			//dojo.debug("in response, type="+ type+" data="+data);
			var vals = eval("(" + data.body.innerHTML + ")");
			var name = vals["name"];
			var path = vals["path"];
			TrainingAdmin.filelist.push(path);
			var newFile = "<div id='file"+TrainingAdmin.filelist.length+"'><a href=\"" + path + "\">" + name + "</a>&nbsp;&nbsp;<a href='#' onclick=\"TrainingAdmin.removeFile('file"+TrainingAdmin.filelist.length+"','"+path+"')\">删除</a><br />";
			
			//var newFile = "<div id='file"+TrainingAdmin.filelist.length+"'><a href=\"" + escape(path) + "\">" + name + "</a>&nbsp;&nbsp;<a href='#' onclick=\"TrainingAdmin.removeFile('file"+TrainingAdmin.filelist.length+"','"+path+"')\">删除</a><br />";
			var filelink = dojo.byId("training-filelink");
			filelink.innerHTML += newFile;
			dojo.byId("training-upload-form").style.display="none";
			dojo.byId("training-upload-file").value= "";
		},
		formNode: dojo.byId("training-upload-form")
	});
};

TrainingAdmin.removeFile = function(divId, path)
{
	dojo.debug(divId+" "+path);
	dojo.byId(divId).style.display="none";
	
	dojo.debug("TrainingAdmin.filelist.length = "+TrainingAdmin.filelist.length);
	for(var i=0;i<TrainingAdmin.filelist.length;i++)
	{
		dojo.debug(TrainingAdmin.filelist[i]);
		dojo.debug(TrainingAdmin.filelist[i].length);
		dojo.debug(path.length);
		if(TrainingAdmin.filelist[i]==path)
		{
		dojo.debug("a");
			TrainingAdmin.filelist.splice(i, 1);
		}
	}
}
var content = dojo.widget.byId("main");
content.onLoad = TrainingAdmin.init;

