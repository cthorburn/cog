package com.trabajo.engine;

import java.nio.file.Path;

import com.trabajo.engine.bobj.TaskUploads;
import com.trabajo.process.ITaskUpload;

public class TaskUploadManager {

	public static ITaskUpload manage(TSession ts, Path target, int taskId, String originalName, String docSet, String username, long size, boolean versioned) {
		ITaskUpload tu=TaskUploads.create(ts.getEntityManager(), taskId, target, originalName, docSet, username, size, versioned);
		return tu;
	}

}
