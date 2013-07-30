package com.trabajo.engine.bobj;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.trabajo.jpa.MapLocationJPA;
import com.trabajo.jpa.MapLocationMetadataJPA;
import com.trabajo.process.IMapLocation;
import com.trabajo.process.IMapLocationMetadata;

public class MapLocationMetadatas {

	public static Map<String, String> metaMap(EntityManager em, IMapLocation ml) {
		Map<String, String> metaMap=new HashMap<String, String>();
		
		TypedQuery<MapLocationMetadataJPA> q=em.createQuery("select mlm from MapLocationMetadata mlm where mlm.location=?1", MapLocationMetadataJPA.class);
		
		q.setParameter(1, ml.entity());
		
		for(MapLocationMetadataJPA mlm: q.getResultList()) {
			metaMap.put(mlm.name, mlm.value);
		}
		return Collections.unmodifiableMap(metaMap);
	}

	public static void setMetadata(EntityManager em, IMapLocation ml, String name, String value) {
		IMapLocationMetadata mlm=findByLocationAndName(em, ml, name);
		
		if(mlm==null) {
			mlm=create(em, ml, name, value);
		}
		else {
			mlm.setValue(value);
		}
	}

	private static IMapLocationMetadata create(EntityManager em, IMapLocation ml, String name, String value) {
		MapLocationMetadataJPA jpa=new MapLocationMetadataJPA();
		MapLocationJPA mlJpa=(MapLocationJPA)ml.entity();
		jpa.location=mlJpa;
		jpa.name=name;
		jpa.value=value;
		em.persist(jpa);
		em.flush();
		
		
		mlJpa.metadata.add(jpa);
		
		return toImpl(em, jpa);		
	}

	private static IMapLocationMetadata findByLocationAndName(EntityManager em, IMapLocation ml, String name) {
		TypedQuery<MapLocationMetadataJPA> q=em.createQuery("select mlm from MapLocationMetadata mlm where mlm.location=?1 and mlm.name=?2", MapLocationMetadataJPA.class);
		
		q.setParameter(1, ml.entity());
		q.setParameter(2, name);
		IMapLocationMetadata result=null;
		try {
			result=toImpl(em, q.getSingleResult());
		}catch(NoResultException nre){
			//ignore
		}
		
		return result;
	}

	private static IMapLocationMetadata toImpl(EntityManager em, MapLocationMetadataJPA jpa) {
		return new MapLocationMetadataImpl(em, jpa) ;
	}

}
