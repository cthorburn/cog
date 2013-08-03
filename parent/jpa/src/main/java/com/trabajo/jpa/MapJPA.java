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

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;
	
	@OneToMany(mappedBy="map", fetch=FetchType.EAGER)	public Set<MapLocationJPA> locations;	
	
	@OneToMany(mappedBy="map", cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)	public Set<MapMetadataJPA> metadata;	

	@Column(name = "TYPE", nullable=true, length=128)	public String type;

	@Override
	public int getId() {
		return id;
	}
}
