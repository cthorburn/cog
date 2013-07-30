package com.trabajo.utils;

import java.io.File;
import java.io.FileFilter;

public class Files {

	public static final FileFilter DIRSONLY_FILTER= new FileFilter() {
		@Override
		public boolean accept(File f) {
			return f.isDirectory();
		}
	};
	
	
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}
}
