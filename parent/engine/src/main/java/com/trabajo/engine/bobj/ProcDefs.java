package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.ProcDefJPA;
import com.trabajo.process.IProcDef;


public class ProcDefs {
	
	public static IProcDef toImpl(EntityManager em, ProcDefJPA procDef) {
		return procDef==null ? null:new ProcDefImpl(em, procDef);
	}

	public static IProcDef findById(EntityManager em, long id) {
		return toImpl(em, em.find(ProcDefJPA.class, id));
	}

	public static IProcDef findByVersion(EntityManager em, DefinitionVersion dv) {
		try {
			TypedQuery<ProcDefJPA> q = em.createQuery("SELECT pd FROM ProcDef pd WHERE pd.definitionVersion.shortName=?1 and pd.definitionVersion.major=?2 and pd.definitionVersion.minor=?3 and pd.definitionVersion.fix=?4", ProcDefJPA.class);
			q.setParameter(1, dv.shortName());
			q.setParameter(2, dv.getMajor());
			q.setParameter(3, dv.getMinor());
			q.setParameter(4, dv.getFix());
			return toImpl(em, q.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}
}
