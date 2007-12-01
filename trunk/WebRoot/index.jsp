<%@ page contentType="text/html; charset=utf-8" language="java"
	import="org.net9.redbud.util.*,java.util.*" errorPage=""%>

<%
	String ticket = request.getParameter("ticket");
	if(ticket != null)
	{
		//已经通过了info认证系统。
		//转交login.do来解析ticket内容
		response.sendRedirect("newlogin.do?ticket="+ticket);
		//这个之后就会有session记录了
		return;
	}

	String isLoggedIn = (String) session.getAttribute("studentnum");
	if (isLoggedIn == null) {
		response.sendRedirect("login.html");

	} else {
		//	out.print("hello!");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>清华大学学生社会工作信息中心</title>
		<script type="text/javascript">
		   djConfig = {isDebug: true
						,debugAtAllCosts : false
						,debugContainerId : "_dojoDebugConsole",
						bindEncoding : "UTF-8",
						preventBackButtonFix : false,
						extraLocale: ['en-us', 'zh-cn', 'zh-tw']			
						};
			var pathRelative = "/sswts/";
</script>
		<script type="text/javascript" src="cookies.js"></script>
		<script type="text/javascript" src="applicationState.js"></script>
		<script type="text/javascript" src="dojoAjax/dojo.js"></script>
		<script type="text/javascript" src="dojoinclude.js"></script>
		<script type="text/javascript" src="boxes/inbox.js"></script>
		<script type="text/javascript" src="index.js"></script>
		<script type="text/javascript" src="left/left.js"></script>


		<link rel="stylesheet" type="text/css" href="style.css" />
	</head>

	<body>
		<div id="outer">
			<div class="logo">
				<img src="images/main/logo.jpg" alt="LOGO"
					onclick="fireNavigatorButtonEvent('ShowInformation')"
					style="cursor: pointer" />
			</div>
			<div class="title">
				<div class="welcome" id="welcome">
					<%
							out.print("欢迎您!"
							+ request.getSession().getAttribute("studentnum"));
							out.flush();
					%>
				</div>
				<div class="inbox">
					<img src="images/dot.gif" />
					<a id="inbox" href="javascript:fireNavigatorButtonEvent('inbox')">收件箱</a>
				</div>
				<div class="logout"
					onclick="SetCookie('lastModule','ShowInformation');document.location='logout.do?method=logout';">
					退出系统
				</div>
				<div class="aboutUs">
					<a href="aboutus.html" target="_blank"
						style="text-decoration:none; color:black">关于我们</a>
				</div>
			</div>
			<div
				style="width: 990px; height: 448px; border-bottom: 1px solid #b2b2b2">
				<div dojoType="AccordionContainer" duration="200" id="acc_personal"
					labelNodeClass="accordionlabel" containerNodeClass="accBody"
					class="left">
					<div dojoType="ContentPane" selected="true"
						label="<img src='images/left1.jpg'>" style="overflow: auto">
						<div class="leftButton">
							<ul style="padding: 0px; margin: 0px">
								<li class="item" id="ShowInformation"
									onclick="fireNavigatorButtonEvent('ShowInformation',0)">
									个人主页
								</li>
								<li class="item" id="EditInformation"
									onclick="fireNavigatorButtonEvent('EditInformation',0)">
									更改信息
								</li>

								<li class="item" id="EditPassword"
									onclick="fireNavigatorButtonEvent('EditPassword',0)">
									修改密码
								</li>
							</ul>
						</div>
					</div>
					<div dojoType="ContentPane" selected="false" id="acc_position"
						label="<img src='images/left2.jpg'>" style="overflow: auto;">
						<div class="leftButton">
							<ul style="padding: 0px; margin: 0px">
								<li class="item" id="WorkingExp"
									onclick="fireNavigatorButtonEvent('WorkingExp',1)">
									社工经历
								</li>
								<li class="item" id="BrowsePosition"
									onclick="fireNavigatorButtonEvent('BrowsePosition',1)">
									岗位浏览
								</li>
								<li class="item" id="ApplyPosition"
									onclick="fireNavigatorButtonEvent('ApplyPosition',1)">
									岗位申请
								</li>
								<%
										if (PermissionValidate.ifUserHasPermission(
										(ArrayList<String>) session.getAttribute("postList"),
										"isAtLeastOrganBoss")) {
								%>

								<li class="item" id="ApprovePosition"
									onclick="fireNavigatorButtonEvent('ApprovePosition',1)">
									岗位审批
								</li>
								<li class="item" id="AppointPosition"
									onclick="fireNavigatorButtonEvent('AppointPosition',1)">
									岗位任免
								</li>
								<%
								} else {
								%>
								<li class="darkitem">
									岗位审批
								</li>
								<li class="darkitem">
									岗位任免
								</li>
								<%
								}
								%>
							</ul>
						</div>
					</div>
					<div dojoType="ContentPane" selected="false" id="acc_training"
						label="<img src='images/left3.jpg'>" style="overflow: auto;">
						<div class="leftButton">
							<ul style="padding: 0px; margin: 0px">
								<li class="item" id="ViewnApplyTraining"
									onclick="fireNavigatorButtonEvent('ViewnApplyTraining','2')">
									查看培训活动
								</li>
								<li class="item" id="ApplyStatus"
									onclick="fireNavigatorButtonEvent('ApplyStatus','2')">
									查看申请状态
								</li>
								<li class="item" id="AuthTraining"
									onclick="fireNavigatorButtonEvent('AuthTraining','2')">
									认证培训活动
								</li>
								<%
										if (PermissionValidate.ifUserHasPermission(
										(ArrayList<String>) session.getAttribute("postList"),
										"isAtLeastOrganBoss")) {
								%>
								<li class="item" id="PostTraining"
									onclick="fireNavigatorButtonEvent('PostTraining','2')">
									发布培训信息
								</li>
								<li class="item" id="ApproveTraining"
									onclick="fireNavigatorButtonEvent('ApproveTraining','2')">
									审批培训申请
								</li>
								<li class="item" id="ManageTraining"
									onclick="fireNavigatorButtonEvent('ManageTraining','2')">
									管理培训活动
								</li>
								<%
								} else {
								%>
								<li class="darkitem">
									发布培训信息
								</li>
								<li class="darkitem">
									审批培训申请
								</li>
								<li class="darkitem">
									管理培训活动
								</li>
								<%
								}
								%>
							</ul>
						</div>
					</div>
					<div dojoType="ContentPane" selected="false" id="acc_information"
						label="<img src='images/left5.jpg'>" style="overflow: auto;">
						<div class="leftButton">
							<ul style="padding: 0px; margin: 0px">

								<%
										if (PermissionValidate.ifUserHasPermission(
										(ArrayList<String>) session.getAttribute("postList"),
										"isAtLeastCategoryBoss")) {
								%>
								<li class="item" id="PublicInformation"
									onclick="fireNavigatorButtonEvent('PublicInformation','3')">
									校级信息发布
								</li>
								<%
								} else {
								%>

								<li class="darkitem">
									校级信息发布
								</li>
								<%
								}
								%>
								<%
										if (PermissionValidate.ifUserHasPermission(
										(ArrayList<String>) session.getAttribute("postList"),
										"isRoot")) {
								%>
								<li class="item" id="ReleaseInformation"
									onclick="fireNavigatorButtonEvent('ReleaseInformation','3')">
									放行校级信息
								</li>
								<%
								}
								%>
								<%
										if (PermissionValidate.ifUserHasPermission(
										(ArrayList<String>) session.getAttribute("postList"),
										"isAtLeastDepartmentBoss")) {
								%>
								<li class="item" id="PostInternalInfo"
									onclick="fireNavigatorButtonEvent('PostInternalInfo','3')">
									内部通知发布
								</li>
								<li class="item" id="PostInternalTask"
									onclick="fireNavigatorButtonEvent('PostInternalTask','3')">
									工作任务部署
								</li>
								<li class="item" id="TaskFeedback"
									onclick="fireNavigatorButtonEvent('TaskFeedback','3')">
									任务反馈查看
								</li>
								<%
								} else {
								%>
								<li class="darkitem">
									内部通知发布
								</li>
								<li class="darkitem">
									工作任务部署
								</li>
								<li class="darkitem">
									任务反馈查看
								</li>
								<%
								}
								%>
							</ul>
						</div>
					</div>
					<div dojoType="ContentPane" selected="false" id="acc_system"
						label="<img src='images/left7.jpg'>" style="overflow: auto;">
						<div class="leftButton">
							<ul style="padding: 0px; margin: 0px">
								<%
										if (PermissionValidate.ifUserHasPermission(
										(ArrayList<String>) session.getAttribute("postList"),
										"isAtLeastCategoryBoss")) {
								%>

								<li class="item" id="ModifyPositionArchitecture"
									onclick="fireNavigatorButtonEvent('ModifyPositionArchitecture','4')">
									修改岗位结构
								</li>
								<%
								} else {
								%>
								<li class="darkitem">
									修改岗位结构
								</li>
								<%
								}
								%>
								<%
										if (PermissionValidate.ifUserHasPermission(
										(ArrayList<String>) session.getAttribute("postList"),
										"isRoot")) {
								%>
								<li class="item" id="Init"
									onclick="fireNavigatorButtonEvent('Init','4')">
									初始化数据库
								</li>
								<%
								}
								%>

							</ul>
						</div>
					</div>
				</div>
				<div class="main" dojoType="ContentPane" id="main"
					executeScripts="true" scriptSeparation="false" cacheContent="false"
					>
					this is the main panel
				</div>

			</div>
			<div class="bottom">
				本系统推荐在1024*768以上分辨率、微软雅黑字体观看
			</div>
			<div dojoType="toaster" id="toast" positionDirection="tr-down"
				outerNodeName="outer" showDelay="4000"
				messageTopic="testMessageTopic"></div>
		</div>
		<div dojoType="dialog" id="loadingDlg" bgColor="#EEEEFF"
			bgOpacity="0.8" toggle="fade" toggleDuration="500"
			style="display: none">
			<table>
				<tr>
					<td>
						加载中，请勿刷新页面
					</td>
				</tr>
			</table>

	</body>
</html>
<%
}
%>
