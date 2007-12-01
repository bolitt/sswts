var PositionExperience = 
{ posExpURL : "position.do?method=getPosExp",
applyHistoryURL :"position.do?method=getApplyHistory"
};

var test;
PositionExperience.init = function()
{
	var container = dojo.byId("Old");
	var container1 = dojo.byId("Current");

	dojo.io.bind({
		url: PositionExperience.posExpURL,
		preventCache : true,
		handler : function (type, data, evt) {
			//dojo.debug(data);
			try{
			if(basicHandler(type, data, evt))
			{
				bigdata = eval("(" + data + ")");
				if(bigdata.Old.length==0)
				{
					container.innerHTML = "您目前尚未担任任何岗位";
				}
				else
				{
					var table = document.createElement("table");
					table.border = "1";
					dojo.html.addClass(table,"getExpTable");
					var tr = table.insertRow(-1);
					
					var td = tr.insertCell(-1);			td.innerHTML = "职位名称";
					td = tr.insertCell(-1);				td.innerHTML = "能力1";
					td = tr.insertCell(-1);				td.innerHTML = "能力2";
					td = tr.insertCell(-1);				td.innerHTML = "能力3";
					td = tr.insertCell(-1);				td.innerHTML = "能力4";
					td = tr.insertCell(-1);				td.innerHTML = "能力5";
					td = tr.insertCell(-1);				td.innerHTML = "能力6";
					td = tr.insertCell(-1);				td.innerHTML = "得分";
					td = tr.insertCell(-1);				td.innerHTML = "开始时间";
					td = tr.insertCell(-1);				td.innerHTML = "结束时间";
					
					for(var i=0;i<bigdata.Old.length;i++)
					{
						var record = bigdata.Old[i];
						tr = table.insertRow(-1);
						td = tr.insertCell(-1);		td.innerHTML = record.posname;
						td = tr.insertCell(-1);		td.innerHTML = record.a1;
						td = tr.insertCell(-1);		td.innerHTML = record.a2;
						td = tr.insertCell(-1);		td.innerHTML = record.a3;
						td = tr.insertCell(-1);		td.innerHTML = record.a4;
						td = tr.insertCell(-1);		td.innerHTML = record.a5;
						td = tr.insertCell(-1);		td.innerHTML = record.a6;
						td = tr.insertCell(-1);		td.innerHTML = record.remark;
						td = tr.insertCell(-1);		td.innerHTML = record.starttime;
						td = tr.insertCell(-1);		td.innerHTML = record.endtime;
						td = tr.insertCell(-1);
	
					}
					container.innerHTML = "";
					container.appendChild(table);
				}
				
				if(bigdata.Current.length==0)
				{
					//dojo.debug("bigdata.Current.length==0"+container1);
					container1.innerHTML = "您尚无曾任岗位记录";
				}
				else
				{
					//dojo.debug("bigdata.Current.length!=0"+container1);
					var table = document.createElement("table");
					table.border = "1";
					dojo.html.addClass(table,"getExpTable");
					var tr = table.insertRow(-1);
					//dojo.debug("a");
					var td = tr.insertCell(-1);			td.innerHTML = "职位名称";
					td = tr.insertCell(-1);				td.innerHTML = "能力1";
					td = tr.insertCell(-1);				td.innerHTML = "能力2";
					td = tr.insertCell(-1);				td.innerHTML = "能力3";
					td = tr.insertCell(-1);				td.innerHTML = "能力4";
					td = tr.insertCell(-1);				td.innerHTML = "能力5";
					td = tr.insertCell(-1);				td.innerHTML = "能力6";
					td = tr.insertCell(-1);				td.innerHTML = "得分";
					td = tr.insertCell(-1);				td.innerHTML = "开始时间";
					td = tr.insertCell(-1);				td.innerHTML = "结束时间";
					//dojo.debug("b");
					for(var i=0;i<bigdata.Current.length;i++)
					{
						var record = bigdata.Current[i];
						tr = table.insertRow(-1);
						td = tr.insertCell(-1);		td.innerHTML = record.posname;
						td = tr.insertCell(-1);		td.innerHTML = record.a1;
						td = tr.insertCell(-1);		td.innerHTML = record.a2;
						td = tr.insertCell(-1);		td.innerHTML = record.a3;
						td = tr.insertCell(-1);		td.innerHTML = record.a4;
						td = tr.insertCell(-1);		td.innerHTML = record.a5;
						td = tr.insertCell(-1);		td.innerHTML = record.a6;
						td = tr.insertCell(-1);		td.innerHTML = record.remark;
						td = tr.insertCell(-1);		td.innerHTML = record.starttime;
						td = tr.insertCell(-1);		td.innerHTML = record.endtime;
						td = tr.insertCell(-1);
	
					}
					//dojo.debug("c");
					container1.innerHTML = "";
					container1.appendChild(table);
				}
			}
			loading(false);
			}catch(e)
			{
			dojo.debug(e);	
			}
		}
		
	});
	
	var container3 = dojo.byId("Apply");
	
	dojo.io.bind({
		url: PositionExperience.applyHistoryURL,
		preventCache : true,
		handler : function (type, data, evt) {
			//dojo.debug(data);
			if(basicHandler(type, data, evt))
			{
				dojo.debug("a");
				bigdata = eval("(" + data + ")");
				test = eval("(" + data + ")");
				dojo.debug("bigdata"+bigdata.length+" adsf");
				if(bigdata.length==0)
				{
					//dojo.debug("bigdata.Current.length==0"+container1);
					container3.innerHTML = "您尚无曾申请记录";
				}
				else
				{
								dojo.debug("a");
					//dojo.debug("bigdata.Current.length!=0"+container1);
					var table = document.createElement("table");
					table.border = "1";
					dojo.html.addClass(table,"getExpTable");
					var tr = table.insertRow(-1);
					//dojo.debug("a");
					td = tr.insertCell(-1);				td.innerHTML = "申请岗位名称";
					td = tr.insertCell(-1);				td.innerHTML = "申请理由";
					td = tr.insertCell(-1);				td.innerHTML = "申请时间";
					td = tr.insertCell(-1);				td.innerHTML = "审批状态";
					dojo.debug("b");
					for(var i=0;i<bigdata.length;i++)
					{
						var record = bigdata[i];
						tr = table.insertRow(-1);
						td = tr.insertCell(-1);		td.innerHTML = record.applyname;
						td = tr.insertCell(-1);		td.innerHTML = record.applyreason;
						td = tr.insertCell(-1);		td.innerHTML = record.applytime;
						td = tr.insertCell(-1);		td.innerHTML = record.applystatus;
//						td = tr.insertCell(-1);		
					}
					//dojo.debug("c");
					container3.innerHTML = "";
					container3.appendChild(table);
				}
				
			}
		}
	});
}

content=dojo.widget.byId("main");
content.onLoad=PositionExperience.init;