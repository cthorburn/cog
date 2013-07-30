package com.trabajo.engine;

import java.io.File;

import com.trabajo.utils.Files;

public class FileSystem {
	private File base;

	public FileSystem() {
		base = new File(new File(System.getProperty("user.home")), ".trabajo");
		base.mkdirs();
	}

	public File getManifestHandlerTmpDir() {
		File f = new File(base, "mhtmp");
		f.mkdirs();
		return f;
	}

	public File getUploadDir() {
		File f = new File(base, "upload");
		f.mkdirs();
		return f;
	}
	
	public File getProcessTmpDir() {
		File f = new File(base, "process_temp");
		f.mkdirs();
		return f;
	}
	
	public void obliterate() {
		Files.deleteDirectory(getManifestHandlerTmpDir());
		Files.deleteDirectory(getUploadDir());
		Files.deleteDirectory(getProcessTmpDir());
	}
}
