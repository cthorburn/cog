package com.anyone;

import java.util.List;

public interface Issue {

	int getId();

	String getTitle();

	String getDescription();

	String getApplication();

	String getReproduce();

	Impact getImpact();

	boolean isInternal();

	Note createNote(String user, String noteText);
	List<Note> getNotes();

	void insert();

	void setDuplicateOf(Integer existingId);
}
