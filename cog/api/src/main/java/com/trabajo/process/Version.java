package com.trabajo.process;

import java.io.Serializable;
import java.util.StringTokenizer;

import com.trabajo.AppVersion;

/**
 * Created by IntelliJ IDEA.
 * User: colin
 * Date: 12-Apr-2008
 * Time: 12:55:52
 */
public class Version implements Comparable<Version>, Serializable {
  private static final long serialVersionUID = AppVersion.MAJOR;

  private int majorVersion;
  private int minorVersion;
  private int fixVersion;

  public Version(int majorVersion, int minorVersion, int fixVersion ) {

    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
    this.fixVersion = fixVersion;
  }

  public int getMajorVersion() {
    return majorVersion;
  }

  public int getMinorVersion() {
    return minorVersion;
  }

  public int getFixVersion() {
    return fixVersion;
  }

  @Override public String toString()
  {
    return new StringBuilder()
          .append(majorVersion)
          .append('.')
          .append(minorVersion)
          .append('.')
          .append(fixVersion)
          .toString();
  }

 
  @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fixVersion;
		result = prime * result + majorVersion;
		result = prime * result + minorVersion;
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
		Version other = (Version) obj;
		if (fixVersion != other.fixVersion)
			return false;
		if (majorVersion != other.majorVersion)
			return false;
		if (minorVersion != other.minorVersion)
			return false;
		return true;
	}

	public static Version parse(String v) {
		return parse(v, ".");
	}
	
	public static Version parse(String v, String delim) {

		StringTokenizer st = new StringTokenizer(v, delim);

		int vMaj = Integer.parseInt(st.nextToken());
		int vMin = Integer.parseInt(st.nextToken());
		int vFix = Integer.parseInt(st.nextToken());

		return new Version(vMaj, vMin, vFix);
	}

	@Override
	public int compareTo(Version o) {
		if(majorVersion > o.majorVersion) {
			return 1;
		} else if(majorVersion == o.majorVersion) {
			if(minorVersion > o.minorVersion) {
				return 1;
			} else if(minorVersion == o.minorVersion) {
				if(fixVersion > o.fixVersion) {
					return 1;
				} else if(fixVersion == o.fixVersion) {
					return 0;
				}
			}
		}
		return -1;
	}
}
