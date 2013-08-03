package com.trabajo.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;

import org.apache.tika.Tika;

import com.trabajo.DefinitionVersion;
import com.trabajo.utils.IOUtils;

public class CogTaskStreamWriter {
	
	private String charSet;
	private boolean binary;
	private boolean _static;
	private String path;
	private String contentType;
	private int taskId;
	private FileCache fc;
	private DefinitionVersion dv;
	
	public CogTaskStreamWriter(DefinitionVersion dv, int taskId, String path, String contentType, String charSet, boolean binary, boolean _static) {
		super();
		this.taskId=taskId;
		this.path=path;
		this.charSet = charSet;
		this.binary = binary;
		this._static=_static;
		this.fc=EngineFactory.fileCache();
		this.dv=dv;
		if(contentType==null) {
			this.contentType=getContentTypeHueristically(contentType, path);
		}
	}

	public boolean isBinary() {
		return binary;
	}

	public boolean is_static() {
		return _static;
	}

	private String getContentTypeHueristically(String ct, String path) {
			return ct==null ? getTikaMapping():ct;
	}


	private String getTikaMapping() {
		String result=null;

		File tikaTarget=fc.getFile(dv, path);
		
		if(tikaTarget!=null && tikaTarget.exists()) {
			result=new Tika().detect(tikaTarget.getAbsolutePath());
		}

		if(result==null) {
			result="application/octet-stream";
		}
		return result;
	}

	public String getContentType() {
		return contentType;
	}
	
	public String getCharSet() {
		return charSet;
	}

	public void writeStatic(PrintWriter printWriter) {
		try {
			copy(new FileReader(fc.getFile(dv, path)), printWriter);
		} catch (FileNotFoundException e) {
			//ignore getFile has confirmed exists
		}
	}
	public void writeStatic(OutputStream out) {
		try {
			copy(new FileInputStream(fc.getFile(dv, path)), out);
		} catch (FileNotFoundException e) {
			//ignore getFile has confirmed exists
		}
	}
	
	private void copy(Reader reader, PrintWriter printWriter)  {

		char[] buf=new char[512];

		try(BufferedReader buffrd=new BufferedReader(reader)) {
			for(int l=buffrd.read(buf); l!= -1; printWriter.write(buf, 0, l));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}finally {
			
		}
	}
	private void copy(InputStream is, OutputStream os) {
		try {
			IOUtils.copyStream(is, os, false);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void write(PrintWriter writer) {
		MagicSubstitutionWriter msw=new MagicSubstitutionWriter(taskId, dv, fc, path);
		msw.write(writer);
	}




}
