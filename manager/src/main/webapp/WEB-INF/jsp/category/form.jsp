<%--

    Robonews.io

    Copyright (c) 2013-2014 Rosty Kerei.
    All rights reserved.

--%>
<%@include file="/WEB-INF/jsp/includes/errorbox.jsp"%>

<form:form method="POST" commandName="category" cssClass="form-horizontal">
    <t:select path="parentCategoryId" label="Parent category" required="true" items="${parentCategories}" itemValue="id" itemLabel="name"/>
    <t:input path="name" label="Name" required="true"/>

    <div class="control-group">
        <label class="control-label">Priority category</label>
        <div class="controls">
            <label class="radio inline">
                <form:radiobutton path="priority" value="true"/> Yes
            </label>
            <label class="radio inline">
                <form:radiobutton path="priority" value="false"/> No
            </label>
        </div>
    </div>

    <div class="control-group">
        <div class="controls">
            <button type="submit" class="btn">Save</button>
        </div>
    </div>
</form:form>
