package com.trabajo;

import java.io.FileWriter;

import org.junit.Test;

public class VizTest {

	
	@Test 
	public void viz() throws Exception {
		
		DAGVisualizer  viz=new DAGVisualizer();
		
		//what instance creates directly when it starts
		viz.groupTasks("_default", "createOrder", "assignProjectManager", "cancelOrder", "coolingDownExpired", "beginManufacture", "arrangeDelivery", "deliver", "onDeliveryDate");
		viz.start().createsTasks("createOrder", "assignProjectManager", "cancelOrder", "coolingDownExpired", "arrangeDelivery", "deliver", "onDeliveryDate");
		
		viz.groupTasks("designWindow", "specifyFrameTask", "specifyUPVCTasks", "SpecifyGlassTasks");
		viz.group("designWindow").createsTasks("specifyFrameTask", "specifyUPVCTasks", "SpecifyGlassTasks");
		
		viz.groupTasks("manufactureWindow", "manufactureUPVCTask", "manufactureGlassTask", "manufactureComplete");
		viz.group("manufactureWindow").createsTasks("manufactureUPVCTask", "manufactureGlassTask", "manufactureComplete");
		viz.group("manufactureWindow").dependsEach("designWindow");
		
		viz.groupTasks("framing", "horizontals", "verticals", "assembleFrame");
		viz.group("framing").createsTasks("horizontals", "verticals", "assembleFrame");
		
		viz.task("coolingDownExpired").dependsOn("createOrder");
		viz.task("beginManufacture").dependsOn("coolingDownExpired");
		viz.task("assignProjectManager").createsTasks("beginManufacture");
		viz.task("beginManufacture").createsGroups("designWindow");
		
		viz.task("specifyFrameTask", "designWindow").createsGroups("framing");
		viz.task("assembleFrame", "framing").dependsOn("horizontals", "verticals");
		
		viz.task("manufactureComplete", "manufactureWindow").dependsOn("manufactureUPVCTask", "manufactureGlassTask");
		viz.task("arrangeDelivery").dependsOn("manufactureComplete");
		viz.task("arrangeDelivery").createsTasks("onDeliveryDate");
		viz.task("deliver").dependsOn("onDeliveryDate");
		viz.task("deliver").canEndProcess();
		viz.task("cancelOrder").canEndProcess();
		
		String base="C:\\_GITHUB3\\sell_windows\\src";
		
		viz.emitCode(base, "com.colin");
		String digraph=viz.overview();
		
    GraphViz.generate("C:\\Program Files\\Graphviz2.30", new FileWriter("C:\\_GITHUB3\\maven.1375521594990\\parent\\api\\src\\test\\java\\test.html"), digraph);
	}
	
}
