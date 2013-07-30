package com.anyone.tasks.investigate;

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
import com.trabajo.IVisualizer;
import com.trabajo.ProcessException;
import com.trabajo.StateContainer;
import com.trabajo.TaskCompletion;
import com.trabajo.annotation.ACog;
import com.trabajo.annotation.AInstance;
import com.trabajo.annotation.ATask;
import com.trabajo.annotation.ActionParameters;
import com.trabajo.annotation.Data;
import com.trabajo.annotation.Service;
import com.trabajo.annotation.Task;
import com.trabajo.annotation.TaskLifecycle;
import com.trabajo.annotation.TaskState;
import com.trabajo.annotation.Visualizer;
import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;
import com.trabajo.process.TaskChronology;
import com.trabajo.process.TaskCreator;

@Task(name = "investigate")
public class TaskInvestigate {

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
		cog.setHTMLView("html/investigate.html");
	}

	@ActionParameters(adapter=DuplicateParms.class, actions="duplicate") private DuplicateParms duplicateParms;
	@TaskLifecycle(phase = ACTION, action = "duplicate")
	public void duplicate() {
		Issue issue=service.getIssue((Integer)stateContainer.get("issueId"));
		issue.setDuplicateOf(duplicateParms.existingId());
		thisTask.end(TaskCompletion.OK, stateContainer);
	}
	
	@ActionParameters(adapter=NewBugParms.class, actions="newBug") private NewBugParms newBugParms;
	@TaskLifecycle(phase = ACTION, action = "newBug")
	public void newBug() {
		try {
			cog.createTask("environment", stateContainer).assignToRoles("environment");
			
			TaskCreator tc = new TaskCreator("reproduce");
			tc.assignToRoles("developer");
			tc.dependsOn("environment", TaskChronology.LATEST);
			cog.createTask(tc, stateContainer);
			
		} catch (ProcessException e) {
		}
		thisTask.end(TaskCompletion.OK, stateContainer);
	}
	
	
	@Data(name="grid", contentType="text/xml")
	public void gridXML(String args, Writer w) throws IOException {
			w.write(service.getGridXML());
	}
	
	@Data(name="formData", contentType="application/json")
	public void formData(String args, Writer w) throws IOException {
		Issue issue=service.getIssue((Integer)stateContainer.get("issueId"));
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(issue);
		jsonElement.getAsJsonObject().addProperty("explainTask", "Determine whether or not this bug represents an issue already raised. If so, supply the id. If not, select new bug");
		w.write(gson.toJson(jsonElement));
	}
}
