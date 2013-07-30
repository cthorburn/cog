package com.trabajo.vcl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ChainUpdate<T extends CLMKey<T>> {

	public static enum TYPE {REDEFINE, DELETE};
	
	private CLMKey<T> dv;
	private File[] files;
	private TYPE type;

	public ChainUpdate(TYPE type, CLMKey<T> dv, File[] files) {
		super();
		this.type=type;
		this.dv = dv;
		this.files = files;
	}
	
	public CLMKey<T> getClassLoaderVersion() {
		return dv;
	}
	
	public void deleteFiles() throws IOException {
		for(File file: files) {
			Files.delete(file.toPath());
		}
	}
	
	public TYPE type() {
		return type;
	}

	public File[] getFiles() {
		return files;
	}
}
