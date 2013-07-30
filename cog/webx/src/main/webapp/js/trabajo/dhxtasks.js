function FormTkAcSt(accordion) {
    FormTkAcSt.obj=this;
    this.accordion=accordion;
    
    this.showProcessGraph = function(taskId) {
        this.accordion.cells("status").attachURL("process?a=processGraph&taskId="+taskId);
    };
    
    this.processGraph_OK = function(jsonResult) { 
        this.accordion.cells("status").show();
        this.accordion.cells("status").open();
    };
}

function FormTkAcAl(dhxForm, accordion, grids) {
	this.dhx=dhxForm;
    this.grids=grids;
    this.accordion=accordion;

	FormTkAcAl.obj=this;
	
	this.dhx.setSkin('dhx_skyblue');
	this.spec=[
	    {type: "settings", position: "label-left", labelWidth: 100},
		
		{type: "fieldset",  label: "Task Actions", width: 750, list:[
           {type: "container", name: "summary", label: "Task Details", inputHeight: 100},
		   {type: "button", name: "view", value: "View"}
		]}
    ];
	
    this.dhx.loadStruct(this.spec, "json", function() {});
    
    this.formGrid = new dhtmlXGridObject(this.dhx.getContainer("summary"));
    var g=this.formGrid;

    g.setImagePath("js/dhtmlxGrid/codebase/imgs/");
    g.setHeader("Property,Value");
    g.setInitWidths("300,300");
    g.setColAlign("left,left");
    g.setColTypes("ro,ro");
    g.setColSorting("na,na");
    g.init();
    g.setSkin("dhx_black");
	
    this.grids['al'].attachEvent("onRowSelect", function(rid, cid) {
        FormTkAcAl.obj.selectedTask=rid;
	    FormTkAcSt.obj.showProcessGraph(rid);
	});
	
	this.dhx.attachEvent("onButtonClick", function(name) {
		switch(name) {
		case 'view':
			tbjEvents.fireEvent({ name: 'save_state', data: null } );
			window.location.href="process?a=view_task&tid=" + FormTkAcAl.obj.selectedTask;
		}
	});

    tbjEvents.addListener({ name: 'al_task_selected', context: this, func: function(evt) {
    	debugger;
    	this.updateForm(evt.data.jsonResult);
		this.accordion.cells("available").close();
		this.accordion.cells("available").hide();
		this.accordion.cells("allocated").show();
		this.accordion.cells("allocated").open();
        this.accordion.cells("status").show();
    }});
    
    
    this.updateForm=function(taskSummary) {
    	this.formGrid.clearAll();
    	this.formGrid.addRow(""+this.formGrid.getRowsNum(), "Description,"+taskSummary.procDesc.value);
    };
    
};


function FormTkAcAv(dhxForm, accordion, grids) {
	this.dhx=dhxForm;
    this.grids=grids;
    this.accordion=accordion;

    FormTkAcAv.obj=this;
	
	this.dhx.setSkin('dhx_skyblue');
	this.spec=[
	    {type: "settings", position: "label-left", labelWidth: 100 },
		
		{type: "fieldset",  label: "Task Actions", width: 750, list:[
           {type: "container", name: "summary", label: "Task Details"},
		   {type: "button", name: "claim", value: "Claim"}
		]}
    ];
	
    this.dhx.loadStruct(this.spec, "json", function() {});
	
	this.formGrid = new dhtmlXGridObject(this.dhx.getContainer("summary"));
	var g=this.formGrid;

	g.setImagePath("js/dhtmlxGrid/codebase/imgs/");
	g.setHeader("Property,Value");
	g.setInitWidths("300,300");
	g.setColAlign("left,left");
	g.setColTypes("ro,ro");
	g.setColSorting("na,na");
	g.init();
	g.setSkin("dhx_black");
	
	this.grids['av'].attachEvent("onRowSelect", function(rid, cid) {
	    debugger;
        FormTkAcAv.obj.selectedTask=rid;
        FormTkAcSt.obj.showProcessGraph(rid);
	});
	
	this.dhx.attachEvent("onButtonClick", function(name) {
		switch(name) {
		case 'claim':
		    debugger;
	        server.asyncServlet({servlet: 'process', action: 'claim_task', data: { tid: FormTkAcAv.obj.selectedTask },   context: FormTkAcAv.obj });
	        break;
		}
	});
    
	this.claim_task_OK = function(response) {
        tbjEvents.fireEvent({name: 'task_claimed', data: response});
    };
	
    tbjEvents.addListener({ name: 'av_task_selected', context: this, func: function(evt) {
		this.accordion.cells("allocated").close();
		this.accordion.cells("allocated").hide();
		this.accordion.cells("available").show();
		this.accordion.cells("available").open();
        this.accordion.cells("status").show();
        this.formGrid.addRow(""+this.formGrid.getRowsNum(), "Description,"+evt.data.jsonResult.taskDesc.value);
    }});
};


