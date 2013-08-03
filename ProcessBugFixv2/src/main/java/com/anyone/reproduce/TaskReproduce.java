package com.anyone.reproduce;

import static com.trabajo.annotation.StandardTaskLifecycle.ACTION;
import static com.trabajo.annotation.StandardTaskLifecycle.CREATE;
import static com.trabajo.annotation.StandardTaskLifecycle.VIEW;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.List;

import com.anyone.Issue;
import com.anyone.Issues;
import com.anyone.Note;
import com.anyone.XMLSBHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.trabajo.ILifecycle;
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
import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;
import com.trabajo.process.TaskCreator;

@Task(name = "reproduce")
public class TaskReproduce {

	@ACog private ILifecycle cog;
	@AInstance private IInstance instance;
	@ATask private ITask thisTask;
  @TaskState private StateContainer stateContainer;
	@Service(name = "issues")	private Issues service;

	@TaskLifecycle(phase = CREATE)
	public void create(StateContainer parms) {
		stateContainer=(StateContainer)parms.get("environment");
	}
	
	@TaskLifecycle(phase = VIEW)
	public void view() {
		cog.setHTMLView("html/reproduce.html");
	}

	@TaskLifecycle(phase = ACTION, action = "can")
	public void can() {
		
		TaskCreator tc = new TaskCreator("fix");
		tc.assignToRoles("developer");
		try {
			cog.createTask(tc, stateContainer);
		} catch (ProcessException e) {
		}
		
		thisTask.end(TaskCompletion.OK, stateContainer);
	}
	
	@ActionParameters(adapter=AddNoteParms.class, actions="addNote") 
	private AddNoteParms addNoteParms;
	
	@TaskLifecycle(phase = ACTION, action = "addNote")
	public void addNote() {
		Issue issue=service.getIssue((Integer)stateContainer.get("issueId"));
		issue.createNote(cog.getTask().getUserAssignedTo().getFullName(), addNoteParms.note());
	}
	
	@TaskLifecycle(phase = ACTION, action = "cant")
	public void cant() {
		thisTask.end(TaskCompletion.OK, stateContainer);
	}
	
	
	@Data(name="formData", contentType="application/json")
	public void formData(String args, Writer w) throws IOException {
		Issue issue=service.getIssue((Integer)stateContainer.get("issueId"));
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(issue);
		jsonElement.getAsJsonObject().addProperty("explainTask", "Reproduce this bug on the given environment");
		jsonElement.getAsJsonObject().addProperty("env", (String)stateContainer.get("env"));
		w.write(gson.toJson(jsonElement));
	}
	
	@Data(name="noteData", contentType="text/xml")
	public void noteData(String args, Writer w) throws IOException {
		Issue issue=service.getIssue((Integer)stateContainer.get("issueId"));
		List<Note> notes=issue.getNotes();
		
		XMLSBHelper xml = new XMLSBHelper(new StringBuilder());
		
		xml.elm("rows").closeTag();
		
		int id=1;
		SimpleDateFormat sdf=new SimpleDateFormat("dd/mm/YYYY hh:MM:ss");
		for(Note note: notes) {
			xml.elm("row").attr("id", String.valueOf(id++)).closeTag();
			
			xml.elm("cell").closeTag().append(sdf.format(note.getCreateTime().getTime())).closeElm("cell");
			xml.elm("cell").closeTag().append(note.getCreator()).closeElm("cell");
			xml.elm("cell").closeTag()

			.append("<![CDATA[")
			.append(note.getNoteText())
			.append("]]>").closeElm("cell")
			.closeElm("row");
			
		}
		xml.closeElm("rows");
		
		
		w.write(xml.toString());
	}
}
