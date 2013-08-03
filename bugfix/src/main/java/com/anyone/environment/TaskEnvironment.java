package com.anyone.environment;

import static com.trabajo.annotation.StandardTaskLifecycle.ACTION;
import static com.trabajo.annotation.StandardTaskLifecycle.CREATE;
import static com.trabajo.annotation.StandardTaskLifecycle.VIEW;

import java.io.IOException;
import java.io.Writer;

import com.anyone.Issue;
import com.anyone.Issues;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.trabajo.ILifecycle;
import com.trabajo.ProcessException;
import com.trabajo.StateContainer;
import com.trabajo.TaskCompletion;
import com.trabajo.TimerType;
import com.trabajo.annotation.ACog;
import com.trabajo.annotation.AInstance;
import com.trabajo.annotation.ATask;
import com.trabajo.annotation.ActionParameters;
import com.trabajo.annotation.Data;
import com.trabajo.annotation.Service;
import com.trabajo.annotation.Task;
import com.trabajo.annotation.TaskLifecycle;
import com.trabajo.annotation.TaskState;
import com.trabajo.annotation.Timeout;
import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;
import com.trabajo.process.TaskCreator;

@Task(name = "environment")
public class TaskEnvironment {

	@ACog private ILifecycle cog;
	@AInstance private IInstance instance;
	@ATask private ITask thisTask;
  @TaskState private StateContainer stateContainer;
	@Service(name = "issues")	private Issues service;

	@TaskLifecycle(phase = CREATE)
	public void create(StateContainer parms) {
		stateContainer=parms;
	}	
	
	@TaskLifecycle(phase = VIEW)
	public void view() {
		cog.setHTMLView("html/environment.html");
	}

	@ActionParameters(adapter=ReadyParms.class, actions="ready") private ReadyParms readyParms;
	@TaskLifecycle(phase = ACTION, action = "ready")
	public void ready() {
			stateContainer.put("env", readyParms.env());
			thisTask.end(TaskCompletion.OK, stateContainer);
			cog.cancelTimer("escalate");
	}
	

	@Data(name="formData", contentType="application/json")
	public void formData(String args, Writer w) throws IOException {
		Issue issue=service.getIssue((Integer)stateContainer.get("issueId"));
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(issue);
		jsonElement.getAsJsonObject().addProperty("explainTask", "Determine whether or not this bug represents an issue already raised. If so, supply the id. If not, select new bug");
		w.write(gson.toJson(jsonElement));
	}
	
	@Timeout(name="escalate", type=TimerType.DUEDATE, repeat=false, minute="10")
	public void dueDateGone() {
			TaskCreator tc = new TaskCreator("escalate");
			tc.assignToRoles("bugfix_manager");
			try {
				cog.createTask(tc, stateContainer);
			} catch (ProcessException e) {
			}
	}
}
