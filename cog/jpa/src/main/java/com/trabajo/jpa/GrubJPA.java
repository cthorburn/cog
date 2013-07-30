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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity(name = "Grub")
@Table(
		name = "TESTGRUB",  
		uniqueConstraints={
				@UniqueConstraint(columnNames={"NAME"})
		})
public class GrubJPA implements JPAEntity<GrubJPA> {

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
		GrubJPA other = (GrubJPA) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	public GrubJPA() {
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
	
	private BeetleJPA dad;
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="dad")
	public BeetleJPA getDad() {
		return dad;
	}
	public void setDad(BeetleJPA dad) {
		this.dad = dad;
	}
	
	public Set<FlyJPA> flys;
	@ManyToMany(cascade=CascadeType.ALL)
	@JoinTable(name = "GRUB_FLIES", joinColumns = { @JoinColumn(name = "GF_GRUB_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "GF_FLY_ID", referencedColumnName = "ID") })
	public Set<FlyJPA> getFlys() {
		return flys;
	}
	public void setFlys(Set<FlyJPA> flys) {
		this.flys=flys;
	}

	public Set<FlyJPA> getFlySet() {
		Set<FlyJPA> flies=getFlys();
		if(flies==null) {
			flies=new HashSet<FlyJPA>();
			setFlys(flies);
		}
		return getFlys();
	}
	

	
}
