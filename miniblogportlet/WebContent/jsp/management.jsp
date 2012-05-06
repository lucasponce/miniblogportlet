<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<portlet:defineObjects/>
<link rel="stylesheet" type="text/css" href="<%=renderResponse.encodeURL(renderRequest.getContextPath() + "/miniblogmanagement.css") %>" />

<c:set var="n"><portlet:namespace/></c:set>

<div>

	<div class="leftContent">
		<b>&nbsp;&nbsp;MiniBlog management</b>
	</div>
	
	<table class="centerTable">
			<tr>
				<th>Blog key</th>
				<th></th>
			</tr>
				
		<c:forEach var="blog" items="${blogs}">
			<tr>
				<td class="tdImage"><b>${blog.name}</b></td>
				<td class="tdImage">
					<c:choose>
						<c:when test="${admin != null}">
								
							<a href='<portlet:actionURL>
								<portlet:param name="deleteblog" value='${blog.name}'/>
							</portlet:actionURL>'
							onclick="return confirm('Confirm delete ${blog.name} blog');"
							>Delete</a>

						</c:when>
					</c:choose>
				</td>
			</tr>
		</c:forEach>
	</table>	
				
</div>