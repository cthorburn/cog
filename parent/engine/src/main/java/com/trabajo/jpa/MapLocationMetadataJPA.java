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

@Entity(name = "MapLocationMetadata")
@Table(name = "MAPLOCATIONMETADATA")
public class MapLocationMetadataJPA implements JPAEntity<MapLocationMetadataJPA> {

	private static final long serialVersionUID = 1L;

	public MapLocationMetadataJPA() {
		super();
	}

	private int id;
	
	@Id
	@SequenceGenerator(name = "MAPLOCATIONMETADATA_SEQUENCE_GENERATOR", sequenceName = "MAPLOCATIONMETADATA_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MAPLOCATIONMETADATA_SEQUENCE_GENERATOR")
	@Column(name = "ID")

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Expose(serialize=false, deserialize=false)
	private MapLocationJPA location;
	@ManyToOne
	public MapLocationJPA getLocation() {
		return location;
	}
	public void setLocation(MapLocationJPA location) {
		this.location = location;
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
