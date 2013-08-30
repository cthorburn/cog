function FormDev(dhxForm) {
    this.dhx = dhxForm;
    this.dhx.FormDev = this;
    
    this.dhx.setSkin('dhx_skyblue');
    this.spec = [ { type : "settings", position : "label-left", labelWidth : 100, inputWidth : 120
    }, { type : "fieldset", label : "Development Tools", width : 400, list : [
          { type : "button", name : "wipeQuartzTables", value : "Wipe Quartz Tables"},
          { type : "button", name : "deleteAllProcesses", value : "Clear Process Tables"}                                                                       
       ]
    } ];

    this.dhx.attachEvent("onButtonClick", function(name) {
        server.asyncServlet({servlet: "dev", action: name });
    });
    this.dhx.loadStruct(this.spec, "json", function() {});
};

