<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ page import="blog.*" %>
<portlet:defineObjects/>
<%@ include file="headercss.jsp" %>
<div class="blog">
<br />
<%@ include file="headerblog.jsp" %>
<!-- Entries -->
<div class="blog_entries">
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
			<div class="blog_admin"><p><a class="blog_admin_link" href="<%= editEntryURL %>">Edit</a> / <a class="blog_admin_link" href="<%= removeEntryURL %>" onclick="return confirm('Confirm remove entry');">Remove</a></p></div>
		</c:when>
	</c:choose>	
	<div class="blog_comments">
		<h3 class="blog_comments_title">Comments</h3>
		<c:forEach var="comment" items="${entry.comments}">
			<div class="blog_comment">
			<p class="blog_comment_user">${comment.published} - by ${comment.author}</p>
			<p>${comment.content}</p>
			</div>		
			<c:choose>
				<c:when test="${admin != null}">			
					<portlet:actionURL var="removeCommentURL">
						<portlet:param name="action" value="removecomment" />
						<portlet:param name="entryname" value="${entry.title}" />
						<portlet:param name="blog" value="${blog.name}" />
						<portlet:param name="uuid" value="${comment.uuid}" />						
					</portlet:actionURL>				
					<div class="blog_admin"><a class="blog_admin_link" href="<%= removeCommentURL %>" onclick="return confirm('Confirm remove comment');">Remove</a></div>
				</c:when>
			</c:choose>					
		</c:forEach>
		<h3 class="blog_comments_title">New comment</h3>
		<div class="blog_new_comment">
			<portlet:actionURL var="commentURL">
				<portlet:param name="entryname" value="${entry.title}" />
				<portlet:param name="blog" value="${blog.name}" />
			</portlet:actionURL>
			<form action="<%= commentURL %>" method="POST">
				<table class="blog_comment_table">
					<tr>
						<td><h3 class="blog_comment_form">Author: <input class="blog_comment_input" type="text" name="author" id="author" tabindex="1" /></h3></td>
					</tr>				
					<tr>
						<td><textarea class="blog_comment_textarea" name="comment" id="comment" cols="50" rows="7" tabindex="2"></textarea></td>
					</tr>
					<tr>
						<td><p class="blog_comment_submit"><input class="blog_comment_submit" name="submit" type="submit" id="submit" tabindex="3" value="Send" /></p></td>
					</tr>
				</table>
			</form>
		</div>						
	</div>	
</div>
<br />
</div>
<!-- Right tags -->
<%@ include file="righttags.jsp" %>
</div>