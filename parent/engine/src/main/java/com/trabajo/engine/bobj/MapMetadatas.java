package com.trabajo.engine.bobj;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.trabajo.jpa.MapJPA;
import com.trabajo.jpa.MapMetadataJPA;
import com.trabajo.process.IMap;
import com.trabajo.process.IMapMetadata;

public class MapMetadatas {

	public static IMapMetadata create(EntityManager em, IMap map, String name, String value) {
		MapMetadataJPA mm=new MapMetadataJPA();
		MapJPA mapJpa=((MapJPA)map.entity());
		mm.setMap((MapJPA)map.entity());
		mm.setName(name);
		mm.setValue(value);
		mapJpa.getMetadata().add(mm);
		em.persist(mm);
		em.flush();
		return toImpl(em, mm);
	}

	public static IMapMetadata ensure(EntityManager em, IMap map, String name, String value) {
		Map<String, IMapMetadata> metaMap=findByMap(em, map);
		IMapMetadata meta;
		
		if(metaMap.containsKey(name)) {
			meta=metaMap.get(name);
			meta.setValue(value);
			meta.save();
		}
		else {
			meta=create(em, map, name, value);
		}
		return meta;
	}
	
	public static Map<String, IMapMetadata> findByMap(EntityManager em, IMap map) {
		Map<String, IMapMetadata> result=new HashMap<>();
		TypedQuery<MapMetadataJPA> q=em.createQuery("select mm from MapMetadata mm where mm.map=?1", MapMetadataJPA.class);

		q.setParameter(1, map.entity());
		
		for(MapMetadataJPA mm: q.getResultList()) {
			result.put(mm.getName(), toImpl(em, mm));
		}
		
		return result;
	}

	private static IMapMetadata toImpl(EntityManager em, MapMetadataJPA mm) {
		return new MapMetadataImpl(em, mm);
	}

}
