package com.trabajo.engine.bobj;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.ClassLoaderDefJPA;
import com.trabajo.jpa.VersionJPA;
import com.trabajo.process.IClassLoaderDef;
import com.trabajo.values.Category;
import com.trabajo.values.Description;

public class ClassLoaderDefs {

	public static IClassLoaderDef findByVersion(EntityManager em,
			DefinitionVersion dv) {
		try {
			TypedQuery<ClassLoaderDefJPA> q = em
					.createQuery(
							"SELECT pd FROM ClassLoaderDef pd WHERE pd.definitionVersion.shortName=?1 and pd.definitionVersion.major=?2 and pd.definitionVersion.minor=?3 and pd.definitionVersion.fix=?4",
							ClassLoaderDefJPA.class);
			q.setParameter(1, dv.shortName());
			q.setParameter(2, dv.getMajor());
			q.setParameter(3, dv.getMinor());
			q.setParameter(4, dv.getFix());
			return toImpl(em, q.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}

	private static IClassLoaderDef toImpl(EntityManager em, ClassLoaderDefJPA jpa) {
		return new ClassLoaderDefImpl(em, jpa);
	}

	public static IClassLoaderDef create(EntityManager em,
			DefinitionVersion version, Category category, Description desc) {
		ClassLoaderDefJPA cld = new ClassLoaderDefJPA();
		cld.setDefinitionVersion(new VersionJPA(version));
		cld.setCategory(category.getValue());
		cld.setDescription(desc.getValue());
		em.persist(cld);
		em.flush();
		return toImpl(em, cld);
	}

}
