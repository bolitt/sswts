
/*
Copyright (c) 2004-2006, The Dojo Foundation
All Rights Reserved.

Licensed under the Academic Free License version 2.1 or above OR the
modified BSD license. For more information on Dojo licensing, see:

http://dojotoolkit.org/community/licensing.shtml
*/
dojo.provide("dojo.widget.RegexpTextbox");
dojo.require("dojo.widget.ValidationTextbox");
dojo.widget.defineWidget("dojo.widget.RegexpTextbox", dojo.widget.ValidationTextbox, {mixInProperties:function (localProperties, frag) {
	dojo.widget.RegexpTextbox.superclass.mixInProperties.apply(this, arguments);
	if (localProperties.regexp) {
		this.flags.regexp = localProperties.regexp;
	}
	if (localProperties.flags) {
		this.flags.flags = localProperties.flags;
	}
}, isValid:function () {
	var regexp = new RegExp(this.flags.regexp, this.flags.flags);
	return regexp.test(this.textbox.value);
}, force:false, update:function () {
	if (this.force) {
		this.missingSpan.style.display = "none";
		this.invalidSpan.style.display = "none";
		this.rangeSpan.style.display = "none";
		var empty = this.isEmpty();
		var valid = true;
		if (this.promptMessage != this.textbox.value) {
			valid = this.isValid();
		}
		if (valid) {
			this.lastCheckedValue = this.textbox.value;
		} else {
			this.textbox.value = this.lastCheckedValue;
		}
	} else {
		this.lastCheckedValue = this.textbox.value;
		this.missingSpan.style.display = "none";
		this.invalidSpan.style.display = "none";
		this.rangeSpan.style.display = "none";
		var empty = this.isEmpty();
		var valid = true;
		if (this.promptMessage != this.textbox.value) {
			valid = this.isValid();
		}
		var missing = this.isMissing();
		if (missing) {
			this.missingSpan.style.display = "";
		} else {
			if (!empty && !valid) {
				this.invalidSpan.style.display = "";
			} else {
				if (!empty && !this.isInRange()) {
					this.rangeSpan.style.display = "";
				}
			}
		}
		this.highlight();
	}
}});

