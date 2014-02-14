<%--

    Robonews.io

    Copyright (c) 2013-2014 Rosty Kerei.
    All rights reserved.

--%>
<%@include file="/WEB-INF/jsp/includes/header.jsp"%>

<div class="span9">
    <ul class="breadcrumb">
        <li><a href="<c:url value="/"/>">Home</a> <span class="divider">/</span></li>
        <li class="active">Categories</li>
    </ul>

    <h3>Categories</h3>

    <%@include file="/WEB-INF/jsp/includes/errorbox.jsp"%>
    <%@include file="/WEB-INF/jsp/includes/infobox.jsp"%>

    <c:forEach items="${categories}" var="category">
        <div class="categoryList" style="margin: 2px 2px 2px ${category.level*25}px;">
            ${category.name} <c:if test="${category.priority}"><span class="label label-important">Priority</span></c:if>
            <div style="float: right;">
                <a href="<c:url value="/category/${category.id}/edit"/>" class="btn btn-mini btn-warning"><i class="icon-edit icon-white"></i> Edit</a>
                <a href="<c:url value="/category/${category.id}/delete"/>" class="btn btn-mini btn-danger confirm"><i class="icon-remove icon-white"></i> Delete</a>
            </div>
        </div>
    </c:forEach>

</div>

<%@include file="/WEB-INF/jsp/includes/footer.jsp"%>