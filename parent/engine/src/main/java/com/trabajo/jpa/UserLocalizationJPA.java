package com.trabajo.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "UserLocalization")
@Table(name = "USERLOC")
public class UserLocalizationJPA implements JPAEntity<UserLocalizationJPA> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getLangPref() == null) ? 0 : getLangPref().hashCode());
		result = prime * result + ((getLocale() == null) ? 0 : getLocale().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserLocalizationJPA other = (UserLocalizationJPA) obj;
		if (getLangPref() == null) {
			if (other.getLangPref() != null)
				return false;
		} else if (!getLangPref().equals(other.getLangPref()))
			return false;
		if (getLocale() == null) {
			if (other.getLocale() != null)
				return false;
		} else if (!getLocale().equals(other.getLocale()))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	public UserLocalizationJPA() {
		super();
	}

	private int id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String langPref;

	@Column(name = "LANGPREF", nullable = false, unique = true, length = 32)
	public String getLangPref() {
		return langPref;
	}

	public void setLangPref(String langPref) {
		this.langPref = langPref;
	}

	private String locale;

	@Column(name = "LOCALE", nullable = false, length = 2)
	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

}
