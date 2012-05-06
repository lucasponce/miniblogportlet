package servlet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import resources.Resource;
import resources.services.ResourcesAPI;
import resources.services.ResourcesFactory;

/**
 * Servlet implementation class ResourceBlog
 */
public class ResourceBlog extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ResourceBlog() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Get key parameter of the object
		String blog = request.getParameter("blog");
		String key = request.getParameter("key");		
		String entry = request.getParameter("entry");
		
		// Access to Images repository
		ResourcesAPI resources = ResourcesFactory.getResources();
		
		Resource resource = null;
		
		if (entry != null)
			resource = resources.getResource(blog, entry, key);		
		else
			resource = resources.getResource(blog, key);
		
		if (resource != null) {
			
			response.setHeader("Content-Type", resource.getContentType() );
			response.setHeader("Content-Length", String.valueOf(resource.getSize()));
			response.setHeader("Content-Disposition", "inline; filename=\"" + resource.getName() + "\"");
			
			response.setContentType( resource.getContentType() );
			response.setStatus(HttpServletResponse.SC_OK);
			

			ByteArrayInputStream input = null;
			BufferedOutputStream output = null;
			
			// input = new BufferedInputStream(file.getInputStream());
			input = new ByteArrayInputStream(resource.getContent());
			output = new BufferedOutputStream(response.getOutputStream());
			
			byte[] buffer = new byte[16384];
		    for (int length = 0; (length = input.read(buffer)) > 0;) {
		        output.write(buffer, 0, length);
		    }
		    
		    input.close();
		    output.flush();
		    output.close();			
			
		}

	}

}
