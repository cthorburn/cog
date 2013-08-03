package com.trabajo.jpa;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "Fly")
@Table(
		name = "TESTFLY",  
		uniqueConstraints={
				@UniqueConstraint(columnNames={"NAME"})
		})
public class FlyJPA implements JPAEntity<FlyJPA> {

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
		FlyJPA other = (FlyJPA) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	public FlyJPA() {
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

	@Column(name = "NAME", nullable = false, unique = true, length = 32)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private Set<GrubJPA> grubs;
	@ManyToMany(mappedBy = "flys", cascade=CascadeType.PERSIST)
	public Set<GrubJPA> getGrubs() {
		return grubs;
	}
	public void setGrubs(Set<GrubJPA> grubs) {
		this.grubs = grubs;
	}

	
}
