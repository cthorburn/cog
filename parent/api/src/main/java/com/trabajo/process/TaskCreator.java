package com.trabajo.process;

import java.util.ArrayList;
import java.util.List;

public class TaskCreator {

	private String[] roles=new String[0];
	public String[] getRoles() {
		return roles;
	}
	public TaskCreator setRoles(String ... roles) {
		this.roles=roles;
		return this;
	}

	public String getTaskName() {
		return taskName;
	}

	public List<Dependency> getDependencies() {
		return dependencies;
	}

	private String taskName;

	private List<Dependency> dependencies = new ArrayList<>();


	public TaskCreator(String taskName) {
		this.taskName = taskName;
	}

	public void dependsOn(String depTaskName, TaskChronology chronology) {
		Dependency d = new Dependency(depTaskName, chronology);
		if (!dependencies.contains(d)) {
			dependencies.add(d);
		}
	}

	public static class Dependency {
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((chronology == null) ? 0 : chronology.hashCode());
			result = prime * result + ((depTaskName == null) ? 0 : depTaskName.hashCode());
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
			Dependency other = (Dependency) obj;
			if (chronology != other.chronology)
				return false;
			if (depTaskName == null) {
				if (other.depTaskName != null)
					return false;
			} else if (!depTaskName.equals(other.depTaskName))
				return false;
			return true;
		}

		public String getDepTaskName() {
			return depTaskName;
		}

		public TaskChronology getChronology() {
			return chronology;
		}

		private String depTaskName;
		private TaskChronology chronology;

		public Dependency(String depTaskName, TaskChronology chronology) {
			super();
			this.depTaskName = depTaskName;
			this.chronology = chronology;
		}
	}

	public void assignToRoles(String ... roles) {
		this.roles=roles;
		
	}
}
