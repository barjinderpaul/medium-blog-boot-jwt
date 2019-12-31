<%--
  Created by IntelliJ IDEA.
  User: barjinder
  Date: 22/12/19
  Time: 10:36 PM
  To change this template use File | Settings | File Templates.
--%>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Home Page"/>
</jsp:include>

<br>
<div class="container">

<h1>${heading}</h1><br>

<c:if test="${customAction == 'postCreated'}">
    <div class="alert alert-success" role="alert">
        <p>Post created : ${title} &nbsp; <a class="btn btn-primary" href="/posts/${id}">Go to Post</a> </p>
    </div>
</c:if>

<form action= "
    <c:choose>
        <c:when test="${customAction == 'addPost' || customAction == 'postCreated'}"> add </c:when>
        <c:otherwise> /posts/update/${id} </c:otherwise>
    </c:choose>"
    method="post">
<div class="form-group">
    <label for="title">Title</label>
    <input id="title" type="text" class="form-control" id="exampleInputEmail1" placeholder="Enter title" name="title"
           required value="<c:if test="${customAction == 'updatePost'}">${title}</c:if>">
    <small id="emailHelp" class="form-text text-muted">First 50 characters from content will be displayed as
        excerpt.</small>
</div>

<div class="md-form">
    <label for="content">Enter content</label>
    <textarea id="content" placeholder="Enter content" class="md-textarea form-control" rows="10" name="content" required><c:if test="${customAction != 'addPost'}">${fn:trim(content)}</c:if></textarea>
</div>
<br>
<%--<c:if test="${customAction} == 'addPost' || ${customAction} == 'updatePost'}">--%>
    <h4>Add categories : </h4>
    <div class="container">
        <div class="row">
            <div class="col-8">
                <c:if test = "${not fn:contains(categorySet, 'java')}">
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input" id="customCheck1" name="categories" value="java">
                        <label class="custom-control-label" for="customCheck1">Java</label>
                    </div>
                </c:if>
                <c:if test = "${not fn:contains(categorySet, 'spring')}">
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input" id="customCheck2" name="categories" value="spring">
                        <label class="custom-control-label" for="customCheck2">Spring</label>
                    </div>
                </c:if>

                <c:if test = "${not fn:contains(categorySet, 'sql')}">
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input" id="customCheck3" name="categories" value="sql">
                        <label class="custom-control-label" for="customCheck3">Sql</label>
                    </div>
                </c:if>
                <c:if test = "${not fn:contains(categorySet, 'jsp')}">
                    <div class="custom-control custom-checkbox">
                        <input type="checkbox" class="custom-control-input" id="customCheck4" name="categories" value="jsp">
                        <label class="custom-control-label" for="customCheck4">Jsp</label>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
<%--</c:if>--%>
    <br>
    <button type="submit" class="btn btn-primary">Save Post</button>
</form>

<%@ include file="layout/footer.html"%>