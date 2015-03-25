package com.trabajo.jpa;

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

@Entity(name = "ReportDef")
@Table(name = "REPORTDEF")
public class ReportDefJPA implements JPAProcessComponent<ReportDefJPA> {

	private static final long serialVersionUID = 1L;

	public ReportDefJPA() {
		super();
	}

	private int id;

    @Override
	@Id
	@SequenceGenerator(name = "REPORTDEF_SEQUENCE_GENERATOR", sequenceName = "REPORTDEF_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPORTDEF_SEQUENCE_GENERATOR")
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

	private String reportName;

	@Column(name = "REPORTNAME", nullable = false)
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	@Expose(serialize=false, deserialize=false)
	private String reportText;
	@Lob
	@Column(name = "REPORTTEXT", nullable = false)
	public String getReportText() {
		return reportText;
	}
	public void setReportText(String reportText) {
		this.reportText=reportText;
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
		ReportDefJPA other = (ReportDefJPA) obj;
		if (getDefinitionVersion() == null) {
			if (other.getDefinitionVersion() != null)
				return false;
		} else if (!getDefinitionVersion().equals(other.getDefinitionVersion()))
			return false;
		return true;
	}

}
