package com.trabajo.jpa;

import java.io.Serializable;

public interface JPAEntity<T extends JPAEntity<T>> extends  Serializable {
	int getId();
}
