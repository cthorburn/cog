package com.trabajo.jpa;

import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity(name = "TaskUpload")
@Table(name = "TASKUPLOAD")
public class TaskUploadJPA implements JPAEntity<TaskUploadJPA> {


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getDocSet() == null) ? 0 : getDocSet().hashCode());
		result = prime * result + ((getTask() == null) ? 0 : getTask().hashCode());
		result = prime * result + ((getUploader() == null) ? 0 : getUploader().hashCode());
		result = prime * result + getVersion();
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
		TaskUploadJPA other = (TaskUploadJPA) obj;
		if (getDocSet() == null) {
			if (other.getDocSet() != null)
				return false;
		} else if (!getDocSet().equals(other.getDocSet()))
			return false;
		if (getTask() == null) {
			if (other.getTask() != null)
				return false;
		} else if (!getTask().equals(other.getTask()))
			return false;
		if (getUploader() == null) {
			if (other.getUploader()!= null)
				return false;
		} else if (!getUploader().equals(other.getUploader()))
			return false;
		if (getVersion() != other.getVersion())
			return false;
		return true;
	}

	private static final long serialVersionUID = 1L;

	public TaskUploadJPA() {
		super();
	}

	private int id;

    @Override
	@Id
	@SequenceGenerator(name = "TASK_UPLOAD_SEQUENCE_GENERATOR", sequenceName = "TASK_UPLOAD_SEQ")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_UPLOAD_SEQUENCE_GENERATOR")
	@Column(name = "ID")
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	private String path;
	@Column(name = "PATH", nullable = false, length = 512)
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	private String contentType;
	@Column(name = "CONTENTTYPE", nullable = true, length = 128)
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	private String originalName;
	@Column(name = "ORIGINALNAME", nullable = false, length = 512)
	public String getOriginalName() {
		return originalName;
	}
	public void setOriginalName(String originalName) {
		this.originalName= originalName;
	}

	private String docSet;
	@Column(name = "DOCSET", nullable = false, length = 128)
	public String getDocSet() {
		return docSet;
	}
	public void setDocSet(String docSet) {
		this.docSet = docSet;
	}
	
	private boolean versioned;
	@Column(name = "VERSIONED", nullable = false)
	public boolean getVersioned() {
		return versioned;
	}
	public void setVersioned(boolean versioned) {
		this.versioned = versioned;
	}
	
	private int version;
	@Column(name = "VERSION", nullable = false)
	public int getVersion() {
		return version;
	}
	
	public void setVersion(int version) {
		this.version = version;
	}
	
	private GregorianCalendar createdOn;
	@Column(name = "CREATEDON", nullable = false)
	public GregorianCalendar getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(GregorianCalendar createdOn) {
		this.createdOn = createdOn;
	}

	private long size;
	@Column(name = "SIZE", nullable = false)
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	
	private UserJPA uploader;
	@OneToOne
	@Column(name = "UPLOADER", nullable = false)
	public UserJPA getUploader() {
		return uploader;
	}

	public void setUploader(UserJPA uploader) {
		this.uploader = uploader;
	}

	private NodeJPA task;
	@OneToOne
	@Column(name = "TASK", nullable = false)
	public NodeJPA getTask() {
		return task;
	}
	public void setTask(NodeJPA task) {
		this.task = task;
	}

	private TaskUploadJPA previousVersion;
	@OneToOne
	@Column(name = "PREVIOUSVERSION", nullable = true)
	public TaskUploadJPA getPreviousVersion() {
		return previousVersion;
	}
	public void setPreviousVersion(TaskUploadJPA previousVersion) {
		this.previousVersion = previousVersion;
	}
}
