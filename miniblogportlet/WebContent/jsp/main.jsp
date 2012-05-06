<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="blog.*" %>
<portlet:defineObjects/>
<%@ include file="headercss.jsp" %>
<div class="blog">
<br />
<%@ include file="headerblog.jsp" %>
<!-- Entries -->
<div class="blog_entries">
<c:forEach var="entry" items="${entries}">
<div class="blog_entry">
	<div class="blog_wrapper">	
	<p>${entry.published} by ${entry.author}</p>
	<portlet:actionURL var="entryURL">
		<portlet:param name="control" value="entry" />
		<portlet:param name="entryname" value="${entry.title}" />
	</portlet:actionURL>	
	<h3 class="blog_entry_title"><a class="blog_link" href="<%= entryURL %>">${entry.title}</a></h3>
	<p>
	<c:forEach var="tag" items="${entry.tags}">	
		<portlet:actionURL var="tagURL">
			<portlet:param name="control" value="tag" />
			<portlet:param name="tag" value="${tag}" />
		</portlet:actionURL>			
		<a class="blog_link" href="<%= tagURL %>">${tag}</a>&nbsp;
	</c:forEach>
	</p>
	<div class="blog_entry_detail">${entry.content}</div>
	</div>
	<c:choose>
		<c:when test="${admin != null}">
			<portlet:actionURL var="editEntryURL">
				<portlet:param name="control" value="edit" />
				<portlet:param name="entryname" value="${entry.title}" />
				<portlet:param name="blog" value="${blog.name}" />
			</portlet:actionURL>			
			<portlet:actionURL var="removeEntryURL">
				<portlet:param name="action" value="removeentry" />
				<portlet:param name="entryname" value="${entry.title}" />
				<portlet:param name="blog" value="${blog.name}" />
				<portlet:param name="control" value="main" />						
			</portlet:actionURL>			
			<div class="blog_admin"><a class="blog_admin_link" href="<%= editEntryURL %>">Edit</a> / <a class="blog_admin_link" href="<%= removeEntryURL %>" onclick="return confirm('Confirm remove entry');" >Remove</a></div>
		</c:when>
	</c:choose>	
</div>
<br />
</c:forEach>
</div>
<!-- Right tags -->
<%@ include file="righttags.jsp" %>
<div class="blog_clean"></div>
<div class="blog_pages">
	<h3 class="blog_more_pages">
	<c:forEach var="i" begin="1" end="${numberpages}" step="1">
		<portlet:actionURL var="pageURL">
			<portlet:param name="control" value="main" />
			<portlet:param name="page" value="${i}" />
		</portlet:actionURL>		
		<c:choose>
			<c:when test="${i == currentpage }">
				<a class="blog_current_page_link" href="<%= pageURL %>">[${i}]</a>
			</c:when>
			<c:otherwise>
				<a class="blog_link" href="<%= pageURL %>">[${i}]</a>			
			</c:otherwise>
		</c:choose>
	</c:forEach>
	</h3>
</div>
</div>