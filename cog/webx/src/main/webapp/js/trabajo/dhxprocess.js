
function FormAdPrEdPrEd(dhxForm, dhxAccord, tree) {
    debugger;
    FormAdPrEdPrEd.obj=this;
    this.dhx = dhxForm;
    this.acdn = dhxAccord;
    this.tree=tree;
    this.dhx.setSkin("dhx_skyblue");
    
    this.spec = [ { type : "settings", position : "label-left", labelWidth : 100, inputWidth : 800},
                  { type : "fieldset", label : "Service Propeties", width : 1000, list : [ 
                  { type : "container", name : "grid", label : "Properties By Service", inputHeight: 300}
    ]
    } ];
   
    this.dhx.loadStruct(this.spec, "json", function() { });
    
    this.grid = new dhtmlXGridObject(this.dhx.getContainer("grid"));
    g = this.grid;
    g.setImagePath("js/dhtmlxGrid/codebase/imgs/");
    g.setHeader("Service,Property,Value");
    g.setInitWidths("275,275,275");
    g.setColAlign("left,left,left");
    g.setColTypes("ro,ro,ed");
    g.setColSorting("na,na,na");
    g.init();
    g.setSkin("dhx_black");
    g.enableEditEvents(true,true,true);
    g.attachEvent("onEditCell", function(stage,rId,cInd,nValue,oValue) { 
        if(cInd == 0 || cInd == 1 ) {
            return false;
        }
        else if(stage == 2){
            
            var split =rId.split('|');
            if(split.length==1) {
                return false;
            }
            if(split[1]=='service-factory') {
                return false;
            }
            
            var serviceDv=split[0];
            var propKey=split[1];
            var propValue=this.cells(rId, 2).getValue();
            FormAdPrEdPrEd.obj.save(serviceDv, propKey, propValue);
        }
        return true;
    });
    
    this.save = function(sdv, k, v) {
        debugger;
        server.asyncServlet({ servlet: 'process', action: 'setProcessServiceProperty', data: { processDv: this.dv, serviceDv: sdv, key: k, value: v}});
    };
    
    this.tree.attachEvent("onClick", function(id) {
        if(this.hasChildren(id)==0) {
            debugger;
            FormAdPrEdPrEd.obj.dv=FormAdPrEdPrEd.obj.dvFromTreePath(id);
            FormAdPrEdPrEd.obj.grid.clearAll();
            FormAdPrEdPrEd.obj.grid.loadXML('xml?feed=AdPrEdPrEd&dv='+FormAdPrEdPrEd.obj.dv);        
        }
    });
    
    this.dvFromTreePath = function(treePath) {
      var split=treePath.split('.');
      split[split.length-1]= split[split.length-1].replace('-', '.');
      split[split.length-1]= split[split.length-1].replace('-', '.');
      return split[split.length-2]+'-'+split[split.length-1];
    };
    
}

function FormAdPrEdPrNw(dhxForm, acdn) {
	this.dhx = dhxForm;
	this.dhx.setSkin("dhx_skyblue");

	this.acdn = acdn;
    this.runCell = this.acdn.cells("run");
    this.runCell.attachObject("sf");
    
	this.dhx.FormAdPrEdPrNw = this;
	this.spec = [ { type : "settings", position : "label-left", labelWidth : 100, inputWidth : 120
	}, { type : "fieldset", label : "Upload Process JAR file", width : 500, list : [ 
            { type : "upload", url : "process", name : "uploader", mode : "html4", inputWidth : 300
	} ]
	} ];

	this.url = 'upload?type=pd';

	this.dhx.attachEvent("onFileAdd", function(realName) {
		var ul = this.FormAdPrEdPrNw.dhx.getUploader('uploader');
		ul.setURL(this.FormAdPrEdPrNw.url + "&rn=" + encodeURI(realName));
		ul.upload();
	});


	this.dhx.attachEvent("onUploadComplete", function(name) {
	    getDeferredMsgs();
	});

    tbjEvents.addListener({ name : 'start_it', context : this, func : function(evt) {
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
        server.asyncServlet({ servlet : 'process', action : 'start_process', data : data });
        this.runCell.startupForm.unload();
        delete this.runCell.startupForm;
        delete this.selectedTreeId;
        this.acdn.openItem("new");
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

	this.dhx.loadStruct(this.spec, "json", function() {	});
};


function FormAdPrEdClNwFn(dhxForm, tabbar, tree) {
	this.dhx = dhxForm;
	this.dhx.FormAdPrEdClNwFn = this;
	this.dhx.setSkin('dhx_skyblue');
	this.spec = [
	// TODO show summary
	                { type : "settings", position : "label-left", labelWidth : 200}, 
	                { type : "fieldset", label : "Finalize ClassLoader Creation", width : 400, 
	                    list : [ { type : "button", name : "next", value : "Next" } ]
	                } 
	            ];
	
	this.tabbar = tabbar;

	tbjEvents.addListener({ name : 'classloader_named', context : this, func : function(evt) {
		this.uploadSpec = evt.data;
	}});

	this.dhx.attachEvent("onButtonClick", function(name) {
		server.asyncServlet({ servlet : 'process', action : 'define_session_cl',  context: this.FormAdPrEdClNwFn, data : { dv : this.FormAdPrEdClNwFn.uploadSpec.dv } });
	});
	
	this.define_session_cl_OK=function() {
	    tbjEvents.fireEvent({name: 'cl_defined', data: {}});
	    this.dhx.reset();
	};
	this.dhx.loadStruct(this.spec, "json", function() {	});
};

function FormAdPrEdClNwJo(dhxForm, tabbar) {
    this.dhx = dhxForm;
    this.dhx.FormAdPrEdClNwJo = this;
    FormAdPrEdClNwJo.obj = this;
    this.dhx.setSkin('dhx_skyblue');
    this.spec = 
        [{ type : "settings", position : "label-left", labelWidth : 100},
         { type : "fieldset", label : "Set JAR Order", width : 500, list : [ 
            { type : "container", name : "grid", label : "JAR Order", inputHeight: 120},
           { type : "button", name : "next", value : "Next"} 
         ]
       }];
    
    this.tabbar = tabbar;

    this.dhx.loadStruct(this.spec, "json", function() {});
    this.grid = new dhtmlXGridObject(this.dhx.getContainer("grid"));
    var g = this.grid;
    g.setImagePath("js/dhtmlxGrid/codebase/imgs/");
    g.setHeader("Seq,Jar,Up,Dn,Del");
    g.setInitWidths("50,250,24,24,24");
    g.setColAlign("right,left,left,left,left");
    g.setColTypes("ro,ro,ro,ro,ro");
    g.setColSorting("na,na,na,na,na");
    g.init();
    g.setSkin("dhx_black");
    
    this.move=function(upDown, index) { 
        server.asyncServlet({servlet: 'process', action: 'cl_order', data: { sequence: index, up: upDown, dv: this.dv },   context: this, quickfire: "cl_jar_order_change" });
    };
    this.up=function(index) {
        this.move(true, index);
    };
    
    this.down=function(index) { 
        this.move(false, index);
    };
    
    this.del=function(index) { 
        server.asyncServlet({servlet: 'process', action: 'cl_del', data: { sequence: index, dv: this.dv },   context: this, quickfire: "cl_jar_order_change" });
    };
    
    this.dhx.attachEvent("onButtonClick", function(name) {
        this.FormAdPrEdClNwJo.tabbar.setTabActive("finish");
    });
    
    tbjEvents.addListener({ name : 'cl_jar_order_change|cljar_uploaded', context : this, func : function(evt) {
        this.grid.clearAll();
        this.grid.loadXML('xml?feed=AdPrEdClNwJo&dv='+this.dv); ///uses the same feed as service!
    }});
    
    tbjEvents.addListener({ name : 'classloader_named', context : this, func : function(evt) {
        this.dv=evt.data.dv;
    }});
    
    tbjEvents.addListener({ name : 'cl_defined', context : this, func : function(evt) {
        this.grid.clearAll();
        this.tabbar.setTabActive("name");
    }});
};


function FormAdPrEdClNwNm(dhxForm, tabbar) {
	this.dhx = dhxForm;
	this.dhx.FormAdPrEdClNwNm = this;
	this.tabbar = tabbar;
	this.dhx.setSkin('dhx_skyblue');
	this.spec = [ { type : "settings", position : "label-left", labelWidth : 100, inputWidth : 120
	}, { type : "fieldset", label : "Basic Details", width : 400, list : [ { type : "input", name : "name", label : "Short name", note : "ClassLoader short name"
	}, { type : "input", name : "version", label : "Version", note : "ClassLoader version (nn.nn.nn)"
	}, { type : "input", name : "category", label : "Category", note : "ClassLoader category (a.b.c)"
	}, { type : "input", name : "desc", label : "Description", note : "ClassLoader short description"
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
        this.FormAdPrEdClNwNm.data = { dv: shortName+'-'+version, cat : cat, desc : desc};
		
        server.asyncServlet({servlet: 'process', action: 'cl_named', data: this.FormAdPrEdClNwNm.data,   context: this.FormAdPrEdClNwNm});
		
	});
    
	this.cl_named_OK=function() {
        tbjEvents.fireEvent({ name : 'classloader_named', data : this.data });
        this.tabbar.setTabActive("jars");
    };
    
    tbjEvents.addListener({ name : 'cl_defined', context : this, func : function(evt) {
        this.dhx.reset();
    }});

	this.dhx.loadStruct(this.spec, "json", function() {});
};

function FormAdPrEdClNwJf(dhxForm, tabbar) {
	this.dhx = dhxForm;
	this.dhx.FormAdPrEdClNwJf = this;

	this.dhx.setSkin('dhx_skyblue');
	this.spec = [ { type : "settings", position : "label-left", labelWidth : 100, inputWidth : 120
	}, { type : "fieldset", label : "Upload ClassLoader JAR files", width : 500, list : [ { type : "upload", url : "process", name : "uploader", mode : "html4", inputWidth : 300
	}, { type : "button", name : "next", value : "Next"
	} ]
	} ];
	this.tabbar = tabbar;
	this.uploaded=false;

    tbjEvents.addListener({ name : 'classloader_named', context : this, func : function(evt) {
        this.url = 'upload?type=sv&dv=' + evt.data.dv + "&cat=" + evt.data.cat + "&desc=" + evt.data.desc;
    }});

	this.dhx.attachEvent("onButtonClick", function(name) {
		if (!this.FormAdPrEdClNwJf.uploaded)
			talert("you need to upload at least one jar");
		else
			this.FormAdPrEdClNwJf.tabbar.setTabActive("order");
	});

	this.dhx.attachEvent("onUploadFile", function(realName, serverName) {
	    this.FormAdPrEdClNwJf.uploaded=true;
		tbjEvents.fireEvent({ name : 'cljar_uploaded', data : { sn : serverName, rn : realName}});
	});

    this.dhx.attachEvent("onFileAdd", function(realName) {
        var copy = this.FormAdPrEdClNwJf.url;
        copy += "&rn=" + encodeURI(realName);
        this.getUploader('uploader').setURL(copy);
    });

    this.dhx.loadStruct(this.spec, "json", function() {});
	this.dhx.getUploader("uploader").enableTitleScreen(true);
	this.dhx.getUploader("uploader").setTitleText("Upload ClassLoader JAR files");

};

// --------------------------------------------------------- New Service
// ---------------------------------

function FormAdPrEdSvNwFn(dhxForm, tabbar) {
	this.dhx = dhxForm;
	this.dhx.FormAdPrEdSvNwFn = this;

	this.dhx.setSkin('dhx_skyblue');
	this.spec = [ { type : "settings", position : "label-left", labelWidth : 200
	}, { type : "fieldset", label : "Finalize Service Creation", width : 400, list : [ { type : "button", name : "next", value : "Next"
	} ]
	} ];

	this.tabbar = tabbar;

	tbjEvents.addListener({ name : 'service_named', context : this, func : function(evt) {
		this.uploadSpec = evt.data;
	}
	});

	tbjEvents.addListener({ name : 'service_registered', context : this, func : function(evt) {
		delete this.uploadSpec;
		this.dhx.clear();
	}
	});

	this.defineSessionSv = function() {
		var data = { dv : this.FormAdPrEdSvNwFn.uploadSpec.sn + "-" + this.FormAdPrEdSvNwFn.uploadSpec.v
		};

		server.asyncServlet({ servlet : 'process', action : 'define_session_sv', data : data, context : { dv : data.dv,

    		define_session_sv_OK : function(response) {
    			tbjEvents.fireEvent({ name : 'service_registered', data : this.dv});
    		}
    		}
		});
	};

	this.dhx.attachEvent("onButtonClick", this.defineSessionSv);
	this.dhx.loadStruct(this.spec, "json", function() {
	});
};

function FormAdPrEdSvNwJo(dhxForm, tabbar) {
	this.dhx = dhxForm;
	this.dhx.FormAdPrEdSvNwJo = this;
	FormAdPrEdSvNwJo.obj = this;
	this.dhx.setSkin('dhx_skyblue');
	this.spec = 
	    [{ type : "settings", position : "label-left", labelWidth : 100},
	     { type : "fieldset", label : "Set JAR Order", width : 500, list : [ 
	        { type : "container", name : "grid", label : "JAR Order", inputHeight: 120},
	       { type : "button", name : "next", value : "Next"} 
	     ]
	   }];
	
	this.tabbar = tabbar;

	this.dhx.loadStruct(this.spec, "json", function() {});
	this.grid = new dhtmlXGridObject(this.dhx.getContainer("grid"));
	var g = this.grid;
	g.setImagePath("js/dhtmlxGrid/codebase/imgs/");
	g.setHeader("Seq,Jar,Up,Dn,Del");
	g.setInitWidths("50,250,24,24,24");
	g.setColAlign("right,left,left,left,left");
	g.setColTypes("ro,ro,ro,ro,ro");
	g.setColSorting("na,na,na,na,na");
	g.init();
	g.setSkin("dhx_black");
	
    this.move=function(upDown, index) { 
        server.asyncServlet({servlet: 'process', action: 'cl_order', data: { sequence: index, up: upDown, dv: this.dv },   context: this, quickfire: "service_jar_order_change" });
    };
    this.up=function(index) {
        this.move(true, index);
    };
    
    this.down=function(index) { 
        this.move(false, index);
    };
    
    this.del=function(index) { 
        server.asyncServlet({servlet: 'process', action: 'cl_del', data: { sequence: index, dv: this.dv },   context: this, quickfire: "service_jar_order_change" });
    };
    
	this.dhx.attachEvent("onButtonClick", function(name) {
		this.FormAdPrEdSvNwJo.tabbar.setTabActive("finish");
	});
    
	tbjEvents.addListener({ name : 'service_jar_order_change|service_jar_uploaded2', context : this, func : function(evt) {
        this.grid.clearAll();
        this.grid.loadXML('xml?feed=AdPrEdSvNwJo&dv='+this.dv);
    }});
	
	tbjEvents.addListener({ name : 'service_named', context : this, func : function(evt) {
        this.dv=evt.data.sn+'-'+evt.data.v;
    }});
    
    tbjEvents.addListener({ name : 'service_registered', context : this, func : function(evt) {
        this.grid.clearAll();
        this.FormAdPrEdSvNwJo.tabbar.setTabActive("name");
    }});
};

function FormAdPrEdSvNwNm(dhxForm, tabbar) {
	this.dhx = dhxForm;
	this.dhx.FormAdPrEdSvNwNm = this;
	this.dhx.setSkin('dhx_skyblue');

	this.spec = [ { type : "settings", position : "label-left", labelWidth : 100, inputWidth : 120
	},

	{ type : "fieldset", label : "Basic Details", width : 400, list : [ { type : "input", name : "name", label : "Short name", note : "Service short name", value : "service1"
	}, { type : "input", name : "version", label : "Version", note : "Service version (nn.nn.nn)", value : "2.0.0"
	}, { type : "input", name : "category", label : "Category", note : "Service category", value : "my.services"
	}, { type : "input", name : "desc", label : "Description", note : "Service short description (a.b.c)", value : "description"
	}, { type : "button", name : "next", value : "Next"
	} ]
	} ];
	this.tabbar = tabbar;

	tbjEvents.addListener({ name : 'service_registered', context : this, func : function(evt) {
		this.dhx.reset();
	}
	});

	this.dhx.attachEvent("onButtonClick", function(name) {
		var shortName = this.getItemValue("name"); // TODO validate
		var version = this.getItemValue("version"); // TODO validate
		var cat = this.getItemValue("category"); // TODO validate
		var desc = escape(this.getItemValue("desc")); // TODO validate

		var validated = true;
		if (!validated) {
		   //TODO
		}
		this.FormAdPrEdSvNwNm.data = { sn : shortName, v : version, c : cat, d : desc};
		
		server.asyncServlet({servlet: 'process', action: 'cl_named', data: { dv: shortName+'-'+version, cat: cat, desc: desc },   context: this.FormAdPrEdSvNwNm});

    });

	this.cl_named_OK=function() {
        tbjEvents.fireEvent({ name : 'service_named', data : this.data });
        this.tabbar.setTabActive("jars");
	};
	
	this.dhx.loadStruct(this.spec, "json", function() {});
};

function FormAdPrEdSvNwJf(dhxForm, tabbar) {
	this.dhx = dhxForm;
	this.dhx.FormAdPrEdSvNwJf = this;
	
	this.dhx.setSkin('dhx_skyblue');
	this.spec = [
			{ type : "settings", position : "label-left", labelWidth : 100, inputWidth : "auto"
			},
			{ type : "fieldset", label : "Begin Service Definition", width : 400,
				list : [ { type : "upload", url : "process", name : "uploader", mode : "html4", inputWidth : 300, autoStart : true
				},
				// NB autoStart=true is crucial for getting the upload URL
				// right. DO NOT
				// CHANGE LIGHTLY !!!
				// TODO but for some reason it's not woring!
				{ type : "button", name : "next", value : "Next"
				} ]
			} ];
	this.tabbar = tabbar;
	this.uploaded=false;
	
	this.url = null;
	
	this.clear = function() {
		this.dhx.getUploader('uploader').clear();
	};
	
    tbjEvents.addListener({ name : 'service_jar_uploaded', context : this, func : function(evt) {
        this.uploaded=true;
        tbjEvents.fireEvent({ name : 'service_jar_uploaded2', data : evt.data });
    }});


	tbjEvents.addListener({ name : 'service_named', context : this, func : function(evt) {
		this.url = 'upload?type=sv&dv=' + evt.data.sn + "-" + evt.data.v + "&cat=" + evt.data.c + "&desc=" + evt.data.d;
	}});

	tbjEvents.addListener({ name : 'service_registered', context : this, func : function(evt) {
		this.clear();
	}
	});

	this.dhx.attachEvent("onButtonClick", function(name) {

		if (!this.FormAdPrEdSvNwJf.uploaded)
			talert("you need to upload at least one jar");
		else
			this.FormAdPrEdSvNwJf.tabbar.setTabActive("order");
	});

	this.dhx.attachEvent("onUploadFile", function(realName, serverName) {
		tbjEvents.fireEvent({ name : 'service_jar_uploaded', data : { sn : serverName, rn : realName}});
	});
    
	this.dhx.attachEvent("onUploadComplete", function(name) {
        getDeferredMsgs();
    });

	this.dhx.attachEvent("onFileAdd", function(realName) {
		var copy = this.FormAdPrEdSvNwJf.url;
		copy += "&rn=" + encodeURI(realName);
		this.getUploader('uploader').setURL(copy);
	});

	this.dhx.loadStruct(this.spec, "json", function() {
	});
};

// -------------- Service details --------------------

function FormAdPrEdSvDt(dhxForm) {
	this.dhx = dhxForm;
	this.dhx.FormAdPrEdSvDt = this;

	this.dhx.setSkin('dhx_skyblue');
	this.spec = [ { type : "settings", position : "label-left", inputWidth : 200, labelWidth : 100
	}, { type : "fieldset", label : "Set JAR Order", width : 500, list : [ { type : "input", disabled : true, name : "version", label : "Version"
	}, { type : "input", disabled : true, name : "desc", label : "Description"
	}, { type : "input", disabled : true, name : "cat", label : "Category"
	}, { type : "container", name : "grid1", inputHeight : 120, inputWidth : 600, label : "JAR Files"
	}, { type : "container", name : "grid2", inputHeight : 120, inputWidth : 600, label : "Properties"
	} ]
	} ];

	this.dhx.loadStruct(this.spec, "json", function() {
	});

	this.grid1 = new dhtmlXGridObject(this.dhx.getContainer("grid1"));
	var g = this.grid1;
	g.setImagePath("js/dhtmlxGrid/codebase/imgs/");
	g.setHeader("Seq,Jar,Original Name");
	g.setInitWidths("50,500,200");
	g.setColAlign("left,left,left");
	g.setColTypes("ro,ro,ro");
	g.setColSorting("na,na,na");
	g.init();
	g.setSkin("dhx_black");

	this.grid2 = new dhtmlXGridObject(this.dhx.getContainer("grid2"));
	g = this.grid2;
	g.setImagePath("js/dhtmlxGrid/codebase/imgs/");
	g.setHeader("Name,Value");
	g.setInitWidths("275,275");
	g.setColAlign("left,left");
	g.setColTypes("ro,ro");
	g.setColSorting("na,na");
	g.init();
	g.setSkin("dhx_black");

	tbjEvents.addListener({
		name : 'service_select',
		context : this,
		func : function(evt) {
			$.ajax({ //TODO use asyncServlet
				url : "json?feed=AdPrEdSvDt&dv=" + evt.data,
				type : 'POST',
				dataType : 'json',
				success : function(clos) {
					return function(cld) {
						if (cld.xerror) {
							tbjEvents.fireEvent({ name : 'xerror', data : cld
							});
							return;
						}

						clos.dhx.setItemValue("version", cld.version.shortName + '-' + cld.version.version.majorVersion + '.' + cld.version.version.minorVersion + '.' + cld.version.version.fixVersion);
						clos.dhx.setItemValue("cat", cld.category.value);
						clos.dhx.setItemValue("desc", cld.description.value);
                        clos.grid1.clearAll();
						
						for ( var i = 0; i < cld.urls.length; i++) {
							clos.grid1.addRow(i, "" + i + "," + cld.urls[i] + "," + cld.originalJarNames[i]);
						}
						clos.grid2.clearAll();
						for (key in cld.properties) {
							clos.grid2.addRow(key, key + "," + cld.properties[key]);
						}

					};
				}(this), error : function(err) {
					tbjEvents.fireEvent({ name : 'syserror', data : err
					});
				}
			});
		}
	});
};

