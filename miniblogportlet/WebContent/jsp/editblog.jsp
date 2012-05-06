<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ page import="blog.*" %>
<portlet:defineObjects/>

<c:set var="n"><portlet:namespace/></c:set>

<%@ include file="headercss.jsp" %>
<div class="blog">
<br />
<%@ include file="headerblog.jsp" %>
<!-- Entries -->
<div class="blog_entries">

<portlet:actionURL var="saveContentURL">
	<portlet:param name="action" value="editblog" />
	<portlet:param name="control" value="main" />
	<portlet:param name="blog" value="${blog.name}" />
</portlet:actionURL>
<form action="<%= saveContentURL %>" method="POST">
<div class="blog_entry">
	<div class="blog_wrapper">	
	<h3 class="blog_entry_title">Blog Title</h3>
	<p><input class="blog_entry_form_title" type="text" name="blogtitle" id="blogtitle" tabindex="1" value="${blog.name}" /></p>
	<h3 class="blog_entry_title">Blog Description</h3>
	<p><input class="blog_entry_form_title" type="text" name="blogdescription" id="blogdescription" tabindex="2" value="${blog.description}" /></p>
	<h3 class="blog_entry_title">Blog CSS resource name</h3>
	<p><input class="blog_entry_form_title" type="text" name="blogcss" id="blogcss" tabindex="3" value="${blog.css}" /></p>		
	<p class="blog_entry_form_submit"><input class="blog_entry_form_submit" name="submit" type="submit" id="submit" tabindex="3" value="Send" /></p>
	</div>
</div>
</form>
<br />
</div>
<!-- Right tags -->
<%@ include file="rightresourcesblog.jsp" %>
</div>