<%--

    Robonews.io

    Copyright (c) 2013-2015 Rosty Kerei.
    All rights reserved.

--%>
<%@include file="/WEB-INF/jsp/includes/header.jsp"%>

<div class="span9">
    <ul class="breadcrumb">
        <li><a href="<c:url value="/"/>">Home</a> <span class="divider">/</span></li>
        <li class="active">Channels</li>
    </ul>


    <h3>Channels</h3>

    <%@include file="/WEB-INF/jsp/includes/errorbox.jsp"%>
    <%@include file="/WEB-INF/jsp/includes/infobox.jsp"%>

<table class="table table-striped table-bordered table-hover">
    <tr>
        <th>Name</th>
        <th>URL</th>
        <th>Actions</th>
    </tr>
    <c:forEach items="${channels}" var="channel">
        <tr>
            <td>${channel.title}</td>
            <td><a href="${channel.url}" target="_blank">${channel.url}</a></td>
            <td class="action">
                <a href="<c:url value="/channel/${channel.id}/edit"/>" class="btn btn-mini btn-warning"><i class="icon-edit icon-white"></i> Edit</a>
                <a href="<c:url value="/channel/${channel.id}/delete"/>" class="btn btn-mini btn-danger confirm"><i class="icon-remove icon-white"></i> Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>

</div>

<%@include file="/WEB-INF/jsp/includes/footer.jsp"%>