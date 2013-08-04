function FormScRs(dhxForm, dhxTabbar) {
    this.dhx=dhxForm;
    this.tabs=dhxTabbar;
    FormScRs.obj=this;

    this.dhx.setSkin("dhx_skyblue");
    
    this.spec=[
            {type: "settings", position: "label-left", labelWidth: 250, inputWidth: 200 },
            {type: "fieldset",  label: "System Config", width: 700, list:[
            {type: "input", name: "GraphVizDir", label: "GraphViz installation  directory"},
            {type: "input", name: "FileCacheDir", label: "File cache directory", },
            {type: "checkbox", name: "development", label: "Develoment System", },
            {type: "select", name: "rdbms", label: "RDBMS", options:[
                                                                    {text: "MS SQLServer 2012", value: "SQLSERVER2012"},
                                                                    {text: "MS SQLServer 2008", value: "SQLSERVER2008"},
                                                                    {text: "Oracle 11g", value: "oracle11"},
                                                                    {text: "Oracle 10g", value: "oracle10"},
                                                                    {text: "MySQL 5.5", value: "MYSQL55"}]},
            {type: "button", name: "update", value: "Update"}]
        }
    ];
    
    this.dhx.attachEvent("onButtonClick", function(name) {
        if("update"==name) {
            server.asyncServlet({servlet: 'config', action: 'update', data: this.getFormData(),   context: FormScRs.obj });
        }
    });
    
    this.dhx.loadStruct(this.spec, "json", function() {});

    this.fetch_OK = function(response) {
	    for(item in response.jsonResult) {
	    	var val=response.jsonResult[item];
	        this.dhx.setItemValue(item, val);
	    }
	    this.dhx.updateValues();
    };
    server.asyncServlet({servlet: 'config', action: 'fetch', context: FormScRs.obj});
    
}
