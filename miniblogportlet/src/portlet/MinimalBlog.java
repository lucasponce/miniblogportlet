package portlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletSecurityException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.UnavailableException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;
import org.apache.log4j.Logger;

import resources.services.ResourcesAPI;
import resources.services.ResourcesFactory;

import blog.Blog;
import blog.Comment;
import blog.Entry;
import blog.services.BlogAPI;
import blog.services.BlogFactory;

public class MinimalBlog extends GenericPortlet {

	private static final Logger _log = Logger.getLogger(MinimalBlog.class);

	final String REPO = "repo:";
	final String INTERNAL_URL = "/miniblogportlet/resource?blog=";

	private BlogAPI blogService() {

		return BlogFactory.getBlog();

	}

	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException, UnavailableException {

		_log.debug("VIEW");

		// Validate users with blog.services and portlet preferences
		renderUsers(request, response);

		// Control parameter
		String control = (String) request.getPortletSession().getAttribute(
				"control");

		if (control == null)
			control = "main";

		// Main view
		String view = "/jsp/main.jsp";
		if (control != null && control.equals("main")) {

			renderMain(request, response);
			view = "/jsp/main.jsp";
		}

		// Entry view
		if (control != null && control.equals("entry")) {

			renderEntry(request, response);
			view = "/jsp/entry.jsp";
		}

		// Tag view
		if (control != null && control.equals("tag")) {

			renderTag(request, response);
			view = "/jsp/tag.jsp";
		}

		// Edit entry view
		if (control != null && control.equals("edit")) {

			renderEditEntry(request, response);
			view = "/jsp/edit.jsp";
		}

		// New entry view
		if (control != null && control.equals("new")) {

			renderNewEntry(request, response);
			view = "/jsp/new.jsp";
		}

		if (control != null && control.equals("editblog")) {

			renderEditBlog(request, response);
			view = "/jsp/editblog.jsp";
		}

		PortletRequestDispatcher prd = getPortletContext()
				.getRequestDispatcher(view);
		prd.include(request, response);

	}

	// Main render phase
	private void renderMain(RenderRequest request, RenderResponse response)
			throws PortletException, IOException, UnavailableException {

		// Portlet preferences
		PortletPreferences prefs = request.getPreferences();
		String blog = prefs.getValue("blog", "none");
		String entries_page = prefs.getValue("entries_page", "3");

		// Pagination
		String page = (String) request.getPortletSession().getAttribute("page");

		// MVC Flow + Business Invocation
		BlogAPI blogService = blogService();

		// Pages calculation
		int nEntries = blogService.getNumberEntries(blog);
		int nEntriesPage = new Integer(entries_page).intValue();
		int nPages = (nEntries / nEntriesPage);
		if (nEntries % nEntriesPage > 0)
			nPages++;
		int iPage = 1;
		if (page != null)
			iPage = new Integer(page);

		int n1 = (iPage - 1) * nEntriesPage;
		int n2 = (iPage - 1) * nEntriesPage + nEntriesPage;
		if (n2 > nEntries)
			n2 = nEntries;

		// Blog
		Blog b = blogService.getBlog(blog);

		// Checking Admin rights
		if (request.getUserPrincipal() != null
				&& request.getUserPrincipal().getName() != null
				&& b != null
				&& b.getUsers() != null
				&& b.getUsers().indexOf(request.getUserPrincipal().getName()) != -1) {
			request.setAttribute("admin", request.getUserPrincipal().getName());
		} else {
			request.setAttribute("admin", null);
		}

		// Entries
		ArrayList<Entry> entries = blogService.getEntries(blog, n1, n2);

		// Tags
		Set<String> tags = blogService.getTags(blog);

		request.setAttribute("blog", b);
		request.setAttribute("entries", entries);
		request.setAttribute("numberpages", nPages);
		request.setAttribute("currentpage", iPage);
		request.setAttribute("tags", tags);

	}

	private void renderEntry(RenderRequest request, RenderResponse response)
			throws PortletException, IOException, UnavailableException {

		// Portlet preferences
		PortletPreferences prefs = request.getPreferences();
		String blog = prefs.getValue("blog", "none");

		// Parameters stored on portlet session
		String entryname = (String) request.getPortletSession().getAttribute(
				"entryname");

		// MVC Flow + Business Invocation
		BlogAPI blogService = blogService();

		// Blog
		Blog b = blogService.getBlog(blog);

		// Checking Admin rights
		if (request.getUserPrincipal() != null
				&& request.getUserPrincipal().getName() != null
				&& b != null
				&& b.getUsers() != null
				&& b.getUsers().indexOf(request.getUserPrincipal().getName()) != -1) {
			request.setAttribute("admin", request.getUserPrincipal().getName());
		} else {
			request.setAttribute("admin", null);
		}

		// Tags
		Set<String> tags = blogService.getTags(blog);

		// Entry
		Entry entry = blogService.getEntry(blog, entryname);

		// Replace content with repo images
		String replacement = INTERNAL_URL + b.getName() + "&entry=" + entryname
				+ "&key=";
		entry.setContent(entry.getContent().replaceAll(REPO, replacement));

		request.setAttribute("blog", b);
		request.setAttribute("entry", entry);
		request.setAttribute("tags", tags);

	}

	private void renderTag(RenderRequest request, RenderResponse response)
			throws PortletException, IOException, UnavailableException {

		// Portlet preferences
		PortletPreferences prefs = request.getPreferences();
		String blog = prefs.getValue("blog", "none");
		String entries_page = prefs.getValue("entries_page", "3");

		// Pagination and Tags
		String page = (String) request.getPortletSession().getAttribute("page");
		String tag = (String) request.getPortletSession().getAttribute("tag");

		// MVC Flow + Business Invocation
		BlogAPI blogService = blogService();

		// Pages calculation
		int nEntries = blogService.getNumberEntries(blog, tag);
		int nEntriesPage = new Integer(entries_page).intValue();
		int nPages = (nEntries / nEntriesPage);
		if (nEntries % nEntriesPage > 0)
			nPages++;
		int iPage = 1;
		if (page != null)
			iPage = new Integer(page);

		int n1 = (iPage - 1) * nEntriesPage;
		int n2 = (iPage - 1) * nEntriesPage + nEntriesPage;
		if (n2 > nEntries)
			n2 = nEntries;

		// Blog
		Blog b = blogService.getBlog(blog);

		// Checking Admin rights
		if (request.getUserPrincipal() != null
				&& request.getUserPrincipal().getName() != null
				&& b != null
				&& b.getUsers() != null
				&& b.getUsers().indexOf(request.getUserPrincipal().getName()) != -1) {
			request.setAttribute("admin", request.getUserPrincipal().getName());
		} else {
			request.setAttribute("admin", null);
		}

		// Entries
		ArrayList<Entry> entries = blogService.getEntries(blog, tag, n1, n2);

		// Tags
		Set<String> tags = blogService.getTags(blog);

		request.setAttribute("blog", b);
		request.setAttribute("entries", entries);
		request.setAttribute("numberpages", nPages);
		request.setAttribute("currentpage", iPage);
		request.setAttribute("tags", tags);
		request.setAttribute("tag", tag);

	}

	private void renderEditEntry(RenderRequest request, RenderResponse response)
			throws PortletException, IOException, UnavailableException {

		// Portlet preferences
		PortletPreferences prefs = request.getPreferences();
		String blog = prefs.getValue("blog", "none");

		// Information from portlet session
		String entryname = (String) request.getPortletSession().getAttribute(
				"entryname");

		// MVC Flow + Business Invocation
		BlogAPI blogService = blogService();

		// Blog
		Blog b = blogService.getBlog(blog);

		// Checking Admin rights
		if (request.getUserPrincipal() != null
				&& request.getUserPrincipal().getName() != null
				&& b != null
				&& b.getUsers() != null
				&& b.getUsers().indexOf(request.getUserPrincipal().getName()) != -1) {
			request.setAttribute("admin", request.getUserPrincipal().getName());
		} else {
			request.setAttribute("admin", null);
		}

		// Tags
		Set<String> tags = blogService.getTags(blog);

		// Entry
		Entry entry = blogService.getEntry(blog, entryname);

		ResourcesAPI resources = ResourcesFactory.getResources();
		Set<String> list = resources.getResources(blog, entryname);

		// listresources: list names of resources by entry
		// validation: message about error uploading resource

		request.setAttribute("listresources", list);
		request.setAttribute("validation", request.getPortletSession()
				.getAttribute("validation"));

		request.setAttribute("blog", b);
		request.setAttribute("entry", entry);
		request.setAttribute("tags", tags);

	}

	private void renderNewEntry(RenderRequest request, RenderResponse response)
			throws PortletException, IOException, UnavailableException {

		// Portlet preferences
		PortletPreferences prefs = request.getPreferences();
		String blog = prefs.getValue("blog", "none");

		// MVC Flow + Business Invocation
		BlogAPI blogService = blogService();

		// Blog
		Blog b = blogService.getBlog(blog);

		// Checking Admin rights
		if (request.getUserPrincipal() != null
				&& request.getUserPrincipal().getName() != null
				&& b != null
				&& b.getUsers() != null
				&& b.getUsers().indexOf(request.getUserPrincipal().getName()) != -1) {
			request.setAttribute("admin", request.getUserPrincipal().getName());
		} else {
			request.setAttribute("admin", null);
		}

		// Tags
		Set<String> tags = blogService.getTags(blog);

		request.setAttribute("blog", b);
		request.setAttribute("tags", tags);

	}

	private void renderEditBlog(RenderRequest request, RenderResponse response)
			throws PortletException, IOException, UnavailableException {

		// Portlet preferences
		PortletPreferences prefs = request.getPreferences();
		String blog = prefs.getValue("blog", "none");

		// MVC Flow + Business Invocation
		BlogAPI blogService = blogService();

		// Blog
		Blog b = blogService.getBlog(blog);

		// Checking Admin rights
		if (request.getUserPrincipal() != null
				&& request.getUserPrincipal().getName() != null
				&& b != null
				&& b.getUsers() != null
				&& b.getUsers().indexOf(request.getUserPrincipal().getName()) != -1) {
			request.setAttribute("admin", request.getUserPrincipal().getName());
		} else {
			request.setAttribute("admin", null);
		}

		// Tags
		Set<String> tags = blogService.getTags(blog);

		ResourcesAPI resources = ResourcesFactory.getResources();
		Set<String> list = resources.getResources(blog);

		// listresources: list names of resources by entry
		// validation: message about error uploading resource

		request.setAttribute("listresources", list);
		request.setAttribute("validation", request.getPortletSession()
				.getAttribute("validation"));

		request.setAttribute("blog", b);
		request.setAttribute("tags", tags);

	}

	// Check users
	private void renderUsers(RenderRequest request, RenderResponse response)
			throws PortletException, IOException, UnavailableException {

		// Portlet preferences
		PortletPreferences prefs = request.getPreferences();
		String blog = prefs.getValue("blog", "none");
		String users = prefs.getValue("users", "root");

		// MVC Flow + Business Invocation
		BlogAPI blogService = blogService();

		// Blog
		Blog b = blogService.getBlog(blog);

		// Create a new blog simply adding a new portlet
		if (b == null) {
			b = new Blog();
			b.setName(blog);
			blogService.addBlog(b);
		}

		for (String user : users.split(",")) {
			if (!b.getUsers().contains(user))
				b.getUsers().add(user);
		}

	}

	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, PortletSecurityException, IOException {

		_log.debug("ACTION");

		// Mapping actions parameters to render phase
		request.getPortletSession().setAttribute("control",
				request.getParameter("control"));
		request.getPortletSession().setAttribute("page",
				request.getParameter("page"));
		request.getPortletSession().setAttribute("entryname",
				request.getParameter("entryname"));
		request.getPortletSession().setAttribute("tag",
				request.getParameter("tag"));

		// Adding new Comment
		if (request.getParameter("author") != null
				&& request.getParameter("comment") != null) {

			actionNewComment(request, response);
		}

		// Remove Comment
		if (request.getParameter("action") != null
				&& request.getParameter("action").equals("removecomment")) {

			actionRemoveComment(request, response);
		}

		// Remove Entry
		if (request.getParameter("action") != null
				&& request.getParameter("action").equals("removeentry")) {

			actionRemoveEntry(request, response);
		}

		// Edit entry
		if (request.getParameter("action") != null
				&& request.getParameter("action").equals("editentry")) {

			actionEditEntry(request, response);
		}

		// New entry
		if (request.getParameter("action") != null
				&& request.getParameter("action").equals("newentry")) {

			actionNewEntry(request, response);
		}

		// Delete resource
		if (request.getParameter("action") != null
				&& request.getParameter("action").equals("deleteresource")) {

			actionDeleteResource(request, response);
		}

		// New resource
		if (request.getParameter("action") != null
				&& request.getParameter("action").equals("newresource")) {

			actionNewResource(request, response);
		}

		// Delete blog resource
		if (request.getParameter("action") != null
				&& request.getParameter("action").equals("deleteblogresource")) {

			actionDeleteBlogResource(request, response);
		}

		// New blog resource
		if (request.getParameter("action") != null
				&& request.getParameter("action").equals("newblogresource")) {

			actionNewBlogResource(request, response);
		}

		// Edit blog
		if (request.getParameter("action") != null
				&& request.getParameter("action").equals("editblog")) {

			actionEditBlog(request, response);
		}

	}

	private void actionNewComment(ActionRequest request, ActionResponse response)
			throws PortletException, PortletSecurityException, IOException {

		String blog = request.getParameter("blog");
		String entryname = request.getParameter("entryname");
		String author = request.getParameter("author");
		String comment = request.getParameter("comment");

		Comment c = new Comment();
		c.setAuthor(author);
		c.setContent(comment);
		c.setEntryTitle(entryname);
		c.setPublished(new Date());

		// Business Invocation
		BlogAPI blogService = blogService();

		blogService.addComment(blog, entryname, c);

		request.getPortletSession().setAttribute("control", "entry");
		request.getPortletSession().setAttribute("entryname", entryname);

	}

	private void actionRemoveComment(ActionRequest request,
			ActionResponse response) throws PortletException,
			PortletSecurityException, IOException {

		String blog = request.getParameter("blog");
		String entryName = request.getParameter("entryname");
		String uuid = request.getParameter("uuid");

		// Business Invocation
		BlogAPI blogService = blogService();

		blogService.removeComment(blog, entryName, uuid);

		request.getPortletSession().setAttribute("control", "entry");
		request.getPortletSession().setAttribute("entryname",
				request.getParameter("entryname"));

	}

	private void actionRemoveEntry(ActionRequest request,
			ActionResponse response) throws PortletException,
			PortletSecurityException, IOException {

		String blog = request.getParameter("blog");
		String entryName = request.getParameter("entryname");

		// Business Invocation
		BlogAPI blogService = blogService();

		blogService.removeEntry(blog, entryName);

	}

	private void actionEditEntry(ActionRequest request, ActionResponse response)
			throws PortletException, PortletSecurityException, IOException {

		String blog = request.getParameter("blog");
		String entryName = request.getParameter("entryname");

		String entrytitle = request.getParameter("entrytitle");
		String entrytags = request.getParameter("entrytags");
		String entrycontent = request.getParameter("entrycontent");

		String author = request.getUserPrincipal().getName();

		// Business Invocation
		BlogAPI blogService = blogService();

		// Entry
		Entry entry = blogService.getEntry(blog, entryName);

		entry.setAuthor(author);
		entry.setTitle(entrytitle);
		entry.setContent(entrycontent);

		entry.setTags(new ArrayList<String>());
		for (String tag : entrytags.split(" ")) {
			entry.getTags().add(tag);
		}

		// We remove the entry from cache and added in case entry has
		// changed the title
		blogService.removeEntry(blog, entryName);
		blogService.addEntry(blog, entry);

	}

	private void actionNewEntry(ActionRequest request, ActionResponse response)
			throws PortletException, PortletSecurityException, IOException {

		String blog = request.getParameter("blog");

		String entrytitle = request.getParameter("entrytitle");
		String entrytags = request.getParameter("entrytags");
		String entrycontent = request.getParameter("entrycontent");

		String author = request.getUserPrincipal().getName();

		// Business Invocation
		BlogAPI blogService = blogService();

		// Entry
		Entry entry = new Entry();

		entry.setAuthor(author);
		entry.setTitle(entrytitle);
		entry.setContent(entrycontent);
		entry.setPublished(new Date());

		entry.setTags(new ArrayList<String>());
		for (String tag : entrytags.split(" ")) {
			entry.getTags().add(tag);
		}

		// We remove the entry from cache and added in case entry has
		// changed the title
		blogService.addEntry(blog, entry);

		request.getPortletSession().setAttribute("control", "entry");
		request.getPortletSession().setAttribute("entryname", entry.getTitle());

	}

	private void actionDeleteResource(ActionRequest request,
			ActionResponse response) throws PortletException,
			PortletSecurityException, IOException {

		String blog = request.getParameter("blog");
		String entryName = request.getParameter("entryname");
		String resource = request.getParameter("resource");

		ResourcesAPI resources = ResourcesFactory.getResources();
		resources.removeResource(blog, entryName, resource);

		request.getPortletSession().setAttribute("control", "edit");
		request.getPortletSession().setAttribute("entryname", entryName);

	}

	private void actionNewResource(ActionRequest request,
			ActionResponse response) throws PortletException,
			PortletSecurityException, IOException {

		String blog = request.getParameter("blog");
		String entryName = request.getParameter("entryname");

		String validation = "";

		// Save the images in memory
		// 750k limit for images

		int maxsize = 750000;
		String s_maxsize = System.getProperty("miniblog.resource.maxsize");
		if (s_maxsize != null) {
			try {
				maxsize = new Integer(s_maxsize);
			} catch (Exception e) {
			}
		}

		FileItemFactory factory = new DiskFileItemFactory(maxsize, null);

		PortletFileUpload upload = new PortletFileUpload(factory);
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);

			String key = null;
			FileItem resource = null;

			for (FileItem f : items) {

				if (f.isFormField()) {
					String name = f.getFieldName();
					String value = f.getString();

					key = value;

					_log.debug("name: " + name + " value: " + value);

				} else {
					String fieldName = f.getFieldName();
					String fileName = f.getName();
					String contentType = f.getContentType();
					boolean isInMemory = f.isInMemory();
					long sizeInBytes = f.getSize();

					_log.debug("fieldName: " + fieldName + " fileName: "
							+ fileName + " contentType: " + contentType
							+ " isInMemory: " + isInMemory + " sizeInBytes: "
							+ sizeInBytes);

					resource = f;
				}

			}

			// Validation if key has no name
			if (key.equalsIgnoreCase(""))
				key = resource.getName();

			// Validation if exists
			boolean exists = false;
			ResourcesAPI resources = ResourcesFactory.getResources();
			if (resources.getResource(blog, entryName, key) != null) {
				exists = true;
				validation = "Resource key: "
						+ key
						+ " has already uploaded. Please, delete it prior re-upload it.";
			}

			// Access Images repository

			if (key != null && resource != null && !exists) {
				resources = ResourcesFactory.getResources();
				resources.setResource(blog, key, entryName, resource);
			}

			request.getPortletSession().setAttribute("validation", validation);

		} catch (FileUploadException e) {
			e.printStackTrace();
			_log.debug("Error uploading file: " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			_log.debug("Error uploading file: " + e.toString());
		}

	}

	private void actionDeleteBlogResource(ActionRequest request,
			ActionResponse response) throws PortletException,
			PortletSecurityException, IOException {

		String blog = request.getParameter("blog");
		String resource = request.getParameter("resource");

		ResourcesAPI resources = ResourcesFactory.getResources();
		resources.removeResource(blog, resource);

		request.getPortletSession().setAttribute("control", "editblog");

	}

	private void actionNewBlogResource(ActionRequest request,
			ActionResponse response) throws PortletException,
			PortletSecurityException, IOException {

		String blog = request.getParameter("blog");

		String validation = "";

		// Save the images in memory
		// 750k limit for images

		int maxsize = 750000;
		String s_maxsize = System.getProperty("miniblog.resource.maxsize");
		if (s_maxsize != null) {
			try {
				maxsize = new Integer(s_maxsize);
			} catch (Exception e) {
			}
		}

		FileItemFactory factory = new DiskFileItemFactory(maxsize, null);

		PortletFileUpload upload = new PortletFileUpload(factory);
		try {
			@SuppressWarnings("unchecked")
			List<FileItem> items = upload.parseRequest(request);

			String key = null;
			FileItem resource = null;

			for (FileItem f : items) {

				if (f.isFormField()) {
					String name = f.getFieldName();
					String value = f.getString();

					key = value;

					_log.debug("name: " + name + " value: " + value);

				} else {
					String fieldName = f.getFieldName();
					String fileName = f.getName();
					String contentType = f.getContentType();
					boolean isInMemory = f.isInMemory();
					long sizeInBytes = f.getSize();

					_log.debug("fieldName: " + fieldName + " fileName: "
							+ fileName + " contentType: " + contentType
							+ " isInMemory: " + isInMemory + " sizeInBytes: "
							+ sizeInBytes);

					resource = f;
				}

			}

			// Validation if key has no name
			if (key.equalsIgnoreCase(""))
				key = resource.getName();

			// Validation if exists
			boolean exists = false;
			ResourcesAPI resources = ResourcesFactory.getResources();
			if (resources.getResource(blog, key) != null) {
				exists = true;
				validation = "Resource key: "
						+ key
						+ " has already uploaded. Please, delete it prior re-upload it.";
			}

			// Access Images repository

			if (key != null && resource != null && !exists) {
				resources = ResourcesFactory.getResources();
				resources.setResource(blog, key, resource);
			}

			request.getPortletSession().setAttribute("validation", validation);

		} catch (FileUploadException e) {
			e.printStackTrace();
			_log.debug("Error uploading file: " + e.toString());
		} catch (Exception e) {
			e.printStackTrace();
			_log.debug("Error uploading file: " + e.toString());
		}

	}

	private void actionEditBlog(ActionRequest request, ActionResponse response)
			throws PortletException, PortletSecurityException, IOException {

		String blog = request.getParameter("blog");

		String blogtitle = request.getParameter("blogtitle");
		String blogdescription = request.getParameter("blogdescription");
		String blogcss = request.getParameter("blogcss");


		// Business Invocation
		BlogAPI blogService = blogService();

		Blog b = blogService.getBlog(blog);
		
		b.setName(blogtitle);
		b.setDescription(blogdescription);
		b.setCss(blogcss);
		
		blogService.editBlog(blog, b);
		
		// Portlet preferences
		PortletPreferences prefs = request.getPreferences();
		prefs.setValue("blog", b.getName());	
		prefs.store();
	}

}