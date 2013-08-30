package com.trabajo.engine;

import java.io.Serializable;

import com.trabajo.annotation.Task;
import com.trabajo.jpa.ProcessJarAnalysis;
import com.trabajo.values.Category;
import com.trabajo.values.Description;

public class TaskSummary implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Description procDesc;
	private Description taskDesc;
	private Category    cat;
	
	public TaskSummary(ProcessClassMetadata pcm, ProcessJarAnalysis wjm, TSession ts, Class<?> taskClass) {
		procDesc=pcm.getDescription();
		taskDesc=wjm.getDescription(taskClass.getAnnotation(Task.class).name());
		cat=pcm.getCategory();
	}

	public Description getProcDesc() {
		return procDesc;
	}

	public Description getTaskDesc() {
		return taskDesc;
	}

	public Category getCat() {
		return cat;
	}
}
