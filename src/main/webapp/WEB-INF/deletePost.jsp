<%--
  Created by IntelliJ IDEA.
  User: barjinder
  Date: 22/12/19
  Time: 10:36 PM
  To change this template use File | Settings | File Templates.
--%>
<%--<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>--%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="layout/header.jsp" >
    <jsp:param name="title" value="Home Page"/>
</jsp:include>

<div class="container">
<form action="/posts/delete/${id}", method="POST">
    <div class="form-group">
        <label for="title">Title</label>
        <input id="title" type="text" class="form-control" id="exampleInputEmail1"  name="title"
                value="${title}" disabled >
    </div>
    <div class="md-form">
        <label for="content">Content</label>
        <textarea id="content" class="md-textarea form-control" rows="10" name="content" required disabled>${content}</textarea>
    </div>
    <br>
    <input class="btn btn-danger" type="submit" value="Delete Post? You sure?">
</form>
</div>

<%@ include file="layout/footer.html"%>