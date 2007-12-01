
var ViewnApplyTraining = {getListURL:"training.do?method=getAllAvailableTraining", getDetailURL:"training.do?method=showTrainingDetail", applyURL:"training.do?method=applyTraining", pubBannerHided:false, prvBannerHided:false, onFadingPubBanner:false, onFadingPrvBanner:false };
ViewnApplyTraining.init = function () {
	dojo.io.bind({url:ViewnApplyTraining.getListURL, preventCache:true, handler:function (type, data, evt) {
		if (basicHandler(type, data, evt)) {
			//dojo.debug(data);
			try{
				var bigdata = eval("(" + data + ")");
				dojo.widget.byId("pubTrainingTab").onShow = ViewnApplyTraining.hidePubBanner;
				dojo.widget.byId("prvTrainingTab").onShow = ViewnApplyTraining.hidePrvBanner;
				ViewnApplyTraining.hidePubBanner();
				ViewnApplyTraining.generateTable(bigdata);
			}catch(e)
			{
			dojo.debug(e);
			}
		}
		loading(false);
	}});
};
ViewnApplyTraining.hidePubBanner = function () {
	if (!ViewnApplyTraining.pubBannerHided && !ViewnApplyTraining.onFadingPubBanner) {
		ViewnApplyTraining.onFadingPubBanner = true;
		dojo.lfx.fadeOut("pubTrainingBanner", 500, null, function () {
			ViewnApplyTraining.onFadingPubBanner = false;
			ViewnApplyTraining.pubBannerHided = true;
			dojo.html.hide(dojo.byId("pubTrainingBanner"));
		}).play(600);
	}
	if (ViewnApplyTraining.pubBannerHided && !ViewnApplyTraining.onFadingPubBanner) {
		ViewnApplyTraining.onFadingPubBanner = true;
		dojo.html.show(dojo.byId("pubTrainingBanner"));
		var fadeInOut = dojo.lfx.fadeIn("pubTrainingBanner", 500, null, function (n) {
			dojo.lfx.fadeOut("pubTrainingBanner", 500, null, function () {
				ViewnApplyTraining.onFadingPubBanner = false;
				dojo.html.hide(dojo.byId("pubTrainingBanner"));
			}).play(600);
		});
		fadeInOut.play();
	}
};
ViewnApplyTraining.hidePrvBanner = function () {
	if (!ViewnApplyTraining.prvBannerHided && !ViewnApplyTraining.onFadingPrvBanner) {
		ViewnApplyTraining.onFadingPrvBanner = true;
		dojo.lfx.fadeOut("prvTrainingBanner", 500, null, function () {
			ViewnApplyTraining.onFadingPrvBanner = false;
			ViewnApplyTraining.prvBannerHided = true;
			dojo.html.hide(dojo.byId("prvTrainingBanner"));
		}).play(600);
	}
	if (ViewnApplyTraining.prvBannerHided && !ViewnApplyTraining.onFadingPubBanner) {
		ViewnApplyTraining.onFadingPrvBanner = true;
		dojo.html.show(dojo.byId("prvTrainingBanner"));
		var fadeInOut = dojo.lfx.fadeIn("prvTrainingBanner", 500, null, function (n) {
			dojo.lfx.fadeOut("prvTrainingBanner", 500, null, function () {
				ViewnApplyTraining.onFadingPrvBanner = false;
				dojo.html.hide(dojo.byId("prvTrainingBanner"));
			}).play(600);
		});
		fadeInOut.play();
	}
};
ViewnApplyTraining.generateTable = function (bigdata) {
	//dojo.debug("generateTable");
	var pubTrainingsContainer = dojo.byId("pubTraining");
	var prvTrainingsContainer = dojo.byId("prvTraining");
	
	//pub training goes first
	if (bigdata[0].length > 0) {
		//dojo.debug("bigdata[0].length= " + bigdata[0].length);
		pubTrainingsContainer.innerHTML = "";
		var table = document.createElement("table");
		table.style.width = "100%";
		//dojo.debug("a");
		for (var i = 0; i < bigdata[0].length; i = i + 1) {
			var item = bigdata[0][i];
			
			var tr = table.insertRow(-1);
			tr.style.cursor = "pointer";
			tr.id = "pub_" + item.id;
			//dojo.debug("b1");
			tr.style.background = "#E8EEF7";
			var td = tr.insertCell(-1);
			td.innerHTML = "<div style='width:100%' onclick='ViewnApplyTraining.showDetail(" + item.id + ",0)'>" + item.name + "</div>";
			td = tr.insertCell(-1);
			td.width = "150px";
			td.innerHTML = "<div style='width:100%' onclick='ViewnApplyTraining.showDetail(" + item.id + ",0)'>" + item.deadline + "</div>";
		}
		//dojo.debug("c");
		pubTrainingsContainer.appendChild(table);
	} else {
		pubTrainingsContainer.innerHTML = "no training available";
	}
	//private training goes second
	if (bigdata[1].length > 0) {
		//dojo.debug("bigdata[0].length= " + bigdata[0].length);
		prvTrainingsContainer.innerHTML = "";
		table = document.createElement("table");
		table.style.width = "100%";
		for (i = 0; i < bigdata[1].length; i = i + 1) {
			var item = bigdata[1][i];
			var tr = table.insertRow(-1);
			tr.style.cursor = "pointer";
			tr.id = "prv_" + item.id;
			tr.style.background = "#E8EEF7";
			var td = tr.insertCell(-1);
			td.innerHTML = "<div style='width:100%' onclick='ViewnApplyTraining.showDetail(" + item.id + ",1,"+item.hasApplied+")'>" + item.name + "</div>";
			var td = tr.insertCell(-1);
			td.width = "150px";
			td.innerHTML = "<div style='width:100%' onclick='ViewnApplyTraining.showDetail(" + item.id + ",1,"+item.hasApplied+")'>" + item.deadline + "</div>";
		}
		prvTrainingsContainer.appendChild(table);
	} else {
		prvTrainingsContainer.innerHTML = "no training available";
	}
};
ViewnApplyTraining.showDetail = function (id, type, hasApplied) {
	//dojo.debug("id = "+id+" type = "+type);
	dojo.io.bind({url:ViewnApplyTraining.getDetailURL, preventCache:false, handler:function (loadtype, data, evt) {
		if(basicHandler(loadtype, data, evt))
		{
		//dojo.debug(data);
		if (type == 0) {
			//for pub
			var pubTraining = dojo.byId("pubTraining");
			var pubTrainingDetail = dojo.byId("pubTrainingDetail");
			pubTrainingDetail.innerHTML = "";
			dojo.html.hide(dojo.byId("pubTrainingDetail"));
			var wipeOut = dojo.lfx.wipeOut("pubTraining", 300, null, function (n) {
				var div = document.createElement("div");
				div.innerHTML = "<a href=\"javascript:ViewnApplyTraining.goBackToPub()\">返回公共培训</a><br />&nbsp;";
				pubTrainingDetail.appendChild(div);
				
				//div = document.createTextNode("\u5f97\u5230\u6570\u636e: " + data);
				div = document.createElement("div");
				//div.innerHTML = data;
				var bigdata = eval("(" + data + ")");
				var table = document.createElement("table");
				
				var tr = table.insertRow(-1);
				var td = tr.insertCell(-1);
				td.style.width= "54px";
				
				td.innerHTML = "信息来源";
				td = tr.insertCell(-1);
				td.innerHTML = bigdata.posName;
				
				tr = table.insertRow(-1);
				td = tr.insertCell(-1);
				td.innerHTML = "发布时间";
				td = tr.insertCell(-1);
				td.innerHTML = bigdata.posttime;
								
				tr = table.insertRow(-1);
				td = tr.insertCell(-1);
				td.innerHTML = "培训名称";
				td = tr.insertCell(-1);
				td.innerHTML = bigdata.name;
				

				/*
				tr = table.insertRow(-1);
				td = tr.insertCell(-1);
				td.innerHTML = "&nbsp;";
				*/
				tr = table.insertRow(-1);
				td = tr.insertCell(-1);
				td.innerHTML = "培训时间";
				td = tr.insertCell(-1);
				td.innerHTML = "<strong>"+bigdata.starttime + " - " + bigdata.endtime+"</strong>";
				
				tr = table.insertRow(-1);
				td = tr.insertCell(-1);
				td.innerHTML = "培训内容";
				td = tr.insertCell(-1);
				td.innerHTML = bigdata.description;
				
				if (bigdata.filelist && bigdata.filelist.length > 0) {
					tr = table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "相关资料";
					td = tr.insertCell(-1);
					td.innerHTML = "<a href=\"" + escape(bigdata.filelist[0]) + "\">" + bigdata.filelist[0].split("/")[1] + "</a>";
					for (var i = 1; i < bigdata.filelist.length; i++) {
						td = tr.insertCell(-1);
						td = tr.insertCell(-1);
						td.innerHTML = "<a href=\"" + escape(bigdata.filelist[i]) + "\">" + bigdata.filelist[i].split("/")[1] + "</a>";
					}
				}
				
				
				
				
				div.appendChild(table);
				pubTrainingDetail.appendChild(div);
				
				//TaskFeedback.generateDetailTable(div,eval("("+data+")"));
				
				dojo.lfx.wipeIn("pubTrainingDetail",300).play();
			});
			wipeOut.play();
		}
		if (type == 1) {
			//for prvTraining
			var prvTraining = dojo.byId("prvTraining");
			var prvTrainingDetail = dojo.byId("prvTrainingDetail");
			prvTrainingDetail.innerHTML = "";
			dojo.html.hide(dojo.byId("prvTrainingDetail"));
			var wipeOut = dojo.lfx.wipeOut("prvTraining", 300, null, function (n) {
				var div = document.createElement("div");
				div.innerHTML = "<a href=\"javascript:ViewnApplyTraining.goBackToPrv()\">返回需申请培训箱</a><br />&nbsp;";
				prvTrainingDetail.appendChild(div);
				if(hasApplied == false)
				{
					var div = document.createElement("div");
					div.id = "-_-b";
					div.innerHTML = "<a id='prepareapply' href=\"javascript:ViewnApplyTraining.prepareApply("+id+")\">申请培训</a>";
					prvTrainingDetail.appendChild(div);
				}
				
				div = document.createElement("div");
				//div.innerHTML = data;
				var bigdata = eval("(" + data + ")");
				var table = document.createElement("table");
				
				var tr = table.insertRow(-1);
				var td = tr.insertCell(-1);
				td.style.width= "54px";
				
				td.innerHTML = "信息来源";
				td = tr.insertCell(-1);
				td.innerHTML = bigdata.posName;
				
				tr = table.insertRow(-1);
				td = tr.insertCell(-1);
				td.innerHTML = "发布时间";
				td = tr.insertCell(-1);
				td.innerHTML = bigdata.posttime;
								
				tr = table.insertRow(-1);
				td = tr.insertCell(-1);
				td.innerHTML = "培训名称";
				td = tr.insertCell(-1);
				td.innerHTML = bigdata.name;
				

				/*
				tr = table.insertRow(-1);
				td = tr.insertCell(-1);
				td.innerHTML = "&nbsp;";
				*/
				tr = table.insertRow(-1);
				td = tr.insertCell(-1);
				td.innerHTML = "培训时间";
				td = tr.insertCell(-1);
				td.innerHTML = "<strong>"+bigdata.starttime + " - " + bigdata.endtime+"</strong>";
				
				tr = table.insertRow(-1);
				td = tr.insertCell(-1);
				td.innerHTML = "培训内容";
				td = tr.insertCell(-1);
				td.innerHTML = bigdata.description;

				if (bigdata.filelist && bigdata.filelist.length > 0) {
					tr = table.insertRow(-1);
					td = tr.insertCell(-1);
					td.innerHTML = "相关资料";
					td = tr.insertCell(-1);
					td.innerHTML = "<a href=\"" + escape(bigdata.filelist[0]) + "\">" + bigdata.filelist[0].split("/")[1] + "</a>";
					for (var i = 1; i < bigdata.filelist.length; i++) {
						td = tr.insertCell(-1);
						td = tr.insertCell(-1);
						td.innerHTML = "<a href=\"" + escape(bigdata.filelist[i]) + "\">" + bigdata.filelist[i].split("/")[1] + "</a>";
					}
				}
				
				
				div.appendChild(table);
				prvTrainingDetail.appendChild(div);
				
				//prvTrainingDetail.appendChild(div);
				dojo.lfx.wipeIn("prvTrainingDetail",300).play();
			});
			wipeOut.play();
		}
		}
	}, content:{id:id}});
};
ViewnApplyTraining.goBackToPub = function () {
	dojo.lfx.chain(
		dojo.lfx.wipeOut("pubTrainingDetail", 300),
		dojo.lfx.wipeIn("pubTraining", 300)
	).play();
};
ViewnApplyTraining.goBackToPrv = function () {
	dojo.lfx.chain(
		dojo.lfx.wipeOut("prvTrainingDetail", 300),
		dojo.lfx.wipeIn("prvTraining", 300)
	).play();
};
ViewnApplyTraining.prepareApply = function (id) {
	if(!dojo.byId("reasondiv"))
	{
		var div = document.createElement("div");
		div.id="reasondiv";
		//div.style.left="50px";
		//div.appendChild(document.createTextNode("申请理由"));
		div.innerHTML = "<div style='width:70px;height:250px;line-height:250px;position:relative;float:left'>申请理由</div>"
		var textarea = document.createElement("textarea");
		textarea.id = "reason";
		
		textarea.style.width = "500px";
		textarea.style.height = "250px";		
		div.appendChild(textarea);
		div1 = document.createElement("div");
		div1.innerHTML += "<input type=\"button\" value=\"提出申请\" onclick=\"ViewnApplyTraining.apply("+id+")\" />";
		div.appendChild(div1);
		//dojo.byId("-_-b").innerHTML="";
		dojo.byId("-_-b").appendChild(div);
	}
};
ViewnApplyTraining.apply = function (id) {
	dojo.io.bind({
		url : ViewnApplyTraining.applyURL,
		preventCache : true,
		handler : function(type,data,evt){
			if(basicHandler(type, data, evt)&&data=="true")
			{
				postMessage("申请提交成功!");
				dojo.lfx.wipeOut("prepareapply",300).play();
				dojo.lfx.wipeOut("reasondiv",300).play();
				ViewnApplyTraining.init();
				//TaskFeedback.init();
			}
		},
		method: "post",
		content : {
			trainid : id,
			"applyReason" : dojo.byId("reason").value
		}
	});
	//dojo.lfx.fadeOut("prepareapply",500).play();
};
var content = dojo.widget.byId("main");
content.onLoad = ViewnApplyTraining.init;

