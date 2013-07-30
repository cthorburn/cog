package com.trabajo.engine;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.jcr.RepositoryException;
import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trabajo.DefinitionVersion;
import com.trabajo.utils.Files;
import com.trabajo.utils.IOUtils;
import com.trabajo.utils.Strings;
import com.trabajo.vcl.ChainUpdate;
import com.trabajo.vcl.ClassLoaderChains;

public class FileCacheImpl extends Observable implements FileCache, Observer {

	private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderChains.class);

	private final File location;
	private File classloaders;
	private JackRabbitProcessStore store;

	public FileCacheImpl(final JackRabbitProcessStore store) {
		super();
		this.store = store;
		// TODO config
		this.location = new File(System.getProperty("user.home"), ".trabajo/filecache");
		store.addObserver(this);
		initStructure();
	}

	@Override
	public URL[] resolve(String[] repositoryClassLoaderURLS) {
		URL[] result = new URL[repositoryClassLoaderURLS.length];

		int i = 0;
		for (File f : classLoaderURLsToFiles(repositoryClassLoaderURLS)) {
			try {
				result[i++] = f.toURI().toURL();
			} catch (MalformedURLException e) {
				// impossible
			}
		}
		return result;
	}

	public File classLoaderURLToFile(String url) {
		return new File(classloaders, Strings.afterFirst(Strings.afterFirst(Strings.afterFirst(url, '/'), '/'), '/'));
	}

	public File classLoaderURLToFile(URL url) {
		return classLoaderURLToFile(url.toString());
	}

	public File classLoaderURLToZipDir(URL url) {
		File jar = new File(classloaders, Strings.afterFirst(Strings.afterFirst(Strings.afterFirst(url.toString(), '/'), '/'), '/'));

		StringBuilder sb = new StringBuilder();
		sb.append(jar.getParent());
		sb.append('/');
		sb.append(jar.getName());
		sb.append("_unzip");

		File zipDir = new File(sb.toString());
		return zipDir;

	}

	public File cacheURLToFile(URL url) {
		try {
			return new File(url.toURI());
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public File[] classLoaderURLsToFiles(URL[] classLoaderURLS) {
		File[] result = new File[classLoaderURLS.length];

		for (int i = 0; i < classLoaderURLS.length; i++) {
			result[i] = classLoaderURLToFile(classLoaderURLS[i]);
		}

		return result;
	}

	public File[] classLoaderURLsToFiles(String[] classLoaderURLS) {
		File[] result = new File[classLoaderURLS.length];

		for (int i = 0; i < classLoaderURLS.length; i++) {
			result[i] = classLoaderURLToFile(classLoaderURLS[i]);
		}

		return result;
	}

	public File[] cacheURLsToFiles(URL[] cacheURLs) {
		File[] result = new File[cacheURLs.length];

		for (int i = 0; i < cacheURLs.length; i++) {
			result[i] = cacheURLToFile(cacheURLs[i]);
		}

		return result;
	}

	@Override
	// TODO test whether or not cached
	//TODO method too big!
	public synchronized void load(DefinitionVersion dv) throws RepositoryException {

		String[] strURLs = store.classLoaderURLsForVersion(dv);

		setChanged();
		File[] targets = this.classLoaderURLsToFiles(strURLs);

		// Make the ClassLoaderManager kick out this version
		this.notifyObservers(new ChainUpdate<DefinitionVersion>(ChainUpdate.TYPE.REDEFINE, dv, targets));

		File dir = new File(Strings.beforeLast(targets[0].getAbsolutePath(), File.separatorChar));
		if (!dir.exists()) {
			dir.mkdir();
		}


		for (File f : dir.listFiles()) {
			// remove any files left if the cl jar list has shortened
			try {
				if(f.isDirectory() && f.getAbsolutePath().endsWith("_unzip")) {
					File unzipDir = new File(f.getAbsolutePath() + "_unzip");
					Files.deleteDirectory(unzipDir);
				}
				else {
					java.nio.file.Files.delete(f.toPath());
				}
			} catch (IOException e) {
				LOG.info("File cache: unable to delete file: " + f.getAbsolutePath());
			}
		}

		// TODO there is still an unsynchronized period between unlinking the cl and
		// the cache jars for the new version being available

		int i = 0;
		for (File f : targets) {
			File unzipDir = new File(f.getAbsolutePath() + "_unzip");
			ZipFile zipFile=null;
			
			try {
				IOUtils.copyStream(store.getJarStream(strURLs[i++]), new FileOutputStream(f));
				boolean justCreated = false;
				if (unzipDir.exists() && !justCreated) {
						Files.deleteDirectory(unzipDir);
						unzipDir.mkdir();
						justCreated = true;
				}
				if (!unzipDir.exists()) {
					unzipDir.mkdir();
				}

				try {
					zipFile = new ZipFile(f);
				} catch (IOException e1) {
					LOG.info("File cache: unable to unzip jar to: " + f.getAbsolutePath() + "_unzip");
					throw new RuntimeException(e1);
				}

				Enumeration<? extends ZipEntry> entries = zipFile.entries();

				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) entries.nextElement();

					if (entry.isDirectory()) {
						(new File(unzipDir, entry.getName())).mkdir();
					}
				}
				zipFile.close();

				zipFile = new ZipFile(f);
				entries = zipFile.entries();

				while (entries.hasMoreElements()) {
					ZipEntry entry = (ZipEntry) entries.nextElement();

					if (!entry.isDirectory()) {
						IOUtils.copyStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(new File(unzipDir, entry.getName()))));
					}
				}
				
				LOG.info("File cache: redefined classloader jar file: " + f.getAbsolutePath());
				
			} catch (IOException e) {
				LOG.info("File cache: unable to write file: " + f.getAbsolutePath());
				throw new RuntimeException(e);
			}finally{
				try {
					if(zipFile!=null) {
						zipFile.close();
					}	
				} catch (IOException e) {
					LOG.info("File cache: unable to close zip file: " + f.getAbsolutePath());
				}
			}
		}
	}

	@Override
	public URL[] resolve(URL[] urls) {

		String[] strURLs = new String[urls.length];

		int i = 0;
		for (URL u : urls) {
			strURLs[i++] = u.toString();
		}
		return resolve(strURLs);
	}

	@Override
	public void destroyCreate() {
		Files.deleteDirectory(location);
		initStructure();
	}

	private void initStructure() {
		
		classloaders = new File(location, "classloaders");
		if (!classloaders.exists()) {
			classloaders.mkdirs();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		ClassLoaderDescriptor cld;

		try {
			cld = (ClassLoaderDescriptor) arg;
			load(cld.getVersion());
		} catch (ClassCastException e) {
			return;
		} catch (RepositoryException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public File findFile(URL[] chainURLS, String muggledPath) {
		File result = null;
		for (URL url : chainURLS) {
			File zd = classLoaderURLToZipDir(url);
			result = new File(zd, muggledPath);
			if (result.exists()) {
				break;
			}
		}
		return result;
	}

	@Override
	public List<String> unzipDirNames(DefinitionVersion dv) {
		final List<String> result=new ArrayList<>();
		
		baseDir(dv).listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if(pathname.getName().endsWith("_unzip")) {
						result.add(pathname.getName());
					}
					return false; 
				}
			});
			
			return result;
	}
	
	@Override
	public Map<String, String> unzipDirPaths(DefinitionVersion dv) {
		final Map<String, String> result=new HashMap<>();
		
		baseDir(dv).listFiles(new FileFilter() {
				@Override
				public boolean accept(File pathname) {
					if(pathname.getName().endsWith("_unzip")) {
						result.put(pathname.getName(), pathname.getAbsolutePath());
					}
					return false; 
				}
			});
			
			return result;
	}

	@Override
	public File baseDir(DefinitionVersion dv) {
		return new File(location, "classloaders/"+dv.toString());
	}

	@Override
	public File getFile(DefinitionVersion dv, String path) {
		for(String zipDir: unzipDirPaths(dv).values()) {
			File target=new File(zipDir+File.separator+path);
			
			if(target.exists()) {
				return target;
			}
		}
		return null;
	}

	@Override
	public void obliterate(EntityManager em, SysConfig sc) {
		Files.deleteDirectory(this.location);
		initStructure();
	}

	@Override
	public void obliterate(EntityManager em, DefinitionVersion dv, SysConfig sc) {
		Files.deleteDirectory(baseDir(dv));
	}
}
