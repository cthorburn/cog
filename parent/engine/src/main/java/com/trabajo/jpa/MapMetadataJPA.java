package com.trabajo.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity(name = "MapMetadata")
@Table(name = "MAPMETADATA")
public class MapMetadataJPA implements JPAEntity<MapMetadataJPA> {

	private static final long serialVersionUID = 1L;

	public MapMetadataJPA() {
		super();
	}

	
	private int id;
	
	@Override
	@Id
	@SequenceGenerator(name = "MAPMETADATA_SEQUENCE_GENERATOR", sequenceName = "MAPMETADATA_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MAPMETADATA_SEQUENCE_GENERATOR")
	@Column(name = "ID")
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
	
	private String name;
	@Column(name = "name", nullable = false, length=128)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	private String value;
	@Column(name = "value", nullable = false, length=512)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

}
