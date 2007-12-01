
var getApprovableTrainingURL = "training.do?method=getApprovableTraining";
var showApplyTrainingDetailURL = "training.do?method=showApplyTrainingDetail";
var approveTrainingURL = "training.do?method=approveTraining";
function onApproveTraining() {
	//dojo.debug("in onApproveTraining");
	dojo.io.bind({url:getApprovableTrainingURL, preventCache:true, handler:function (type, data, evt) {
		if (basicHandler(type, data, evt)) {
			//dojo.debug(data);
			var container = dojo.byId("approvabletrainings");
			var bigdata = eval("(" + data + ")");
			container.innerHTML = "";
			if(bigdata.length===0)
			{
				var div=document.createElement("div");
				div.innerHTML="您没有需要审批的请求";
				container.appendChild(div);
				return;
			}
			
			for(var i=0;i<bigdata.length;i=i+1)
			{
				uniondata=bigdata[i];
				//dojo.debug("for union"+uniondata.name+", there're "+uniondata.children.length+" applying");
					
				var div=document.createElement("div");
				container.appendChild(div);
	
				var params = {label: uniondata.name,labelNodeClass:"label", containerNodeClass:"content"};
				var titlepane = dojo.widget.createWidget("TitlePane", params,div);
				titlepane.setContent(generateApprovableTrainingTable(uniondata.children));
				
				if(uniondata.children.length===0)
				{
					titlepane.onLabelClick(10);
				}
			}
		}
		loading(false);
	}});
}

function generateApprovableTrainingTable(children) {
	if (children.length === 0) {
		return "\u6682\u65f6\u65e0\u4eba\u7533\u8bf7";
	}
	var table = document.createElement("table");
	var tr = table.insertRow(-1);
	var td = tr.insertCell(-1);
	td.innerHTML = "培训名称";
	td = tr.insertCell(-1);
	td.innerHTML = "申请者学号";
	td = tr.insertCell(-1);
	td.innerHTML = "申请时间";
	td = tr.insertCell(-1);
	td.innerHTML = "查看详情";
	td = tr.insertCell(-1);
	td.innerHTML = "同意";
	td = tr.insertCell(-1);
	td.innerHTML = "拒绝";
	
	for (var i = 0; i < children.length; i = i + 1) {
		tr = table.insertRow(-1);
		tr.setAttribute("id", children[i].id);
		td = tr.insertCell(-1);
		td.innerHTML = children[i].trainingname;
		td = tr.insertCell(-1);
		td.innerHTML = children[i].studentnum;
		td = tr.insertCell(-1);
		td.innerHTML = children[i].applytime;
		td = tr.insertCell(-1);
		td.innerHTML = "<a href=\"javascript:ApproveTrainingDetail('" + children[i].id + "')\">详情</a>";
		td=tr.insertCell(-1);
		td.innerHTML="<a href=\"javascript:approveTraining('"+children[i].id+"',true)\">同意</a>";
		td=tr.insertCell(-1);
		td.innerHTML="<a href=\"javascript:approveTraining('"+children[i].id+"',false)\">拒绝</a>";
		
	}
	return "<table border=\"0\" style='margin-left:4%'>" + table.innerHTML + "</table>";
}

function ApproveTrainingDetail(id)
{
	//dojo.debug("in ApproveTrainingDetail, id = "+id);
	dojo.io.bind({url: showApplyTrainingDetailURL, 
		handler : function (type,data,evt){
			//dojo.debug(data);
			if (basicHandler(type, data, evt)) {
				detail = eval("("+data+")");
				dojo.byId("trainingName").innerHTML=detail.trainingName;
				//dojo.byId("trainingDeadline").innerHTML=detail.trainingDeadline;
				dojo.byId("trainingStarttime").innerHTML=detail.trainingStarttime;
				dojo.byId("trainingEndtime").innerHTML=detail.trainingEndtime;
				dojo.byId("trainingDescription").innerHTML=detail.trainingDescription;
				//dojo.byId("trainingIspublic").innerHTML=detail.trainingIspublic;
				//dojo.byId("trainingPostcode").innerHTML=detail.trainingPostcode;
				dojo.byId("applyTime").innerHTML=detail.applyTime;
				dojo.byId("applyReason").innerHTML=detail.applyReason;
				//alert("a");
				dojo.byId("region").innerHTML=detail.userInfo.region;
				//alert("a");
				dojo.byId("political").innerHTML=detail.userInfo.political;
				//alert("a");
				dojo.byId("department").innerHTML=detail.userInfo.department;
				//alert("a");
				dojo.byId("tel").innerHTML=detail.userInfo.tel;
				//alert("a");
				dojo.byId("postRec").innerHTML=detail.userInfo.postRec;
				//alert("a");
				dojo.byId("classnum").innerHTML=detail.userInfo.classnum;
				//alert("a");
				dojo.byId("trainingRec").innerHTML=detail.userInfo.trainingRec;
				//alert("a");
				dojo.byId("ability1").innerHTML=detail.userInfo.ability1;
				//alert("a");
				dojo.byId("email").innerHTML=detail.userInfo.email;
				//alert("a");
				dojo.byId("address").innerHTML=detail.userInfo.address;
				//alert("a");
				dojo.byId("name").innerHTML=detail.userInfo.name;
				//alert("a");
				dojo.byId("ability6").innerHTML=detail.userInfo.ability6;
				//alert("a");
				dojo.byId("grade").innerHTML=detail.userInfo.grade;
				//alert("a");
				dojo.byId("gender").innerHTML=detail.userInfo.gender;
				//alert("a");
				dojo.byId("ability2").innerHTML=detail.userInfo.ability2;
				//alert("a");
				dojo.byId("specialty").innerHTML=detail.userInfo.specialty;
				//alert("a");
				dojo.byId("ability3").innerHTML=detail.userInfo.ability3;
				//alert("a");
				dojo.byId("ability4").innerHTML=detail.userInfo.ability4;
				//alert("a");
				dojo.byId("ability5").innerHTML=detail.userInfo.ability5;
				//alert("a");
				dojo.byId("mobile").innerHTML=detail.userInfo.mobile;
		
			}
		},
		preventCache : true,
		content:{applyTraingingId : id}
	});
	var container = dojo.byId("approvabletrainingdetail");
}

function approveTraining(id,status)
{
	dojo.io.bind({url: approveTrainingURL, 
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
		},
		preventCache : true,
		content:{
			applyTrainingId : id,
			approveStatus : status}
	});
}


var content = dojo.widget.byId("main");
content.onLoad = onApproveTraining;

