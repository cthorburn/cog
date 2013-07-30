function FormAcEdGpNw(dhxForm, dhxAccord) {
    
    this.dhx=dhxForm;
    this.dhx.setSkin("dhx_skyblue");
    this.acdn=dhxAccord;
    this.dhx.FormAcEdGpNw=this;
    
    this.spec=[
            {type: "settings", position: "label-left", labelWidth: 100, inputWidth: 500 },
            
            {type: "fieldset",  label: "New Group", width: 700, list:[
               {type: "input", name: "name",    label: "Name",  required: true},
               {type: "input", name: "category", label: "Category", required: true},
               {type: "input", name: "description", label: "Description", rows: 5, required: true},
               {type: "button", name: "create", value: "Create"}]
            }
    ];
    
    this.dhx.attachEvent("onButtonClick", function(name) {
        if("create"==name) {
            var data=this.getFormData();
            server.asyncServlet({servlet: 'process', action: 'create_group', data: data,   context: this.FormAcEdGpNw, quickfire: "group_created" });
        }
    });
    
    this.create_group_OK=function(result) {
        this.dhx.clear();
    };
    
    this.exception_DUPLICATE_GROUPNAME=function(exception) {
        this.dhx.setItemValue(name, "");
        this.dhx.updateValues();
        logger.info("Duplicate group name: "+exception.detail);
    };
    
    this.dhx.loadStruct(this.spec, "json", function() {});
}

function FormAcEdRlNw(dhxForm, dhxAccord) {
    
    this.dhx=dhxForm;
    this.dhx.setSkin("dhx_skyblue");
    this.acdn=dhxAccord;
    this.dhx.FormAcEdRlNw=this;
    
    this.spec=[
            {type: "settings", position: "label-left", labelWidth: 100, inputWidth: 500 },
            
            {type: "fieldset",  label: "New Role", width: 700, list:[
               {type: "input", name: "name",    label: "Name",  required: true},
               {type: "input", name: "category", label: "Category", required: true},
               {type: "input", name: "description", label: "Description", rows: 5, required: true},
               {type: "button", name: "create", value: "Create"}]
            }
    ];
    
    this.dhx.attachEvent("onButtonClick", function(name) {
        if("create"==name) {
            var data=this.getFormData();
            server.asyncServlet({servlet: 'process', action: 'create_role', data: data,   context: this.FormAcEdRlNw, quickfire: "role_created" });
        }
    });
    
    this.create_role_OK=function(result) {
        this.dhx.clear();
    };
    
    this.exception_DUPLICATE_ROLENAME=function(exception) {
        this.dhx.setItemValue(name, "");
        this.dhx.updateValues();
        logger.info("Duplicate role name: "+exception.detail);
    };
    
    this.dhx.loadStruct(this.spec, "json", function() {});
}

function FormAcEdGpEd(dhxAccord) {
    debugger;
    this.acdn=dhxAccord;
    this.tabbar=this.acdn.cells("edit").attachTabbar();
    this.tabbar.setImagePath("js/dhtmlxTabbar/codebase/imgs/");
    this.tabbar.addTab("users", "Users", 100);    
    this.tabbar.addTab("roles", "Roles", 100);
    
    this.roleGrid=this.tabbar.cells("roles").attachGrid();
    this.roleGrid.setImagePath("js/dhtmlxGrid/codebase/imgs/");
    this.roleGrid.setHeader("Name, Description,Assigned");
    this.roleGrid.setInitWidths("150,500,80");
    this.roleGrid.setColAlign("left,left,left");
    this.roleGrid.setColTypes("ro,ro,ch");
    this.roleGrid.setColSorting("str,str,str");
    this.roleGrid.init();
    this.roleGrid.preventIECaching(true);
    this.roleGrid.tbj=this;
    this.roleGrid.attachEvent('onCheck', function(rId,cInd,state) {
        server.asyncServlet({servlet: 'acl', action: 'modify_group_roles', data: {set: state, groupName: this.tbj.groupName, roleid: rId }});
    });
    
    this.userGrid=this.tabbar.cells("users").attachGrid();
    this.userGrid.setImagePath("js/dhtmlxGrid/codebase/imgs/");
    this.userGrid.setHeader("Name, Email, Assigned");
    this.userGrid.setInitWidths("150,500,80");
    this.userGrid.setColAlign("left,left,left");
    this.userGrid.setColTypes("ro,ro,ch");
    this.userGrid.setColSorting("str,str,str");
    this.userGrid.init();
    this.userGrid.preventIECaching(true);
    this.userGrid.tbj=this;
    this.userGrid.attachEvent('onCheck', function(rId,cInd,state) {
        server.asyncServlet({servlet: 'acl', action: 'modify_group_users', data: {set: state, groupName: this.tbj.groupName, userid: rId }});
    });
    
    tbjEvents.addListener({ name: "group_select", context: this, func: function(evt) {
        debugger;
        this.groupName=evt.data;
        this.tabbar.setTabActive("roles");
        this.roleGrid.clearAll();
        this.userGrid.clearAll();
        this.userGrid.loadXML("xml?feed=AcEdGpEd_group_users&groupName="+evt.data);
        this.roleGrid.loadXML("xml?feed=AcEdGpEd_group_roles&groupName="+evt.data);
        this.acdn.cells("edit").open();
    }});
}