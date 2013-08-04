package com.trabajo.jpa;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity(name = "MapLocation")
@Table(name = "MAP_LOCATION")
public class MapLocationJPA implements JPAEntity<MapLocationJPA> {

	private static final long serialVersionUID = 1L;

	public MapLocationJPA() {
		super();
	}

	private int id;
	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	@Expose(serialize=false, deserialize=false)
	private MapJPA map;
	@ManyToOne 
	public MapJPA getMap() {
		return map;
	}
	public void setMap(MapJPA map) {
		this.map = map;
	}

	private Double latitude;
	@Column(name = "latitude", nullable = false)
	public Double getLatitude() {
		return latitude;
	}
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	private Double longitude;
	@Column(name = "longitude", nullable = false)
	public Double getLongitude() {
		return longitude;
	}
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	private String name;
	@Column(name = "name", nullable = false, length=128)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Set<MapLocationMetadataJPA> metadata;
	@OneToMany(mappedBy="location", fetch=FetchType.EAGER)
	public Set<MapLocationMetadataJPA> getMetadata() {
		return metadata;
	}
	public void setMetadata(Set<MapLocationMetadataJPA> metadata) {
		this.metadata = metadata;
	}	

}
