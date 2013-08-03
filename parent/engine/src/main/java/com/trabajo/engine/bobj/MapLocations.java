package com.trabajo.engine.bobj;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import com.trabajo.jpa.MapJPA;
import com.trabajo.jpa.MapLocationJPA;
import com.trabajo.process.IMap;
import com.trabajo.process.IMapLocation;

public class MapLocations {

	public static IMapLocation findById(EntityManager em, long id) {
		return toImpl(em, em.find(MapLocationJPA.class, id));
	}

	public static IMapLocation toImpl(EntityManager em, MapLocationJPA mapLocation) {
		return new MapLocationImpl(em, mapLocation);
	}
	
	public static Map<String, IMapLocation> findByMap(EntityManager em, IMap map) {
		Map<String, IMapLocation> result=new HashMap<>();
		TypedQuery<MapLocationJPA> q=em.createQuery("select ml from MapLocation ml where ml.map=?1", MapLocationJPA.class);

		q.setParameter(1, map.entity());
		
		for(MapLocationJPA ml: q.getResultList()) {
			result.put(ml.name, MapLocations.toImpl(em, ml));
		}
		
		return result;
	}

	public static IMapLocation create(EntityManager em, IMap map, String location, double lat, double lng) {
		MapJPA mapJpa=(MapJPA)map.entity();
		MapLocationJPA jpa=new MapLocationJPA();
		jpa.latitude=lat;
		jpa.longitude=lng;
		jpa.name=location;
		jpa.metadata=new HashSet<>();
		jpa.map=mapJpa;
		mapJpa.locations.add(jpa);
		em.persist(jpa);
		
		return toImpl(em, jpa);
	}

	public static IMapLocation ensure(EntityManager em, IMap map, String location, double lat, double lng) {
		Map<String, IMapLocation> mapLocs=findByMap(em, map);
		IMapLocation result=mapLocs.get(location);
		
		if(result==null) {
			result=create(em, map, location, lat, lng); 
		}
		return result;
	}

}
