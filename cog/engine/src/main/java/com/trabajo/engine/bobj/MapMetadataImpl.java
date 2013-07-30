package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;

import com.trabajo.jpa.MapMetadataJPA;
import com.trabajo.process.IMapMetadata;

public class MapMetadataImpl extends CogEntity<MapMetadataJPA> implements IMapMetadata {

	public MapMetadataImpl(EntityManager em, MapMetadataJPA jpa) {
		super(em, jpa);
	}

	@Override
	public void setValue(String value) {
		entity().value=value;
	}
}
