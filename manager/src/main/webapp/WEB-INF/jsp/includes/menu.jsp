<%--

    Robonews.io

    Copyright (c) 2013-2014 Rosty Kerei.
    All rights reserved.

--%>
<c:set var="pageName" value="${requestScope['javax.servlet.forward.servlet_path']}"/>

<div class="span2">
    <div class="well sidebar-nav">
        <ul class="nav nav-list">
            <li class="nav-header">Channels</li>
            <li<c:if test="${pageName == '/channel/create'}"> class="active"</c:if>><a href="<c:url value="/channel/create"/>">Add new channel</a></li>
            <li<c:if test="${pageName == '/channel/list'}"> class="active"</c:if>><a href="<c:url value="/channel/list"/>">Manage channels</a></li>
            <li class="nav-header">Categories</li>
            <li<c:if test="${pageName == '/category/create'}"> class="active"</c:if>><a href="<c:url value="/category/create"/>">Add new category</a></li>
            <li<c:if test="${pageName == '/category/list'}"> class="active"</c:if>><a href="<c:url value="/category/list"/>">Manage categories</a></li>
            <li class="nav-header">Feeds</li>
            <li<c:if test="${pageName == '/feed/create'}"> class="active"</c:if>><a href="<c:url value="/feed/create"/>">Add new feed</a></li>
            <li<c:if test="${pageName == '/feed/list'}"> class="active"</c:if>><a href="<c:url value="/feed/list"/>">Manage feeds</a></li>
            <li class="nav-header">Items</li>
            <li><a href="#">Manage items</a></li>
            <li class="nav-header">Users</li>
            <li><a href="#">Add new users</a></li>
            <li><a href="#">Manage users</a></li>
            <li class="divider"></li>
            <li><a href="#">Logout</a></li>
        </ul>
    </div>
</div>