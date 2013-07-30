function FormScRs(dhxForm, dhxTabbar) {
    this.dhx=dhxForm;
    this.dhx.setSkin("dhx_skyblue");
    this.tabs=dhxTabbar;
    this.dhx.FormScRs=this;
    
    this.spec=[
            {type: "settings", position: "label-left", labelWidth: 250, inputWidth: 200 },
            {type: "fieldset",  label: "System Config", width: 700, list:[
            {type: "input", name: "GraphVizDir", label: "GraphViz installation  directory"},
            {type: "input", name: "BIRTImageDir", label: "BIRT generated images directory"},
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
            server.asyncServlet({servlet: 'config', action: 'update', data: this.getFormData(),   context: this.FormScRs });
        }
    });
    
    this.dhx.loadStruct(this.spec, "json", function() {});

    server.asyncServlet({servlet: 'config', action: 'fetch', data: {},   context: { form: this.dhx,
        fetch_OK: function(response) {
            for(item in response.jsonResult) {
                this.form.setItemValue(item, response.jsonResult[item]);
            }
            this.form.updateValues();
        }
    }  });
    
}
