package com.trabajo.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;

@Entity(name = "MapLocationMetadata")
@Table(name = "MAPLOCATIONMETADATA")
public class MapLocationMetadataJPA implements JPAEntity<MapLocationMetadataJPA> {

	private static final long serialVersionUID = 1L;

	public MapLocationMetadataJPA() {
		super();
	}

	@Id		@GeneratedValue(strategy = GenerationType.IDENTITY) public int id;
	@ManyToOne @Expose(serialize=false, deserialize=false) public MapLocationJPA location;
	
	@Column(name = "name", nullable = false, length=128)	public String name;
	@Column(name = "value", nullable = false, length=512)	public String value;

	@Override
	public int getId() {
		throw new UnsupportedOperationException();
	}
}
