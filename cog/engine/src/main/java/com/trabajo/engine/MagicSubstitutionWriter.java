package com.trabajo.engine;

import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.trabajo.DefinitionVersion;
import com.trabajo.utils.Strings;

public class MagicSubstitutionWriter {
	private FileCache fc;
	private String path;
	private DefinitionVersion dv;
	private int taskId;

	//Dealing with response content types. Use path file extensions to write a hint into the the translated URL 
	//about the content header setting that he server needs to set
	//when the magic URL is invoked
	
	public MagicSubstitutionWriter(int taskId, DefinitionVersion dv, FileCache fc, String path) {
		super();
		this.fc=fc;
		this.taskId=taskId;
		this.dv=dv;
		this.path=path;
	}

	public void write(Writer w) {

		VelocityEngine ve = new VelocityEngine();

		
		ve.setProperty("resource.loader", "file"); 
		ve.setProperty("file.resource.loader.path", Strings.toCSV(fc.unzipDirPaths(dv).values())); 
		ve.setProperty("fileresource.loader.cache", "false");
		ve.setProperty("file.resource.loader.modificationCheckInterval", "10");

		ve.init();
		
		VelocityContext context = new VelocityContext();

		context.put("magic", this);

		Template template = null;

		try {
			template = ve.getTemplate(path);
		} catch (ResourceNotFoundException e) {
			throw new RuntimeException(e);
		} catch (ParseErrorException e) {
			throw new RuntimeException(e);
		} catch (MethodInvocationException e) {
			throw new RuntimeException(e);
		}

		template.merge(context, w);
	}
	
	public String binary(String path) {
		StringBuilder sb=new StringBuilder();
		sb.append("process?a=cog_resource&___task=");
		sb.append(taskId);
		sb.append("&___binary=true&___path=");
		sb.append(path);
		
		return sb.toString();
	}
	
	
	public String data(String name) {
		StringBuilder sb=new StringBuilder();
		sb.append("process?a=cog_data&___task=");
		sb.append(taskId);
		sb.append("&___name=");
		sb.append(name);
		
		return sb.toString();
	}
	
	public String data(String name, String args) {
		StringBuilder sb=new StringBuilder();
		sb.append("process?a=cog_data&___task=");
		sb.append(taskId);
		sb.append("&___name=");
		sb.append(name);
		sb.append("&___args=");
		sb.append(args);
		
		return sb.toString();
	}
	
	public String dhxSupport() {
		
		return "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/main.css\"/>\r\n" + 
				"\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxLayout/codebase/dhtmlxlayout.css\">\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxLayout/codebase/skins/dhtmlxlayout_dhx_black.css\">\r\n" + 
				"<script src=\"js/dhtmlxMenu/codebase/dhtmlxcommon.js\"></script>\r\n" + 
				"<script src=\"js/dhtmlxLayout/codebase/dhtmlxlayout.js\"></script>\r\n" + 
				"\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxMenu/codebase/skins/dhtmlxmenu_dhx_black.css\"/>\r\n" + 
				"<script src=\"js/dhtmlxMenu/codebase/dhtmlxmenu.js\"></script>\r\n" + 
				"<script src=\"js/dhtmlxLayout/codebase/dhtmlxcontainer.js\"></script>\r\n" + 
				"\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxAccordion/codebase/skins/dhtmlxaccordion_dhx_black.css\">\r\n" + 
				"<script src=\"js/dhtmlxAccordion/codebase/dhtmlxaccordion.js\"></script>\r\n" + 
				"\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxGrid/codebase/dhtmlxgrid.css\"/>\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxGrid/codebase/dhtmlxgrid_skins.css\"/>\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxGrid/codebase/skins/dhtmlxgrid_dhx_black.css\"/>\r\n" + 
				"<script src=\"js/dhtmlxGrid/codebase/dhtmlxgrid.js\"></script>\r\n" + 
				"<script src=\"js/dhtmlxGrid/codebase/dhtmlxgridcell.js\"></script>\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxWindows/codebase/dhtmlxwindows.css\">\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxWindows/codebase/skins/dhtmlxwindows_dhx_black.css\">\r\n" + 
				"<script src=\"js/dhtmlxWindows/codebase/dhtmlxwindows.js\"></script>\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxForm/codebase/skins/dhtmlxform_dhx_skyblue.css\">\r\n" + 
				"<script src=\"js/dhtmlxForm/codebase/dhtmlxform.js\"></script>\r\n" + 
				"<script src=\"js/dhtmlxForm/codebase/ext/dhtmlxform_item_container.js\"></script>\r\n" + 
				"<script src=\"js/dhtmlxForm/codebase/ext/dhtmlxform_item_upload.js\"></script>\r\n" + 
				"\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxCombo/codebase/dhtmlxcombo.css\">\r\n" + 
				"<script src=\"js/dhtmlxCombo/codebase/dhtmlxcombo.js\"></script>\r\n" + 
				"    \r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxTabbar/codebase/dhtmlxtabbar.css\">\r\n" + 
				"<script  src=\"js/dhtmlxTabbar/codebase/dhtmlxtabbar.js\"></script>    \r\n" + 
				"\r\n" + 
				"<link rel=\"stylesheet\" type=\"text/css\" href=\"js/dhtmlxTree/codebase/dhtmlxtree.css\">\r\n" + 
				"<script src=\"js/dhtmlxTree/codebase/dhtmlxtree.js\"></script>\r\n" + 
				"   \r\n" + 
				"<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js\"></script>\r\n" + 
				"<script src=\"js/trabajo/trabajo.js\"></script>\r\n" +
				"<script>var taskId="+taskId+"</script>\r\n";
	}
}

