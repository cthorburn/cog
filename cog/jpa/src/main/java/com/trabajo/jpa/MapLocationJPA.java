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

	@Id	@GeneratedValue(strategy = GenerationType.IDENTITY) public int id;
	@ManyToOne @Expose(serialize=false, deserialize=false) public MapJPA map;
	
	@Column(name = "latitude", nullable = false)	public Double latitude;
	@Column(name = "longitude", nullable = false)	public Double longitude;
	@Column(name = "name", nullable = false, length=128)	public String name;

	@OneToMany(mappedBy="location", fetch=FetchType.EAGER)	public Set<MapLocationMetadataJPA> metadata;	

	@Override
	public int getId() {
		throw new UnsupportedOperationException();
	}
}
