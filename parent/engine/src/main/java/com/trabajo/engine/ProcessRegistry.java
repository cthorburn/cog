package com.trabajo.engine;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

import javax.jcr.RepositoryException;
import javax.persistence.EntityManager;

import com.trabajo.DefinitionVersion;
import com.trabajo.IProcessServiceProperties;
import com.trabajo.ValidationException;
import com.trabajo.engine.ClassLoaderUploadManager.ClassLoaderUpload;
import com.trabajo.engine.clazz.oops.Analyzer2;
import com.trabajo.vcl.ChainUpdate;
import com.trabajo.vcl.ClassLoaderChainProvider;

public class ProcessRegistry extends Observable implements ClassLoaderChainProvider<DefinitionVersion>, Observer {

	private JackRabbitProcessStore store;
	public  FileCache fileCache;
	
	public ProcessRegistry(JackRabbitProcessStore store, FileCache fc)  {
		super();
		this.store=store;
		this.fileCache=fc;
		addObserver(store);
		addObserver(fileCache);
	}

	public void deployProcess(ProcessJar pj, String ojn) throws ValidationException {
		store.archive(pj, ojn);
	}

	public ProcessClassMetadata getProcessClassMetadata(DefinitionVersion dv) throws RepositoryException, IOException {
	    return store.createProcessJar(dv).getClassMetadata();
	}

	public DefinitionVersion getContextKey() {
		return null;
	}

	public URL[] jarURLsForVersion(DefinitionVersion dv) throws RepositoryException {
        return fileCache.resolve(store.classLoaderURLsForVersion(dv));
	}


	public File findFile(DefinitionVersion processDV, String muggledPath) throws RepositoryException {
		return fileCache.findFile(jarURLsForVersion(processDV), muggledPath);
	}

	
    public List<String> fixupListFor(DefinitionVersion dv) throws RepositoryException {
        return store.getPerProcessClasses(dv);
    }

    public void deployClassLoader(ClassLoaderUpload clu) {
        try{
            store.archive(clu);
            Set<String> perProcessClasses = performClassLoaderAnalysis(clu.version);
            store.setPerProcessClasses(clu.version, perProcessClasses);            
        }catch(Exception e) {
        	throw new IllegalStateException(e);
        }    
    }

    Set<String> performClassLoaderAnalysis(DefinitionVersion dv) throws RepositoryException {
    		fileCache.load(dv);
        File[] jars=fileCache.classLoaderURLsToFiles(store.classLoaderURLsForVersion(dv));
        Analyzer2.Analysis a=new Analyzer2(jars).getAnalysis();
        System.out.println(a.toString());
        return a.asFixList();
    }

    public String[] getRegisteredProcessNames() {
        return store.getRegisteredProcessNames();
    }

    public String[] getRegisteredClassloaderNames() {
        return store.getRegisteredClassLoaderNames();
    }

		public JackRabbitProcessStore getStore() {
			return store;
		}

		public ClassLoaderDescriptor getClassLoaderDescriptor(DefinitionVersion version) {
			try {
				return store.getClassLoaderDescriptor(version);
			} catch (RepositoryException e) {
				throw new RuntimeException(e);
			}
		}

		public Properties getServiceProperties(DefinitionVersion dv)  throws RepositoryException {
			return store.getServiceProperties(dv);
		}

		public FileCache getCache() {
			return fileCache;
		}

		public boolean processExists(DefinitionVersion dv) throws RepositoryException {
			return store.processExists(dv);
		}

		public IProcessServiceProperties getProcessServiceProperties(DefinitionVersion dv) throws RepositoryException {
			return store.getProcessServiceProperties(dv);
		}

		public void setProcessServiceProperties(IProcessServiceProperties psp) throws RepositoryException {
			store.setProcessServiceProperties(psp);
		}

		public void setProcessServiceProperty(DefinitionVersion processDv, DefinitionVersion serviceDv, String key, String value) throws RepositoryException {
			store.setProcessServiceProperty(processDv, serviceDv, key, value);
		}

		public synchronized void purgeProcess(EntityManager em, DefinitionVersion dv)  throws RepositoryException {
			setChanged();
			notifyObservers(new ChainUpdate<DefinitionVersion>(ChainUpdate.TYPE.PURGE_PROCESS, dv));
		}

		@Override
		public void update(Observable o, Object arg) {
			// nothing to do yet
		}

		public void purgeClassLoader(EntityManager entityManager, DefinitionVersion dv)  throws RepositoryException {
			setChanged();
			notifyObservers(new ChainUpdate<DefinitionVersion>(ChainUpdate.TYPE.PURGE_CLASSLOADER, dv));
    }
		
		public void purgeService(EntityManager entityManager, DefinitionVersion dv)  throws RepositoryException {
			setChanged();
			notifyObservers(new ChainUpdate<DefinitionVersion>(ChainUpdate.TYPE.PURGE_SERVICE, dv));
			//after all service stuff is processed we'just left with a classloader
			setChanged();
			notifyObservers(new ChainUpdate<DefinitionVersion>(ChainUpdate.TYPE.PURGE_CLASSLOADER, dv));
    }
}
