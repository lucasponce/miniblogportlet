package blog.services;

public class BlogFactory {

	public static BlogAPI getBlog() {
		return new CacheBlogImpl();
	}
	
}
