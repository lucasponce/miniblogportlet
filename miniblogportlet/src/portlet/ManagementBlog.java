package portlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.log4j.Logger;

import blog.Blog;
import blog.services.BlogAPI;
import blog.services.BlogFactory;

public class ManagementBlog extends GenericPortlet {

	protected final static Logger _log = Logger.getLogger(ManagementBlog.class);

	private BlogAPI blogService() {

		return BlogFactory.getBlog();		
	}	
	
	protected void doView(RenderRequest request, RenderResponse response)
			throws PortletException, IOException {

		_log.debug("VIEW");

		// Portlet preferences
		PortletPreferences prefs = request.getPreferences();
		String users = prefs.getValue("users", "root"); 		

		// Business Invocation
		BlogAPI blogService = blogService();		

		ArrayList<Blog> blogs = blogService.getBlogs();
		
		// Checking Admin rights
		if (request.getUserPrincipal() != null &&
				request.getUserPrincipal().getName() != null) {
			for (String user : users.split(","))
				if (user.equals(request.getUserPrincipal().getName())) {
					request.setAttribute("admin", request.getUserPrincipal().getName());
					break;
				}
		} else {
			request.setAttribute("admin", null);
		}		
				
		request.setAttribute("blogs", blogs);
		
		// Controlling the view		
		String view = "/jsp/management.jsp";

		PortletRequestDispatcher prd = getPortletContext()
				.getRequestDispatcher(view);
		prd.include(request, response);
		
	}

	public void processAction(ActionRequest request, ActionResponse response)
			throws PortletException, IOException {

		_log.debug("ACTION");	
		
		String deleteblog = request.getParameter("deleteblog");
		
		// Business Invocation
		BlogAPI blogService = blogService();	
		
		blogService.removeBlog(deleteblog);
		
	}

}
