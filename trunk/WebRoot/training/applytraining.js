getTrainingURL = "training.do?method=getAvailableTraining";
showTrainingDetailURL = "training.do?method=showTrainingDetail";
applyTrainingURL = "training.do?method=applyTraining";
IsReasonShowed = false;

function onApplyTraining() {
	//dojo.debug("on apply training");
	dojo.lfx.wipeOut("reasondiv",1).play();
	if (dojo.render.html.ie) {
		dojo.lfx.wipeOut("applybuttondiv", 1).play();
	} else {
		dojo.lfx.wipeOut("applybuttondiv", 1).play();
	}
	IsReasonShowed = false;
	
	//show loading dialog
	//loading(true);
	dojo.io.bind({url:getTrainingURL, handler:function (type, data, evt) {
		//dojo.debug("aaa"+type+" "+data+" "+evt);
		if(basicHandler(type, data, evt))
		{
			var Trainings = dojo.byId("availabletrainingcontainer");
			var table = document.createElement("table");
			table.border="1";
			var tr = table.insertRow(-1);
			var td = tr.insertCell(-1);
			td.innerHTML="培训名称";
			td = tr.insertCell(-1);
			td.innerHTML="申请截止日期";
			td = tr.insertCell(-1);
			td.innerHTML="查看详情";
			var children = eval(data);
			for(var i=0;i<children.length;i=i+1)
			{
				tr=table.insertRow(-1);
				td=tr.insertCell(-1);
				td.innerHTML=children[i].name;
				td=tr.insertCell(-1);
				td.innerHTML=children[i].deadline;
				td=tr.insertCell(-1);
				td.innerHTML="<a href=\"javascript:showtrainingDetail('"+children[i].id+"')\">查看详情</a>";
			}
			Trainings.appendChild(table);
		}	
		loading(false);
	}, preventCache:true, content:{}});
}
function showtrainingDetail(trainingNo) {
	//dojo.debug("in show training detail, training no is : " + trainingNo);
	currenttraining = trainingNo;
	dojo.io.bind({url:showTrainingDetailURL, handler:showtrainingDetailCallback, preventCache:true, content:{"id":trainingNo}});
	if (IsReasonShowed) {
		dojo.lfx.wipeOut("applybuttondiv", 300).play();
		IsReasonShowed = !IsReasonShowed;
	}
}
currenttraining = null;
appliedtraining = [];
function showtrainingDetailCallback(type, data, evt) {
	if (basicHandler(type, data, evt)) {
		if (data == "NULL") {
			alert("该申请不存在");
		} else {
			var details = eval("(" + data + ")");
			dojo.byId("trainingname").innerHTML = details.name;
			dojo.byId("trainingdescription").innerHTML = details.description;
			dojo.byId("trainingpostcode").innerHTML = details.postcode;
			dojo.byId("trainingstarttime").innerHTML = details.starttime;
			dojo.byId("trainingendtime").innerHTML = details.endtime;
			dojo.byId("trainingdeadline").innerHTML = details.deadline;
			if (appliedtraining[currenttraining] === true) {
				dojo.lfx.wipeOut("applybuttondiv", 250).play();
			} else {
				dojo.lfx.wipeIn("applybuttondiv", 250).play();
			}
			if (!appliedtraining[currenttraining]) {
				dojo.lfx.wipeIn("applybuttondiv", 100).play();
				appliedtraining[currenttraining] = false;
			} else {
				dojo.lfx.wipeOut("applybuttondiv", 100).play();
				appliedtraining[currenttraining] = true;
			}
		}
	}
}
function applyTraining() {
	//dojo.debug("in applyTraining");
	//dojo.debug("poscode is " + currentPos);


	appliedtraining[currenttraining] = true;

	//alert("appliedPos["+currentPos+"]="+appliedPos[currentPos]);
	if (!IsReasonShowed)
	{
		dojo.byId("reason").value="";
		dojo.lfx.wipeIn("reasondiv",300).play();
		//dojo.widget.byId("applyButton").setDisabled(true);
		IsReasonShowed=!IsReasonShowed;
		dojo.lfx.wipeOut("applybuttondiv",100).play();
	}

	//dojo.lfx.wipeIn("explode", 250).play();	
}


function submitApply(){
	//dojo.debug("in submitApply "+currenttraining);
	appliedtraining[currenttraining] = true;
	dojo.io.bind({
		url: applyTrainingURL, 
		preventCache: true,
		handler: function (type, data, evt) {
			if (type == "error") {
				//dojo.debug("sth wrong....");
			} else {
				if (data == "true") {
					postMessage("申请提交成功!","MESSAGE");
				} else {
					postMessage("申请提交失败,请联系管理员!","FATAL");
				}
			}
		},
		content:{trainid: currenttraining,applyReason: dojo.byId("reason").value}
	});
	if (IsReasonShowed)
	{
		dojo.lfx.wipeOut("reasondiv",300).play();
		IsReasonShowed=!IsReasonShowed;
	}
	
}
content = dojo.widget.byId("main");
content.onLoad = onApplyTraining;

