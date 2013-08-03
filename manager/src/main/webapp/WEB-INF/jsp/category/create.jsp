<%@include file="/WEB-INF/jsp/includes/header.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<div class="span9">
    <ul class="breadcrumb">
        <li><a href="<c:url value="/"/>">Home</a> <span class="divider">/</span></li>
        <li><a href="<c:url value="/category/list"/>">Categories</a> <span class="divider">/</span></li>
        <li class="active">New</li>
    </ul>

    <h3>New category</h3>

    <%@include file="form.jsp"%>
</div>

<%@include file="/WEB-INF/jsp/includes/footer.jsp"%>