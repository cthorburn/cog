package com.trabajo.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity(name="Permission")
@Table(name = "PERMISSION")
public class PermissionJPA  implements JPACategorised<PermissionJPA> {
   
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
		PermissionJPA other = (PermissionJPA) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}

		private static final long serialVersionUID = 1L;
	  
		public PermissionJPA() {
        super();
    }

    private  int id;
	
    @Override
	@Id
	@SequenceGenerator(name = "PERMISSION_SEQUENCE_GENERATOR", sequenceName = "PERMISSION_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERMISSION_SEQUENCE_GENERATOR")
	@Column(name = "ID")
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id=id;
    }
    
    private String name; 
    @Column(name = "PERMISSION_NAME", nullable = false, unique=true, length=32)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    private String description;
    @Column(name = "DESCRIPTION", nullable = false, length=255)
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
}
