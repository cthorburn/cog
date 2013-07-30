package com.anyone;

public interface Issues {

	Issue createIssue(int id, String title, String description, String application, String reproduce, Impact impact, boolean internal);

	String getGridXML();

	Issue getIssue(Integer integer);
	
	void close();

}
