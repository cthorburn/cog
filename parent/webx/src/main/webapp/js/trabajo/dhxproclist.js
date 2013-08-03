

function FormPlEdIn(dhxForm, dhxAccord) {
    this.dhx=dhxForm;
    this.dhx.setSkin("dhx_skyblue");
    this.acdn=dhxAccord;
    this.dhx.FormPlEdIn=this;
    this.runCell=this.acdn.cells("run");
    
    this.spec=[
            {type: "settings", position: "label-left", labelWidth: 100, inputWidth: 120 },
            {type: "fieldset",  label: "Process Info", width: 400, list:[
               {type: "input", name: "desc", label: "Process Description", required: true}]
            }
    ];
    
    
    tbjEvents.addListener({ name : 'start_it', context : this, func : function(evt) {
        debugger;
        if(!this.selectedTreeId) {
            alert("Nothing to start!");
            return;
        }
        var data =this.runCell.startupForm.getFormData();
        
        for(parm in data) {
            data['pp__'+parm]=data[parm];
            delete data[parm]; 
        }
        
        data.dv = this.selectedTreeId;
        server.asyncServlet({ servlet : 'process', action : 'start_process', data : data , context: {
            start_process_OK: function() {
                tbjEvents.fireEvent({ name : 'process_started', data: {}});
            }
        }});
        this.runCell.startupForm.unload();
        delete this.runCell.startupForm;
        delete this.selectedTreeId;
    }});
    
    tbjEvents.addListener({ name : 'process_select', context : this, func : function(evt) {
        this.selectedTreeId=evt.data;
        if (this.runCell.startupForm) {
            this.runCell.startupForm.unload();
        }
        
        this.runCell.startupForm=new dhtmlXForm("form_container");
        this.runCell.startupForm.setSkin('dhx_skyblue');
        
        this.runCell.startupForm.loadStruct("process?a=starter_form&dv=" + evt.data);
        this.runCell.open();
    }
    });    
    
    this.dhx.loadStruct(this.spec, "json", function() {});
}

