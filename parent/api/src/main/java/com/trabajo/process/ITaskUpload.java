package com.trabajo.process;

public interface ITaskUpload extends IEntity{

	String getPath();
	String getDocSet();
	String getOriginalName();

	boolean getVersioned();
	int getVersion();
}
