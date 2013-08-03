package com.trabajo.engine;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.WholeJarMetadataV1;
import com.trabajo.vcl.ClassLoaderManager;

public class SandBoxFactory   {

	private ClassLoaderManager<DefinitionVersion, DVFactory> clm;
	
	public ClassLoaderManager<DefinitionVersion, DVFactory> getClm() {
		return clm;
	}

	public SandBoxFactory(ClassLoaderManager<DefinitionVersion, DVFactory> clm) {
		super();
    this.clm = clm;
	}

  public Sandbox newSandbox(TSession ts, ProcessClassMetadata pcm, WholeJarMetadataV1 wjm) {
	    Sandbox sandbox = new SandBoxImpl(ts, clm);
	    sandbox.setMetadata(pcm, wjm);
	    return sandbox;
	}	
}
