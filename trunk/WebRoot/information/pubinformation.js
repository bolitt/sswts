var pubInformation = {
	postPubInformationURL : "pubinfo.do?method=postPublicInfo"
};
//发布全校性信息
pubInformation.addPubInformation =function(){
	if(confirm("该公告将发送给全校同学，您确定要发布么？"))
	{
		dojo.io.bind({
			url: pubInformation.postPubInformationURL,
			preventCache: true,
			method: "POST",
			formNode: dojo.byId("pubInformation"),
			handler: function(type, data, evt){
				if(basicHandler(type, data, evt)&&data=="true")
				{
					postMessage("发布成功，等待管理员审批");
				}
				else
				{
					postMessage("发布失败","FATAL");
				}
			}
		});
	}
};

var content = dojo.widget.byId("main");
content.onLoad = function(){
	dojo.widget.byId("postcode").domNode.childNodes[2].disabled=true;
	dojo.widget.byId("postcode").setAllValues("","");
	loading(false);
	
};

