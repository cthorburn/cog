package com.trabajo.jpa;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.trabajo.ValidationException;
import com.trabajo.annotation.AnnotationQueryClassLoader;
import com.trabajo.annotation.Form;
import com.trabajo.annotation.FormBuilder;
import com.trabajo.annotation.Forms;
import com.trabajo.annotation.Task;
import com.trabajo.annotation.TaskDescription;
import com.trabajo.utils.IOUtils;
import com.trabajo.utils.Strings;
import com.trabajo.values.Description;

public class WholeJarMetadataV1 implements Serializable {

	private static final long serialVersionUID = -6912541101020988457L;

	private static final String[] ESA=new String[0];

	private Map<String, TaskAnnotationModel> taskAnnotations = new HashMap<String, TaskAnnotationModel>() {
		private static final long serialVersionUID = 4812759669363034174L;
	};

	public WholeJarMetadataV1() {

	}

	public WholeJarMetadataV1(File f) throws ValidationException {

		try (AnnotationQueryClassLoader aql = new AnnotationQueryClassLoader(); JarFile jf = new JarFile(f)) {
			Enumeration<JarEntry> enm = jf.entries();
			while (enm.hasMoreElements()) {
				JarEntry ze = enm.nextElement();
				if (isClassEntry(ze)) {
					processEntry(jf, ze, aql);
				}
			}
		} catch (IOException e) {
			throw new ValidationException("not a jar file: " + f.getAbsolutePath());
		}

	}

	private void processEntry(JarFile jf, JarEntry ze, AnnotationQueryClassLoader aql) throws ValidationException {
		String clazzName = classNameForEntry(ze);
		Class<?> clazz = aql.define(clazzName, IOUtils.classBytesFromJar(clazzName, jf));

		List<TaskAnnotationModel> models = TaskAnnotationModel.analyze(clazz);

		for (TaskAnnotationModel tam : models) {
			TaskAnnotationModel exists = taskAnnotations.get(tam.getName());
			if (exists != null && !exists.equals(tam)) {
				throw new ValidationException("duplicate task name: " + clazzName + "," + exists.getClassName());
			}
			taskAnnotations.put(tam.getName(), tam);
		}
	}

	private String classNameForEntry(JarEntry ze) {
		String result = ze.getName().replace('/', '.');
		return Strings.beforeLast(result, '.'); // remove .class suffix
	}

	private boolean isClassEntry(JarEntry ze) {
		return !ze.isDirectory() && ze.getName().toLowerCase().endsWith(".class");
	}

	static class TaskAnnotationModel implements Serializable {

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((className == null) ? 0 : className.hashCode());
			result = prime * result + ((name == null) ? 0 : name.hashCode());
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
			TaskAnnotationModel other = (TaskAnnotationModel) obj;
			if (className == null) {
				if (other.className != null)
					return false;
			} else if (!className.equals(other.className))
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}

		private static final long serialVersionUID = -5856532800518454864L;
		
		public static List<TaskAnnotationModel> analyze(final Class<?> clazz) throws ValidationException {
			final List<TaskAnnotationModel> result = new ArrayList<>();

			Task taskAnn = clazz.getAnnotation(Task.class);
			if (taskAnn != null) {
				result.add(new TaskAnnotationModel(clazz, taskAnn.name()));
			}

			for (Class<?> member : clazz.getClasses()) {
				taskAnn = member.getAnnotation(Task.class);
				if (taskAnn != null) {
					result.add(new TaskAnnotationModel(member, taskAnn.name()));
				}
			}

			return result;
		}

		private String className;
		private String name;
		private Description description;
		private HashMap<String, String> forms;
		private HashMap<String, String[]> javascript;
		private HashMap<String, String[]> javascriptInclude;

		public TaskAnnotationModel(Class<?> clazz, String name) throws ValidationException {
			this.className = clazz.getName();
			this.name = name;

			TaskDescription descAnn = clazz.getAnnotation(TaskDescription.class);

			this.description=(descAnn == null) ? new Description("No description for task", 255) : new Description(descAnn.value(), 255);

			this.forms = new HashMap<String, String>();
			this.javascript = new HashMap<String, String[]>();
			this.javascriptInclude = new HashMap<String, String[]>();

			Forms fs = clazz.getAnnotation(Forms.class);
			if (fs != null) {
				for (Form f : fs.value()) {
					forms.put(f.name(), new FormBuilder().buildForm(f, name));
					javascript.put(f.name(), (f.javascript() == null) ? ESA:f.javascript());
					javascriptInclude.put(f.name(), (f.javascriptInclude() == null) ? ESA:f.javascriptInclude());
				}
			}
			
		}
		
		public String getName() {
			return name;
		}

		public String getClassName() {
			return className;
		}

		

		public String getForm(String formName) {
			return forms.get(formName);
		}

		public Description getDescription() {
			return description;
		}

		public List<String> getJavascript(String formName) {
			return Arrays.asList(javascript.get(formName));
		}

		public List<String> getJavascriptInclude(String formName) {
			return Arrays.asList(javascriptInclude.get(formName));
		}
	}

	public String getTaskClassName(String taskName) {
		return taskAnnotations.get(taskName).getClassName();
	}

	public String getTaskForm(String taskName, String formName) {
		return taskAnnotations.get(taskName).getForm(formName);
	}
	

	public Description getDescription(String taskName) {
		return taskAnnotations.get(taskName).getDescription();
	}

	public List<String> getJavascript(String taskName, String formName) {
		return taskAnnotations.get(taskName).getJavascript(formName);
	}

	public List<String> getJavascriptInclude(String taskName, String formName) {
		return taskAnnotations.get(taskName).getJavascriptInclude(formName);
	}

}
