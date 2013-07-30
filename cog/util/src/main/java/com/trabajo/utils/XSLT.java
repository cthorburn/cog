package com.trabajo.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

public class XSLT {
	private static Map<String, Templates> xsltAliases=Collections.synchronizedMap(new HashMap<String, Templates>());

  private static TransformerFactory factory;
  
  private static IResources resources;
  
  static{
      factory=TransformerFactory.newInstance();
      try {
				addTemplate("Identity", new StreamSource(Thread.currentThread().getContextClassLoader().getResourceAsStream("/Identity.xslt")));
			} catch (TransformerConfigurationException e) {
				throw new IllegalStateException(e);
			}
  }

  protected XSLT() {}

  public static class XFormParms {
  	private Map<String, ? super Object> parms=new HashMap<String, Object>();

		public XFormParms set(String key, Object value) {
			parms.put(key, value);
			return this;
		}

		public void applyTo(Transformer t) {
			t.clearParameters();
			
			for(Map.Entry<String, ? super Object> parm: parms.entrySet()) {
				String k=parm.getKey();
				Object v=parm.getValue();
				t.setParameter(k, v);
			}
		}
	}

  public static String xformDocumentToStringUsingResourcesAliasXSLT(Document data, String alias, Writer writer, XFormParms parms) {
  	StringWriter sw=new StringWriter();
  	xformDocumentToWriterUsingResourcesAliasXSLT(data, alias, sw, parms);
  	return sw.toString();
  }	
  
  public static String xformDocumentToStringUsingResourcesAliasXSLT(Document data, String alias, Writer writer) {
  	return xformDocumentToStringUsingResourcesAliasXSLT(data, alias, writer, null);
  }	
  
 	public static void xformDocumentToWriterUsingResourcesAliasXSLT(Document data, String alias, Writer writer) {
  	xformDocumentToWriterUsingResourcesAliasXSLT(data, alias, writer, null);
  }
  
  public static void xformDocumentToWriterUsingResourcesAliasXSLT(Document data, String alias, Writer writer, XFormParms parms) {
    DOMSource xml=new DOMSource(data);
    StreamResult result=new StreamResult(new NonClosingWriter(writer)); 

    Templates templates=xsltAliases.get(alias);
    
    if(templates==null) {
      try {
        templates=addTemplate(alias, new StreamSource(resources.asStream(alias)));
      } catch (TransformerConfigurationException e) {
        throw new IllegalStateException(e);
      }
    }
    transform(templates, xml, result, parms);
  }
  	
  public static void print(Document doc) {
    xformDocumentToWriterUsingResourcesAliasXSLT(doc, "Identity", new PrintWriter(System.out));
  }
  
  private static void transform(Templates templates, Source xml, Result result, XFormParms parms) {
    try {
    	Transformer t=templates.newTransformer();

    	if(parms!=null) {
    		parms.applyTo(t);
    	}
      
    	t.transform(xml, result);
    } catch (TransformerConfigurationException e) {
      throw new IllegalStateException(e);
    } catch (TransformerException e) {
      throw new IllegalStateException(e);
    }
  }

  public static Templates addTemplate(String alias, StreamSource source) throws TransformerConfigurationException {
    Templates templates=factory.newTemplates(source);
    xsltAliases.put(alias, templates);
    return templates;
  }

  public static class NonClosingWriter extends Writer {

    private Writer writer;
    
    public NonClosingWriter(Writer writer) {
      this.writer=writer;
    }

    @Override
    public Writer append(char c) throws IOException {
      return writer.append(c);
    }

    @Override
    public Writer append(CharSequence csq, int start, int end) throws IOException {
      return writer.append(csq, start, end);
    }

    @Override
    public Writer append(CharSequence csq) throws IOException {
      return writer.append(csq);
    }

    @Override
    public void close() throws IOException {
    	//prevent close
    }

    @Override
    public void flush() throws IOException {
      writer.flush();
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
      writer.write(cbuf, off, len);
    }

    @Override
    public void write(char[] cbuf) throws IOException {
      writer.write(cbuf);
    }

    @Override
    public void write(int c) throws IOException {
      writer.write(c);
    }

    @Override
    public void write(String str, int off, int len) throws IOException {
      writer.write(str, off, len);
    }

    @Override
    public void write(String str) throws IOException {
      writer.write(str);
    }
  }
}
