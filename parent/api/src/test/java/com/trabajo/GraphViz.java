package com.trabajo;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class GraphViz {

	
	public static void generate(String gvLocation, Writer w, String digraph) throws IOException, TransformerException {
		
		System.out.println("digraph: " +digraph);
		ProcessBuilder pb=new ProcessBuilder(gvLocation+"/bin/dot", "-Tsvg");
		
		Process proc=pb.start();
		BufferedOutputStream bos=new  BufferedOutputStream(proc.getOutputStream());
		bos.write(digraph.getBytes());
		bos.close();
		
		TransformerFactory transformerFactory=TransformerFactory.newInstance();
		InputStream xsl=Thread.currentThread().getContextClassLoader().getResourceAsStream("xsl/visualizer.xsl");
		
//		BufferedReader bre=new BufferedReader(new InputStreamReader(proc.getErrorStream()));
//		
//		for(String l=bre.readLine(); l!=null; ) {
//			System.out.println(l);
//			l=bre.readLine();
//		}
//		bre.close();
				
		xsl=Thread.currentThread().getContextClassLoader().getResourceAsStream("xsl/visualizer.xsl");
		Transformer t=transformerFactory.newTransformer(new StreamSource(xsl));
		xsl.close();

		t.transform(new StreamSource(proc.getInputStream()), new StreamResult(w));

//		proc=pb.start();
//		bos=new  BufferedOutputStream(proc.getOutputStream());
//		bos.write(digraph.getBytes());
//		bos.close();
//		
//		bre=new BufferedReader(new InputStreamReader(proc.getErrorStream()));
//		
//		for(String l=bre.readLine(); l!=null; ) {
//			System.out.println(l);
//			l=bre.readLine();
//		}
//		bre.close();
//		
//		t.transform(new StreamSource(proc.getInputStream()), new StreamResult(System.out));
		
	}
}
