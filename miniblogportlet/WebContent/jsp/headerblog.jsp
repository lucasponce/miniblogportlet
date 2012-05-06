<div class="blog_title">
	<div class="blog_wrapper">
	<h1 class="blog_main_title">
	<portlet:actionURL var="titleURL">
		<portlet:param name="control" value="main" />
		<portlet:param name="page" value="1" />
	</portlet:actionURL>
	<a class="blog_title_link" href="<%= titleURL %>">${blog.name}</a>
	</h1>
	</div>	
	<div class="blog_wrapper"><p class="blog_title_description">${blog.description}</p></div>
	<c:choose>
		<c:when test="${admin != null}">
			<portlet:actionURL var="newURL">
				<portlet:param name="control" value="new" />
			</portlet:actionURL>
			<portlet:actionURL var="editBlogURL">
				<portlet:param name="control" value="editblog" />
			</portlet:actionURL>									
			<div class="blog_admin"><a class="blog_admin_link" href="<%= newURL %>">New Entry</a> / <a class="blog_admin_link" href="<%= editBlogURL %>">Edit Blog</a></div>
		</c:when>
	</c:choose>
</div>
<br />