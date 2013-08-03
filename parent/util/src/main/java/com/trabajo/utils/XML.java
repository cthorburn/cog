package com.trabajo.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XML {

	public static ErrorHandler DEFAULT_ERROR_HANDLER=new ErrorHandler() {
			@Override
			public void error(SAXParseException exception) throws SAXException {
				throw new IllegalStateException(exception);
			}
	
			@Override
			public void fatalError(SAXParseException exception) {
				throw new IllegalStateException(exception);
			}
	
			@Override
			public void warning(SAXParseException exception) {
				throw new IllegalStateException(exception);
			}
		};

	
	
	private XML() {
	}

	public static Document parseNSAware(InputStream is) {
		try {
			return newNSAwareDocumentBuilder().parse(is);
		} catch (SAXException e) {
			throw new IllegalArgumentException();
		} catch (IOException e) {
			throw new IllegalArgumentException();
		}
	}

	public static DocumentBuilder newNSAwareDocumentBuilder() {
		try {
			DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			DocumentBuilder db=dbf.newDocumentBuilder();
			db.setErrorHandler(DEFAULT_ERROR_HANDLER);
			return db;
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException();
		}
	}

	public static DocumentBuilder newNSAwareValidatingDocumentBuilder(Schema schema) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			dbf.setSchema(schema);
			DocumentBuilder db=dbf.newDocumentBuilder();
			db.setErrorHandler(DEFAULT_ERROR_HANDLER);
			return db;
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}
	}

	public static XPath newXPath(NamespaceContext nsContext) {
		XPath xp=newXPath();
		xp.setNamespaceContext(nsContext);
		return xp;
	}

	public static XPath newXPath() {
		return XPathFactory.newInstance().newXPath();
	}

	public static String string(NamespaceContext nsContext, String xPathExpression, Object target) {
		try {
			return newXPath(nsContext).evaluate(xPathExpression, target);
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException();
		}
	}

	public static String string(String xPathExpression, Object target) {
		try {
			return newXPath().evaluate(xPathExpression, target);
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException();
		}
	}

	public static NodeList nodeList(NamespaceContext nsContext, String xPathExpression, Object target) {
		try {
			return (NodeList) newXPath(nsContext).evaluate(xPathExpression, target, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException();
		}
	}

	public static NodeList nodeList(String xPathExpression, Object target) {
		try {
			return (NodeList) newXPath().evaluate(xPathExpression, target,
					XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException();
		}
	}


	public static List<String> nodeListText(NamespaceContext nsContext, String xPathExpression, Object target) {
		try {
			return nodeListTextContent((NodeList) newXPath(nsContext).evaluate(xPathExpression, target, XPathConstants.NODESET));
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException();
		}
	}

	public static List<String> nodeListText(String xPathExpression, Object target) {
		try {
			return nodeListTextContent((NodeList) newXPath().evaluate(xPathExpression, target, XPathConstants.NODESET));
		} catch (XPathExpressionException e) {
			throw new IllegalArgumentException();
		}
	}

	private static List<String> nodeListTextContent(NodeList nl) {
		List<String> result = new ArrayList<String>();

		for (int i = 0; i < nl.getLength(); i++) {
			Node n = nl.item(i);
			result.add(n.getTextContent().trim());
		}
		return result;
	}

	public static int _int(NamespaceContext nsContext, String xPathExpression, Object target) {
		return Integer.parseInt(string(nsContext, xPathExpression, target).trim());
	}

	public static int _int(String xPathExpression, Object target) {
		return Integer.parseInt(string(xPathExpression, target).trim());
	}

	public static Document newDocument() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			throw new IllegalStateException(e);
		}
	}

	public static Document newDocument(String rootElementName) {
		Document doc = newDocument();
		doc.appendChild(doc.createElement(rootElementName));
		return doc;
	}

	public static Document newDocument(String nameSpace, String rootElementName) {
		Document doc = newDocument();
		doc.appendChild(doc.createElementNS(nameSpace, rootElementName));
		return doc;
	}


	public static Element addChildElementOfName(String tagName, Element parentElm) {
		Element elm = parentElm.getOwnerDocument().createElement(tagName);
		parentElm.appendChild(elm);
		return elm;
	}

	public static Schema schemaFromFile(File f) {
		try {
			Schema schema=SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(f);
			return schema;
		} catch (SAXException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static Schema schemaFromResource(String resource) {
		try {
			InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
			Schema schema=SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(is));
			return schema;
		} catch (SAXException e) {
			throw new IllegalStateException(e);
		}
	}
	
	public static void docToStream(Document doc, OutputStream os) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("indent", "yes");
			transformer.transform(new DOMSource(doc), new StreamResult(os));	
			os.close();
		} catch (TransformerConfigurationException e) {
			throw new IllegalStateException(e);
		} catch (TransformerException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	public static Schema schemaFromString(String s) {
		try {
			Schema schema=SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new StreamSource(new ByteArrayInputStream(s.getBytes())));
			return schema;
		} catch (SAXException e) {
			throw new IllegalStateException(e);
		}
	}

	public static Element element(NamespaceContext nsContext, String xPathExpression, Node target) throws XPathExpressionException {
		return (Element)newXPath(nsContext).evaluate(xPathExpression, target, XPathConstants.NODE);
	}

}
