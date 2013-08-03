<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Bootstrap 101 Template</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="<spring:url value="/css/ui-bootstrap/jquery-ui-1.10.0.custom.css"/>" rel="stylesheet"/>
    <link href="<spring:url value="/css/bootstrap.min.css"/>" rel="stylesheet" media="screen">
    <link href="<spring:url value="/css/main.css"/>" rel="stylesheet" media="screen">
</head>
<body>

<script src="<spring:url value="/js/jquery.min.js"/>"></script>
<script src="<spring:url value="/js/jquery-ui.min.js"/>"></script>
<script src="<spring:url value="/js/jquery-timepicker.js"/>"></script>
<script src="<spring:url value="/js/bootstrap.min.js"/>"></script>
<script src="<spring:url value="/js/bootbox.min.js"/>"></script>
<script src="<spring:url value="/js/main.js"/>"></script>

<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="brand" href="<c:url value="/"/>">News Manager</a>
            <div class="nav-collapse collapse">
                <p class="navbar-text pull-right">
                    Logged in as <a href="#" class="navbar-link">Username</a>
                </p>
                <ul class="nav">
                    <li class="active"><a href="<c:url value="/"/>"><i class="icon-home icon-white"></i> Home</a></li>
                    <li><a href="#about">About</a></li>
                    <li><a href="#contact">Contact</a></li>
                </ul>
            </div><!--/.nav-collapse -->
        </div>
    </div>
</div>

<div class="container-fluid">
    <div class="row-fluid">
        <%@include file="menu.jsp"%>