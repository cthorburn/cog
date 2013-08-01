package com.anyone;

import static com.trabajo.annotation.StandardLifecycle.COMPLETE;
import static com.trabajo.annotation.StandardLifecycle.START;

import java.util.Map;

import com.trabajo.DeadlineTimescale;
import com.trabajo.ILifecycle;
import com.trabajo.IVisualizer;
import com.trabajo.ProcessException;
import com.trabajo.StateContainer;
import com.trabajo.annotation.ACog;
import com.trabajo.annotation.AInstance;
import com.trabajo.annotation.Activation;
import com.trabajo.annotation.ClassLoaderHierarchy;
import com.trabajo.annotation.ControlType;
import com.trabajo.annotation.Development;
import com.trabajo.annotation.Form;
import com.trabajo.annotation.FormItem;
import com.trabajo.annotation.Forms;
import com.trabajo.annotation.Lifecycle;
import com.trabajo.annotation.OnCompletion;
import com.trabajo.annotation.Process;
import com.trabajo.annotation.ProcessState;
import com.trabajo.annotation.Service;
import com.trabajo.annotation.StartupForm;
import com.trabajo.annotation.Visualizer;
import com.trabajo.process.IInstance;
import com.trabajo.process.ITask;

//Annotations tell the container a lot about this process without writing any code as such!
//order is unimportant
//The IDE will always tell you what is and what is not allowed on a certain annotation
//the Javadoc lists what's available
//No XML, or other resources of any kind are required, just java classes.


//(NOT MANDATORY) During development we want to blat old stuff that didn't quite work. -- TODO need a system timer in order to process this  
@Development(removePreviousVersions=true)

//(MANDATORY)
//Give our process a name and version to distinguish it.
//Give it some initial categorisation so that we can place see it in the process tree view.
//Give it a bit of description so that we know roughly what it does.
//We can't change the name or version once deployed, but we can modify the category and description later if desired
@Process(	name = "bugfix", version = "1.0.46", category = "software.development", description = "Manage a software issue")

//(NOT MANDATORY) Do we really want to keep all the database records 
//concerning delivery of tea and biscuits to meeting rooms? I think not.  
//This is just the default action. It's specified here as a reminder only. 
//Expunge all the process infomation once the proces is complete.
//Metric information is not deleted so we can still monitor the performance of the tea ladies just as effectively.  
@OnCompletion( erase=true )  

//TODO need a system timer in order to process this
//When deployed into production don't make available until 'at'.
//From then on, any new invocation of Bugfix will start this version.
//At that point prevent users from starting older versions (these are probably filtered from views anyway but they won't be from web services)
@Activation(deprecatePreviousVersions=true, at="2013-07-11 17:00:00")

//This process relies on the Google Gson classloader and the "issues" service. 
//Actually it doesn't REALLY rely on Google Gson it's just for show
@ClassLoaderHierarchy({ "gson-2.2.2", "issues-1.1.12" })


//Defines a dynamically built form. It does what you think it does. 
@Forms({ 
	@Form(name = "startup", dataItems = { @FormItem(type = ControlType.INPUT, name = "title", label = "Issue Title"),
			@FormItem(type = ControlType.INPUT, name = "description", rows = 10, label = "Issue Description", value = "describe the problem!"),
			@FormItem(type = ControlType.INPUT, name = "application", label = "Application"),
			@FormItem(type = ControlType.INPUT, name = "reproduce", rows = 10, label = "How to reproduce"),
			@FormItem(type = ControlType.SELECT, name = "impact", label = "Impact", value = "LOW, Low, MEDIUM, Medium, HIGH, High"),
			@FormItem(type = ControlType.CHECKBOX, name = "internal", label = "Raised internally?")}, 
    title = "New Issue", 
    actionButtons = {} // it is an error to specify an action button for a process startup form. We add a Big Green One automatically.
	)}
)
//the form presented to the user when they start the process. Captures the initial startup parameters obviously!
@StartupForm("startup")
public class RaiseBug {

	
	//(MANDATORY) ILifecycle lets us do things like create tasks  
	@ACog	private ILifecycle cog; // Inject provides the commonly used dependencies
	
	//(NOT MANDATORY) Ah, we don't have to do anything to get our service, we just say 'put it here'. we need to know it implements the Issues interface but that's it! 
	@Service(name = "issues")	private Issues service;
	
	
	//(MANDATORY) we use things to remember stuff, it's basically just a map    
	@ProcessState	private StateContainer state;
	
	//(MANDATORY) This is the object that represents a specific invocation of a process: 
	//you can run the same (process/version)  any number of times at the same time
	//you can run the same (process/different versions)  any number of times at the same time
	//This keeps track of which is which.
	@AInstance IInstance instance;
	
	//(NOT MANDATORY) This is what you use to define the process graph
	@Visualizer IVisualizer viz;
	
	
	//(MANDATORY) Starting a process needs something to run phase = START defines this method as that bit of code. 
	//We pass it the parameters the user entered on the startup form.  
	@Lifecycle(phase = START)
	public void start(Map<String, String> parms) throws ProcessException {

		//It sets up the task list status graph exactly how it reads.
		//This example currently uses every option available but that list will grow in the future.  
		viz.instance().creates("investigate");
		viz.task("investigate").creates("environment", "reproduce");
		viz.task("investigate").canEndProcess();
		viz.task("environment").dependsOn("investigate");
		viz.task("environment").creates("escalate");
		viz.task("reproduce").dependsOn("environment");
		viz.task("reproduce").creates("fix");
		viz.task("reproduce").canEndProcess();
		viz.task("fix").dependsOn("reproduce");
		viz.task("fix").canEndProcess();
		
		//Issue is an object defined by the issues service. we use it later.
		Issue issue;
		
		//TODO Need to be able to inject pre-validated startup parms like we do with task callbacks!
		// to eliminate the need for all this parm parsing code
		try{
			//If we have decent startup info..
			String title = parms.get("title");
			String description = parms.get("description");
			String reproduce = parms.get("reproduce");
			String application = parms.get("application");
			Impact impact= Impact.valueOf(parms.get("impact"));
			boolean internal = "1".equals(parms.get("internal"));
			try {
				//then create an issue record in the exteranl issues system..
				issue = service.createIssue(instance.getId(), title, description, application, reproduce, impact, internal);
			} catch (Exception e) {
				//otherwise, don't finish staring this process and generate a message for the bottom status bar instead.
				cog.abandon("Error validating supplied startup data");
				return;
			}
		}catch(Exception e){
			//If the startup parameters were really crappy, don't finish staring this process and generate a message for the bottom status bar instead.
			cog.abandon("bad parameters");
			return;
		}

		//All is well, keep reference to the issue we just created   
		state.put("issueId", issue.getId());
		
		//...and CREATE A TASK for someone to start investigating this problem
		//notice how we inform the creation of that task with the issue id!
		//Actual tasks are defined in classes of their own.
		ITask task = cog.createTask("investigate", state);
		
		//Show this task only to folks deemed appropriate to perform it
		//hopefully one of them will volunteer to do it.
		task.assignToRoles("service_level_1");
		
		//we could use
		//task.assignToUser("Alice");
		//If Alice leaves the company, get's hit by a bus or is just plain swamped with every bugfix... you can see where this is going. 
		
		//In a perfect world we would assume that someone volunteered to do this task.
		//In reality, it's going to be left sitting on the role task list every so often.
		//We say here (just notionally for the time being, nothing actually happens) 
		task.setDeadline(1, DeadlineTimescale.DAYS);
	}
	
	//(NOT MANDATORY) once all tasks created by this process are complete  
	@Lifecycle(phase = COMPLETE)
	public void end() throws ProcessException {
	
	}
}
