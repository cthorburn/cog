package com.trabajo.engine;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.trabajo.DefinitionVersion;
import com.trabajo.IVisualizer;
import com.trabajo.ValidationException;
import com.trabajo.annotation.ClassLoaderHierarchy;
import com.trabajo.annotation.Development;
import com.trabajo.annotation.Form;
import com.trabajo.annotation.FormBuilder;
import com.trabajo.annotation.Forms;
import com.trabajo.annotation.Lifecycle;
import com.trabajo.annotation.Process;
import com.trabajo.annotation.StandardLifecycle;
import com.trabajo.annotation.StartupForm;
import com.trabajo.process.Version;
import com.trabajo.values.Category;
import com.trabajo.values.Description;

public class ProcessClassMetadata {

	private DefinitionVersion dv;
	private Category category;
	private Description description;
	private String className;
	private List<DefinitionVersion> clh = new ArrayList<>();
	private String suForm;
	private Map<String, String> forms = new HashMap<>();
	private boolean removePreviousVersions;
	private boolean development;
	private ActivationSpec activationSpec;

	public Category getCategory() {
		return category;
	}

	public Description getDescription() {
		return description;
	}

	public ProcessClassMetadata(Class<?> processClass) throws ValidationException {
		className = processClass.getName();
		activationSpec = ActivationSpec.forClass(processClass);

		Process p = processClass.getAnnotation(Process.class);

		if (p == null) {
			throw new ValidationException("The given class is not annotated @Process: " + processClass.getName());
		}

		try {
			dv = new DefinitionVersion(p.name(), Version.parse(p.version()));
		} catch (Exception e) {
			throw new ValidationException("Unable to parse @Process.version: " + p.version());
		}

		category = new Category(p.category());
		description = new Description(p.description(), 255);

		clh.add(dv);

		if(processClass.getAnnotation(StartupForm.class)!=null) {
			suForm = processClass.getAnnotation(StartupForm.class).value();
	
			Forms fs = processClass.getAnnotation(Forms.class);
			if (fs != null) {
				for (Form f : fs.value()) {
					forms.put(f.name(), new FormBuilder().buildStartForm(f));
				}
			}
		}
		ClassLoaderHierarchy clhx = processClass.getAnnotation(ClassLoaderHierarchy.class);

		if (clhx != null) {
			for (String dvs : clhx.value()) {
				try {
					clh.add(DefinitionVersion.parse(dvs));
				} catch (Exception e) {
					throw new ValidationException("@ClassLoaderHierarchy bad version: " + dvs);
				}
			}
		}
		Development d = processClass.getAnnotation(Development.class);
		if (d != null) {
			development = true;
			removePreviousVersions = d.removePreviousVersions();
		}
	}

	public String getStartupForm() {
		if(suForm!=null)
			return forms.get(suForm);
		else
			return FormBuilder.NO_FORM;
	}

	public static Method getStartMethod(Class<?> processClass) {
		List<Method> startCandidates = com.trabajo.utils.AnnotationUtils.<StandardLifecycle> getMethodsAnnotatedXWithParticluarEnumYForAttributeZ(processClass,
				Lifecycle.class, "phase", StandardLifecycle.START);

		if (startCandidates.size() == 0) {
			throw new RuntimeException("no start method defined");
		} else if (startCandidates.size() > 1) {
			throw new RuntimeException("multiple start methods defined");
		}

		return startCandidates.get(0);
	}

	public String getClassName() {
		return className;
	}

	public DefinitionVersion getVersion() {
		return dv;
	}

	public List<DefinitionVersion> getDefaultClassLoaderHierarchy() {
		return clh;
	}

	public boolean removePreviousVersions() {
		return removePreviousVersions;
	}

	public boolean inDevelopment() {
		return development;
	}

	public ActivationSpec activationSpec() {
		return activationSpec;
	}
}