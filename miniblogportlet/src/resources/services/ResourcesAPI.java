package resources.services;

import java.util.Set;

import org.apache.commons.fileupload.FileItem;

import resources.Resource;

public interface ResourcesAPI {

	Resource getResource(String blog, String key);
	void setResource(String blog, String key, FileItem file);
	void removeResource(String blog, String key);
	public Set<String> getResources(String blog);
	
	Resource getResource(String blog, String entry, String key);
	void setResource(String blog, String entry, String key, FileItem file);
	void removeResource(String blog, String entry, String key);
	public Set<String> getResources(String blog, String entry);
		
}
