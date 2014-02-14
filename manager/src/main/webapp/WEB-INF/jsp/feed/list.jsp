<%--

    Robonews.io

    Copyright (c) 2013-2014 Rosty Kerei.
    All rights reserved.

--%>
<%@include file="/WEB-INF/jsp/includes/header.jsp"%>

<div class="span9">
    <ul class="breadcrumb">
        <li><a href="<c:url value="/"/>">Home</a> <span class="divider">/</span></li>
        <li class="active">Feeds</li>
    </ul>


    <h3>Feeds</h3>

<c:if test="${errorMessage != null}">
    <div class="alert alert-error">
        <c:out value="${errorMessage}"/>
    </div>
</c:if>
<c:if test="${infoMessage != null}">
    <div class="alert alert-success">
        <c:out value="${infoMessage}"/>
    </div>
</c:if>


<!-- table class="table table-striped table-bordered table-hover" -->
<table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered" id="feeds">
    <thead>
        <tr>
            <th>Title</th>
            <th>URL</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
    <%--<c:forEach items="${feeds}" var="feed">
        <tr>
            <td>${feed.name}<c:if test="${feed.video}"> <span class="label label-success">Video</span></c:if></td>
            <td><a href="${feed.url}" target="_blank">${feed.url}</a></td>
            <td class="action">
                <a href="#" class="btn btn-mini btn-info"><i class="icon-list icon-white"></i> Items</a>
                <a href="<c:url value="/feed/${feed.id}/edit"/>" class="btn btn-mini btn-warning"><i class="icon-edit icon-white"></i> Edit</a>
                <a href="<c:url value="/feed/${feed.id}/delete"/>" class="btn btn-mini btn-danger confirm"><i class="icon-remove icon-white"></i> Delete</a>
            </td>
        </tr>
    </c:forEach>--%>
    </tbody>
</table>

    <script type="text/javascript">
        $(document).ready(function() {
            $('#feeds').dataTable( {
                "sAjaxSource": "table",
                "bProcessing": true,
                "bServerSide": true,
                "sPaginationType": "bootstrap",
                "oLanguage": {"sLengthMenu": "_MENU_ records per page",
                    "sInfo": "Displaying _START_ to _END_ of _TOTAL_ records"},
                "aoColumns":[
                    {"mDataProp":"id"},
                    {"mDataProp":"name"}
                ]
            } );
        } );
    </script>


















</div>

<%@include file="/WEB-INF/jsp/includes/footer.jsp"%>