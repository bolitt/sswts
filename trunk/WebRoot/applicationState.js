
/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/
/*
ApplicationState is an object that represents the application state.
It will be given to dojo.undo.browser to represent the current application state.
*/
ApplicationState = function (navFunc, stateData, bookmarkValue) {
	this.navFunc = navFunc;
	this.stateData = stateData;
	this.changeUrl = bookmarkValue;
	//dojo.debug("push "+document.title+" to stack");
	this.title = document.title;
	if(this.stateData == "go index!")
	{
		document.title = "清华大学社会工作系统";
	}
	else
	{
		document.title = "清华大学社会工作系统 -- " + stateData;
	} 
};
ApplicationState.prototype.back = function () {
	this.showBackForwardMessage("BACK for State Data: " + this.stateData);
	this.showStateData();
	this.navFunc();
	myNavigator.setButtonStyles(this.stateData);
	document.title = this.title;
};
ApplicationState.prototype.forward = function () {
	this.showBackForwardMessage("FORWARD for State Data: " + this.stateData);
	this.showStateData();
	this.navFunc();
	myNavigator.setButtonStyles(this.stateData);
	document.title = this.title;
};
ApplicationState.prototype.showStateData = function () {
	//dojo.debug(this.stateData);
};
ApplicationState.prototype.showBackForwardMessage = function (message) {
	//dojo.debug(message);
};
//dojo.addOnLoad(function () {
//	var appState = new ApplicationState(goIndex,"go index!");
//	appState.showStateData();
//	dojo.undo.browser.setInitialState(appState);
//});

