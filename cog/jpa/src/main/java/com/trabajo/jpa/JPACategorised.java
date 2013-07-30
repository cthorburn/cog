package com.trabajo.jpa;

public interface JPACategorised<T extends JPAEntity<T>> extends JPAEntity<T> {
	String getName();
	String getCategory();
	String getDescription();
	void setCategory(String value);
	void setDescription(String value);
}
