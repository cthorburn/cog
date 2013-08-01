package com.trabajo.engine.bobj;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import com.trabajo.DefinitionVersion;
import com.trabajo.jpa.ReportDefJPA;
import com.trabajo.jpa.VersionJPA;
import com.trabajo.process.IReportDef;


public class ReportDefs {
	
	public static IReportDef toImpl(EntityManager em, ReportDefJPA reportDef) {
		return reportDef==null ? null:new ReportDefImpl(em, reportDef);
	}

	public static IReportDef findById(EntityManager em, long id) {
		return toImpl(em, em.find(ReportDefJPA.class, id));
	}

	public static IReportDef findByVersion(EntityManager em, DefinitionVersion dv) {
		try {
			TypedQuery<ReportDefJPA> q = em.createQuery("SELECT pd FROM ReportDef pd WHERE pd.definitionVersion.shortName=?1 and pd.definitionVersion.major=?2 and pd.definitionVersion.minor=?3 and pd.definitionVersion.fix=?4", ReportDefJPA.class);
			q.setParameter(1, dv.shortName());
			q.setParameter(2, dv.getMajor());
			q.setParameter(3, dv.getMinor());
			q.setParameter(4, dv.getFix());
			return toImpl(em, q.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}

	public static IReportDef create(EntityManager em, Path target, DefinitionVersion dv, String category, String description, String originalName) {
		ReportDefJPA j=new ReportDefJPA();
		j.setDefinitionVersion(new VersionJPA(dv));
		j.setCategory(category);
		j.setDescription(description);
		j.setReportName(originalName);
		try {
			j.setReportText(com.trabajo.utils.IOUtils.stringFromUTF8Stream(new FileInputStream(target.toFile())));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		em.persist(j);
		em.flush();
		return toImpl(em, j);
	}

	public static IReportDef findByName(EntityManager em, String name) {
		try {
			TypedQuery<ReportDefJPA> q = em.createQuery("SELECT pd FROM ReportDef pd WHERE pd.reportName=?1", ReportDefJPA.class);
			q.setParameter(1, name);
			return toImpl(em, q.getSingleResult());
		} catch (NoResultException e) {
			return null;
		}
	}
}
