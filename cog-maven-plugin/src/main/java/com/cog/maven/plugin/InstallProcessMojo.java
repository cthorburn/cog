package com.cog.maven.plugin;

import java.io.File;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name = "install-process")
public class InstallProcessMojo extends AbstractMojo {

	@Parameter(required = true)
	private URL url;

	@Parameter(required = true)
	private String realName;

	@Parameter(required = true)
	private String file;

	public void execute() throws MojoFailureException {
		try {
			File f=new File(file);
			getLog().info("file: "+file+" exists: "+new File(file).exists());

			HttpClient client = new HttpClient();

			PostMethod filePost = new PostMethod(url.toString());
			Part[] parts = { new StringPart("a", "deploy_process"), new StringPart("rn", realName), new FilePart("file", f) };
			filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
			client.executeMethod(filePost);
		} catch (Exception e) {
				this.getLog().info("error contacting server: upload process manually");
		}
	}
}
