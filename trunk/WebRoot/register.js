var registerCheckURL = "servlet/CheckNewUser";
var registerURL = "servlet/Register";
var ShowNation = false;
var userinfoShowed = true;
function init()
{
	dojo.lfx.html.wipeOut('nation1', 1).play();
	dojo.lfx.html.wipeOut('nation2', 1).play();
}

dojo.addOnLoad(init);

function toggleNation()
{
	//dojo.debug("in toggleNation");
	currentRegion=dojo.widget.byId("region").comboBoxSelectionValue.value;
	if(currentRegion === "CN" && ShowNation === false)
	{
		//dojo.debug("you selected china, show nation!");
		dojo.lfx.html.wipeIn('nation1', 300).play();
		dojo.lfx.html.wipeIn('nation2', 300).play();
		ShowNation = true;
	}
	if(currentRegion != "CN" && ShowNation ===true)
	{
		//dojo.debug("you didn't select china, hide nation!");
		dojo.lfx.html.wipeOut('nation1', 300).play();
		dojo.lfx.html.wipeOut('nation2', 300).play();
		ShowNation = false;	
	}
}

function checkStudentnum()
{
	var currentNumber = dojo.byId("studentnum").value;
	dojo.io.bind({
				url: registerCheckURL,
				handler: checkStudentnumCallback,
				preventCache: true,
				content:{
				studentnum: currentNumber
				}
	});	
}

function checkStudentnumCallback(type,data,evt)
{
	if(type=='error')
	{
		//dojo.debug("sth wrong....");
		//dojo.debug("i'm sorry.. something's wrong with the system");
	}
	else
	{
		if(data==="true")
		{
			//login successful
			//dojo.debug("current number occupied");
			dojo.byId("studentnumExists").innerHTML="occupied";
		}
		if(data==="false")
		{
			//dojo.debug("current number available, show userinfo");
			dojo.byId("studentnumExists").innerHTML="available";
			if(!userinfoShowed)
			{
			dojo.lfx.wipeOut("userinfo",350).play();
			userinfoShowed=!userinfoShowed;
			}
		}
		if(data==="induct")
		{
			//dojo.debug("inducted, hide userinfo");
			if(userinfoShowed)
			{
			dojo.lfx.wipeOut("userinfo",350).play();
			userinfoShowed=!userinfoShowed;
			}
		}
	}
}


function doRegister()
{
	var mystudentnum = dojo.byId("studentnum").value;
	var myname = dojo.byId("name").value;
	var password1 = dojo.byId("password1").value;
	var password2 = dojo.byId("password2").value;
	var mygrade= dojo.byId("grade").value;
	var mygender= dojo.widget.byId("gender").comboBoxSelectionValue.value;
	var myregion= dojo.widget.byId("region").comboBoxSelectionValue.value;
	var mynation= dojo.widget.byId("nation").comboBoxSelectionValue.value;
	var mydepartment= dojo.widget.byId("department").comboBoxSelectionValue.value;
	var myclassnum= dojo.byId("classnum").value;
	var mypolitics= dojo.widget.byId("politics").comboBoxSelectionValue.value;
	var mytel= dojo.byId("tel").value;
	var mymobile= dojo.byId("mobile").value;
	var myemail= dojo.byId("email").value;
	var myaddress= dojo.byId("address").value;

	//do more validation here
	if(password1!=password2)
	{
		//dojo.debug("password not match");
		return;
	}
	var mypassword =dojo.crypto.MD5.compute(password1, dojo.crypto.outputTypes.Hex);

	dojo.io.bind({
				url: registerURL,
				handler: registerCallback,
				preventCache: true, 
				content:{
				studentnum: mystudentnum,
				name:myname,
				password:mypassword,
				grade:mygrade,
				gender:mygender,
				region:myregion,
				nation:mynation,
				department:mydepartment,
				classnum:myclassnum,
				political:mypolitics,
				tel:mytel,
				mobile:mymobile,
				email:myemail,
				address:myaddress,
				specialty:dojo.byId("specialty").value}
	});	
}

function registerCallback(type,data,evt)
{
	if(type=='error')
	{
		//dojo.debug("sth wrong....");
		//dojo.debug("i'm sorry.. something's wrong with the system");
	}
	else
	{
		if(data==="true")
		{
			//login successful
			//dojo.debug("register OK");
			document.location="index.jsp";
		}
		else
		{
			//dojo.debug("register failed");
		}
	}
}