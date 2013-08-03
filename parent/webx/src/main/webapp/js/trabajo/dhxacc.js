function FormAc(dhxForm) {
    this.dhx = dhxForm;
    this.dhx.FormAc= this;
    
    this.dhx.setSkin('dhx_skyblue');
    this.spec = [ { type : "settings", position : "label-left", labelWidth : 100, inputWidth : 120
    }, { type : "fieldset", label : "User Preferences", width : 400, list : [
            { type : "button", name : "save", value : "Save"}
       ]
    } ];

    this.dhx.attachEvent("onButtonClick", function(name) {
        server.asyncServlet({servlet: "user", action: name });
    });
    this.dhx.loadStruct(this.spec, "json", function() {});
};

