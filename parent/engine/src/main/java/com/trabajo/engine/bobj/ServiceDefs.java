package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.ServiceDefJPA;
import com.trabajo.jpa.VersionJPA;
import com.trabajo.process.IServiceDef;
import com.trabajo.values.Category;
import com.trabajo.values.Description;

public class ServiceDefs {
	
	public static IServiceDef findByVersion(EntityManager em, DefinitionVersion dv) {
		try {
			TypedQuery<ServiceDefJPA> q = em.createQuery("SELECT pd FROM ServiceDef pd WHERE pd.definitionVersion.shortName=?1 and pd.definitionVersion.major=?2 and pd.definitionVersion.minor=?3 and pd.definitionVersion.fix=?4", ServiceDefJPA.class);
			q.setParameter(1, dv.shortName());
			q.setParameter(2, dv.getMajor());
			q.setParameter(3, dv.getMinor());
			q.setParameter(4, dv.getFix());
			return  toImpl(em, q.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}

	public static DefinitionVersion latestServiceVersion(EntityManager em, String serviceName) {
		TypedQuery<VersionJPA> q = em.createQuery("SELECT sd.definitionVersion FROM ServiceDef sd WHERE sd.definitionVersion.shortName=?1", VersionJPA.class);
		
		q.setParameter(1, serviceName);
		
		VersionJPA current=null;
		for(VersionJPA v: q.getResultList()) {
			
			if(current==null) {
				current=v;
				continue;
			}
			
			if(v.compareTo(current) ==1 ) {
				current=v;
			}
		}
		
		return current.toDefinitionVersion();
	}
	
	public static IServiceDef create(EntityManager em, DefinitionVersion version, Category category, Description desc) {
		ServiceDefJPA cld = new ServiceDefJPA();
		cld.setDefinitionVersion(new VersionJPA(version));
		cld.setCategory(category.getValue());
		cld.setDescription(desc.getValue());
		em.persist(cld);
		em.flush();
		return toImpl(em, cld);
	}

	private static IServiceDef toImpl(EntityManager em, ServiceDefJPA jpa) {
		return new ServiceDefImpl(em, jpa);
	}


}
