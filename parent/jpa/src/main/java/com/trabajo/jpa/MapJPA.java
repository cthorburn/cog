package com.trabajo.jpa;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity(name = "Map")
@Table(name = "MAP")
public class MapJPA implements JPAEntity<MapJPA> {

	private static final long serialVersionUID = 1L;

	public MapJPA() {
		super();
	}

	private int id;
	@Override
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	private Set<MapLocationJPA> locations;	
	@OneToMany(mappedBy="map", fetch=FetchType.EAGER)	
	public Set<MapLocationJPA> getLocations() {
		return locations;
	}
	public void setLocations(Set<MapLocationJPA> locations) {
		this.locations = locations;
	}

	private Set<MapMetadataJPA> metadata;
	@OneToMany(mappedBy="map", cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)	
	public Set<MapMetadataJPA> getMetadata() {
		return metadata;
	}
	public void setMetadata(Set<MapMetadataJPA> metadata) {
		this.metadata = metadata;
	}

	private String type;
	@Column(name = "TYPE", nullable=true, length=128)	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}




}
