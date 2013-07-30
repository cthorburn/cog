package com.trabajo.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity(name="Permission")
@Table(name = "ROLE_PERMISSION")
@IdClass(RolePermissionId.class)
public class RolePermissionJPA  implements JPAEntity<RolePermissionJPA> {
   
	  @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((permission == null) ? 0 : permission.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		RolePermissionJPA other = (RolePermissionJPA) obj;
		if (permission == null) {
			if (other.permission != null)
				return false;
		} else if (!permission.equals(other.permission))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

		private static final long serialVersionUID = 1L;
	  
    @Id 
    @ManyToOne
    RoleJPA role;
    
    
    @Id 
    @ManyToOne
    PermissionJPA permission;
    
    @Column(name = "VALUE", nullable = false, unique=true, length=3)
    String value; 

		@Override
		public int getId() {
			return 0;
		}
}
