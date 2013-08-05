

String.prototype.endsWith = function(suffix) {
    return this.indexOf(suffix, this.length - suffix.length) !== -1;
};

String.prototype.startsWith = function(prefix) {
    return this.indexOf(prefix) == 0;
};

function talert(msg) { 
	//TODO todo msgKey
	//our own fancy alert;
	alert(msg);
}

var logger;

function newByName(functionName, context /*, args */) {
  var args = Array.prototype.slice.call(arguments).splice(2);
  var namespaces = functionName.split(".");
  var func = namespaces.pop();
  for(var i = 0; i < namespaces.length; i++) {
    context = context[namespaces[i]];
  }
  var f=context[func];
  
  if(f) {
	  return f.apply(f, args);
  }
  else {
	  logger.error("newObjectByName:"+functionName+"is undefined. Perhaps you still need to implement me!");
	  return null;
  }
};

function TrabajoLayout(id, pattern, titles) {
	
	this.id=id;
	this.cells={};
	
	this.cell=function(key) {
		return this.cells[key];
	};
	
	this.dhxLayout=new dhtmlXLayoutObject(document.body, pattern);
	
	this.dhxLayout.cont.obj._offsetTop = 50; // top margin
	this.dhxLayout.cont.obj._offsetLeft = 2; // left margin
	this.dhxLayout.cont.obj._offsetHeight = -4; // bottom margin
	this.dhxLayout.cont.obj._offsetWidth = -4; // right margin
	
	var index="a b c d e f g h i ";
	
	for(var i=0; i< titles.length; i+=2) {
		var letter=index.charAt(i);
		this.cells[titles[i+1]]=this.dhxLayout.cells(letter);
		this.dhxLayout.cells(letter).setText(titles[i]);
	}
	
	this.menuClicked=function(menuId) {
		if ("admin:users" === menuId) {
			window.location.href='dhxusers.html';
		} 
		else if ("admin:acl" === menuId) {
			window.location.href='dhxacl.html';
		} 
		else if ("admin:process" === menuId) {
			window.location.href='dhxprocess.html';
		} 
		else if ("admin:dependency" === menuId) {
			window.location.href='dhxdeps.html';
		} 
		else if ("admin:service" === menuId) {
			window.location.href='dhxsvc.html';
		} 
		else if ("processes:proclist" === menuId) {
			window.location.href='dhxproclist.html';
		} 
        else if ("processes:tasklist" === menuId) {
            window.location.href='dhxtasks.html';
        } 
        else if ("reports:run" === menuId) {
            window.location.href='dhxrpt.html';
        } 
        else if ("system:configure" === menuId) {
            window.location.href='dhxsyscfg.html';
        } 
        else if ("system:dev" === menuId) {
            window.location.href='dhxdev.html';
        } 
        else if ("account:settings" === menuId) {
            window.location.href='dhxacc.html';
        } 
	};

	this.buildMenu=function(menu) {
		menu.setIconsPath("img/");
		menu.loadXML('xml?feed=menu', function() {} );
		menu.attachEvent('onclick', this.menuClicked);
	};

	this.menu=this.buildMenu(this.dhxLayout.attachMenu());
};

function Events() {
	
	this.eventHandlers = {};
	this.startOfChain = null;
	this.endOfChain = null;
	
	this.addListener = function(listener) {
		var split=listener.name.split('|');
		
		for(var i=0; i< split.length; i++) {
	        if(this.eventHandlers[split[i]] === undefined) {
	            this.eventHandlers[split[i]] = new Array();
	        }
			this.eventHandlers[split[i]].push(listener);
		}
	};
	
	this.removeListener = function(listener) {
		
		var handlerList=this.handlers[evt.name];
		
		if(handlerList && listener.id) {
			for(var i=0; i < handlerList.length; i++) {
				var h=handlerList[i];
				if(h.id && h.id === listener.id) {
					handlerList[i]=null;
				}
			}
		}
		
		if(this.eventHandlers[listener.name]!==undefined) {
			this.eventHandlers[listener.name] = new Array();
		}
		
		this.eventHandlers[listener.name].push(listener);
	};
	
	this.fireEvent=function(event) {
		
		if(typeof(event)==='string') {
			event={ name: event};
		}
		
		if(this.startOfChain === null) {
			this.startOfChain=event;
			this.endOfChain=event;
		} else {
			event.prev=this.endOfChain;
			this.endOfChain.next=event;
			this.endOfChain=event;
			return;
		}

		
		while(this.startOfChain!==null) {
			
			var evt=this.startOfChain;
			
			if(evt.next) {
				this.startOfChain=evt.next;
				delete this.startOfChain.prev;
				delete evt.next;
			}
			else {
				this.startOfChain=null;
				this.endOfChain=null;
			}
	
			var handlerList=this.eventHandlers[evt.name];
			
			if(handlerList) {
				for(var i=0; i < handlerList.length; i++) {
					try {
						
						var listener=handlerList[i];
						if(!listener || (listener === null)) {
							continue;
						}
						if(!listener.context || (listener.context === null)) {
							listener.context=window;
						}
						listener.func.apply(listener.context, [evt]);

					}catch(e) {
						alert(e);
					}
				}
			}
		}
	};
}

var tbjEvents=new Events();

function Logger(dhxLayoutCell) {
	this.cell=dhxLayoutCell;
	
	this.grid=this.cell.attachGrid();
	this.grid.setImagePath("js/dhtmlxGrid/codebase/imgs/");
	this.grid.setHeader("Time,Severity,Message");
	this.grid.setInitWidths("350,100,1200");
	this.grid.setColAlign("left,left,left");
	this.grid.setColTypes("ro,ro,ro");
	this.grid.setColSorting("str,str,str");
	this.grid.init();
	this.grid.setSkin("dhx_black");
	
	this.msgId=0;
	
	this.log=function(msg, severity) {
		
		if(msg.msgs) {
			this.log(msg.msgs);
			return;
		}
		
		if($.isArray(msg)) {
			for(var i=0; i< msg.length; i++) {
				this.log(msg[i]);
			}
			return;
		}
		
		if(msg.txt) {
			this.log(msg.txt, msg.sev);
			return;
		}
		
		var as=severity ? severity : "INFO";
		var d=new Date();
		var id=this.msgId++;
		this.grid.addRow(id, d+","+as+","+msg);
		
		switch(as) {
		case "INFO":
			this.grid.setRowColor(id, "palegreen");
			break;
		case "ERROR":
			this.grid.setRowColor(id, "red");
		case "TECH":
			this.grid.setRowColor(id, "violet");
		}
	};
	
	//deprecated, just use log
	this.logProcessStatus = function(ps) {
		logger.log(ps.msgs);
	};
	
	this.info=function(msg) {
		this.log(msg, "INFO");
	};
	
	this.error=function(msg) {
		this.log(msg, "ERROR");
	};
}

function  disposeTask(actionName, parms) {
    
    if(!parms) {
        parms={};
    }
   
    parms.___task=taskId;
    parms.___name=actionName;
    
    server.asyncServlet({servlet: 'process', action: 'dispose_task', data: parms, context: {
        
        dispose_task_OK: function(response) {
            window.parent.location=response.jsonResult;
        }
    } });
}

function  getTaskJSON(dataName, args, func) {

    var parms={ ___task: taskId, ___name: dataName, ___args: args };

    server.asyncServlet({servlet: 'process', action: 'cog_data', data: parms, context: {
        onloaded: func,
        
        cog_data_OK: function(response) {
            this.onloaded(response.jsonResult);
        }
    } });
}

function Server() {
	
	this.unhandledException=function(ex) {
		if(logger)
			logger.error("Unhandled server exception: "+ex.name+" detail: "+ex.detail);
	};
	
	this.defaultErrorFunc=function(arg) {
		if(logger)
			logger.error("Error response from server");
	};
	
	this.defaultOkFunc=function(arg) {
	};
	
	this.defaultBadFunc=function(arg) {
		if(logger)
			logger.error("Server response was good but it was not in the expected format");
	};
	
	this.asyncServlet=function(spec) {
		
		if(!spec.data){
			spec.data={};
		}
	
		if(spec.feed) {
            spec.data.feed=spec.feed;
		}
		else {
		    if(spec.action) {
		        spec.data.a=spec.action;
		    }
		}
		
		var callBackFuncPrefix=(spec.feed) ? spec.feed:spec.action;
		
		var ctx=spec.context;
		
        var okFunc=this.defaultOkFunc;
        var badFunc=this.defaultBadFunc;;
        var errorFunc=this.defaultErrorFunc;

        if(ctx) {
            if(ctx[callBackFuncPrefix+"_OK"]) {
                okFunc=ctx[callBackFuncPrefix+"_OK"];
            }
            if(ctx[callBackFuncPrefix+"_BAD"]) {
                okFunc=ctx[callBackFuncPrefix+"_BAD"];
            }
            if(ctx[callBackFuncPrefix+"_ERROR"]) {
                okFunc=ctx[callBackFuncPrefix+"_ERROR"];
            }
		}
        else {
            ctx={};
        }
			
		$.ajax({
			url : spec.servlet,
			type : 'POST',
			data : spec.data,
			dataType : spec.type ? spec.type:'json',
			success : function(ok, bad, context, clos, spec) {
					return function(response) {
						if(logger && response.msgs) {
							logger.log(response);
						}
						
						if(response.ok && response.ok==false) 
                            bad.apply(context, [response]);
						else
                            ok.apply(context, [response]);
						
						if(response.exceptions) {
							for(var i=0; i< response.exceptions.length; i++) {
								var ex=response.exceptions[i];
								var exName='exception_'+ex.name;
								if(context[exName]) {
									context[exName].apply(context, [ex]);
								}
								else if(context['exception_ALL']) {
									context['exception_ALL'].apply(context, [ex]);
								}
								else {
									clos.unhandledException(ex);
								}
							}
						}
						
                        if(logger && response.msgs) { 
    			            for(var i=0; i < response.msgs.length; i++) {
    			                logger.log(response.msgs[i].txt, response.msgs[i].sev);
    			            }
                        }
                        
                        if(response.ok) { 
                            if(typeof spec.quickfire == 'undefined') {
                                spec.quickfire=[];
                            }
                            else if(typeof spec.quickfire == 'string') {
    					        spec.quickfire=[spec.quickfire];
    						}
    				        for(var j=0; j< spec.quickfire.length; j++) {
    				            tbjEvents.fireEvent({name: spec.quickfire[j], context: window, data: {}});
    				        }
					    }
					};
				}(okFunc, badFunc, ctx, this, spec), 
			error : errorFunc
		});
	};
	
};

var server=new Server();

function initLogger(cell) {
    cell.setHeight(250);
	logger=new Logger(cell);
	getDeferredMsgs();
}

function getDeferredMsgs() {
	server.asyncServlet({servlet: 'process', action: 'deferred_msgs', context:{
		
		deferred_msgs_OK: function(response) {
			
			for(var i=0; i < response.msgs.length; i++) {
				logger.log(response.msgs[i].txt, response.msgs[i].sev);
			}
		},
		
		deferred_msgs_ERROR: function(response) {
			
		}
	}});
}

function TreeWalker(dhxTree, nodeToBegin, visitor) {
    this.tree=dhxTree;
    this.nodeToBegin=nodeToBegin;
    this.visitor=visitor;
    
    this.walk=function(context) {
        this.walk2(this.tree, nodeToBegin, context);
    };
    
    this.walk2=function(tree, current, context) {
        for(var i=0; i< tree.hasChildren(current); i++) {
            this.walk2(tree, tree.getChildItemIdByIndex(current, i), context);
        }
        this.visitor(tree, current, context);
    };
}


function CellToolbar(name, config) {
    this.config=config;
    
    this.toString=function() {
        var s='<table style="table-layout: fixed; width: 90%;"><tr>';
        s+='<td style="padding: none; margin-right: 30px; vertical-align: top">';
        s+=name;
        s+='</td>';
        
        for(var i in this.config) {
            s+='<td style="text-align: right; width: 10%; vertical-align: top"><a href="';
            s+=config[i];    
            s+='"><img style="position: relative; top: -10px; border:none; float:right; width: 32px; height: 32px;" src="';
            s+='img/tbr_'+i+'.png';
            s+='"/></a></td>';
        }
        s+='</tr></table>';
        return s;
    };
}

function showAbout() {
    $('.pop').css("display", "block");
}
function unShowAbout() {
    $('.pop').css("display", "none");
}
function logout() {
    window.location.href='logout';
}

function Graphs(dhx) {
    
    this.win=null;
    
    this.spec=[
          {type: "settings", position: "label-left", labelWidth: 100, inputWidth: 150 },
          {type: "fieldset",  label: "Task Info", width: 500, list:[
            {type: "input", disabled: "true", name: "id", label: "Task DB ID"}
          ]}
        ];

    this.detail=function(taskId) {
        this.win=dhx.dhxWins.createWindow("Task: "+taskId, 300,300,700,500);
        this.win.setText(name);
        this.form=this.win.attachForm();
        this.form.setSkin("dhx_skyblue");

        this.form.loadStruct(this.spec, "json", function() {});
        
        this.form.setItemValue("id", taskId);
        this.win.show();
    };
    
    tbjEvents.addListener({ name: 'graph_task_select', context:this, func: function(evt){
        this.detail(evt.data.task);
    }});
}

function documentLoaded() {
    
    $.ajax( 
            {
                url: 'header.html',
                success: 
                    function(data) {
                        $('#cog_header').html(data);
                    }
        }
    );
}

function MapManager(cell) {
    MapManager.obj=this;
    
    this.dhxWins= new dhtmlXWindows();
    this.dhxWins.setImagePath("js/dhtmlxWindows/codebase/imgs/");
    this.dhxWins.setSkin("dhx_black");

    this.addLocationEnabled=false;
    this.cell=cell;
    this.userpk=0;
    this.jsonMap=null;
    this.map = this.cell.attachMap({ center: new google.maps.LatLng(57.992348, -3.719837 ), zoom: 9, mapTypeId: google.maps.MapTypeId.ROADMAP });
    
    this.updateLocationMeta=function(locName, metaName, metaValue) {
        debugger;
        this.jsonMap.locations[locName].metadata[metaName]=metaValue;
        var lat=this.jsonMap.locations[locName].latitude;
        var lng=this.jsonMap.locations[locName].longitude;
        server.asyncServlet(
                {
                    servlet: 'map', 
                    action: 'updateLocMeta', 
                    context: this, 
                    data: { location: locName, lat: lat, lng: lng, name: metaName, value: metaValue } ,
                    func: function() {}
                });
    };
    
    this.editLocation=function(locName) {
        new LocationMetaPopup(this, locName, this.jsonMap.locations[locName]);
    };
    
    this.loadUserLocationMap=function() {
        server.asyncServlet({servlet: 'map', action: 'user', context: this });
    };

    this.user_OK=function(response) {
        debugger;
        this.jsonMap=eval('('+response.jsonResult+')');
        
        var centerLat=this.jsonMap.metadata['CENTER_LAT'];
        var centerLong=this.jsonMap.metadata['CENTER_LONG'];
        
        if(!centerLat) {
            centerLat=57.9;
            centerLong=-3.7;
            this.jsonMap.metadata['CENTER_LAT']=centerLat;
            this.jsonMap.metadata['CENTER_LONG']=centerLong;
        }

        this.map.setCenter(new google.maps.LatLng(centerLat, centerLong));

        for(var locName in this.jsonMap.locations) {
            this.loadLocation(locName, this.jsonMap.locations[locName]);
        }
        
        this.enableAddLocation(true);
    };

    this.loadLocation=function(locName, location) {
        debugger;
        var latLng=new google.maps.LatLng(location.latitude, location.longitude);
        var marker=new google.maps.Marker({position: latLng, title: locName, map: this.map});
        marker.tbjId=locName;
        marker.tbjMessage = '<a href="javascript:MapManager.obj.editLocation(\''+marker.tbjId+'\');">Edit</a><br/>';
        marker.infowindow = new google.maps.InfoWindow( { content: marker.tbjMessage, size: new google.maps.Size(100,100) });
        
        google.maps.event.addListener(marker, 'click', function() {
            marker.infowindow.open(MapManager.obj.map, marker);
        });
    };
    
    this.enableAddLocation=function(is) {
        this.addLocationEnabled=is;
    };
    
    this.addLocation=function(goolatlng) {
        debugger;
        if(this.addLocationEnabled) {
            this.addLocationPopup=new LocationPopup(this, goolatlng, this.dhxWins.createWindow("AddLocation", 200, 200, 800, 300));
        }
    };    
    
    google.maps.event.addListener(this.map, 'click', function(location) { MapManager.obj.addLocation(location.latLng); });
}

function LocationMetaPopup(mapManager, locName, location) {
    this.mapManager=mapManager;
    this.locName=locName;
    this.location=location;
    LocationMetaPopup.obj=this;
    
    this.dhxWin=mapManager.dhxWins.createWindow("locmeta", 500, 100, 800, 400);
    this.dhxWin.setText("Location: "+locName);
    this.dhxWin.show();

    this.spec=[
               {type: "settings", position: "label-left", labelWidth: 100, inputWidth: 120, width: 800 },
               {type: "fieldset",  label: "Location Properties", width: 400, list:[
                    {type: "container", name: "props", label: "Properties", inputWidth: 600, inputHeight: 150 },
                  ]
               }
               ];
    debugger;
    this.form=this.dhxWin.attachForm();
    this.form.setSkin("dhx_skyblue");
    this.form.loadStruct(this.spec, 'json',  function() {});
    
    this.grid = new dhtmlXGridObject(this.form.getContainer("props"));
    g = this.grid;
    g.setImagePath("js/dhtmlxGrid/codebase/imgs/");
    g.setHeader("Property,Value");
    g.setInitWidths("275,275");
    g.setColAlign("left,left");
    g.setColTypes("ed,ed");
    g.setColSorting("na,na");
    g.init();
    g.setSkin("dhx_black");
    g.enableEditEvents(true,true,true);
    g.attachEvent("onEditCell", function(stage,rId,cInd,nValue,oValue) {
        debugger;
        if(stage==2) {
            if(rId=='new' && cInd==0) {
                this.changeRowId("new", nValue);    
                this.cellById(nValue, 1).setValue('');
                if(!this.cellById('new', 0)) {
                    this.addRow('new', '[name],[value]');
                }

                return true;
            }
            else if(cInd==1 && rId!='new') {
                MapManager.obj.updateLocationMeta(LocationMetaPopup.obj.locName, rId, nValue);
                if(!this.cellById('new', 0)) {
                    this.addRow('new', '[name],[value]');
                }
                return true;
            }
            else {
                return false;
            }
        }
        return true;
    });

    for(var metaName in this.location.metadata) {
        g.addRow(metaName, metaName+','+this.location.metadata[metaName]);
    }
    g.addRow('new', '[name],[value]');
    
    
};

function LocationPopup(mapManager, goolatlng, dhxWin) {
    this.mapManager=mapManager;
    this.goolatlng=goolatlng;
    LocationPopup.obj=this;
    this.dhxWin=dhxWin;
    this.spec=[
               {type: "settings", position: "label-left", labelWidth: 100, inputWidth: 120 },
               {type: "fieldset",  label: "Location Details", width: 400, list:[
                    {type: "input", disabled: true, name: "latitude", label: "Latitude", value: goolatlng.lat()},
                    {type: "input", disabled: true, name: "longitude", label: "Longitude", value: goolatlng.lng()},
                    {type: "input", name: "name", label: "Location Name", value: "",required: true},
                    {type: "button", name: "create", value: "Create"}]
               }
               ];
    
    
    
    this.form=dhxWin.attachForm();
    this.form.setSkin("dhx_skyblue");
    this.form.loadStruct(this.spec, 'json',  function() {});
    
    this.form.attachEvent("onButtonClick", function(){
        debugger;
        var loc={ latitude: this.getItemValue("latitude"), longitude: this.getItemValue("longitude"), metadata: {}};
        var locName=this.getItemValue("name");
        LocationPopup.obj.mapManager.loadLocation(locName, loc);
        LocationPopup.obj.dhxWin.close();
        MapManager.obj.jsonMap.locations[locName]=loc;
    });
};

