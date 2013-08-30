package com.trabajo.engine;

import com.trabajo.ProcessCompletion;
import com.trabajo.jpa.ProcessJarAnalysis;
import com.trabajo.process.IInstance;

public class InstanceEnding {

	private IInstance instance;
	@SuppressWarnings("unused")
	private TSession ts;
	@SuppressWarnings("unused")
	private ProcessClassMetadata pcm;
	@SuppressWarnings("unused")
	private ProcessJarAnalysis wjm;
	@SuppressWarnings("unused")
	private ClassLoader cl;
	
	public InstanceEnding(IInstance instance, TSession ts, ProcessClassMetadata pcm, ProcessJarAnalysis wjm, ClassLoader cl) {
		super();
		this.ts = ts;
		this.pcm = pcm;
		this.wjm = wjm;
		this.cl = cl;
		this.instance = instance;
	}

	public void end() {
		instance.end(ProcessCompletion.OK);
		processEndRelevantAnnotations();
	}

	private void processEndRelevantAnnotations() {
		// TODO Auto-generated method stub
		
	}
}
