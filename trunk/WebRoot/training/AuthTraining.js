var AuthTraining = {
	getTrainingForAuthURL : "training.do?method=getTrainingsForAuthentication",
	getAuthenticatedTrainingURL : "training.do?method=getAuthenticatedTraining",
	commitURL : "training.do?method=authenticateTraining",
	faded : false,
	currentId : 0
};

AuthTraining.init = function()
{
	dojo.io.bind({
		url : AuthTraining.getTrainingForAuthURL,
		preventCache : true,
		handler : function(type, data, evt)
		{
			if (basicHandler(type, data, evt))
			{
				if(data=="[][]")
				{
					dojo.byId("needAuthDiv").innerHTML = "无需要认证的培训";
					return;
				}
				var bigdata  = eval("(" + data + ")");
				dojo.byId("needAuthDiv").innerHTML = "";
				var table = document.createElement("table");
				table.border = "1";
				dojo.html.addClass(table, "mytable");
				
				var tr = table.insertRow(-1);
				var td = tr.insertCell(-1);
				td.innerHTML = "培训发布者";
				td = tr.insertCell(-1);
				td.innerHTML = "培训名称";
				td = tr.insertCell(-1);
				td.innerHTML = "培训起始时间";
				td = tr.insertCell(-1);
				td.innerHTML = "培训结束时间";
				td = tr.insertCell(-1);
				td.innerHTML = "认证";
				
				for(var i =0; i<bigdata.length;i=i+1)
				{
					var item = bigdata[i];
					tr = table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = item.publishPostName;
					td = tr.insertCell(-1);
					td.innerHTML = item.trainingName;
					td = tr.insertCell(-1);
					td.innerHTML = item.startTime;
					td = tr.insertCell(-1);
					td.innerHTML = item.endTime;
					td = tr.insertCell(-1);
					td.innerHTML = "<a href='javascript:AuthTraining.showCommitArea("+item.trainingId+",\""+item.trainingName+"\")'>认证</a>";
				}
				dojo.byId("needAuthDiv").appendChild(table);
			}	
		}
	});
	dojo.io.bind({
		url : AuthTraining.getAuthenticatedTrainingURL,
		preventCache : true,
		handler : function(type, data, evt)
		{
			if (basicHandler(type, data, evt))
			{
				if(data=="[]")
				{
					dojo.byId("AuthedDiv").innerHTML = "无曾参加的培训";
					return;
				}
				var bigdata  = eval("(" + data + ")");
				dojo.byId("AuthedDiv").innerHTML = "";
				var table = document.createElement("table");
				table.border = "1";
				dojo.html.addClass(table, "mytable");
				var tr = table.insertRow(-1);
				var td = tr.insertCell(-1);
				td.innerHTML = "培训发布者";
				td = tr.insertCell(-1);
				td.innerHTML = "培训名称";
				td = tr.insertCell(-1);
				td.innerHTML = "培训起始时间";
				td = tr.insertCell(-1);
				td.innerHTML = "培训结束时间";
				
				for(var i =0; i<bigdata.length;i=i+1)
				{
					var item = bigdata[i];
					tr = table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = item.publishPostName;
					td = tr.insertCell(-1);
					td.innerHTML = item.trainingName;
					td = tr.insertCell(-1);
					td.innerHTML = item.startTime;
					td = tr.insertCell(-1);
					td.innerHTML = item.endTime;
				}
				dojo.byId("AuthedDiv").appendChild(table);
			}	
		}
	});
	loading(false);
};

AuthTraining.showCommitArea = function(id, name)
{
	this.currentId = id;
	dojo.byId("trainingname").innerHTML = name;
	if(this.faded)
	{
		dojo.lfx.fadeIn("commitAuthArea",300,null,function(){
			this.faded = true;
		}).play()		
	}
};

AuthTraining.commit = function()
{
	var token = dojo.byId("token").value;
	if(token.length > 0 && this.currentId)
	{
		dojo.io.bind({
			url: AuthTraining.commitURL,
			preventCache : true,
			handler : function(type, data, evt)
			{
				if (basicHandler(type, data, evt))
				{
					if(data == "true")
					{
						postMessage("提交成功");
						//try{
						//dojo.widget.byId("postcode").setAllValues("","");
						dojo.byId("token").value = "";
						//}catch(e)
//						{
//						dojo.debug(e);
//						}
						AuthTraining.init();
					}
				}
			},
			content : {
				token　:　token,
				trainingid : AuthTraining.currentId
			}
		});
	}
}

var content = dojo.widget.byId("main");
content.onLoad = function(){
	dojo.html.setOpacity("commitAuthArea",0);
	AuthTraining.faded = true;
	AuthTraining.init();
	//dojo.widget.byId("postcode").domNode.childNodes[2].disabled=true;
	//dojo.widget.byId("postcode").setAllValues("","");
	
}