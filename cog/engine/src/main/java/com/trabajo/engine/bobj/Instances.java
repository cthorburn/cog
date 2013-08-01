package com.trabajo.engine.bobj;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.InstanceJPA;
import com.trabajo.process.IInstance;

public class Instances {

	public static IInstance toImpl(EntityManager em, InstanceJPA instance) {
		return instance==null ? null:new InstanceImpl(em, instance);
	}

	public static boolean allTasksComplete(EntityManager em, IInstance instance) {
		TypedQuery<Long> q=em.createQuery("select count(n) from Node n where n.instance=?1 and n.complete IS NULL", Long.class); 
		q.setParameter(1,  instance.entity());
		return q.getSingleResult()==0L;
	}

	public static Set<IInstance> findByVersion(EntityManager em, DefinitionVersion dv) {
		TypedQuery<InstanceJPA> q=em.createQuery("select i from Instance i inner join i.procDef pd where pd.definitionVersion.shortName=?1 and pd.definitionVersion.major=?2 and pd.definitionVersion.minor=?3 and pd.definitionVersion.fix=?4", InstanceJPA.class); 
		q.setParameter(1,  dv.shortName());
		q.setParameter(2,  dv.getMajor());
		q.setParameter(3,  dv.getMinor());
		q.setParameter(4,  dv.getFix());
		return toImpl(em, q.getResultList());
	}

	public static Set<IInstance> toImpl(EntityManager em, List<InstanceJPA> set) {
		Set<IInstance> impls = new HashSet<>(set.size());
		for (InstanceJPA s : set) {
			impls.add(new InstanceImpl(em, s));
		}
		return impls;
	}

	public static IInstance findById(EntityManager em, int id) {
		return toImpl(em, em.find(InstanceJPA.class, id));
	}
	
}
