package com.trabajo.jpa;

import java.io.Serializable;

public class RolePermissionId implements Serializable {
	
  private static final long serialVersionUID = 1L;
	
  public int role;
	public int permission;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + permission;
		result = prime * result + role;
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
		RolePermissionId other = (RolePermissionId) obj;
		if (permission != other.permission)
			return false;
		if (role != other.role)
			return false;
		return true;
	}
}
