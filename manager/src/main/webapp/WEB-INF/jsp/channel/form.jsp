<%@include file="/WEB-INF/jsp/includes/errorbox.jsp"%>

<form:form method="POST" commandName="channel" cssClass="form-horizontal">
    <t:input path="name" label="Name" required="true"/>
    <t:input path="url" label="URL" required="true"/>
    <t:textarea path="description" label="Description"/>
    <form:hidden path="id"/>
    <form:hidden path="version"/>

    <div class="control-group">
        <div class="controls">
            <button type="submit" class="btn">Save</button>
        </div>
    </div>
</form:form>
