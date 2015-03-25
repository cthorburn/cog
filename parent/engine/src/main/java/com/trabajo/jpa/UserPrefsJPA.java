package com.trabajo.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.apache.openjpa.persistence.jdbc.Unique;

@Entity(name = "UserPrefs")
@Table(name = "USERPREFS")
public class UserPrefsJPA implements JPAEntity<UserPrefsJPA> {

	private static final long serialVersionUID = 1L;

	public UserPrefsJPA() {
		super();
	}

	@Id
	@SequenceGenerator(name = "USERPREFS_SEQUENCE_GENERATOR", sequenceName = "USERPREFS_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USERPREFS_SEQUENCE_GENERATOR")
	@Column(name = "ID")
	private int id;

	@Column(name = "skin", nullable = true, length = 32)
	@Unique
	private String skin;

	@OneToOne
	private MapJPA userMap;

	public int getId() {
		throw new UnsupportedOperationException();
	}

	public String getSkin() {
		return skin;
	}

	public void setSkin(String skin) {
		this.skin = skin;
	}

}
