package com.trabajo.engine;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;
import javax.persistence.EntityManager;

import org.apache.jackrabbit.rmi.repository.URLRemoteRepository;
import org.apache.jackrabbit.value.BinaryValue;

import com.trabajo.DefinitionVersion;
import com.trabajo.IProcessServiceProperties;
import com.trabajo.ValidationException;
import com.trabajo.engine.ClassLoaderUploadManager.ClassLoaderUpload;
import com.trabajo.process.ServiceInfo;
import com.trabajo.utils.IOUtils;
import com.trabajo.utils.Strings;
import com.trabajo.values.Category;
import com.trabajo.values.Description;
import com.trabajo.vcl.ChainUpdate;

public class JackRabbitProcessStore extends Observable implements Observer {
	public String ROOT = "cog1000";

	private Repository repository;
	private ThreadLocal<Session> session;
	private ThreadLocal<Node> root;
	private String jcrUrl;
	private FileSystem fs;

	public JackRabbitProcessStore(String jcrUrl, FileSystem fs) {
		super();
		this.jcrUrl = jcrUrl;
		session = new ThreadLocal<>();
		root = new ThreadLocal<>();
		this.fs = fs;
	}

	public synchronized void disconnect() {
		// TODO the JCR store should be a per session notion rather than relying on
		// ThreadLocals
		// TODO identify & configure an optimum data store type
	}

	public synchronized void connect() {
		try {

			if (repository == null) {
				repository = new URLRemoteRepository(jcrUrl);
			}

			if (session.get() == null) {
				SimpleCredentials adminCred = new SimpleCredentials("admin", "admin".toCharArray());
				session.set(repository.login(adminCred));
				root.set(session.get().getRootNode());
				if (!standardPathsExist()) {
					createStandardPaths();
				}
			}
		} catch (MalformedURLException | RepositoryException e) {
			throw new RuntimeException(e);
		}
	}

	private void createStandardPaths() throws RepositoryException {
		ensureFollowingPath(root.get(), ROOT, "classloaders");
		session.get().save();
	}

	private boolean standardPathsExist() throws RepositoryException {
		boolean result = false;

		try {
			root.get().getNode(ROOT);
			result = true;
		} catch (PathNotFoundException e) {
			// ignore
		}

		return result;
	}

	public void archive(ProcessJar pj, String originalJarName) throws ValidationException {
		ProcessClassMetadata pcm = pj.getClassMetadata();
		ClassLoaderUploadManager.ClassLoaderUpload clu = new ClassLoaderUploadManager.ClassLoaderUpload(pcm.getVersion(), pcm.getCategory(), pcm.getDescription());
		clu.addUpload(pj.getFile().toPath(), originalJarName, true);
		archive(clu);
	}

	public ProcessJar createProcessJar(DefinitionVersion dv) throws RepositoryException, IOException {
		connect();
		String[] urls = classLoaderURLsForVersion(dv);
		InputStream is = getJarStream(urls[0]);
		byte[] jarBytes = IOUtils.bytesFromStream(is);
		ProcessJar pj = null;
		try {
			pj = new ProcessJar(jarBytes);
		} catch (ValidationException e) {
			// AlreadyValidated
		}
		pj.setDependencies(urls);
		return pj;
	}

	public void archive(ClassLoaderUpload clu) {
		try {
			connect();

			DefinitionVersion dv = clu.getVersion();
			Category category = clu.getCatgeory();
			Description description = clu.getDesc();
			List<ClassLoaderUploadManager.ClassLoaderUpload.JarUpload> jarUploads = clu.getJars();

			String name = dv.shortName();
			String version = dv.version().toString();
			Node versionNode = ensureFollowingPath(root.get(), ROOT, "classloaders", name, version);

			versionNode.setProperty("per_process_class_list", "");
			versionNode.setProperty("category", category.getValue());
			versionNode.setProperty("description", description.getValue());

			String[] repoURLs = new String[jarUploads.size()];

			Properties p = null;

			int i = 0;
			for (ClassLoaderUploadManager.ClassLoaderUpload.JarUpload ju : jarUploads) {
				try {
					String jarName = "jar" + i;
					Node jarNode = ensureFollowingPath(versionNode, jarName);
					copyURLBinaryToNodeBinaryProperty(jarNode, "bytes", ju.target.toUri().toURL());
					repoURLs[i++] = translate(dv, jarName);
					ManifestHandler mh = new ManifestHandler(fs.getManifestHandlerTmpDir(), ju.target);
					if (mh.getServiceFactoryClassName() != null) {
						ServicePropertiesHandler sph = new ServicePropertiesHandler(fs.getManifestHandlerTmpDir(), ju.target);
						p = sph.getServiceProperties();
						p.setProperty(ServiceInfo.PROP_IMPL_FACTORY_CLASS, mh.getServiceFactoryClassName());
					}
				} catch (Exception e) {
					throw new IllegalStateException("Process store: failed to write JAR: " + ju.originalFileName);
				}
			}

			versionNode.setProperty("urls", repoURLs);
			versionNode.setProperty("originalJarNames", clu.getOriginalNames());
			versionNode.setProperty("properties", propsToString(p == null ? new Properties() : p));
			session.get().save();

			setChanged();
			notifyObservers(getClassLoaderDescriptor(dv));
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
	}

	private String propsToString(Properties props) {
		StringWriter sw = new StringWriter();

		try {
			props.store(sw, "");
		} catch (IOException e) {
			// ignore, StringWriter
		}
		return sw.toString();
	}

	private Properties propsFromString(String s) {
		Properties props = new Properties();
		try {
			props.load(new StringReader(s));
		} catch (IOException e) {
			// ignore, StringReader
		}
		return props;
	}

	private String translate(DefinitionVersion dv, String jarName) {
		StringBuilder sb = new StringBuilder();

		sb.append("TRABAJO://classloader/");
		sb.append(dv.shortName());
		sb.append("-");
		sb.append(dv.version().toString());
		sb.append("/");
		sb.append(jarName);
		return sb.toString();
	}

	public String[] originalJarNamesForVersion(DefinitionVersion dv) {
		try {
			connect();

			try {
				Node versionNode = getVersionNode(dv);
				Value[] values = versionNode.getProperty("originalJarNames").getValues();

				String[] result = new String[values.length];

				int i = 0;
				for (Value v : values) {
					result[i++] = v.getString();
				}

				return result;
			} catch (PathNotFoundException e) {
				throw new RuntimeException(e);
			}

		} catch (RepositoryException | IllegalStateException e) {
			throw new RuntimeException(e);
		}
	}

	public String[] classLoaderURLsForVersion(DefinitionVersion dv) throws RepositoryException {
		connect();

		Node versionNode = getVersionNode(dv);
		Value[] values = versionNode.getProperty("urls").getValues();

		String[] urls = new String[values.length];

		int i = 0;
		for (Value v : values) {
			urls[i++] = v.getString();
		}

		return urls;
	}

	/**
	 * eg: TRABAJO://classloader/JackRabbit-2.4.2/jackrabbit-standalone-2.4.2.jar
	 */

	public InputStream getJarStream(String url) {
		connect();
		// TRABAJO://classloader/x-1.0.0/jar1

		String name = Strings.afterLast(url, '/');
		String before = Strings.beforeLast(url, '/');
		DefinitionVersion dv = DefinitionVersion.parse(Strings.afterLast(before, '/'));

		try {
			// TODO SB
			String path = ROOT + "/classloaders/";
			// TODO SB
			Node jarNode = root.get().getNode(path + dv.shortName() + "/" + dv.version().toString() + "/" + name);
			Binary b = jarNode.getProperty("bytes").getBinary();
			return b.getStream();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void setPerProcessClasses(DefinitionVersion dv, Set<String> perProcessClasses) throws RepositoryException {

		StringBuilder sb = new StringBuilder();

		for (String s : perProcessClasses) {
			sb.append(s);
			sb.append(',');
		}

		if (sb.length() > 0) {
			sb.setLength(sb.length() - 1);
		}

		Node versionNode = getVersionNode(dv);
		versionNode.setProperty("per_process_class_list", sb.toString());
	}

	public List<String> getPerProcessClasses(DefinitionVersion dv) throws RepositoryException {
		Node versionNode = getVersionNode(dv);
		return Arrays.asList(versionNode.getProperty("per_process_class_list").getString().split(","));
	}

	// TODO use JCR Querying

	public String[] getRegisteredProcessNames() {
		connect();
		List<String> names = new ArrayList<>();
		try {
			Node clsNode = root.get().getNode(ROOT + "/classloaders");
			NodeIterator nr = clsNode.getNodes("*");

			while (nr.hasNext()) {
				Node n = nr.nextNode();
				Node clNode = clsNode.getNode("*");

				if (clNode.hasProperty("per_process_class_list")) {
					names.add(n.getName() + "-" + clNode.getName());
				}
			}

		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}

		return names.toArray(new String[names.size()]);
	}

	// TODO use JCR Querying

	public String[] getRegisteredClassLoaderNames() {
		connect();
		List<String> names = new ArrayList<>();
		try {
			Node clsNode = root.get().getNode(ROOT + "/classloaders");
			NodeIterator nr = clsNode.getNodes("*");

			while (nr.hasNext()) {
				Node n = nr.nextNode();
				Node clNode = clsNode.getNode("*");
				names.add(n.getName() + "-" + clNode.getName());
			}

		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}

		return names.toArray(new String[names.size()]);
	}

	private void copyURLBinaryToNodeBinaryProperty(Node jarNode, String propName, URL url) throws IOException, ValueFormatException, VersionException,
			LockException, ConstraintViolationException, RepositoryException {
		InputStream fileIn = url.openStream();
		jarNode.setProperty(propName, new BinaryValue(fileIn));
		fileIn.close();
	}

	private Node ensureFollowingPath(Node node, String... path) throws RepositoryException {
		Node current = node;

		for (String elm : path) {
			Node exists;
			try {
				exists = current.getNode(elm);
			} catch (PathNotFoundException e) {
				exists = current.addNode(elm);
			}
			current = exists;
		}

		return current;
	}

	private Node getVersionNode(DefinitionVersion dv) throws RepositoryException {
		return root.get().getNode(ROOT + "/classloaders/" + dv.shortName() + "/" + dv.version().toString());
	}

	public ClassLoaderDescriptor getClassLoaderDescriptor(DefinitionVersion dv) throws RepositoryException {
		connect();

		Node versionNode = getVersionNode(dv);

		ClassLoaderDescriptor cld = new ClassLoaderDescriptor();

		cld.setVersion(dv);
		try {
			cld.setCategory(new Category(versionNode.getProperty("category").getString()));
			cld.setDescription(new Description(versionNode.getProperty("description").getString(), 255));
		} catch (ValidationException e) {
			// ignore already validated
		}
		cld.setPerProcessClasses(getPerProcessClasses(dv));
		cld.setUrls(classLoaderURLsForVersion(dv));
		cld.setOriginalJarNames(originalJarNamesForVersion(dv));
		try {
			cld.setProperties(propsFromString(versionNode.getProperty("properties").getString()));
		} catch (PathNotFoundException e) {
			cld.setProperties(new Properties());
		}
		return cld;
	}

	public Properties getServiceProperties(DefinitionVersion dv) throws RepositoryException {
		return propsFromString(getVersionNode(dv).getProperty("properties").getString());
	}

	public boolean processExists(DefinitionVersion dv) throws RepositoryException {
		connect();
		boolean exists;
		try {
			getVersionNode(dv);
			exists = true;
		} catch (PathNotFoundException e) {
			exists = false;
		}

		return exists;
	}

	public void obliterate(EntityManager em, DefinitionVersion dv, SysConfig sc) throws RepositoryException {
		connect();
		try {
			getVersionNode(dv).remove();
		} catch (PathNotFoundException e) {
			// ignore
		}
	}

	public IProcessServiceProperties getProcessServiceProperties(DefinitionVersion dv) throws RepositoryException {
		connect();
		IProcessServiceProperties result;
		Node processNode = getVersionNode(dv);
		try {
			result = ProcessServiceProperties.parse(processNode.getProperty("serviceProperties").getString());
		} catch (PathNotFoundException e) {
			result = null;
		}
		return result;
	}

	public void setProcessServiceProperties(IProcessServiceProperties psp) throws RepositoryException {
		connect();
		Node processNode = getVersionNode(psp.getProcessVersion());
		processNode.setProperty("serviceProperties", psp.toString());
		session.get().save();
	}

	public void setProcessServiceProperty(DefinitionVersion processDv, DefinitionVersion serviceDv, String key, String value) throws RepositoryException {
		IProcessServiceProperties psp=getProcessServiceProperties(processDv);
		if(psp==null) {
			psp=new ProcessServiceProperties(processDv);
		}
		
		psp.setServiceProperty(serviceDv, key, value);
		setProcessServiceProperties(psp);
		session.get().save();
	}

	private void purge(DefinitionVersion dv)  throws RepositoryException {
		connect();
		try{
			getVersionNode(dv).remove();
			session.get().save();
		}catch(PathNotFoundException e) {
			//ignore, removing it anyway;
		}		
	}

	@Override
	public void update(Observable o, Object arg) {
		try {
			@SuppressWarnings("unchecked")
			ChainUpdate<DefinitionVersion> cu=(ChainUpdate<DefinitionVersion>)arg;
			
			switch(cu.type()) {
			case DELETE:
			{
				purge(cu.getClassLoaderVersion());
				break;
			}
			default:
				break;
			}	
		} catch (ClassCastException e) {
			//ignore
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
	}
}
