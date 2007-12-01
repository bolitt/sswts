var TrainingApplyStatus = 
{ applyStatusURL : "training.do?method=getAppliedTraining"
};

TrainingApplyStatus.init = function()
{
	var container = dojo.byId("applystatus");
	dojo.io.bind({
		url: TrainingApplyStatus.applyStatusURL,
		preventCache : true,
		handler : function (type, data, evt) {
			//dojo.debug(data);
			if(basicHandler(type, data, evt))
			{
				bigdata = eval("(" + data + ")");
				if(bigdata.length==0)
				{
					container.innerHTML = "您尚无申请记录";
					return;
				}
				
				var table = document.createElement("table");
				table.border = "1";
				dojo.html.addClass(table,"applyStatusTable");
				/*table.style["border-collapse"]="collapse";
				table.style["background"]="#d8d8d8";
				table.style["border"]="1px solid white";
				table.style["width"]="97%";
				*/
				var tr = table.insertRow(-1);
				var td = tr.insertCell(-1);
				td.innerHTML = "培训名称";
				td = tr.insertCell(-1);
				td.innerHTML = "培训开始时间";
				td = tr.insertCell(-1);
				td.innerHTML = "培训结束时间";
				td = tr.insertCell(-1);
				td.innerHTML = "申请状态";
				for(var i=0;i<bigdata.length;i++)
				{
					var record = bigdata[i];
					tr = table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = record.name;
					td = tr.insertCell(-1);
					td.innerHTML = record.starttime;
					td = tr.insertCell(-1);
					td.innerHTML = record.endtime;
					td = tr.insertCell(-1);
					switch(record.applystatus){
						case "0":
							td.innerHTML = "待审批";
							break;
						case "1":
							td.innerHTML = "已通过";
							break;
						case "2":
							td.innerHTML = "被否决";
							break;
						case "3":
							td.innerHTML = "培训被取消";
							break;						
					}
				}
				container.innerHTML = "";
				container.appendChild(table);
			}
		
		}
	});
	loading(false);
}

content=dojo.widget.byId("main");
content.onLoad=TrainingApplyStatus.init;
