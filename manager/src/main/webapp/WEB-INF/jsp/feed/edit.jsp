<%--

    Robonews.io

    Copyright (c) 2013-2015 Rosty Kerei.
    All rights reserved.

--%>
<%@include file="/WEB-INF/jsp/includes/header.jsp"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<div class="span9">
    <ul class="breadcrumb">
        <li><a href="<c:url value="/"/>">Home</a> <span class="divider">/</span></li>
        <li><a href="<c:url value="/feed/list"/>">Feed</a> <span class="divider">/</span></li>
        <li class="active">Edit</li>
    </ul>

    <h3>Edit feed</h3>

    <%@include file="/WEB-INF/jsp/includes/errorbox.jsp"%>

    <form:form method="POST" servletRelativeAction="/feed/save" commandName="feed" cssClass="form-horizontal">
        <t:input path="url" label="URL" required="true"/>

        <hr/>

        <t:select path="channelId" label="Channel" items="${channels}" required="true" itemValue="id" itemLabel="name"/>
        <t:select path="categoryId" label="Category" items="${categories}" required="true" itemValue="id" itemLabel="name"/>

        <div class="control-group">
            <label class="control-label">Video feed</label>
            <div class="controls">
                <label class="radio inline">
                    <form:radiobutton path="video" value="true"/> Yes
                </label>
                <label class="radio inline">
                    <form:radiobutton path="video" value="false"/> No
                </label>
            </div>
        </div>

        <hr/>

        <t:input path="name" label="Name" required="true"/>
        <t:input path="link" label="Link" required="true"/>
        <t:input path="author" label="Author"/>
        <t:input path="imageUrl" label="Image URL"/>
        <t:textarea path="description" label="Description"/>
        <t:textarea path="copyright" label="Copyright"/>

        <hr/>

        <t:velocity path="velocity" label="Velocity" required="true"/>
        <t:velocity path="minVelocityThreshold" label="Minimum velocity" required="true"/>
        <t:velocity path="maxVelocityThreshold" label="Maximum velocity" required="true"/>

        <hr/>

        <t:input path="plannedCheck" label="Planned check" required="true"/>

        <div class="control-group">
            <div class="controls">
                <button type="submit" class="btn">Save</button>
            </div>
        </div>
    </form:form>
</div>

<script>
    $('#plannedCheck').datetimepicker({
        dateFormat: "yy-mm-dd",
        timeFormat: "HH:mm:ss"
    });
</script>


<%@include file="/WEB-INF/jsp/includes/footer.jsp"%>