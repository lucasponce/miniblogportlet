<div class="blog_right_resources">
<div class="blog_resources">
	<div class="blog_wrapper">
	<h3 class="blog_resources_title">resources</h3>
	<!-- Start resources -->
	<c:forEach var="resource" items="${listresources}">

		<portlet:actionURL var="deleteResourceURL">
			<portlet:param name="action" value="deleteblogresource" />
			<portlet:param name="control" value="editblog" />
			<portlet:param name="blog" value="${blog.name}" />
			<portlet:param name="resource" value="${resource}" />
		</portlet:actionURL>

		<div class="blog_resources_name"><h3 class="blog_resource_name">${resource}</h3>
		<p class="blog_resources_link"><a class="blog_resource_link" href="<%= deleteResourceURL %>" onclick="return confirm('Confirm delete ${resource} resource');">Delete</a> /  <a class="blog_resource_link"href="/miniblogportlet/resource?blog=${blog.name}&key=${resource}" target="_blank">View</a></p>
		</div>
	
	</c:forEach>
		
	</div>
	<!-- New resource -->
	<c:if test='${validation != "" }'>
		<div class="validation">${validation}</div>
		
		<br />			
	</c:if>

	<portlet:actionURL var="newResourceURL">
		<portlet:param name="action" value="newblogresource" />
		<portlet:param name="control" value="editblog" />
		<portlet:param name="blog" value="${blog.name}" />
	</portlet:actionURL>
		
	<form method="POST" 
		   enctype="multipart/form-data"
		   action="<%= newResourceURL %>">
		<p class="blog_resource_description">New Resource name</p>
		<p><input class="blog_entry_form_title" type="text" name="resource_key" /></p>
		<p><input class="blog_entry_form_title" type="file" name="uploadfile" /></p>		
		<p class="blog_resources_link"><input class="blog_entry_form_submit" type="submit" value="Upload"/></p>		
	</form>		
</div>
</div>