package com.trabajo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.trabajo.process.ITask;
import com.trabajo.utils.Strings;

public class DAGTask implements IVizTask, Serializable {

	private static final long	    serialVersionUID	= 1L;

	private DAGVisualizer	        viz;
	private DAGGroup	            group;
	private String	              name;
	private int	                  taskId;

	private Map<String, DAGTask>	tasksCreated	   = new HashMap<>();
	private Map<String, DAGGroup>	groupsCreated	   = new HashMap<>();
	private Map<String, DAGTask>	depends	         = new HashMap<>();
	private Map<String, DAGGroup>	dependsGroup     = new HashMap<>();

	private boolean	              canEndProcess;

	private boolean emitted;

	public DAGTask() {
	}

	public DAGTask(DAGVisualizer viz, DAGGroup group, String name) {
		this();
		if (group == null || name == null) {
			throw new IllegalArgumentException();
		}
		this.viz = viz;
		this.group = group;
		this.name = name;
	}

	@Override
	public void setId(int id) {
		this.taskId = id;
	}

	@Override
	public void createsTasks(String... taskNames) {
		for (String name : taskNames) {
			DAGTask t = group.getTask(name);
			if (t == null) {
				DAGGroup dflt = viz.getGroup("_default");
				t = dflt.getTask(name);
				if (t == null) {
					throw new IllegalStateException("group:" + group.getName() + " or default group not previously associated with task : " + name);
				}
			}
			tasksCreated.put(name, t);
		}
	}

	@Override
	public void dependsOn(String... taskNames) {
		for (String name : taskNames) {
			for (DAGGroup g : viz.groups()) {
				DAGTask t = group.getTask(name);
				if (t != null) {
					depends.put(t.qualifiedName(), t);
					break;
				}
			}
		}
	}

	@Override
	public void createsGroups(String... groupNames) {
		for (String groupName : groupNames) {
			groupsCreated.put(groupName, viz.getGroup(groupName));
		}
	}

	@Override
	public void canEndProcess() {
		canEndProcess = true;
	}

	public void overview(StringBuilder sb) {
		String fill = "white";
		String fontColor = computeFontcolor(fill);
		String qName = qualifiedName();

		if ("dummy".equals(name)) {
			sb.append(qualifiedName());
			sb.append(" [style=filled;fillcolor=orange];\n");
			return;
		}

		if (group.isDefault()) {
			for (DAGGroup groupCreated : groupsCreated.values()) {
				sb.append(qualifiedName());
				sb.append(" -> ");
				sb.append(((DAGTask) groupCreated.getTask("dummy")).qualifiedName());
				sb.append(";\n");
			}
		}

		if ("Process".equals(name)) {
			sb.append(qualifiedName());
			sb.append(" [id=INSTANCE;label=INSTANCE;style=filled;fillcolor=black;fontcolor=white;fontname=Arial;fontsize=10.0;];\n");
		} else {
			sb.append(qName);
			sb.append(" [label=");
			sb.append(name);
			sb.append(";URL=\"javascript:overview(");
			sb.append(taskId);
			sb.append(")\"");
			sb.append(";style=filled;fillcolor=");
			sb.append(fill);
			sb.append(";fontcolor=");
			sb.append(fontColor);
			sb.append(";shape=box;fontname=Arial;fontsize=10.0;];\n");
		}

		for (DAGTask created : tasksCreated.values()) {
			if (created.group == this.group || created.group.getName().equals("_default")) {
				sb.append(qName);
				sb.append(" -> ");
				sb.append(created.qualifiedName());
				sb.append(";\n");
			}
		}

		if (depends.size() > 0)
			sb.append("edge [color=red];\n");

		for (DAGTask depend : depends.values()) {
			sb.append(qualifiedName());
			sb.append(" -> ");
			sb.append(depend.qualifiedName());
			sb.append(";\n");
		}

		if (depends.size() > 0)
			sb.append("edge [color=black];\n");

		if (canEndProcess) {
			sb.append(qualifiedName());
			sb.append(" -> _default__END_PROCESS;");
		}
	}

	private boolean isGroupDummy() {
		return "dummy".equals(name);
	}

	String qualifiedName() {
		return group.getName() + "__" + name;
	}

	private String computeFontcolor(String fill) {
		String result = "black";

		switch (fill) {
			case "black":
			case "indigo":
				result = "white";
		}
		return result;
	}

	private String computeBgcolor(ITask task) {
		String result = "white";
		if (task != null) {
			result = "indigo";
			if (task.getComplete() != null) {
				switch (task.getComplete()) {
					case FAILED:
						result = "crimson";
						break;
					case OK:
						result = "green";
						break;
				}
			}
		}
		return result;
	}

	public String getName() {
		return name;
	}

	public int getTaskId() {
		return taskId;
	}

	public boolean isInDefaultGroup() {
		return group.isDefault();
	}

	public void externalLinks(StringBuilder sb) {
		for (DAGTask created : tasksCreated.values()) {
			if (created.isInDefaultGroup()) {
				sb.append(qualifiedName());
				sb.append(" -> ");
				sb.append(created.qualifiedName());
				sb.append(";\n");
			}
		}

		for (DAGGroup groupCreated : groupsCreated.values()) {
			sb.append(qualifiedName());
			sb.append(" -> ");
			sb.append(((DAGTask) groupCreated.getTask("dummy")).qualifiedName());
			sb.append(";\n");
		}

	}

	void emitCode(String base, String pkg) throws IOException {
		if(this.emitted)
			return;
		
		this.emitted=true;
		
		File f=new File(base+"\\main\\java\\"+pkg.replace('.', '\\')+"\\Task" + Strings.upperFirst(name) + ".java");
		if(!f.exists()) {
		PrintWriter out = new PrintWriter(f);
		emit(out, "package "+pkg+";");
		emit(out, "");
		emit(out, "import static com.trabajo.annotation.StandardTaskLifecycle.CREATE;");
		emit(out, "import static com.trabajo.annotation.StandardTaskLifecycle.VIEW;");
		emit(out, "");
		emit(out, "import com.trabajo.ILifecycle;");
		emit(out, "import com.trabajo.StateContainer;");
		emit(out, "import com.trabajo.annotation.ACog;");
		emit(out, "import com.trabajo.annotation.AInstance;");
		emit(out, "import com.trabajo.annotation.ATask;");
		emit(out, "import com.trabajo.annotation.Task;");
		emit(out, "import com.trabajo.annotation.TaskLifecycle;");
		emit(out, "import com.trabajo.annotation.TaskState;");
		emit(out, "import com.trabajo.process.IInstance;");
		emit(out, "import com.trabajo.process.ITask;");
		emit(out, "");
		emit(out, "@Task(name = \"" + name + "\")");
		emit(out, "public class Task"+Strings.upperFirst(name)+" {");
		emit(out, "");
		emit(out, "  @ACog private ILifecycle cog;");
		emit(out, "  @AInstance private IInstance instance;");
		emit(out, "  @ATask private ITask thisTask;");
		emit(out, "  @TaskState private StateContainer stateContainer;");
		emit(out, "");
		emit(out, "  @TaskLifecycle(phase = CREATE)");
		emit(out, "  public void create(StateContainer parms) {");
		emit(out, "    stateContainer=parms;");
		emit(out, "  }	");
		emit(out, "");
		emit(out, "  @TaskLifecycle(phase = VIEW)");
		emit(out, "  public void view() {");
		emit(out, "    cog.setHTMLView(\"html/"+name+".html\");");
		emit(out, "  }");
		emit(out, "");
		emit(out, "}");
		out.close();
		
		String html="<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"		<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" + 
				"\n" + 
				"		$magic.dhxSupport()\n" + 
				"\n" + 
				"		<script>\n" + 
				"		var layout;	\n" + 
				"\n" + 
				"\n" + 
				"		$(document).ready(\n" + 
				"			function() {\n" + 
				"				server=new Server();\n" + 
				"				layout=buildLayout();\n" + 
				"			}\n" + 
				"		);\n" + 
				"\n" + 
				"		function buildLayout() {\n" + 
				"			layout=new dhtmlXLayoutObject(document.body, \"2U\");\n" + 
				"			layout.cont.obj._offsetTop = 50; // top margin\n" + 
				"			layout.cont.obj._offsetLeft = 2; // left margin\n" + 
				"			layout.cont.obj._offsetHeight = -4; // bottom margin\n" + 
				"			layout.cont.obj._offsetWidth = -4; // right margin	buildCellRight(layout.cell(\"right\"))\n" + 
				"\n" + 
				"			buildCellLeft(layout.cells('a'));\n" + 
				"			buildCellRight(layout.cells('b'));\n" + 
				"			layout.cells('a').hideHeader();\n" + 
				"			layout.cells('b').hideHeader();\n" + 
				"		}\n" + 
				"\n" + 
				"		function buildCellLeft(layoutCell) {\n" + 
				"	    var form=layoutCell.attachForm(\"explain_task\");\n" + 
				"			form.setSkin('dhx_skyblue');\n" + 
				"			var spec=[\n" + 
				"			   	    {type: \"settings\", position: \"label-left\", inputWidth: 300, labelWidth: 200},\n" + 
				"			   		\n" + 
				"			   		{type: \"fieldset\",  label: \"Task Actions\", width: 750, list:[\n" + 
				"			           {type: \"input\", rows: 5,  name: \"explainTask\", disabled: true, label: \"Task description\"},\n" + 
				"			   		   \n" + 
				"			   		   {type: \"button\", name: \"fixed\", value: \"disposeName\"}\n" + 
				"			   		]}\n" + 
				"			       ];\n" + 
				"			\n" + 
				"			form.loadStruct(spec, \"json\", function() {});\n" + 
				"			form.attachEvent(\"onButtonClick\", function(name) {\n" + 
				"			});\n" + 
				"			\n" + 
				"		    getTaskJSON('formData', {}, function(form) { return function(json) {\n" + 
				"		        for(item in json) {\n" + 
				"		            form.setItemValue(item, json[item]);\n" + 
				"		        }\n" + 
				"		        form.updateValues();\n" + 
				"		    };}(form));\n" + 
				"		}\n" + 
				"\n" + 
				"		function buildCellRight(layoutCell) {\n" + 
				"		}\n" + 
				"\n" + 
				"\n" + 
				"		</script>\n" + 
				"			 \n" + 
				"</head>\n" + 
				"<body>\n" + 
				"  <div id=\"taskLayout\" style=\"position: relative; top: 20px; left: 20px; width: 600px; height: 800px; aborder: #B5CDE4 1px solid;\"></div>\n" + 
				"</body>\n" + 
				"</html>\n" + 
				"";		
		
		
		out = new PrintWriter(base+"\\main\\resources\\html\\"+name + ".html");
		emit(out, html);
		out.close();
		
		}
		
		
		for(DAGGroup g: groupsCreated.values()) {
			try {
	      g.emitCode(base, pkg);
      } catch (IOException e) {
	      e.printStackTrace();
      }
		}

	}

	private void emit(PrintWriter out, String string) {
		out.println(string);
	}

	public void dependsGroup(String ... groups) {
	  for(String groupName: groups) {
	  	dependsGroup.put(groupName, viz.getGroup(groupName));
	  }
  }

}