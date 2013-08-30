package com.trabajo.jpa;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.trabajo.DefinitionVersion;
import com.trabajo.process.Version;

@Embeddable
public class VersionJPA implements Comparable<VersionJPA> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getFix();
		result = prime * result + getMajor();
		result = prime * result + getMinor();
		result = prime * result + ((getShortName() == null) ? 0 : getShortName().hashCode());
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
		VersionJPA other = (VersionJPA) obj;
		if (getFix() != other.getFix())
			return false;
		if (getMajor() != other.getMajor())
			return false;
		if (getMinor() != other.getMinor())
			return false;
		if (getShortName() == null) {
			if (other.getShortName() != null)
				return false;
		} else if (!getShortName().equals(other.getShortName()))
			return false;
		return true;
	}

	public VersionJPA() {
	}

	public VersionJPA(DefinitionVersion version) {
		setMajor(version.version().getMajorVersion());
		setMinor(version.version().getMinorVersion());
		setFix(version.version().getFixVersion());
		setShortName(version.shortName());
	}

	public DefinitionVersion toDefinitionVersion() {
		return new DefinitionVersion(getShortName(), new Version(getMajor(), getMinor(), getFix()));
	}

	private String shortName;

	@Column(name = "SHORTNAME")
	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	private int major;

	@Column(name = "MAJOR")
	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	private int minor;

	@Column(name = "MINOR")
	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	private int fix;

	@Column(name = "FIX")
	public int getFix() {
		return fix;
	}

	public void setFix(int fix) {
		this.fix = fix;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(getShortName());
		sb.append('-');
		sb.append(getMajor());
		sb.append('.');
		sb.append(getMinor());
		sb.append('.');
		sb.append(getFix());

		return sb.toString();
	}

	@Override
	public int compareTo(VersionJPA o) {
		if (getMajor() > o.getMajor()) {
			return 1;
		} else if (getMajor() == o.getMajor()) {
			if (getMinor() > o.getMinor()) {
				return 1;
			} else if (getMinor() == o.getMinor()) {
				if (getFix() > o.getFix()) {
					return 1;
				} else if (getFix() == o.getFix()) {
					return 0;
				}
			}
		}
		return -1;
	}
}
