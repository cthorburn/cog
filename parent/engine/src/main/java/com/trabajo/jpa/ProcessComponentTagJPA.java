
package com.trabajo.jpa;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name="ProcessComponentTag")
@Table(name = "PCTAG")
public class ProcessComponentTagJPA implements JPAEntity<ProcessComponentTagJPA> {

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getDefinitionVersion() == null) ? 0 : getDefinitionVersion().hashCode());
		result = prime * result + ((getTagName() == null) ? 0 : getTagName().hashCode());
		result = prime * result + ((getTagValue() == null) ? 0 : getTagValue().hashCode());
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
		ProcessComponentTagJPA other = (ProcessComponentTagJPA) obj;
		if (getDefinitionVersion() == null) {
			if (other.getDefinitionVersion() != null)
				return false;
		} else if (!getDefinitionVersion().equals(other.getDefinitionVersion()))
			return false;
		if (getTagName() == null) {
			if (other.getTagName() != null)
				return false;
		} else if (!getTagName().equals(other.getTagName()))
			return false;
		if (getTagValue() == null) {
			if (other.getTagValue() != null)
				return false;
		} else if (!getTagValue().equals(other.getTagValue()))
			return false;
		return true;
	}

		private static final long serialVersionUID = 1L;    

	public ProcessComponentTagJPA() {
		super();
	} 
    
    private int id;
    @Override
	@Id
	@SequenceGenerator(name = "PCTAG_SEQUENCE_GENERATOR", sequenceName = "PCTAG_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PCTAG_SEQUENCE_GENERATOR")
	@Column(name = "ID")
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    
  private VersionJPA definitionVersion;
	@Embedded
	public VersionJPA getDefinitionVersion() {
		return definitionVersion;
	}
	public void setDefinitionVersion(VersionJPA definitionVersion) {
		this.definitionVersion = definitionVersion;
	}
	
	private String tagName;	  
	@Column(name = "tagname", nullable=false, length=64)
	public String getTagName() {
		return tagName;
	}
	
	public void setTagName(String tagName) {
		this.tagName=tagName;
	}

	private String tagValue;	  
	@Column(name = "tagvalue", nullable=false, length=64)
	public String getTagValue() {
		return tagValue;
	}
	
	public void setTagValue(String tagValue) {
		this.tagValue=tagValue;
	}
	
	@PrePersist
	@PreUpdate
  	public  void prePersist() {
		
  	}
}

