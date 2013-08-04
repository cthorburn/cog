package com.trabajo.engine.bobj;

import java.util.Map;

import javax.persistence.EntityManager;

import com.trabajo.jpa.MapJPA;
import com.trabajo.jpa.MapLocationJPA;
import com.trabajo.jpa.MapLocationMetadataJPA;
import com.trabajo.jpa.MapMetadataJPA;
import com.trabajo.process.IMap;
import com.trabajo.process.IMapLocation;

public class MapImpl extends CogEntity<MapJPA> implements IMap {

	public MapImpl(EntityManager em, MapJPA map) {
		super(em, map);
	}

	public Map<String, IMapLocation> getLocations() {
		return MapLocations.findByMap(em, this);
	}
	
	@Override 
	public String toJson() {
	//TODO this blows up for some reason.lloks like circularity but it should not be since we donlt serialize entity references	
	//return new Gson().toJson(entity());

		//doing this by hand is pain in the arse! Get Gson working for this type!
		StringBuilder sb=new StringBuilder();
		
		sb.append("{ \"id\": ");
		sb.append(entity().getId());
		sb.append(", \"locations\": {");
		
		for(MapLocationJPA ml: entity().getLocations()) {
			sb.append('"');
			sb.append(ml.getName());
			sb.append("\": {");
			sb.append("\"latitude\": ");
			sb.append(ml.getLatitude());
			sb.append(",");
			sb.append("\"longitude\": ");
			sb.append(ml.getLongitude());
			sb.append(", \"metadata\": {");
			for(MapLocationMetadataJPA mlm: ml.getMetadata()) {
				sb.append('"');
				sb.append(mlm.getName());
				sb.append("\": \"");
				sb.append(mlm.getValue());
				sb.append("\", ");
			}	
			if(ml.getMetadata().size() > 0)
				sb.setLength(sb.length()-2);
			sb.append("}");
			sb.append("}, ");
		}
		if(entity().getLocations().size() > 0)
			sb.setLength(sb.length()-2);
		sb.append("}, ");
		
		sb.append("\"metadata\": {");
		
		for(MapMetadataJPA mm: entity().getMetadata()) {
			sb.append('"');
			sb.append(mm.getName());
			sb.append("\": \"");
			sb.append(mm.getValue());
			sb.append("\", ");
		}	
		if(entity().getMetadata().size() > 0)
			sb.setLength(sb.length()-2);
		
		sb.append("}}");

		return sb.toString();
	}
	@Override
	public void setLocation(String location, double lat, double lng) {
		Map<String, IMapLocation> locations=getLocations();
		IMapLocation ml=locations.get(location);
		
		if(ml==null) {
			ml=MapLocations.create(em, this, location, lat, lng);
		}
		else {
			ml.setLatitude(lat);
			ml.setLongitude(lng);
		}
	}	

	@Override
	public void setLocationMeta(String location, String name, String value) {
		Map<String, IMapLocation> locations=getLocations();
		IMapLocation ml=locations.get(location);
		
		if(ml==null) {
			throw new IllegalArgumentException("no such location");
		}	
		ml.getMetaMap().put(name, value);
	}

	@Override
	public void setMetadata(String name, String value) {
		MapMetadatas.ensure(em, this, name, value);
	}

	@Override
	public IMapLocation getOrCreateLocation(String location, double lat, double lng) {
		return MapLocations.ensure(em, this, location, lat, lng);
	}
}
