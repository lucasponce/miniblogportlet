package blog.services;

import java.util.ArrayList;
import java.util.Set;

import javax.ejb.Remote;

import blog.Blog;
import blog.Comment;
import blog.Entry;

// TODO Change to @Local once test phase finished
@Remote
public interface BlogAPI {

	ArrayList<Blog> getBlogs();
	Blog getBlog(String name);
	
	int getNumberEntries(String blogName);
	ArrayList<Entry> getEntries(String blogName, int n1, int n2);

	int getNumberComments(String blogName, String entryName);
	ArrayList<Comment> getComments(String blogName, String entryName, int n1, int n2);
	
	Set<String> getTags(String blogName);

	int getNumberEntries(String blogName, String tag);
	ArrayList<Entry> getEntries(String blogName, String tag, int n1, int n2);
	
	Entry getEntry(String blogName, String entryName);
	
	void addBlog(Blog newBlog);
	void addEntry(String blogName, Entry newEntry);
	void addComment(String blogName, String entryName, Comment newComment);
	
	void editBlog(String oldBlog, Blog editBlog);
	
	void removeBlog(String blogName);
	void removeEntry(String blogName, String entryName);
	void removeComment(String blogName, String entryName, String uuid);
		
}
