var postInternalInfo = {
	getPosURL : "pubinfo.do?method=showStaffTree",
	postpostInternalInfoURL : "pubinfo.do?method=postInternalInfo",
	//maybe need review on these []s
	mydata : [],
	selected : [],
	simple_selected : []
};

//init function
postInternalInfo.init = function() {

	dojo.widget.byId("postcode").domNode.childNodes[2].disabled=true;
	dojo.widget.byId("postcode").setAllValues("","");
	
	//show loading dialog
	loading(true);
	dojo.io.bind({url:postInternalInfo.getPosURL, handler:function (type, data, evt) {
		if (basicHandler(type, data, evt)) {
			//dojo.debug("get data: " + data);
			var bigdata = eval(data);
			//dojo.debug(bigdata);
			//parse school positions
			//var schooldata = bigdata[0];
			var node = dojo.byId("trees");
			//dojo.debug("start preparse");
			//after get data, generate tree
			postInternalInfo.generateSelectTree(node, postInternalInfo.newPreParse(bigdata));
		}
		//hide dialog
		loading(false, 1);
	}, preventCache:true, content:{}});
};

//这是使用了递归来简单处理数据的函数,貌似目前看来没有问题.
postInternalInfo.newPreParse = function(data)
{
	//dojo.debug("new parser start!");
	//get one copy
	var oridata = data;
	for (var i=0; i<oridata.length; i=i+1)
	{
		//original node could be cate/org/dept
		var oriNode = oridata[i];
		var node = {};
		var node_selected = {};
		if(oriNode.postNo)
		{
			node.title = "<input name=\"tree_" + i + "\" type=\"checkbox\"posno=" + oriNode.postNo + " onchange='postInternalInfo.selectPart(this)' />" + oriNode.title;
		}
		else
		{
			node.title = "<input name=\"tree_" + i + "\" type=\"checkbox\" onchange='postInternalInfo.selectPart(this)' />" + oriNode.title;
		}
		//dojo.debug(oriNode.title);
		node.children = [];
		node_selected.title = oriNode.title;
		node_selected.children = [];
		
		postInternalInfo._newPreParse(oriNode, node, node_selected, "tree_"+i);
		
		postInternalInfo.selected.push(node_selected);
		postInternalInfo.mydata.push(node);
	}
	return postInternalInfo.mydata;
};

//var oriNode_parent = [];  	//			cate			org				org
postInternalInfo._newPreParse = function(oriNode_parent, node_parent, node_selected_parent, name_prefix)
{
	try
	{
	//dojo.debug("in _newPreParse");
	//oriNode_parent = oriNode_parent1;
	//var oriCate = oridata[i];
	//here oriCate equals to oriNode
		//cate.children
	if(oriNode_parent.children)
	{
		//dojo.debug(oriNode_parent.title+" has "+oriNode_parent.children.length+" children"); 
						//cate.children.length
		for(var i=0;i<oriNode_parent.children.length;i=i+1)
		{
			//dojo.debug(" 第"+i+"次循环");
			//oriOrg
			var oriNode = oriNode_parent.children[i];
			
			//dojo.debug("get one oriNode"+oriNode);
			//org	
			var node = {};
			//org_selected
			var node_selected = {};
			
			var currenPrefix = name_prefix+"_"+i;
			//dojo.debug("currenPrefix "+currenPrefix);
			if(oriNode.postNo)
			{
				node.title = "<input name=\"" + currenPrefix + "\" type=\"checkbox\"posno=" + oriNode.postNo + " onchange='postInternalInfo.selectPart(this)' />" + oriNode.title;
			}
			else
			{
				node.title = "<input name=\"" + currenPrefix + "\" type=\"checkbox\" onchange='postInternalInfo.selectPart(this)' />" + oriNode.title;
			}
			//org
			node.children = [];
			
			//org_selected
			node_selected.title = oriNode.title;
			node_selected.children = [];
			//dojo.debug("before push");
			postInternalInfo._newPreParse(oriNode, node, node_selected, currenPrefix);
			node_parent.children.push(node);
			node_selected_parent.children.push(node_selected);
			
		}
	}
	}catch(e)
	{
		//dojo.debug("ERROR: "+e);
	}
}
/*
postInternalInfo.preParse = function(data) {
	var oridata = data;
	//var mydata = [];
	//dojo.debug("start");
	for (var i = 0; i < oridata.length; i=i+1) {
		var oriCate = oridata[i];
		var Cate = {};
		var Cate_selected = {};
		Cate.title = "<input name=\"tree_" + i + "\" type=\"checkbox\" onchange='postInternalInfo.selectPart(this)' />" + oriCate.title;
		Cate.children = [];
		Cate_selected.selected = false;
		Cate_selected.title = oriCate.title;
		Cate_selected.children = [];
		//dojo.debug("Cate : "+oriCate.title);
		if(oriCate.children)
		{
			//dojo.debug("has children");
			for (var j = 0; j < oriCate.children.length; j=j+1) {
				var oriOrg = oriCate.children[j];
				var Org = {};
				Org.title = "<input name=\"tree_" + i + "_" + j + "\" type=\"checkbox\" onchange='postInternalInfo.selectPart(this)' />" + oriOrg.title;
				Org.children = [];
				var Org_selected = {};
				Org_selected.selected = false;
				Org_selected.title = oriOrg.title;
				Org_selected.children = [];
				//dojo.debug("oriOrg : "+oriOrg.title);
				if(oriOrg.children)
				{
					//dojo.debug("has children");
					for (var k = 0; k < oriOrg.children.length; k=k+1) {
						var oriDept = oriOrg.children[k];
						var Dept = {};
						Dept.title = "<input name=\"tree_" + i + "_" + j + "_" + k + "\" type=\"checkbox\" onchange='postInternalInfo.selectPart(this)' />" + oriDept.title;
						Dept.children = [];
						var Dept_selected = {};
						Dept_selected.selected = false;
						Dept_selected.title = oriDept.title;
						Dept_selected.children = [];
						//dojo.debug("oriDept : "+oriDept.title);
						if(oriDept.children)
						{
							//dojo.debug("has children");
							for (var l = 0; l < oriDept.children.length; l=l+1) {
								var oriPos = oriDept.children[l];
								var Pos = {};
								Pos.title = "<input name=\"tree_" + i + "_" + j + "_" + k + "_" + l + "\" type=\"checkbox\" posno=" + oriPos.postNo + " onchange='postInternalInfo.selectPart(this)' />" + oriPos.title;
								//dojo.debug("a..."+Pos.title);
								var Pos_selected = {};
								Pos_selected.selected = false;
								Pos_selected.title = oriPos.title;
								Dept.children.push(Pos);
								Dept_selected.children.push(Pos_selected);
							}
						}
						Org.children.push(Dept);
						Org_selected.children.push(Dept_selected);
					}
				}
				Cate.children.push(Org);
				Cate_selected.children.push(Org_selected);
			}
		}
		postInternalInfo.selected.push(Cate_selected);
		postInternalInfo.mydata.push(Cate);
	}
	//dojo.debug("end preparse");
	return postInternalInfo.mydata;
};
*/

//画树,并展开
postInternalInfo.generateSelectTree = function(node, data) {
	//dojo.debug("in generateTree , there're " + data.length + " orgs");
	var controller = dojo.widget.createWidget("TreeBasicControllerV3");
	//var doccontroller = dojo.widget.createWidget("TreeDocIconExtension");
	var TreeSelectorV3 = dojo.widget.createWidget("TreeSelectorV3");
	var TreeEmphasizeOnSelect = dojo.widget.createWidget("TreeEmphasizeOnSelect", {selector:TreeSelectorV3});
	var tree = dojo.widget.createWidget("TreeV3", {toggle:"wipe",listeners:[controller.widgetId, TreeSelectorV3.widgetId]});
	tree.setChildren(data);
	
	controller.expandToLevel(tree,4);
	//controller.collapse();
	node.innerHTML = "";
	node.appendChild(tree.domNode);
	/*var reporter = function (reporter) {
		this.name = "select";
		this.go = function (message) {
			if (message.node.postNo) {
				showPosDetail(message.node.postNo);
			}
		};
	};*/
	//dojo.event.topic.subscribe(TreeSelectorV3.eventNames.select, new reporter("TreeSelectorV3"), "go");
};
//当点选某个选择框的时候,自动选择子节点
postInternalInfo.selectPart = function(checkBox) {
	//dojo.debug(checkBox.name + " clicked!,set status :" + checkBox.checked);
	
	var namePrefix = checkBox.name;
	var inputs = document.getElementsByTagName("input");
	
	for (var i=0;i<inputs.length;i=i+1)
	{
		var input = inputs[i];
		
		if(input.name.substr(0,namePrefix.length)==namePrefix)
		{
			input.checked = checkBox.checked;
			//dojo.debug(input.getAttribute("posno")+" "+input.checked);
			postInternalInfo.simple_selected[input.getAttribute("posno")] = input.checked;
			postInternalInfo.setSelectedStatus(input, checkBox.checked);
		}
	}
	//dojo.debug(dojo.json.serialize(selected));
	//alert(inputs.length);
};
//顺便设置js变量的状态
postInternalInfo.setSelectedStatus = function(input,status)
{
	//dojo.debug("input name:" + input.name);
	var flags = input.name.split("_");
	if(flags[4])//is pos
	{
		//dojo.debug("got one position: " + selected[flags[1]].children[flags[2]].children[flags[3]].children[flags[4]].title);
		postInternalInfo.selected[flags[1]].children[flags[2]].children[flags[3]].children[flags[4]].selected = status;
		
		return;
	}
	if(flags[3])//is dept
	{
		//dojo.debug("got one dept: " + selected[flags[1]].children[flags[2]].children[flags[3]].title);
		postInternalInfo.selected[flags[1]].children[flags[2]].children[flags[3]].selected = status;
		return;
	}
	if(flags[2])//is org
	{
		//dojo.debug("got one org: " + selected[flags[1]].children[flags[2]].title);
		postInternalInfo.selected[flags[1]].children[flags[2]].selected = status;
		return;
	}
	if(flags[1])//is cate
	{
		//dojo.debug("got one cate: " + selected[flags[1]].title);
		postInternalInfo.selected[flags[1]].selected = status;
		return;
	}	
};
//当提交前,根据选择状态,生成合适的postition列表
postInternalInfo.generatePostList = function()
{
	var lists = "";
	for(var prop in postInternalInfo.simple_selected)
	{
		if(prop!="null"&&postInternalInfo.simple_selected[prop])
		{
			lists+=prop+",";
		}
	}
	lists.substr(0,lists.length-1);
	//dojo.debug(lists);
	return lists;
};
//发布出去
postInternalInfo.addPubInformation =function(){
	if(confirm("该公告将发送给许多同学，您确定要发布么？"))
	{
		dojo.byId("lists").value=this.generatePostList();
		dojo.io.bind({
			url: postInternalInfo.postpostInternalInfoURL,
			preventCache: true,
			method: "POST",
			formNode: dojo.byId("PostInternalInfoForm"),
			handler: function(type, data, evt){
				if(basicHandler(type, data, evt))
				{
				if(data=="true")
				{
					postMessage("OK");
				}
				else
				{
					postMessage("failed","FATAL");
				}
			}
			}
			
		});
	}
}


var content = dojo.widget.byId("main");
content.onLoad = postInternalInfo.init;

