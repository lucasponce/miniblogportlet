package resources.services;

public class ResourcesFactory {

	public static ResourcesAPI getResources() {
		return new CacheResourcesImpl();
	}
	
}
