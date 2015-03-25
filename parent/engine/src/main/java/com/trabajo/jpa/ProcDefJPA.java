package com.trabajo.jpa;

import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.google.gson.annotations.Expose;
import com.trabajo.DAGVisualizer;

@Entity(name = "ProcDef")
@Table(name = "PROCDEF")
public class ProcDefJPA implements JPAProcessComponent<ProcDefJPA> {

	private static final long serialVersionUID = 1L;

	public ProcDefJPA() {
		super();
	}

	private int id;

    @Override
	@Id
	@SequenceGenerator(name = "PROCDEF_SEQUENCE_GENERATOR", sequenceName = "PROCDEF_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROCDEF_SEQUENCE_GENERATOR")
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

	private String mainClass;

	@Column(name = "MAINCLASS", nullable = false)
	public String getMainClass() {
		return mainClass;
	}

	public void setMainClass(String mainClass) {
		this.mainClass = mainClass;
	}

	private String category;

	@Column(name = "CATEGORY", nullable = false)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	private String description;

	@Column(name = "DESCRP", nullable = false)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	

	/** Process activation time */
	private GregorianCalendar goActive;	  
	@Column(name = "GO_ACTIVE", nullable=true)
	public GregorianCalendar getGoActive() {
		return goActive;
	}

	public void setGoActive(GregorianCalendar goActive) {
		this.goActive = goActive;
	}

	private boolean deprecated;
	@Column(name = "DEPRECATED", columnDefinition="int default 0")
	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}
	
	
	private boolean suspended;
	@Column(name = "SUSPENDED", columnDefinition="int default 0")
	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}
	
	private boolean deprecatePreviousVersions;

	@Column(name = "DEPRECATE_ON_ACTIVATION", columnDefinition="int default 0")
	public boolean isDeprecatePreviousVersions() {
		return deprecatePreviousVersions;
	}

	public void setDeprecatePreviousVersions(boolean deprecatePreviousVersions) {
		this.deprecatePreviousVersions = deprecatePreviousVersions;
	}

	
	private boolean inDevelopment;
	@Column(name = "INDEV", nullable = false, columnDefinition="int default 0")
	public boolean isInDevelopment() {
		return inDevelopment;
	}

	public void setInDevelopment(boolean inDevelopment) {
		this.inDevelopment = inDevelopment;
	}

	
	@Expose(serialize=false, deserialize=false)
	private ProcessJarAnalysis jarMetadata;
	@Lob
	@Column(name = "JARMETADATA", nullable = false)
	public ProcessJarAnalysis getJarMetadata() {
		return jarMetadata;
	}
	public void setJarMetadata(ProcessJarAnalysis  jarMetadata) {
		this.jarMetadata=jarMetadata;
	}

	@Expose(serialize = false, deserialize = false)
	private 	DAGVisualizer visualizer;
	@Lob
	@Column(name = "VISUALIZER", nullable = true)
	public DAGVisualizer getVisualizer() {
		return visualizer;
	}
	public void setVisualizer(DAGVisualizer visualizer) {
		this.visualizer=visualizer;
	}
	
	
	@PrePersist
	@PreUpdate
	public void prePersist() {

	}

	@Override
	public String getName() {
		return getDefinitionVersion().toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getDefinitionVersion() == null) ? 0 : getDefinitionVersion().hashCode());
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
		ProcDefJPA other = (ProcDefJPA) obj;
		if (getDefinitionVersion() == null) {
			if (other.getDefinitionVersion() != null)
				return false;
		} else if (!getDefinitionVersion().equals(other.getDefinitionVersion()))
			return false;
		return true;
	}
	
	
	
}
