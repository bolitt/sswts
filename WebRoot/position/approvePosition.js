var ApprovePosition = {
	getApplyingPosURL : "position.do?method=getApprovable",
	showApplyingPosDetailURL : "position.do?method=showApplyDetail",
	appovePosURL : "position.do?method=approvePos"
}; 

ApprovePositionIniting = false;

ApprovePosition.init = function() {
	//dojo.debug("in on appointposition");
	dojo.html.setOpacity("detailsContainer",0);
	if(!ApprovePositionIniting)
	{
		ApprovePositionIniting = true;
		dojo.io.bind({url:ApprovePosition.getApplyingPosURL, preventCache: true, handler:function(type,data,evt)
		{
			if (basicHandler(type, data, evt)) {
				//dojo.debug("get data: " + data);
				var bigdata =eval("(" + data + ")");
				var container=dojo.byId("applyingPosContainer");
				container.innerHTML = "";
				if(bigdata.length==0)
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
					titlepane.setContent(ApprovePosition.generateApplyingPosTable(uniondata.children));
					
					if(uniondata.children.length===0)
					{
						titlepane.onLabelClick(10);
					}
				}
				ApprovePositionIniting = false;
			}
			loading(false);
		}});
	}
}



ApprovePosition.generateApplyingPosTable = function(children)
{

	if(children.length === 0)
	{
		return "<div style='margin-left:4%'>暂时无人申请</div>";
	}

	var table=document.createElement("table");
	//table.style["margin"] = "4%";
/*	var tr=table.insertRow(-1);
	var td=tr.insertCell(-1);
	td.innerHTML="岗位名称";
	td=tr.insertCell(-1);
	td.innerHTML="姓名";
	td=tr.insertCell(-1);
	td.innerHTML="学号";
	td=tr.insertCell(-1);
	td.innerHTML="详情";
	td=tr.insertCell(-1);
	td.innerHTML="同意"
	td=tr.insertCell(-1);
	td.innerHTML="拒绝";
*/
	for(var i=0;i<children.length;i=i+1)
	{
		var tr=table.insertRow(-1);
		tr.setAttribute("id",children[i].id);
		var td=tr.insertCell(-1);
		td.innerHTML=children[i].posname;
		td=tr.insertCell(-1);
		td.innerHTML=children[i].stuname;
		td=tr.insertCell(-1);
		td.innerHTML=children[i].studentnum+" ";
		td=tr.insertCell(-1);
		td.innerHTML="<a href=\"javascript:ApprovePosition.viewDetail('"+children[i].id+"')\">查看详情</a>";
		td=tr.insertCell(-1);
		td.innerHTML="<a href=\"javascript:ApprovePosition.approvePos('"+children[i].id+"',true)\">同意</a>";
		td=tr.insertCell(-1);
		td.innerHTML="<a href=\"javascript:ApprovePosition.approvePos('"+children[i].id+"',false)\">拒绝</a>";
	}
	return "<table border=\"0\" style='margin-left:4%'>"+table.innerHTML+"</table>";
}

ApprovePosition.viewDetail = function(id)
{
	//dojo.debug("in on approveposition");
	dojo.io.bind({url: ApprovePosition.showApplyingPosDetailURL, 
		handler : function(type, data, evt){
				if (basicHandler(type, data, evt)) {
				var fadeInOut = dojo.lfx.fadeOut("detailsContainer",100,null,function()
				{
				detail = eval("("+data+")");
				dojo.byId("reason").innerHTML=detail.reason;
				//dojo.byId("activityRec").innerHTML=detail.userinfo.activityRec;
				dojo.byId("region").innerHTML=detail.userinfo.region;
				dojo.byId("political").innerHTML=detail.userinfo.political;
				dojo.byId("department").innerHTML=detail.userinfo.department;
				dojo.byId("tel").innerHTML=detail.userinfo.tel;
				dojo.byId("postRec").innerHTML=detail.userinfo.postRec;
				dojo.byId("classnum").innerHTML=detail.userinfo.classnum;
				dojo.byId("trainingRec").innerHTML=detail.userinfo.trainingRec;
				dojo.byId("ability1").innerHTML=detail.userinfo.ability1;
				dojo.byId("email").innerHTML=detail.userinfo.email;
				dojo.byId("address").innerHTML=detail.userinfo.address;
				dojo.byId("name").innerHTML=detail.userinfo.name;
				dojo.byId("ability6").innerHTML=detail.userinfo.ability6;
				dojo.byId("grade").innerHTML=detail.userinfo.grade;
				dojo.byId("gender").innerHTML=detail.userinfo.gender;
				dojo.byId("ability2").innerHTML=detail.userinfo.ability2;
				dojo.byId("specialty").innerHTML=detail.userinfo.specialty;
				dojo.byId("ability3").innerHTML=detail.userinfo.ability3;
				dojo.byId("ability4").innerHTML=detail.userinfo.ability4;
				dojo.byId("ability5").innerHTML=detail.userinfo.ability5;
				dojo.byId("mobile").innerHTML=detail.userinfo.mobile;
				dojo.byId("applytime").innerHTML=detail.applytime;
				//dojo.byId("applystatus").innerHTML=detail.applystatus;
				//dojo.byId("pos_cost").innerHTML=detail.pos.pos_cost;
				dojo.byId("pos_ability6").innerHTML=detail.pos.pos_ability6;
				dojo.byId("pos_ability5").innerHTML=detail.pos.pos_ability5;
				dojo.byId("pos_ability4").innerHTML=detail.pos.pos_ability4;
				dojo.byId("pos_ability3").innerHTML=detail.pos.pos_ability3;
				dojo.byId("pos_ability2").innerHTML=detail.pos.pos_ability2;
				dojo.byId("pos_ability1").innerHTML=detail.pos.pos_ability1;
				dojo.byId("pos_Descrip").innerHTML=detail.pos.descrip;
				dojo.byId("fullname").innerHTML=detail.pos.posname;
				dojo.byId("pos_relate").innerHTML=detail.pos.relatepos;
				dojo.byId("pos_require").innerHTML=detail.pos.posrequire;
				dojo.lfx.fadeIn("detailsContainer",100).play();
				});
				fadeInOut.play();
				//dojo.debug(data);
			}
		},
		preventCache : true,
		content:{applyid : id}
	});

}


ApprovePosition.approvePos = function(id,status)
{
	dojo.io.bind({url: ApprovePosition.appovePosURL, 
		handler : function(type,data,evt){
			if(basicHandler(type, data, evt))
			{
				if (data == "true") {
					//dojo.lfx.wipeOut(id+"",250).play();
					ApprovePosition.init();
					postMessage("审批第"+id+"号申请成功!","MESSAGE");
				} else {
					//dojo.lfx.wipeOut(id+"",250).play();
					//postMessage("审批第"+id+"号申请失败,请联系管理员!","FATAL");
				}		
			}
		},
		preventCache : true,
		content:{
			applyid : id,
			ok : status}
	});
}

var content=dojo.widget.byId("main");
content.onLoad=ApprovePosition.init;
