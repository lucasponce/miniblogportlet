<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ page import="blog.*" %>
<portlet:defineObjects/>

<c:set var="n"><portlet:namespace/></c:set>

<link rel="stylesheet" type="text/css" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/jquery.cleditor.css") %>" />
<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/jquery.min.js") %>"></script>
<script type="text/javascript" src="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/jquery.cleditor.min.js") %>"></script>
<%@ include file="headercss.jsp" %>
<div class="blog">
<br />
<%@ include file="headerblog.jsp" %>
<!-- Entries -->
<div class="blog_entries">

<script type="text/javascript">
  $(document).ready(function() {
    $("#${n}inputcontent").cleditor({width:"100%", height:"350px"});
  });
</script>
<portlet:actionURL var="saveContentURL">
	<portlet:param name="action" value="editentry" />
	<portlet:param name="control" value="entry" />
	<portlet:param name="blog" value="${blog.name}" />
	<portlet:param name="entryname" value="${entry.title}" />
</portlet:actionURL>
<form action="<%= saveContentURL %>" method="POST">
<div class="blog_entry">
	<div class="blog_wrapper">	
	<p>${entry.published} by ${entry.author}</p>
	<h3 class="blog_entry_title">Title</h3>
	<p><input class="blog_entry_form_title" type="text" name="entrytitle" id="entrytitle" tabindex="1" value="${entry.title}" /></p>
	<p>tags	</p>
	<c:set var="taglist" value="" />
	<c:forEach var="tag" items="${entry.tags}">
		<c:set var="taglist" value="${taglist}${tag} " />
	</c:forEach>
	<p><input class="blog_entry_form_tags" type="text" name="entrytags" id="entrytags" tabindex="1" value="${taglist}" /></p>	
	<div class="blog_entry_detail">Content</div>
	<p><textarea id="${n}inputcontent" class="centerEditor" name="entrycontent" cols="50" rows="50">${entry.content}</textarea>
	</p>
	<p class="blog_entry_form_submit"><input class="blog_entry_form_submit" name="submit" type="submit" id="submit" tabindex="3" value="Send" /></p>
	</div>
</div>
</form>
<br />
</div>
<!-- Right tags -->
<%@ include file="rightresources.jsp" %>
</div>