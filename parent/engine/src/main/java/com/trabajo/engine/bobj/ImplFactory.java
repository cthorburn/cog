package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;

import com.trabajo.jpa.JPAEntity;

public interface ImplFactory<T extends JPAEntity<T>, J extends JPAEntity<T>> {
		T create(J jpa, EntityManager em);
}
