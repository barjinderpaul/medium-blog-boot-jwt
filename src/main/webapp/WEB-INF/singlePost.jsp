<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Created by IntelliJ IDEA.
  User: barjinder
  Date: 22/12/19
  Time: 7:32 PM
  To change this template use File | Settings | File Templates.
--%>

<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Home Page"/>
</jsp:include>

<br>
<div class="container">
    <h4 class="border border-primary p-2">${post.title.toUpperCase()}</h4>
    <br>
    <p class="border border-warning p-2" >${post.content}</p>
    <c:set var = "categories" scope = "session" value = "${categorySet}"/>
    <p class="font-weight-bold">Categories :
        <c:forEach items="${categories}" var="category">
            <span> <a href="/posts/filter?tag=${category.categoryName}" class="btn btn-outline-primary">${category.categoryName}</a> </span>
        </c:forEach>
        <c:if test="${fn:length(categories) lt 1}">
            <span>No categories found</span>
        </c:if>
    </p>
</div>

<%@ include file="layout/footer.html"%>
