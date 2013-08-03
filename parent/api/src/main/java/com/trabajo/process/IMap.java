package com.trabajo.process;


public interface IMap extends IEntity {

	String toJson();

	void setMetadata(String name, String value);
	void setLocation(String location, double lat, double lng);
	void setLocationMeta(String location, String name, String value);

	IMapLocation getOrCreateLocation(String location, double lat, double lng);

}
