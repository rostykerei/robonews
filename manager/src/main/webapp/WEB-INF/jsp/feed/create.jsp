<%--

    Robonews.io

    Copyright (c) 2013-2014 Rosty Kerei.
    All rights reserved.

--%>
<%@include file="/WEB-INF/jsp/includes/header.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<div class="span9">
    <ul class="breadcrumb">
        <li><a href="<c:url value="/"/>">Home</a> <span class="divider">/</span></li>
        <li><a href="<c:url value="/feed/list"/>">Feeds</a> <span class="divider">/</span></li>
        <li class="active">New</li>
    </ul>

    <h3>New feed</h3>

    <%@include file="/WEB-INF/jsp/includes/errorbox.jsp"%>

    <form:form method="POST" commandName="feedUrl" cssClass="form-horizontal">
        <t:input path="url" label="URL" required="true"/>

        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn">Save</button>
            </div>
        </div>
    </form:form>
</div>

<%@include file="/WEB-INF/jsp/includes/footer.jsp"%>