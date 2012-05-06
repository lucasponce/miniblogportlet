<div class="blog_right">
<div class="blog_tag">
	<div class="blog_wrapper">
	<h3 class="blog_tags_title">tags</h3>
	<h3 class="blog_tag_title">
	<c:forEach var="tag" items="${tags}">
		<portlet:actionURL var="tagURL">
			<portlet:param name="control" value="tag" />
			<portlet:param name="tag" value="${tag}" />
		</portlet:actionURL>		
		<a class="blog_link" href="<%= tagURL %>">${tag}</a>&nbsp;
	</c:forEach>
	</h3>
	</div>	
</div>
</div>