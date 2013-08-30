package com.trabajo;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.trabajo.utils.Strings;

public class DAGGroup implements IVizTaskGroup, Serializable {
  private static final long serialVersionUID = 1L;
  
	private String	             name;
	private Map<String, DAGTask>	tasks	       = new HashMap<>();
	private Map<String, DAGTask>	tasksCreated	= new HashMap<>();
	private Map<String, DAGGroup>	groupDependsEach	= new HashMap<>();
	private DAGVisualizer	       viz;
	private boolean emitted;

	public DAGGroup() {
	}

	public DAGGroup(String groupName, DAGVisualizer viz, String... taskNames) {
		this.name = groupName;
		this.viz = viz;
		tasks.put("dummy", new DAGTask(viz, this, "dummy"));
		for (String taskName : taskNames) {
			tasks.put(taskName, new DAGTask(viz, this, taskName));
		}
	}

	public DAGVisualizer getViz() {
		return viz;
	}

	public void overview(StringBuilder sb) {
		if ("_default".equals(name)) {
			sb.append("digraph G {\n");

			for (DAGTask t : tasks.values()) {
				if (!"dummy".equals(t.getName())) {
					t.overview(sb);
				}
			}

			for (DAGGroup g : viz.groups()) {
				if ("_default".equals(g.getName())) {
					continue;
				}

				g.overview(sb);
			}

			sb.append("_default__END_PROCESS [id=END_PROCESS;label=END_PROCESS;style=filled;fillcolor=black;fontcolor=white;fontname=Arial;fontsize=10.0;];\n");
			sb.append("}\n");
		} else {
			sb.append("\n\n\nsubgraph cluster_");
			sb.append(name);
			sb.append(" {\n");
			if (!"_default".equals(name)) {
				for (DAGTask t : tasksCreated.values()) {
					sb.append(name);
					sb.append("__dummy  -> ");
					sb.append(t.qualifiedName());
					sb.append(";\n");
				}
			}

			for (DAGTask t : tasks.values()) {
				t.overview(sb);
			}


			sb.append("}\n");
			if (groupDependsEach.size() > 0)
				sb.append("edge [color=red];\n");
			for (DAGGroup g : groupDependsEach.values()) {
				sb.append(name);
				sb.append("__dummy  -> ");
				sb.append(g.name);
				sb.append("__dummy;\n");
			}
			if (groupDependsEach.size() > 0)
				sb.append("edge [color=black];\n");

			for (DAGTask t : tasks.values()) {
				t.externalLinks(sb);
			}
		}
	}

	@Override
	public void createsTasks(String... taskNames) {
		for (String name : taskNames) {
			DAGTask t = getTask(name);
			if (t == null) {
				DAGGroup dflt = viz.getGroup("_default");
				t = dflt.getTask(name);
				if (t == null) {
					throw new IllegalStateException("group:" + getName() + " or default group not previously associated with task : " + name);
				}
			}
			tasksCreated.put(name, t);
		}
	}

	public void status(IGraphEntityFinder gef, StringBuilder sb) {
		sb.append("digraph G {\n");

		// TODO

		sb.append("END_PROCESS [id=END_PROCESS;style=filled;fillcolor=black;fontcolor=white;fontname=Arial;fontsize=10.0;];\n");
		sb.append("}\n");
	}

	public DAGTask getTask(String name) {
		return tasks.get(name);
	}

	public String getName() {
		return name;
	}

	public boolean isDefault() {
		return "_default".equals(name);
	}

	@Override
  public void dependsEach(String groupName) {
		if("_default".equals(name)) {
			throw new IllegalStateException("the default group cannot depend on any other");
		}
		
		groupDependsEach.put(groupName, viz.getGroup(groupName));	  
  }

	void emitCode(String base, String pkg) throws IOException {
		if(this.emitted)
			return;
		
		this.emitted=true;
	  
		File f=new File(base+"\\main\\java\\"+pkg.replace('.', '\\')+"\\TaskGroup" + Strings.upperFirst(name) + ".java");
		if(!f.exists()) {

		PrintWriter out = new PrintWriter(f);
		emit(out, "package "+pkg+";");
		emit(out, "");
		emit(out, "import com.trabajo.annotation.TaskGroup;");
		emit(out, "import com.trabajo.annotation.TaskGroupStart;");
		emit(out, "import com.trabajo.annotation.TaskGroupEnd;");
		emit(out, "");
		emit(out, "@TaskGroup(name=\""+name+"\")");
		emit(out, "public class TaskGroup"+Strings.upperFirst(name)+" {");
		emit(out, "");
		emit(out, "  @TaskGroupStart");
		emit(out, "  public void start() {");
		emit(out, "");
		emit(out, "  }");
		emit(out, "");
		emit(out, "  @TaskGroupEnd");
		emit(out, "  public void end() {");
		emit(out, "");
		emit(out, "  }");
		emit(out, "}");
		out.close();
		}
		
		for(DAGTask t: tasks.values()) {
	      t.emitCode(base, pkg);
		}
  }
	
	private void emit(PrintWriter out, String string) {
		out.println(string);
	}
}
