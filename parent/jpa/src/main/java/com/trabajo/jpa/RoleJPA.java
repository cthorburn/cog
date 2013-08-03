package com.trabajo.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.openjpa.persistence.jdbc.Unique;

import com.google.gson.annotations.Expose;

@Entity(name = "Role")
@Table(name = "ROLE")
public class RoleJPA implements JPACategorised<RoleJPA> {

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
		RoleJPA other = (RoleJPA) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	public RoleJPA() {
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

	@Column(name = "ROLE_NAME", nullable = false, unique = true, length = 32)
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

	@Expose(serialize=false, deserialize=false)
	private Set<UserJPA> users;
	@ManyToMany(mappedBy = "roles", cascade={ CascadeType.PERSIST, CascadeType.MERGE } )
	public Set<UserJPA> getUsers() {
		return users;
	}
	public void setUsers(Set<UserJPA> users) {
		this.users = users;
	}

	@Expose(serialize=false, deserialize=false)
	private Set<NodeJPA> tasks;
	@ManyToMany(mappedBy = "roles", cascade={ CascadeType.PERSIST, CascadeType.MERGE })
	public Set<NodeJPA> getTasks() {
		return tasks;
	}
	public void setTasks(Set<NodeJPA> tasks) {
		this.tasks = tasks;
	}
	
	@Expose(serialize=false, deserialize=false)
	private Set<GroupJPA> groups;
	@ManyToMany(mappedBy = "roles", cascade={ CascadeType.PERSIST, CascadeType.MERGE })
	public Set<GroupJPA> getGroups() {
		return groups;
	}
	public void setGroups(Set<GroupJPA> groups) {
		this.groups = groups;
	}
	
	
	@Expose(serialize=false, deserialize=false)
	public Set<RolePermissionJPA> rolePermissions;
	@OneToMany(cascade=CascadeType.PERSIST)
	public Set<RolePermissionJPA> getRolePermissions() {
		return rolePermissions;
	}
	public void setRolePermissions(Set<RolePermissionJPA> rolePermissions) {
		this.rolePermissions = rolePermissions;
	}
	
	public Set<NodeJPA> getTaskSet() {
		Set<NodeJPA> s=getTasks();
		if(s==null) {
			s=new HashSet<NodeJPA>();
			setTasks(s);
		}
		return getTasks();
	}
	public Set<UserJPA> getUserSet() {
		Set<UserJPA> s=getUsers();
		if(s==null) {
			s=new HashSet<UserJPA>();
			setUsers(s);
		}
		return getUsers();
	}

	public Set<GroupJPA> getGroupSet() {
		Set<GroupJPA> s=getGroups();
		if(s==null) {
			s=new HashSet<GroupJPA>();
			setGroups(s);
		}
		return getGroups();
	}

	public Set<RolePermissionJPA> getRolePermissionSet() {
		Set<RolePermissionJPA> s=getRolePermissions();
		if(s==null) {
			s=new HashSet<RolePermissionJPA>();
			setRolePermissions(s);
		}
		return getRolePermissions();
	}

}
