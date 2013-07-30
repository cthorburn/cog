package com.trabajo.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.apache.openjpa.persistence.jdbc.Unique;

@Entity(name = "Group")
@Table(name = "TGROUP")
public class GroupJPA implements JPACategorised<GroupJPA> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
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
		GroupJPA other = (GroupJPA) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	public GroupJPA() {
		super();
	}

	private int id;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String name;

	@Column(name = "GROUP_NAME", nullable = false, unique = true, length = 32)
	@Unique
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private String description;

	@Column(name = "DESCRIPTION", nullable = false, length = 255)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private String category;

	@Column(name = "CATEGORY", nullable = false)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Set<RoleJPA> roles;
	@ManyToMany(cascade={ CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "GROUP_ROLES", joinColumns = { @JoinColumn(name = "GR_GROUP_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "GR_ROLE_ID", referencedColumnName = "ID") })
	public Set<RoleJPA> getRoles() {
		return roles;
	}
	public void setRoles(Set<RoleJPA> roles) {
		this.roles = roles;
	}
	
	public Set<UserJPA> users;
	@ManyToMany(cascade={ CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "GROUP_USERS", joinColumns = { @JoinColumn(name = "GU_GROUP_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "GU_USER_ID", referencedColumnName = "ID") })
	public Set<UserJPA> getUsers() {
		return users;
	}
	public void setUsers(Set<UserJPA> users) {
		this.users = users;
	}
	
	public Set<RoleJPA> getRoleSet() {
		if(getRoles()==null) {
			setRoles(new HashSet<RoleJPA>());
		}
		return getRoles();
	}

	
	public Set<UserJPA> getUserSet() {
		if(getUsers()==null) {
			setUsers(new HashSet<UserJPA>());
		}
		return getUsers();
	}
	
}
