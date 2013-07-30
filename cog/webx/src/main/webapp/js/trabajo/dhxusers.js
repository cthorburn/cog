tbjEvents.addListener( { name: 'usermodel_changed', context: this, func: function(evt) { this.data=evt.data; }} );

tbjEvents.addListener( { name: 'grid_dblclick', context: this, func: function(evt) {
        server.asyncServlet({servlet: 'json', feed: 'AdUsSu_user_detail', data: {userid: evt.data.userid  }, context: {

            userid: evt.data.userid,
            
            AdUsSu_user_detail_OK: function(response) {
                tbjEvents.fireEvent({name: 'usermodel_changed', data: response});
            }
        }});
} } );

function FormAdUsEdNwId(dhxForm, dhxTbr, dhxAccord) {
	this.dhx=dhxForm;
	this.dhx.setSkin("dhx_skyblue");

	this.tbbr=dhxTbr;
	this.acdn=dhxAccord;
	
	this.dhx.FormAdUsEdNwId=this;
	this.spec=[
	   	    {type: "settings", position: "label-left", labelWidth: 100, inputWidth: 120 },
	   		
	   		{type: "fieldset",  label: "Identity", width: 400, list:[
               {type: "input", name: "fullName", label: "Full Name", value: "Colin Thorburn",  required: true},
	   		   {type: "input", name: "email", label: "Email Address", value: "cthorburn8@gmail.com",required: true},
	   		   {type: "input", name: "username", value: "colin", label: "Username", required: true},
	   		   {type: "password", name: "password", value: "colin", label: "Password", required: true},
	   		   {type: "button", name: "create", value: "Create"}]
			}
	];
	
	this.dhx.attachEvent("onButtonClick", function(name) {
		if("create"==name) {
			var data=this.getFormData();
			server.asyncServlet({servlet: 'user', action: 'create', data: data,   context: this.FormAdUsEdNwId });
		}
	});
	
	this.createOK=function(result) {
		this.dhx.clear();
	};
	
	this.exception_DUPLICATE_USERNAME=function(exception) {
		this.dhx.setItemValue("username", "");
		talert("That username already exists!");
	};
	
	this.dhx.loadStruct(this.spec, "json", function() {});

	tbjEvents.addListener( { name: 'grid_dblclick', context: this, func: 
		function (evt) {
			this.dhx.clear(); 
			this.acdn.cells("new").close();
		}});
}


function FormAdUsEdEdId(dhxForm, dhxTbr, dhxAccord) {
	this.dhx=dhxForm;
	this.dhx.setSkin("dhx_skyblue");
	this.tbbr=dhxTbr;
	this.acdn=dhxAccord;
	this.dhx.FormAdUsEdNwId=this;
	
	this.spec=[
	   	    {type: "settings", position: "label-left", labelWidth: 100, inputWidth: 120 },
	   		
	   		{type: "fieldset",  label: "Identity", width: 400, list:[
               {type: "input", name: "fullName", label: "Full Name", required: true},
	   		   {type: "input", name: "email", label: "Email Address", required: true},
	   		   {type: "password", name: "password", label: "Password", value: "Password", required: true},
	   		   {type: "button", name: "update", value: "Update"}]
			}
	];
	
	this.dhx.attachEvent("onButtonClick", function(name) {
		alert("send user update to server");
	});

	this.userChanged=function(evt) {
	    debugger;
		this.dhx.setItemValue('fullName', evt.data.fullName);
		this.dhx.setItemValue('email', evt.data.email);
		this.dhx.updateValues();
	};
	
	this.dhx.loadStruct(this.spec, "json", function() {});

	tbjEvents.addListener( { name: 'usermodel_changed', context: this, func: this.userChanged } );
}


function FormAdUsEdEdLoc(dhxForm, dhxTbr, dhxAccord) {
	this.dhx=dhxForm;
	this.dhx.setSkin("dhx_skyblue");
	this.tbbr=dhxTbr;
	this.acdn=dhxAccord;
	this.dhx.FormAdUsEdNwLoc=this;
	
	this.xmlFile='form/formAdUsEdEdLoc.xml';
	this.dhx.loadStruct(this.xmlFile);
}


function FormAdUsEdEdLoc(dhxForm, dhxTbr, dhxAccord) {
	this.dhx=dhxForm;
	this.dhx.setSkin("dhx_skyblue");
	this.tbbr=dhxTbr;
	this.acdn=dhxAccord;
	this.dhx.FormAdUsEdEdLoc=this;
	
	this.dhx.attachEvent("onButtonClick", function(name) {
		this.validate();
	});
	
	this.xmlFile='form/formAdUsEdEdLoc.xml';
}

function ValidateLocale(data) {
	return data=="EN";
}

