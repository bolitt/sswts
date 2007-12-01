var PostTraining = {
	PostTrainingURL : "training.do?method=postTraining",
	UploadURL : "training.do?method=upload",
	fileList : [],
	deadLineShowed : true
};

PostTraining.init = function()
{
	dojo.widget.byId("postcode").domNode.childNodes[2].disabled=true;
	dojo.widget.byId("postcode").setAllValues("","");
	loading(false);
};

PostTraining.setPubStatus = function(isPub){
	//alert("a "+isPub.value);
	if(isPub == 1)
	{
		if(PostTraining.deadLineShowed == false)
		{
			dojo.lfx.wipeIn("deadline1",200).play();
			dojo.lfx.wipeIn("deadline2",200).play();
			dojo.lfx.wipeIn("deadline3",200,null,function(){
				PostTraining.deadLineShowed = true;
			}).play();
		}
	}
	else
	{
		if(PostTraining.deadLineShowed == true)
		{
			dojo.lfx.wipeOut("deadline1",200).play();
			dojo.lfx.wipeOut("deadline2",200).play();
			dojo.lfx.wipeOut("deadline3",200,null,function(){
				PostTraining.deadLineShowed = false;
			}).play();
		}
	}
};

PostTraining.addNewTraining = function() {
	if (PostTraining.validateDate()) {
		var filelist = dojo.byId("training-filelist");
		var listString = "";
		if (PostTraining.fileList.length != 0) {
			listString = PostTraining.fileList[0];
			for(var i = 1;i<PostTraining.fileList.length;i++)
			{
				listString += ":" + PostTraining.fileList[i];
			}
		}
		filelist.value=listString;
		dojo.io.bind({url:PostTraining.PostTrainingURL, preventCache:true, encoding:"utf-8", handler:function (type, data, evt) {
			//dojo.debug(data);
			if(basicHandler(type, data, evt))
			{
				if (data == "true") {
					postMessage("添加新培训成功!");
					fireNavigatorButtonEvent("ManageTraining");
				} else {
					postMessage("添加新培训失败，请联系管理员!","FATAL");
				}
			}
			//loadTrainings();
		}, formNode:document.getElementById("Training"),
		method: "post"});
	} else {
		alert("\u65e5\u671f\u51b2\u7a81");
	}
};
//需要重新验证一下ie下的时间问题
PostTraining.validateDate = function() {
	//"20070808"
	try
	{
	var startdateValue = parseInt(dojo.widget.byId("startdate").getValue());
	var enddateValue = parseInt(dojo.widget.byId("enddate").getValue());
	if(PostTraining.deadLineShowed)
	var deadlinedateValue = parseInt(dojo.widget.byId("deadlinedate").getValue());
	
	//"2007-08-07T21:45:00+08:00"
	var starttimeValue = dojo.widget.byId("starttime").getTime();
	var endtimeValue = dojo.widget.byId("endtime").getTime();
	if(PostTraining.deadLineShowed)
	var deadlinetimeValue = dojo.widget.byId("deadlinetime").getTime();
	
	var starttime = startdateValue + starttimeValue.split("T")[1].split("+")[0];
	var endtime = enddateValue + endtimeValue.split("T")[1].split("+")[0];
	if(PostTraining.deadLineShowed)
	var deadline = deadlinedateValue + deadlinetimeValue.split("T")[1].split("+")[0];

	document.getElementsByName("starttime")[0].value = starttime;
	document.getElementsByName("endtime")[0].value = endtime;
	if(PostTraining.deadLineShowed)
	document.getElementsByName("deadline")[0].value = deadline;
	dojo.byId("finalstarttime").value = starttime;
	dojo.byId("finalendtime").value = endtime;
	if(PostTraining.deadLineShowed)
	dojo.byId("finaldeadline").value = deadline;
	//alert(dojo.byId("finalstarttime").value);
	}
	catch(e)
	{
		//dojo.debug(e);
		return false;
	}
	//dojo.debug("deadline: " + deadline + " start: " + starttime + " end: " + endtime);
	
	var final_start = parseInt(starttime.replace(":",""));
	var final_end = parseInt(endtime.replace(":",""));
	if(PostTraining.deadLineShowed)
	var final_deadline = parseInt(deadline.replace(":",""));
	if(PostTraining.deadLineShowed)
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

PostTraining.upload = function() {
	//dojo.debug("in upload");
	dojo.require("dojo.io.IframeIO");
	dojo.require("dojo.json");
	//dojo.debug("start upload");
	dojo.io.bind({ 
		url: PostTraining.UploadURL, 
		mimetype: "text/html",
		method: "post",
		handler: function(type, data, evt) {
			//dojo.debug("in response, type="+ type+" data="+data);
			var vals = eval("(" + data.body.innerHTML + ")");
			var name = vals["name"];
			var path = vals["path"];
			PostTraining.fileList.push(path);
			var newFile = "<div id='file"+PostTraining.fileList.length+"'><a href=\"" + path + "\">" + name + "</a>&nbsp;&nbsp;<a href='#' onclick=\"PostTraining.removeFile('file"+PostTraining.fileList.length+"','"+escape(path)+"')\">删除</a><br />";
			var filelink = dojo.byId("training-filelink");
			filelink.innerHTML += newFile;
			dojo.byId("training-upload-form").style.display="none";
			dojo.byId("training-upload-file").value= "";
		},
		formNode: dojo.byId("training-upload-form")
	});
	
};

PostTraining.removeFile = function(divId, path)
{
	dojo.byId(divId).style.display="none";
	for(var i=0;i<PostTraining.fileList.length;i++)
	{
		if(PostTraining.fileList[i]==path)
		{
			PostTraining.fileList.splice(i, 1);
		}
	}
}

PostTraining.startUploadFile = function()
{
	dojo.byId("startUploadFile").innerHTML = "上传另一个文件";
	dojo.byId("training-upload-form").style.display="";
}
PostTraining.cancelUpload = function()
{
	//dojo.byId("startUploadFile").innerHTML = "上传另一个文件";
	dojo.byId("training-upload-form").style.display="none";
}


var content = dojo.widget.byId("main");
content.onLoad = PostTraining.init;