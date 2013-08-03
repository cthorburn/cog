package com.trabajo.jpa;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "Beetle")
@Table(
		name = "TESTBEETLE",  
		uniqueConstraints={
				@UniqueConstraint(columnNames={"USERNAME"})
		})
public class BeetleJPA implements JPAEntity<BeetleJPA> {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getUsername() == null) ? 0 : getUsername().hashCode());
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
		UserJPA other = (UserJPA) obj;
		if (getUsername() == null) {
			if (other.getUsername() != null)
				return false;
		} else if (!getUsername().equals(other.getUsername()))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	public BeetleJPA() {
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

	private String username;

	@Column(name = "USERNAME", nullable = false, unique = true, length = 32)
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public Set<GrubJPA> grubs;
	@OneToMany(mappedBy="dad", cascade=CascadeType.ALL)
	public Set<GrubJPA> getGrubs() {
		return grubs;
	}
	public void setGrubs(Set<GrubJPA> grubs) {
		this.grubs = grubs;
	}
	
	public Set<GrubJPA> getGrubSet() {
		Set<GrubJPA> grubs=getGrubs();
		if(grubs==null) {
			grubs=new HashSet<GrubJPA>();
			setGrubs(grubs);
		}
		return getGrubs();
	}

}
