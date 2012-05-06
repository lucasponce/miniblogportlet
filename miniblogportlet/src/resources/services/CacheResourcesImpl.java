package resources.services;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.apache.commons.fileupload.FileItem;
import org.apache.log4j.Logger;
import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.jmx.CacheJmxWrapperMBean;
import org.jboss.mx.util.MBeanServerLocator;

import resources.Resource;

public class CacheResourcesImpl implements ResourcesAPI {

	@SuppressWarnings("rawtypes")
	public Cache resourcesCache = null;
	public final String CACHE_NAME="jboss.cache:service=MiniBlogCache";	
	
	private static final Logger log = Logger.getLogger(CacheResourcesImpl.class);
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Resource getResource(String blog, String key) {
		checkCache();
		if (resourcesCache != null) {
			String fqnstr = "/blog/" + blog + "/resources/blog/" + key;
			Fqn fqn = Fqn.fromString(fqnstr);
			Resource resource = (Resource)resourcesCache.get(fqn, key);
			return resource;
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setResource(String blog, String key, FileItem file) {
		checkCache();
		if (resourcesCache != null) {
			String fqnstr = "/blog/" + blog + "/resources/blog/" + key;
			Fqn fqn = Fqn.fromString(fqnstr);
			Resource resource = convert(file);
			resourcesCache.put(fqn, key, resource);								
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void removeResource(String blog, String key) {
		checkCache();
		if (resourcesCache != null) {
			String fqnstr = "/blog/" + blog + "/resources/blog/" + key;
			Fqn fqn = Fqn.fromString(fqnstr);
			resourcesCache.remove(fqn, key);	
			resourcesCache.removeNode(fqn);
		}		
	}

	@SuppressWarnings("unchecked")
	public Set<String> getResources(String blog) {
		checkCache();
		if (resourcesCache != null) {
			return resourcesCache.getChildrenNames("/blog/" + blog + "/resources/blog");
		}
		return null;
	}

	// Storing resources ordered by entries
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Resource getResource(String blog, String entry, String key) {
		checkCache();
		if (resourcesCache != null) {
			String fqnstr = "/blog/" + blog + "/resources/" + entry + "/" + key;
			Fqn fqn = Fqn.fromString(fqnstr);
			Resource resource = (Resource)resourcesCache.get(fqn, key);
			return resource;
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setResource(String blog, String key, String entry, FileItem file) {
		checkCache();
		if (resourcesCache != null) {
			String fqnstr = "/blog/" + blog + "/resources/" + entry + "/" + key;
			Fqn fqn = Fqn.fromString(fqnstr);
			Resource resource = convert(file);
			resourcesCache.put(fqn, key, resource);								
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void removeResource(String blog, String entry, String key) {
		checkCache();
		if (resourcesCache != null) {
			String fqnstr = "/blog/" + blog + "/resources/" + entry + "/" + key;
			Fqn fqn = Fqn.fromString(fqnstr);
			resourcesCache.remove(fqn, key);	
			resourcesCache.removeNode(fqn);
		}		
	}

	@SuppressWarnings("unchecked")
	public Set<String> getResources(String blog, String entry) {
		checkCache();
		if (resourcesCache != null) {
			return resourcesCache.getChildrenNames("/blog/" + blog + "/resources/" + entry);
		}
		return null;
	}
		
	// Private method to support publics methods
	
	@SuppressWarnings("rawtypes")
	private void checkCache()  {
		if (resourcesCache == null) {
			try {
				log.debug("Creating API to Cache: " + CACHE_NAME);
				MBeanServer server = MBeanServerLocator.locateJBoss();
				ObjectName on = new ObjectName(CACHE_NAME);
				CacheJmxWrapperMBean cacheWrapper = 
						(CacheJmxWrapperMBean) MBeanServerInvocationHandler
						.newProxyInstance(server, on, CacheJmxWrapperMBean.class, false);
				resourcesCache = cacheWrapper.getCache();
			} catch(Exception e) {
				log.error("Error creating Cache " + e.toString() );
				e.printStackTrace();
			}
		}
	}
	
	private Resource convert(FileItem f) {
		if (f==null) return null;
		
		Resource resource = new Resource();
		resource.setName(f.getName());
		resource.setContentType(f.getContentType());
		resource.setSize(f.getSize());

		BufferedInputStream input = null;
		ByteArrayOutputStream output = null;
		
		try {
			input = new BufferedInputStream(f.getInputStream());		
			output = new ByteArrayOutputStream((int)f.getSize());
			byte[] buffer = new byte[16384];
		    for (int length = 0; (length = input.read(buffer)) > 0;) {
		        output.write(buffer, 0, length);
		    }
	    
		    input.close();
		    output.flush();
		    output.close();
		    
		    resource.setContent(output.toByteArray());
		
		} catch (IOException e) {
			log.error("Error reading file " + f.getName());
			e.printStackTrace();
		}
	    
		return resource;
	}

}
