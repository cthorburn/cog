package com.trabajo.jpa;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Generated Class DO NOT MODIFY
 */
@Entity(name = "SysConfig")
@Table(name = "SYSCONFIG")
public class ConfigJPA implements Serializable {

	private static final long serialVersionUID = 1L;

	public ConfigJPA() {
		super();
	}

	private String key;

	@Id
	@Column(name = "K")
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key=key;
	}

	private String value;

	@Column(name = "V", nullable = false, length = 512)
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value=value;
	}
}
