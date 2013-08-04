package com.trabajo.engine.bobj;

import java.util.Map;

import javax.persistence.EntityManager;

import com.trabajo.jpa.MapLocationJPA;
import com.trabajo.process.IMapLocation;

public class MapLocationImpl extends CogEntity<MapLocationJPA> implements IMapLocation  {

	public MapLocationImpl(EntityManager em, MapLocationJPA mapLocation) {
		super(em, mapLocation);
	}

	@Override
	public void setLatitude(double lat) {
		entity().setLatitude(lat);
	}

	@Override
	public void setLongitude(double lng) {
		entity().setLongitude(lng);
		
	}

	@Override
	public Map<String, String> getMetaMap() {
		return MapLocationMetadatas.metaMap(em, this);
	}

	@Override
	public void setMetaData(String name, String value) {
		MapLocationMetadatas.setMetadata(em, this, name, value);
	}
}
