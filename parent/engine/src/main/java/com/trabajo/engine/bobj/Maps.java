package com.trabajo.engine.bobj;

import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.trabajo.jpa.MapJPA;
import com.trabajo.process.IMap;
import com.trabajo.process.IUser;

public class Maps {

	public static IMap findById(EntityManager em, long id) {
		return toImpl(em, em.find(MapJPA.class, id));
	}

	public static IMap toImpl(EntityManager em, MapJPA map) {
		return new MapImpl(em, map);
	}
	
	public static IMap create(EntityManager em, IUser user, String type) {
		MapJPA map=new MapJPA();
		map.type=type;
		map.metadata=new HashSet<>();
		map.locations=new HashSet<>();
		em.persist(map);
		em.flush();
		IMap impl=toImpl(em, map);
		impl.setMetadata("user", String.valueOf(user.getId()));
		
		return impl;
	}
	
	public static IMap findByUserAndType(EntityManager em, IUser user, String type) {
		TypedQuery<MapJPA> q=em.createQuery("select m from Map m inner join m.metadata md where md.name='user' and m.type=?1 and md.value=?2", MapJPA.class);
		
		q.setParameter(1,  type);
		q.setParameter(2,  String.valueOf(user.getId()));
		IMap map;
		
		try {
			map=toImpl(em, q.getSingleResult());
		}catch(NoResultException e){
			map=create(em, user, "user_location");
		}
		
		return map;
	}
}
