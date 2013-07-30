function FormRpEdInsNm(dhxForm, tabbar) {
    this.dhx = dhxForm;
    this.dhx.FormRpEdInsNm = this;
    this.tabbar = tabbar;
    
    this.dhx.setSkin('dhx_skyblue');
    this.spec = [ { type : "settings", position : "label-left", labelWidth : 100, inputWidth : 120
    }, { type : "fieldset", label : "Report Details", width : 400, list : [ 
    { value: "Processes", type : "input", name : "name", label : "Short name", note : "ClassLoader short name"
    }, { value: "1.0.0", type : "input", name : "version", label : "Version", note : "Report version (nn.nn.nn), required: true"
    }, { value: "builtin.processes", type : "input", name : "category", label : "Category", note : "Report category", required: true
    }, { value: "process list", type : "input", name : "desc", label : "Description", note : "Report short description", required: true
    }, { type : "button", name : "next", value : "Next"
    } ]
    } ];

    this.dhx.attachEvent("onButtonClick", function(name) {
        var shortName = this.getItemValue("name"); // TODO validate
        var version = this.getItemValue("version"); // TODO validate
        var cat = this.getItemValue("category"); // TODO validate
        var desc = escape(this.getItemValue("desc")); // TODO validate

        var validated = true;
        if (!validated) {
            // TODO
        }

        tbjEvents.fireEvent({ name : 'report_named', data : { sn : shortName, v : version, c : cat, d : desc } });
        this.FormRpEdInsNm.tabbar.setTabActive("upload");
    });
    
    tbjEvents.addListener({ name : 'report_installed', context : this, func : function(evt) { this.dhx.clear(); }});
    
    this.dhx.loadStruct(this.spec, "json", function() {});
};

function FormRpEdInsUl(dhxForm, tabbar) {
	this.dhx = dhxForm;
	this.dhx.setSkin("dhx_skyblue");
	this.tabbar=tabbar;
	this.dhx.FormRpEdInsUl = this;
	
	this.spec = [ { type : "settings", position : "label-left", labelWidth : 100, inputWidth : 120
	}, { type : "fieldset", label : "Upload Report", width : 500, list : [ 
            { type : "upload", url : "upload", name : "uploader", mode : "html4", inputWidth : 300
	} ]
	} ];
	//data : { sn : shortName, v : version, c : cat, d : desc }
	
    tbjEvents.addListener({ name : 'report_named', context: this, func: function(evt) {this.data=evt.data;} });

	this.dhx.attachEvent("onFileAdd", function(realName) {
        var data = this.FormRpEdInsUl.data;
        if(!data) {
            this.FormRpEdInsUl.tabbar.setTabActive("name");
            return;
        }
        
		var ul = this.FormRpEdInsUl.dhx.getUploader('uploader');
		ul.setURL("upload?type=report&rn=" + encodeURI(realName)+"&name="+data.sn+"&version="+data.v+"&desc="+data.d+"&cat="+data.c);
		ul.upload();
	});

	this.dhx.attachEvent("onUploadComplete", function(name) {
	    getDeferredMsgs();
	});

	this.dhx.loadStruct(this.spec, "json", function() {	});
};


function FormRpEdRn(dhxForm, acc) {
    this.dhx = dhxForm;
    this.dhx.setSkin("dhx_skyblue");
    this.acdn=acc;
    FormRpEdRn.obj=this;
    
    this.dhx.setSkin('dhx_skyblue');
    this.spec = [ { type : "settings", position : "label-left", labelWidth : 100, inputWidth : 120
    }, { type : "fieldset", label : "Run Report", width : 400, list : [ 
    
    //build report params here?
    
    { type : "button", name : "run", value : "Run"},
    { type : "button", name : "export", value : "Export"}
        
        ]
    } ];
    
    tbjEvents.addListener({ name : 'report_selected', context: this, func: function(evt) {
        this.reportId=evt.data;
        this.acdn.openItem("run");
    }});
    
    this.dhx.attachEvent("onButtonClick", function(name) {
        debugger;
        if(name=='run') {
            if(!FormRpEdRn.obj.reportId) {
                alert("no report selected");
                return;
            }
            debugger;
            acc.openItem("report");
            server.asyncServlet({ servlet : 'report', action : 'deser',  context: FormRpEdRn.obj, data : { report: FormRpEdRn.obj.reportId } });
            
        }
        else if(name=='export') {
           window.open("download?type=report&id="+this.FormRpEdRn.reportId);
        }
    });
    
    
    
    this.deser_OK=function() {
        debugger;
        var url="http://localhost:8180/birt/frameset?";  //TODO from config
        url+="__report="+this.reportId+".rptdesign";
        this.acdn.cells("report").attachURL(url);
    }
    
    this.dhx.loadStruct(this.spec, "json", function() { });
    
}
