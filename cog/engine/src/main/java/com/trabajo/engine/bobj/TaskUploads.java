package com.trabajo.engine.bobj;

import java.io.IOException;
import java.nio.file.Path;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.tika.Tika;

import com.trabajo.jpa.NodeJPA;
import com.trabajo.jpa.TaskUploadJPA;
import com.trabajo.jpa.UserJPA;
import com.trabajo.process.ITask;
import com.trabajo.process.ITaskUpload;

public class TaskUploads {

	public static ITaskUpload create(EntityManager em, int taskId, Path target, String originalName, String docSet, String username, long size, boolean versioned) {
		Set<ITaskUpload> othersOfSameOriginalName=find(em, taskId, docSet, originalName);

		String contentType="application/octet-stream";
		
		try {
			contentType = new Tika().detect(target.toFile());
		} catch (IOException e) {
			// ignore
		}


		boolean actuallyVersioned=versioned;
		
		if(!othersOfSameOriginalName.isEmpty()) {
			ITaskUpload anyPreviousVersion=othersOfSameOriginalName.iterator().next();
			actuallyVersioned=anyPreviousVersion.getVersioned();
		}
		
		ITaskUpload previousVersion=null;
		
		int version=0;
		if(actuallyVersioned) {
			for(ITaskUpload test: othersOfSameOriginalName) {
				if(test.getVersion() > version) {
					version=test.getVersion();
					previousVersion=test;
				}
			}
		}

		ITaskUpload result=null;
		
		if(actuallyVersioned || othersOfSameOriginalName.isEmpty()) {
			TaskUploadJPA newUpload=new TaskUploadJPA();
			newUpload.setCreatedOn(new GregorianCalendar());
			newUpload.setDocSet(docSet);
			newUpload.setOriginalName(originalName);
			newUpload.setPath(target.toString());
			newUpload.setSize(size);
			newUpload.setTask((NodeJPA)Tasks.findById(em, taskId).entity());
			newUpload.setUploader((UserJPA)Users.findByName(em, username).entity());
			newUpload.setPreviousVersion((TaskUploadJPA)(previousVersion!=null ? previousVersion.entity():null));
			newUpload.setVersioned(actuallyVersioned);
			newUpload.setVersion(version+1);
			newUpload.setContentType(contentType);
			em.persist(newUpload);
			
			result=toImpl(em, newUpload);
		}
		else {
			TaskUploadJPA newUpload=(TaskUploadJPA)othersOfSameOriginalName.iterator().next().entity();
			newUpload.setCreatedOn(new GregorianCalendar());
			newUpload.setOriginalName(originalName);
			newUpload.setPath(target.toString());
			newUpload.setSize(size);
			newUpload.setContentType(contentType);
			newUpload.setUploader((UserJPA)Users.findByName(em, username));
		}
		
		return result;
	}

	private static Set<ITaskUpload> find(EntityManager em, int taskId, String docSet, String originalName) {
		
		String jpql="select tu from TaskUpload tu where tu.task=?1 and tu.docSet=?2 and tu.originalName=?3";
				
		TypedQuery<TaskUploadJPA> q=em.createQuery(jpql, TaskUploadJPA.class);		
				
		q.setParameter(1,  Tasks.findById(em, taskId).entity());
		q.setParameter(2, docSet);
		q.setParameter(3, originalName);
		
		return toImpl(em, q.getResultList());
	}

	private static Set<ITaskUpload> toImpl(EntityManager em, List<TaskUploadJPA> set) {
		Set<ITaskUpload> impls = new HashSet<>();
		for (TaskUploadJPA s : set) {
			impls.add(new TaskUploadImpl(em, s));
		}
		return impls;
	}
	
	private static ITaskUpload toImpl(EntityManager em, TaskUploadJPA j) {
		return new TaskUploadImpl(em, j);
	}

	public static void delete(EntityManager em, ITask task) {
		String jpql="delete from TaskUpload tu where tu.task=?1";
		Query q=em.createQuery(jpql);
		q.setParameter(1, task.entity());
		q.executeUpdate();
	}
}
