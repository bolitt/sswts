/*
	Copyright (c) 2004-2006, The Dojo Foundation
	All Rights Reserved.

	Licensed under the Academic Free License version 2.1 or above OR the
	modified BSD license. For more information on Dojo licensing, see:

		http://dojotoolkit.org/community/licensing.shtml
*/

dojo.provide("dojo.lfx.extras");
dojo.require("dojo.lfx.html");
dojo.require("dojo.lfx.Animation");
dojo.lfx.html.fadeWipeIn = function (nodes, duration, easing, callback) {
	nodes = dojo.lfx.html._byId(nodes);
	var anim = dojo.lfx.combine(dojo.lfx.fadeIn(nodes, duration, easing), dojo.lfx.wipeIn(nodes, duration, easing));
	if (callback) {
		anim.connect("onEnd", function () {
			callback(nodes, anim);
		});
	}
	return anim;
};
dojo.lfx.html.fadeWipeOut = function (nodes, duration, easing, callback) {
	nodes = dojo.lfx.html._byId(nodes);
	var anim = dojo.lfx.combine(dojo.lfx.fadeOut(nodes, duration, easing), dojo.lfx.wipeOut(nodes, duration, easing));
	if (callback) {
		anim.connect("onEnd", function () {
			callback(nodes, anim);
		});
	}
	return anim;
};
dojo.lfx.html.scale = function (nodes, percentage, scaleContent, fromCenter, duration, easing, callback) {
	nodes = dojo.lfx.html._byId(nodes);
	var anims = [];
	dojo.lang.forEach(nodes, function (node) {
		var outer = dojo.html.getMarginBox(node);
		var actualPct = percentage / 100;
		var props = [{property:"width", start:outer.width, end:outer.width * actualPct}, {property:"height", start:outer.height, end:outer.height}];
		var anim = dojo.lfx.propertyAnimation(node, props, duration, easing);
		if (callback) {
			anim.connect("onEnd", function () {
				callback(node, anim);
			});
		}
		anims.push(anim);
	});
	return dojo.lfx.combine(anims);
};
dojo.lang.mixin(dojo.lfx, dojo.lfx.html);

