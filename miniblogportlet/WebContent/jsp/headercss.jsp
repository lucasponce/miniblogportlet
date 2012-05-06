<c:choose> 
	<c:when test="${blog.css == null || blog.css == ''}">
		<link type="text/css" rel="stylesheet" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/miniblog.css") %>">
	</c:when>
	<c:when test="${blog.css != null}">
		<link type="text/css" rel="stylesheet" href="/miniblogportlet/resource?blog=${blog.name}&key=${blog.css}">	
	</c:when>
</c:choose>