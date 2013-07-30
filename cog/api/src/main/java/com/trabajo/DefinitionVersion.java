package com.trabajo;

import java.io.Serializable;

import com.trabajo.process.Version;
import com.trabajo.vcl.CLMKey;

public class DefinitionVersion implements CLMKey<DefinitionVersion>, Serializable {

	private static final long serialVersionUID = AppVersion.MAJOR;
	
	private String shortName;
	private Version version;

	public DefinitionVersion() {}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((shortName == null) ? 0 : shortName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		DefinitionVersion other = (DefinitionVersion) obj;
		if (shortName == null) {
			if (other.shortName != null)
				return false;
		} else if (!shortName.equals(other.shortName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		return true;
	}

	public String shortName() {
		return shortName;
	}

	public Version version() {
		return version;
	}

	public DefinitionVersion(String shortName, Version version) {
		if (shortName == null || version == null)
			throw new IllegalArgumentException("null constructor args");

		this.shortName = shortName;
		this.version = version;
	}
	
	public static DefinitionVersion parse(final String s) {
		String shortName = parseShortName(s);
		String version = parseVersionSpecifier(s);

		return new DefinitionVersion(shortName, Version.parse(version));
	}

	public static String parseShortName(String s) {
		return s.substring(0, s.lastIndexOf("-"));
	}

	public static String parseVersionSpecifier(String s) {
			return s.substring(s.lastIndexOf("-") + 1);
	}

	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		
		sb.append(shortName());
		sb.append('-');
		sb.append(version().getMajorVersion());
		sb.append('.');
		sb.append(version().getMinorVersion());
		sb.append('.');
		sb.append(version().getFixVersion());
		
		return sb.toString();	
	}

	public int getMajor() {
		return version.getMajorVersion();
	}
	public int getMinor() {
		return version.getMinorVersion();
	}
	public int getFix() {
		return version.getFixVersion();
	}

	@Override
	public String toPathString() {
		return toString();
	}

	@Override
	public int compareTo(DefinitionVersion o) {
		if(shortName.equals(o.shortName)) {
			return version.compareTo(o.version);
		}
		else {
			return shortName.compareTo(o.shortName);
		}
	}
}
