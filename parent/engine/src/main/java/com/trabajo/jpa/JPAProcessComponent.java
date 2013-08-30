package com.trabajo.jpa;

public interface JPAProcessComponent<T extends JPAEntity<T>> extends JPACategorised<T> {
	VersionJPA getDefinitionVersion();
}
