package blog.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.management.MBeanServer;
import javax.management.MBeanServerInvocationHandler;
import javax.management.ObjectName;

import org.apache.log4j.Logger;
import org.jboss.cache.Cache;
import org.jboss.cache.Fqn;
import org.jboss.cache.jmx.CacheJmxWrapperMBean;
import org.jboss.mx.util.MBeanServerLocator;

import blog.Blog;
import blog.Comment;
import blog.Entry;

public class CacheBlogImpl implements BlogAPI {

	private static final Logger log = Logger.getLogger(CacheBlogImpl.class);
	@SuppressWarnings("rawtypes")
	public Cache blogCache = null;
	public final String CACHE_NAME = "jboss.cache:service=MiniBlogCache";

	@SuppressWarnings("rawtypes")
	private void checkCache() {
		if (blogCache == null) {
			try {
				log.debug("Creating API to Cache: " + CACHE_NAME);
				MBeanServer server = MBeanServerLocator.locateJBoss();
				ObjectName on = new ObjectName(CACHE_NAME);
				CacheJmxWrapperMBean cacheWrapper = (CacheJmxWrapperMBean) MBeanServerInvocationHandler
						.newProxyInstance(server, on,
								CacheJmxWrapperMBean.class, false);
				blogCache = cacheWrapper.getCache();
			} catch (Exception e) {
				log.error("Error creating Cache " + e.toString());
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Blog> getBlogs() {
		checkCache();
		if (blogCache != null) {
			ArrayList<Blog> result = new ArrayList<Blog>();
			for (String child : (Set<String>) blogCache
					.getChildrenNames("/blog")) {
				String fqnstrchild = "/blog/" + child;
				Fqn fqn = Fqn.fromString(fqnstrchild);
				Blog blog = (Blog) blogCache.get(fqn, "blog");
				result.add(blog);
			}

			return result;
		}
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Blog getBlog(String name) {
		checkCache();
		if (blogCache != null && name != null) {
			String fqnstr = "/blog/" + name;
			Fqn fqn = Fqn.fromString(fqnstr);
			Blog blog = (Blog) blogCache.get(fqn, "blog");
			return blog;
		}
		return null;
	}

	@SuppressWarnings({ "unchecked" })
	public int getNumberEntries(String blogName) {
		checkCache();
		if (blogCache != null && blogName != null) {
			String fqnstr = "/blog/" + blogName + "/entries";
			return ((Set<String>) blogCache.getChildrenNames(fqnstr)).size();
		}
		return -1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Entry> getEntries(String blogName, int n1, int n2) {
		checkCache();
		if (blogCache != null && blogName != null) {

			ArrayList<Entry> result = new ArrayList<Entry>();
			String fqnstr = "/blog/" + blogName + "/entries";

			for (String child : (Set<String>) blogCache
					.getChildrenNames(fqnstr)) {
				String fqnchild = fqnstr + "/" + child;
				Fqn fqn = Fqn.fromString(fqnchild);
				Entry entry = (Entry) blogCache.get(fqn, "entry");
				result.add(entry);
			}

			// Entries sorted by published date
			Collections.sort(result, new EntryComparator());

			int size = result.size();
			if (n1 < 0 || n1 > (size - 1))
				return result;
			if (n2 > size)
				return result;
			if (n2 < n1)
				return result;

			ArrayList<Entry> orderedResult = new ArrayList<Entry>();
			for (int i = n1; i < n2; i++) {
				orderedResult.add(result.get(i));
			}

			return orderedResult;
		}

		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int getNumberComments(String blogName, String entryName) {
		checkCache();
		if (blogCache != null && blogName != null && entryName != null) {

			String fqnstr = "/blog/" + blogName + "/entries/" + entryName;
			Fqn fqn = Fqn.fromString(fqnstr);
			Entry entry = (Entry) blogCache.get(fqn, entryName);

			if (entry != null)
				return entry.getComments().size();
		}
		return -1;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ArrayList<Comment> getComments(String blogName, String entryName,
			int n1, int n2) {
		checkCache();
		if (blogCache != null && blogName != null && entryName != null) {

			String fqnstr = "/blog/" + blogName + "/entries/" + entryName;
			Fqn fqn = Fqn.fromString(fqnstr);
			Entry entry = (Entry) blogCache.get(fqn, entryName);

			if (entry != null) {
				ArrayList<Comment> result = new ArrayList<Comment>();

				int size = entry.getComments().size();
				if (n1 < 0 || n1 > (size - 1))
					return entry.getComments();
				if (n2 > size)
					return entry.getComments();
				if (n2 < n1)
					return entry.getComments();

				for (int i = n1; i <= n2; i++) {
					result.add(entry.getComments().get(i));
				}

				return result;
			}
		}
		return null;
	}

	public Set<String> getTags(String blogName) {

		int size = getNumberEntries(blogName);
		if (size <= 0)
			return null;

		HashSet<String> result = new HashSet<String>();
		ArrayList<Entry> entries = getEntries(blogName, 0, size);
		for (Entry e : entries) {
			if (e.getTags() != null) {
				for (String tag : e.getTags())
					result.add(tag);
			}
		}
		return result;

	}

	public int getNumberEntries(String blogName, String tag) {

		int size = getNumberEntries(blogName);
		if (size <= 0)
			return -1;

		int result = 0;
		ArrayList<Entry> entries = getEntries(blogName, 0, size);
		for (Entry e : entries) {
			if (e.getTags() != null) {
				for (String etag : e.getTags())
					if (etag.equalsIgnoreCase(tag))
						result++;
			}
		}
		return result;
	}

	public ArrayList<Entry> getEntries(String blogName, String tag, int n1,
			int n2) {

		int size = getNumberEntries(blogName);
		if (size <= 0)
			return null;

		ArrayList<Entry> inter = new ArrayList<Entry>();
		ArrayList<Entry> entries = getEntries(blogName, 0, size);
		for (Entry e : entries) {
			if (e.getTags() != null) {
				for (String etag : e.getTags())
					if (etag.equalsIgnoreCase(tag))
						inter.add(e);
			}
		}

		int intersize = inter.size();
		if (n1 < 0 || n1 > (intersize - 1))
			return inter;
		if (n2 > intersize)
			return inter;
		if (n2 < n1)
			return inter;

		ArrayList<Entry> result = new ArrayList<Entry>();
		for (int i = n1; i < n2; i++)
			result.add(inter.get(i));

		return result;
	}

	public Entry getEntry(String blogName, String entryName) {

		int size = getNumberEntries(blogName);
		if (size <= 0)
			return null;

		for (Entry e : getEntries(blogName, 0, size)) {
			if (e.getTitle().equalsIgnoreCase(entryName))
				return e;
		}

		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addBlog(Blog newBlog) {
		checkCache();
		if (blogCache != null && newBlog != null && newBlog.getName() != null) {

			if (getBlog(newBlog.getName()) != null) {
				log.info("Trying to overrride a existing blog: "
						+ newBlog.getName());
			}

			String fqnstr = "/blog/" + newBlog.getName();
			Fqn fqn = Fqn.fromString(fqnstr);
			blogCache.put(fqn, "blog", newBlog);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addEntry(String blogName, Entry newEntry) {
		checkCache();
		if (blogCache != null && blogName != null && newEntry != null
				&& newEntry.getTitle() != null
				&& newEntry.getPublished() != null) {

			String fqnstr = "/blog/" + blogName + "/entries/"
					+ newEntry.getTitle();
			Fqn fqn = Fqn.fromString(fqnstr);
			blogCache.put(fqn, "entry", newEntry);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addComment(String blogName, String entryName, Comment newComment) {
		checkCache();
		if (blogCache != null && blogName != null && entryName != null
				&& newComment != null && newComment.getPublished() != null
				&& newComment.getAuthor() != null) {

			String fqnstr = "/blog/" + blogName + "/entries/" + entryName;
			Fqn fqn = Fqn.fromString(fqnstr);
			Entry entry = (Entry) blogCache.get(fqn, "entry");

			if (entry != null) {
				entry.getComments().add(newComment);
				blogCache.put(fqn, "entry", entry);
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void editBlog(String oldBlog, Blog newBlog) {
		checkCache();
		if (blogCache != null && newBlog != null && newBlog.getName() != null
				&& oldBlog != null) {

			if (oldBlog.equals(newBlog.getName())) {

				String fqnstr = "/blog/" + newBlog.getName();
				Fqn fqn = Fqn.fromString(fqnstr);
				blogCache.put(fqn, "blog", newBlog);

			} else {
								
				String fqnstrParent = "/blog/" + newBlog.getName();
				Fqn fqnParent = Fqn.fromString(fqnstrParent);
				
				// Creating new blog
				blogCache.put(fqnParent, "blog", newBlog);
				
				String fqnstr = "/blog/" + oldBlog + "/entries";
				Fqn fqn = Fqn.fromString(fqnstr);
				
				// Moving entries to new node				
				blogCache.move(fqn, fqnParent);
				
				fqnstr = "/blog/" + oldBlog + "/resources";
				fqn = Fqn.fromString(fqnstr);
				
				// Moving resources to new node				
				blogCache.move(fqn, fqnParent);		
					
				// Deleting old blog
				blogCache.removeNode("/blog/" + oldBlog);
				

			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void removeBlog(String blogName) {
		checkCache();
		if (blogCache != null && blogName != null) {

			String fqnstr = "/blog/" + blogName;
			Fqn fqn = Fqn.fromString(fqnstr);
			blogCache.remove(fqn, "blog");
			blogCache.removeNode(fqn);

			for (String child : (Set<String>) blogCache
					.getChildrenNames("/blog/" + blogName + "/entries")) {

				String fqnstrchild = fqnstr + "/" + child;
				fqn = Fqn.fromString(fqnstrchild);
				Entry entry = (Entry) blogCache.get(fqn, "entry");
				if (entry != null
						&& entry.getBlogName().equalsIgnoreCase(blogName)) {
					blogCache.remove(fqn, "entry");
					blogCache.removeNode(fqn);
				}

			}

		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void removeEntry(String blogName, String entryName) {
		checkCache();
		if (blogCache != null && blogName != null && entryName != null) {

			String fqnstr = "/blog/" + blogName + "/entries/" + entryName;
			Fqn fqn = Fqn.fromString(fqnstr);
			blogCache.remove(fqn, "entry");
			blogCache.removeNode(fqn);

		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void removeComment(String blogName, String entryName, String uuid) {
		checkCache();
		if (blogCache != null && blogName != null && entryName != null
				&& uuid != null) {

			String fqnstr = "/blog/" + blogName + "/entries/" + entryName;
			Fqn fqn = Fqn.fromString(fqnstr);
			Entry entry = (Entry) blogCache.get(fqn, "entry");
			if (entry != null) {
				UUID uuidCompare = UUID.fromString(uuid);
				Comment removeComment = null;
				for (Comment c : entry.getComments()) {
					if (c.getUuid().equals(uuidCompare))
						removeComment = c;
				}
				entry.getComments().remove(removeComment);
				blogCache.put(fqn, "entry", entry);
			}
		}

	}

	public class EntryComparator implements Comparator<Entry> {

		public int compare(Entry e1, Entry e2) {
			if (e1 == null)
				return 0;
			if (e2 == null)
				return 0;
			if (e1.getPublished() == null)
				return 0;
			if (e2.getPublished() == null)
				return 0;

			// Change order: recently before
			return e1.getPublished().compareTo(e2.getPublished()) * -1;

		}

	}

}
