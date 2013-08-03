package com.anyone.escalate;

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
import com.trabajo.StateContainer;
import com.trabajo.TaskCompletion;
import com.trabajo.annotation.ACog;
import com.trabajo.annotation.AInstance;
import com.trabajo.annotation.ATask;
import com.trabajo.annotation.Data;
import com.trabajo.annotation.Service;
import com.trabajo.annotation.Task;
import com.trabajo.annotation.TaskLifecycle;
import com.trabajo.annotation.TaskState;
import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;

@Task(name = "escalate")
public class TaskEscalate {

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
		cog.setHTMLView("html/escalate.html");
	}

	@TaskLifecycle(phase = ACTION, action = "keep")
	public void keep() {
	}
	
	@TaskLifecycle(phase = ACTION, action = "backOnTrack")
	public void backOnTrack() {
		thisTask.end(TaskCompletion.OK, stateContainer);
	}
	
	@Data(name="formData", contentType="application/json")
	public void formData(String args, Writer w) throws IOException {
		Issue issue=service.getIssue((Integer)stateContainer.get("issueId"));
		Gson gson = new Gson();
		JsonElement jsonElement = gson.toJsonTree(issue);
		jsonElement.getAsJsonObject().addProperty("explainTask", "Go find out what's holding things up");
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
			xml.elm("row").attr("id", String.valueOf(id)).closeTag();
			
			xml.elm("cell").closeTag().append(sdf.format(note.getCreateTime().getTime())).closeElm("cell");
			xml.elm("cell").closeTag().append(note.getCreator()).closeElm("cell");
			xml.elm("cell").closeTag()
			.append("<![CDATA[")
			.append(note.getNoteText())
			.append("]]>")
			
			.closeElm("cell");
			
			xml.closeElm("row");
			
		}
		xml.closeElm("rows");
		
		
		w.write(xml.toString());
	}
}
