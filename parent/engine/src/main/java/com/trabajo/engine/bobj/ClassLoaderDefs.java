package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.ClassLoaderDefJPA;

public class ClassLoaderDefs {
	
	public static ClassLoaderDefJPA findByVersion(EntityManager em, DefinitionVersion dv) {
		try {
			Query q = em.createQuery("SELECT pd FROM ClassLoaderDef pd WHERE pd.definitionVersion.shortName=?1 and pd.definitionVersion.major=?2 and pd.definitionVersion.minor=?3 and pd.definitionVersion.fix=?4", ClassLoaderDefJPA.class);
			q.setParameter(1, dv.shortName());
			q.setParameter(2, dv.getMajor());
			q.setParameter(3, dv.getMinor());
			q.setParameter(4, dv.getFix());
			return (ClassLoaderDefJPA) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
