package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;

import com.trabajo.jpa.MapLocationMetadataJPA;
import com.trabajo.process.IMapLocationMetadata;

public class MapLocationMetadataImpl extends CogEntity<MapLocationMetadataJPA> implements IMapLocationMetadata  {

	public MapLocationMetadataImpl(EntityManager em, MapLocationMetadataJPA jpa) {
		super(em, jpa);
	}

	@Override
	public void setValue(String value) {
		entity().value=value;
		
	}


}
