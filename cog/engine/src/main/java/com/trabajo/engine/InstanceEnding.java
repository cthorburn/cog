package com.trabajo.engine;

import com.trabajo.ProcessCompletion;
import com.trabajo.jpa.WholeJarMetadataV1;
import com.trabajo.process.IInstance;

public class InstanceEnding {

	private IInstance instance;
	private TSession ts;
	private ProcessClassMetadata pcm;
	private WholeJarMetadataV1 wjm;
	private ClassLoader cl;
	
	public InstanceEnding(IInstance instance, TSession ts, ProcessClassMetadata pcm, WholeJarMetadataV1 wjm, ClassLoader cl) {
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
