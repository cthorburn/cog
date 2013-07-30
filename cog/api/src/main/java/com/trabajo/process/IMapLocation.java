package com.trabajo.process;

import java.util.Map;

public interface IMapLocation extends IEntity  {

	void setLatitude(double lat);
	void setLongitude(double lng);
	Map<String, String> getMetaMap();
	void setMetaData(String name, String value);

}
