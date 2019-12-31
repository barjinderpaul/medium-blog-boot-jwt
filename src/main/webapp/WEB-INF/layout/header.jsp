<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title><%=request.getParameter("title")%></title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <%--    <link rel="stylesheet" href="./css/admin.css">--%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js">
    </script>
    <script>
        jQuery(document).ready(function($){
            $("#my-form").submit(function() {
                $(this).find(":input").filter(function(){ return !this.value; }).attr("disabled", "disabled");
                return true;
            });
            $( "#my-form" ).find( ":input" ).prop( "disabled", false );
        })
    </script>
</head>
<body>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div>
        <a class="navbar-brand" href="#">
            <img src="https://img.icons8.com/ios-filled/50/000000/medium-logo.png" alt="medium-logo">
        </a>
        <span>Medium</span>
    </div>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
            aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav ml-auto ">
            <li>
                <div class="btn-group">
                    <button type="button" class="btn btn-primary dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                        Filter
                    </button>
                    <div class="dropdown-menu">
                        <form id="my-form" class="px-4 py-3" method="GET" action="posts">
                        <p>Tags:</p>
                            <div class="form-check">
                                <input type="checkbox"class="form-check-input" name="tag" value="spring">
                                <label class="form-check-label">Spring</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox"class="form-check-input" name="tag" value="java">
                                <label class="form-check-label">Java</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox"class="form-check-input" name="tag" value="jsp">
                                <label class="form-check-label">Jsp</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox"class="form-check-input" name="tag" value="sql">
                                <label class="form-check-label">Sql</label>
                            </div>
                            <div class="form-check">
                                <input type="checkbox"class="form-check-input" name="tag" value="uncategorized">
                                <label class="form-check-label">Uncategorized</label>
                            </div>
                            <br>
                            <p>Sort:</p>
                            <div class="form-check">
                                <label class="form-check-label">
                                    <input type="radio" class="form-check-input" name="orderBy" value="UpdateDateTime">Last updated
                                </label>
                            </div>
                            <div class="form-check">
                                <label class="form-check-label">
                                    <input type="radio" class="form-check-input" name="orderBy" value="CreateDateTime">Last published
                                </label>
                            </div>
                            <br>
                            <p>User</p>
                            <div class="form-check">
                                <input type="checkbox"class="form-check-input" name="user" value="admin">
                                <label class="form-check-label">Admin</label>
                            </div>
                            <br>
                            <div class="form-group">
                                <label for="search">Search Keyword</label>
                                <input type="text" class="form-control" name="search" id="search">
                            </div>


                            <button type="submit" class="btn btn-primary">Filter</button>
                        </form>

                    </div>
                </div>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="/?">
                    <p>Home</p>
                </a>
            </li>
            <li class="nav-item active">
                <a class="nav-link" href="/posts/add">
                    <p>Create Post</p>
                </a>
            </li>

        </ul>
    </div>
</nav>