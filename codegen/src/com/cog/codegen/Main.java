package com.cog.codegen;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import com.trabajo.utils.IOUtils;
import com.trabajo.utils.Strings;

public class Main {

	private String type;
	private String feed;
	private String base;
	private String loc;
	private String formgen;
	
	public static void main(String[] args) {
		Main main=new Main(args);
		try {
			main.run();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Main(String[] args) {
		super();
		type=fromArgs("-type", args);
		feed=fromArgs("-feed", args);
		base=fromArgs("-base", args);
		loc=fromArgs("-loc", args);
		formgen=fromArgs("-formgen", args);
	}

	private String fromArgs(String v, String[] args) {
		for(String a: args) {
			if(a.startsWith(v+":")) {
				return Strings.afterFirst(a, ':');
			}
			
		}
		return null;
	}

	private void run() throws IOException {
		switch(type) {
			case "form": doForm(); break;
		}
	}

	private void doForm() throws IOException {
		switch(formgen) {
		case "jsp": doFormJsp(); break;
		case "xml": doFormXml(); break;
		case "none": break;
		}
	}

	private void doFormXml() throws IOException {
		copyRenameTemplate("cog/src/main/resources/codegen-templates/form_loc_.xml", "_loc_", loc, "cog/web/src/main/webapp/form/");
		copyRenameTemplate("cog/src/main/resources/codegen-templates/snippet__loc_.txt", "_loc_", loc, "cog/web/src/main/webapp/form/");

		switch(feed) {
		case "json": doFeedJson(); break;
		case "xml": doFeedXml(); break;
		}
	}
	
	private void doFormJsp() throws IOException {
		copyRenameTemplate("cog/src/main/resources/codegen-templates/form_loc_.jsp", "_loc_", loc, "cog/web/src/main/webapp/form/");
		copyRenameFilterTemplate("cog/src/main/resources/codegen-templates/snippet__loc_.txt", "_loc_", loc, "cog/web/src/main/webapp/form/");

		switch(feed) {
		case "json": doFeedJson(); break;
		case "xml": doFeedXml(); break;
		}
	}

	private void doFeedXml() throws IOException {
		copyRenameFilterTemplate("cog/src/main/resources/codegen-templates/DHXXMLFeed__loc_.java", "_loc_", loc, "cog/web/src/main/java/com/trabajo/feed/xml/");
		copyRenameFilterTemplate("cog/src/main/resources/codegen-templates/FeedParms__loc_.java", "_loc_", loc, "cog/web/src/main/java/com/trabajo/feed/xml/");
	}

	private void doFeedJson() throws IOException {
		copyRenameFilterTemplate("cog/src/main/resources/codegen-templates/DHXJSONFeed__loc_.java", "_loc_", loc, "cog/web/src/main/java/com/trabajo/feed/json/");
		copyRenameFilterTemplate("cog/src/main/resources/codegen-templates/FeedParms__loc_.java", "_loc_", loc, "cog/web/src/main/java/com/trabajo/feed/json/");
	}
	
	private void copyRenameFilterTemplate(String template, String search, String replace, String destDir) throws IOException {
		String newFileName=Strings.afterLast(template, '/').replace(search, replace);
		
		String destStr=base+ "/" + destDir +newFileName;
		
		Path src=Paths.get(base + "/" + template);
		Path dest=Paths.get(destStr);
		
		String srcContent=IOUtils.stringFromStream(new FileInputStream(base + "/" + template), "UTF-8");
		
		srcContent=srcContent.replaceAll("\\$\\{loc\\}", loc);
		File f=IOUtils.tmpFileFromByteArry(new File("."), "tmp", srcContent.getBytes());
		Files.copy(f.toPath(),  dest, StandardCopyOption.REPLACE_EXISTING);
		f.delete();
	}

	private void copyRenameTemplate(String template, String search, String replace, String destDir) throws IOException {
		
		String newFileName=Strings.afterLast(template, '/').replace(search, replace);
		
		String destStr=base+ "/" + destDir +newFileName;
		
		Path src=Paths.get(base + "/" + template);
		Path dest=Paths.get(destStr);
		
		Files.copy(src,  dest, StandardCopyOption.REPLACE_EXISTING);
		
	}

}

